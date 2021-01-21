package com.vnpay.airbooking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "booking_infor")
@Setter
@Getter
public class BookingInfor {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "book_code")
    private String bookCode;

    @Column(name ="amount")
    private Long amount;

    @OneToOne
    @JoinColumn(name = "vnp_txn_ref")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "payer_id")
    private Payer payer;

    @ManyToMany
    @JoinTable(name = "booking_flight_detail",
            joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id", referencedColumnName = "id"))
    private List<FlightDetail> flightDetails = new ArrayList<>();

    @ManyToMany
    @JoinTable(name ="booking_passenger_detail",
            joinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private List<Passenger> passengerDetails = new ArrayList<>();



}
