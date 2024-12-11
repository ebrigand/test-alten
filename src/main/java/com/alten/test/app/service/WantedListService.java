package com.alten.test.app.service;

import com.alten.test.app.model.WantedListDto;

public interface WantedListService {

    WantedListDto post(String username, WantedListDto wantedListDto);
    WantedListDto get(String username);
}
