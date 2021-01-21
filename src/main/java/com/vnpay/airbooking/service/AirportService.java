package com.vnpay.airbooking.service;

import com.vnpay.airbooking.repository.AirportRepository;
import com.vnpay.airbooking.entity.Airport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {
    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> getAllAirport(){
        return airportRepository.findAll();
    }

    public Airport findByCode(String code){
        return airportRepository.findByCode(code);
    }
}
