package com.vnpay.airbooking.service;

import com.vnpay.airbooking.entity.Passenger;
import com.vnpay.airbooking.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;
    public Iterable<Passenger> getAllPassenger(){
        return this.passengerRepository.findAll();
    }
    public Passenger findById(Integer id) {
        return passengerRepository.findById(id).get();
    }

    public void deletePassenger(Passenger passenger){
        passengerRepository.delete(passenger);
    }
    public Passenger addPassengerName(Passenger passenger){
        return passengerRepository.save(passenger);
    }
    public void saveAll(List<Passenger> passengerList) {
        for (int i = 0; i < passengerList.size(); i++) {
            passengerRepository.save(passengerList.get(i));
        }
    }
}
