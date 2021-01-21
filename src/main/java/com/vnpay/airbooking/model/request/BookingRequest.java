package com.vnpay.airbooking.model.request;

import com.vnpay.airbooking.entity.Passenger;
import com.vnpay.airbooking.entity.Payer;
import com.vnpay.airbooking.model.form.BookingForm;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BookingRequest {
    private String app_id= "VCB|VI";
    private String request_id;
    private Long query_id;
    private long depart_flight_id;
    private long return_flight_id;
    private List<Passenger> passengers = new ArrayList<>();
    private Payer contact_info;
    private Integer pay_now= 1;
    private String version ="2.0" ;
    private String promotion_code="";
    private String request_date;
    private String checksum= "0";

    public BookingRequest(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        this.request_id = dtf.format(now);
        this.request_date = dtf.format(now);
    }

    public void setData(BookingForm bookingForm, Long query_id){
        this.query_id = query_id;
        this.depart_flight_id = bookingForm.getDepartFlightID();
        this.return_flight_id = bookingForm.getReturnFlightID();
        this.passengers = bookingForm.getPassengerList();
        this.contact_info = bookingForm.getPayer();
    }



}
