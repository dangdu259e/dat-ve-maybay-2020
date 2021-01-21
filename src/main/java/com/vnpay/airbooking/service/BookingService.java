package com.vnpay.airbooking.service;

import com.vnpay.airbooking.entity.BookingInfor;
import com.vnpay.airbooking.repository.BookingInforRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
    @Autowired
    private BookingInforRepository bookingInforRepository;

    public BookingInfor save(BookingInfor bookingInfor){
        return bookingInforRepository.save(bookingInfor);
    }


}
