package com.vnpay.airbooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vnpay.airbooking.entity.BookingInfor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "payer")
@JsonIgnoreProperties({"id", "bookingInfors"})
public class Payer {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private  Integer id;

    @Column(name = "gender")
    private String gender;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName ="";

    @Column(name = "phone")
    private String phone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @OneToMany(mappedBy = "payer")
    private List<BookingInfor> bookingInfors = new ArrayList<>();

    @Override
    public String toString() {
        return "Payer{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", address='" + address + '\'' +
                ", note='" + note + '\'' +
                ", bookingInfors=" + bookingInfors +
                '}';
    }
}
