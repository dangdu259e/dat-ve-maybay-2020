package com.vnpay.airbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "passenger")
@JsonIgnoreProperties({"passportId","bookingInfors"})
public class Passenger {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int index;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "type")
    private String type;

    @Column(name = "gender")
    private String gender;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "passport_id")
    private Integer passportId;

    @ManyToMany(mappedBy = "passengerDetails", cascade = CascadeType.ALL)
    private List<BookingInfor> bookingInfors = new ArrayList<>();


}
