package com.codingshuttle.TestingApp.services.impl;

import com.codingshuttle.TestingApp.services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class DataServiceImplDev implements DataService {

    @Override
    public String GetData() {
        return "Dev Data";
    }
}
