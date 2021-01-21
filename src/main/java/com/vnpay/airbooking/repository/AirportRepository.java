package com.vnpay.airbooking.repository;

import com.vnpay.airbooking.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, String> {
    Airport findByCode(String airport_code);
}
