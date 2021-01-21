package com.vnpay.airbooking.repository;

import com.vnpay.airbooking.entity.Passenger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends CrudRepository<Passenger,Integer> {
}
