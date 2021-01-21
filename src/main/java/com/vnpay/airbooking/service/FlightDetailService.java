package com.vnpay.airbooking.service;

import com.vnpay.airbooking.entity.FlightDetail;
import com.vnpay.airbooking.repository.FlightDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightDetailService {
    @Autowired
    private FlightDetailRepository flightDetailRepository;

    public FlightDetail save(FlightDetail flightDetail){
        return flightDetailRepository.save(flightDetail);
    }
}
