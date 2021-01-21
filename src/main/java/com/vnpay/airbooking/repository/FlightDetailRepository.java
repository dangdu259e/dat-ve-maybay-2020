package com.vnpay.airbooking.repository;

import com.vnpay.airbooking.entity.FlightDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightDetailRepository extends JpaRepository<FlightDetail, Long> {
}
