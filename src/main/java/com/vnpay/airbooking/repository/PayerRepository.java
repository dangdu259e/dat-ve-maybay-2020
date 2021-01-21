package com.vnpay.airbooking.repository;

import com.vnpay.airbooking.entity.Payer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayerRepository extends CrudRepository<Payer,Integer> {
}
