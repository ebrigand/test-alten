package com.alten.test.app.service;

import com.alten.test.app.exception.AccountNotFoundException;
import com.alten.test.app.model.WishListDto;
import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.WishListRepository;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.repository.domain.WishList;
import com.alten.test.app.service.mapper.WishListMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {

    private final AccountRepository accountRepository;
    private final WishListRepository wishListRepository;
    private final WishListMapper wishListMapper;

    WishListServiceImpl(AccountRepository accountRepository,
                        WishListRepository wishListRepository,
                        WishListMapper wishListMapper) {
        this.accountRepository = accountRepository;
        this.wishListRepository = wishListRepository;
        this.wishListMapper = wishListMapper;
    }

    // Je filtre les ProductCounts existant pour supprimer ceux qui n'existent plus puis en ajoutant les nouveaux
    // Peut être il serait plus simple de supprimer le Set et de le sauvegarder un nouveau Set issu du frontend
    // mais je n'arrive pas à le faire dans une même transaction a cause des contraintes uniques de ProductCount,
    // je pourrais supprimer ces contraintes ou éviter la transaction
    // mais deux save sans utiliser de transaction pour une fonctionnalité ce n'est pas recommandé
    // en tout cas je pense qu'il y'a moyen de mieux faire, c'est la partie de mon code ou je doute
    @Override
    public WishListDto post(String username, WishListDto wishListDto){
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        WishList wishList = wishListMapper.mapToWishList(wishListDto);
        wishList.setAccount(account);
        WishList wishListToSave = wishListRepository.findByAccount(account).map(existingWishList -> {
            //Remove DB ProductCounts not present in the Request body
            Set<Long> newIds = wishList.getProductCounts().stream().map(productCount -> productCount.getProduct().getId()).collect(Collectors.toSet());
            Set<ProductCount> existingToRemove = existingWishList.getProductCounts().stream().filter(productCount -> !newIds.contains(productCount.getProduct().getId())).collect(Collectors.toSet());
            existingWishList.getProductCounts().removeAll(existingToRemove);

            //Update quantity of existing DB ProductCounts
            existingWishList.getProductCounts().stream().filter(productCount -> newIds.contains(productCount.getProduct().getId())).forEach(productCount -> {
              ProductCount newProductCount = wishList.getProductCounts().stream().filter(newPCount -> newPCount.getProduct().getId().equals(productCount.getProduct().getId())).findFirst().get();
              productCount.setQuantity(newProductCount.getQuantity());
            });

            //Remove Request body ProductCounts already present in DB
            Set<Long> existingIds = existingWishList.getProductCounts().stream().map(productCount -> productCount.getProduct().getId()).collect(Collectors.toSet());
            Set<ProductCount> newToRemove = wishList.getProductCounts().stream().filter(productCount -> existingIds.contains(productCount.getProduct().getId())).collect(Collectors.toSet());
            wishList.getProductCounts().removeAll(newToRemove);

            //Add new Request body ProductCounts (not present in DB)
            existingWishList.getProductCounts().addAll(wishList.getProductCounts());

            return existingWishList;
        }).orElse(wishList);
        //Set wishList to productCount: the other side of the relationship
        wishListToSave.getProductCounts().forEach(productCount -> productCount.setWishList(wishListToSave));
        return wishListMapper.mapToWishListDto(wishListRepository.save(wishListToSave));
    }

    @Override
    public WishListDto get(String username){
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        WishList wishList = wishListRepository.findByAccount(account).orElse(new WishList());
        return wishListMapper.mapToWishListDto(wishList);
    }
}
