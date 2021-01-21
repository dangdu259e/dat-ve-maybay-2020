package com.vnpay.airbooking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airport")
public class Airport {
    @Id
    @Column(name = "airport_code")
    private String code;

    @Column(name = "airport_name")
    private String airport_name;

    public void setAirport_code(String airport_code) {
        this.code = airport_code;
    }

    public String getAirport_code() {
        return code;
    }

    public void setAirport_name(String airport_name) {
        this.airport_name = airport_name;
    }

    public String getAirport_name() {
        return airport_name;
    }

    @Override
    public String toString() {
        return  airport_name + "( " + code +" )";
    }
}
