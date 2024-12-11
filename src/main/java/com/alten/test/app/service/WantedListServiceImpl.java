package com.alten.test.app.service;

import com.alten.test.app.exception.AccountNotFoundException;
import com.alten.test.app.model.WantedListDto;
import com.alten.test.app.repository.AccountRepository;
import com.alten.test.app.repository.WantedListRepository;
import com.alten.test.app.repository.domain.Account;
import com.alten.test.app.repository.domain.ProductCount;
import com.alten.test.app.repository.domain.WantedList;
import com.alten.test.app.service.mapper.WantedListMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WantedListServiceImpl implements WantedListService {

    private final AccountRepository accountRepository;
    private final WantedListRepository wantedListRepository;
    private final WantedListMapper wantedListMapper;

    WantedListServiceImpl(AccountRepository accountRepository,
                          WantedListRepository wantedListRepository,
                          WantedListMapper wantedListMapper) {
        this.accountRepository = accountRepository;
        this.wantedListRepository = wantedListRepository;
        this.wantedListMapper = wantedListMapper;
    }

    // Je filtre les ProductCounts existant pour supprimer ceux qui n'existent plus puis en ajoutant les nouveaux
    // Peut être il serait plus simple de supprimer le Set et de le sauvegarder un nouveau Set issu du frontend
    // mais je n'arrive pas à le faire dans une même transaction a cause des contraintes uniques de ProductCount,
    // je pourrais supprimer ces contraintes ou éviter la transaction
    // mais deux save sans utiliser de transaction pour une fonctionnalité ce n'est pas recommandé
    // en tout cas je pense qu'il y'a moyen de mieux faire, c'est la partie de mon code ou je doute
    @Override
    public WantedListDto post(String username, WantedListDto wantedListDto){
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        WantedList wantedList = wantedListMapper.mapToWantedList(wantedListDto);
        wantedList.setAccount(account);
        WantedList wantedListToSave = wantedListRepository.findByAccount(account).map(existingWantedList -> {
            //Remove DB ProductCounts not present in the Request body
            Set<Long> newIds = wantedList.getProductCounts().stream().map(productCount -> productCount.getProduct().getId()).collect(Collectors.toSet());
            Set<ProductCount> existingToRemove = existingWantedList.getProductCounts().stream().filter(productCount -> !newIds.contains(productCount.getProduct().getId())).collect(Collectors.toSet());
            existingWantedList.getProductCounts().removeAll(existingToRemove);

            //Update quantity of existing DB ProductCounts
            existingWantedList.getProductCounts().stream().filter(productCount -> newIds.contains(productCount.getProduct().getId())).forEach(productCount -> {
              ProductCount newProductCount = wantedList.getProductCounts().stream().filter(newPCount -> newPCount.getProduct().getId().equals(productCount.getProduct().getId())).findFirst().get();
              productCount.setQuantity(newProductCount.getQuantity());
            });

            //Remove Request body ProductCounts already present in DB
            Set<Long> existingIds = existingWantedList.getProductCounts().stream().map(productCount -> productCount.getProduct().getId()).collect(Collectors.toSet());
            Set<ProductCount> newToRemove = wantedList.getProductCounts().stream().filter(productCount -> existingIds.contains(productCount.getProduct().getId())).collect(Collectors.toSet());
            wantedList.getProductCounts().removeAll(newToRemove);

            //Add new Request body ProductCounts (not present in DB)
            existingWantedList.getProductCounts().addAll(wantedList.getProductCounts());

            return existingWantedList;
        }).orElse(wantedList);
        //Set wantedList to productCount: the other side of the relationship
        wantedListToSave.getProductCounts().forEach(productCount -> productCount.setWantedList(wantedListToSave));
        return wantedListMapper.mapToWantedListDto(wantedListRepository.save(wantedListToSave));
    }

    @Override
    public WantedListDto get(String username){
        Account account = accountRepository.findByUsername(username).orElseThrow(() -> new AccountNotFoundException("Could not find account with username " + username));
        WantedList wantedList = wantedListRepository.findByAccount(account).orElse(new WantedList());
        return wantedListMapper.mapToWantedListDto(wantedList);
    }
}
