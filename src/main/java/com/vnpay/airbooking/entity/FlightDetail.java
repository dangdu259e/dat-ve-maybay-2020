package com.vnpay.airbooking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight_detail")
@Getter
@Setter
public class FlightDetail {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "airplane_code")
    private String airplaneCode;

    @Column(name = "original")
    private String original;

    @Column(name = "destination")
    private String destination;

    @Column(name = "depart_date")
    private String departDate;

    @Column(name = "arrival_date")
    private String arrivalDate;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "passengerDetails", cascade = CascadeType.ALL)
    private List<BookingInfor> bookingInfors = new ArrayList<>();

    public FlightDetail(Long id, String airplaneCode, String original, String destination, String departDate, String arrivalDate, String type){
        this.id = id;
        this.airplaneCode = airplaneCode;
        this.original = original;
        this.destination = destination;
        this.departDate = departDate;
        this.arrivalDate = arrivalDate;
        this.type = type;
    }

    public FlightDetail(){

    }

}
