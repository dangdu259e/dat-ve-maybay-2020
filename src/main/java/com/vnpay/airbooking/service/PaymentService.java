package com.vnpay.airbooking.service;

import com.vnpay.airbooking.entity.Payment;
import com.vnpay.airbooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;

    public Iterable<Payment> getAllPayment(){
        return this.paymentRepository.findAll();
    }
    public Payment findById(Long id) {
        return paymentRepository.findById(id).get();
    }

    public void deletePayment(Payment payment){
        paymentRepository.delete(payment);
    }
    public Payment save(Payment payment){
        return paymentRepository.save(payment);
    }

}
