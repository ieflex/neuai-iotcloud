package com.neuai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neuai.com.repository.DeviceDataRepository;
import com.neuai.dao.DeviceData;

@RestController
@RequestMapping("/")
public class IndexController {
    
    @Autowired
    DeviceDataRepository deviceDataRepository;

    @GetMapping(value = "/dv/{dataId}")
    public ResponseEntity<DeviceData> getById(@PathVariable String dataId){
    	DeviceData findOne = deviceDataRepository.findOne(dataId);
        return new ResponseEntity<>(findOne, HttpStatus.OK);
    }
}
