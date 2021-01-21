package com.vnpay.airbooking.repository;

import com.vnpay.airbooking.entity.BookingInfor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingInforRepository extends JpaRepository<BookingInfor, Integer> {
}
