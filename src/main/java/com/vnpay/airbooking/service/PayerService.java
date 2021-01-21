package com.vnpay.airbooking.service;

import com.vnpay.airbooking.entity.Payer;
import com.vnpay.airbooking.repository.PayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayerService {
    @Autowired
    PayerRepository payerRepository;

    public Iterable<Payer> getAllPayer() {
        return this.payerRepository.findAll();
    }

    public Payer findById(Integer id) {
        return payerRepository.findById(id).get();
    }

    public void deletePayer(Payer payer) {
        payerRepository.delete(payer);
    }

    public Payer save(Payer payer) {
        return payerRepository.save(payer);
    }
}
