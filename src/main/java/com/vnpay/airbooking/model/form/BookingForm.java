package com.vnpay.airbooking.model.form;

import com.vnpay.airbooking.entity.Passenger;
import com.vnpay.airbooking.entity.Payer;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BookingForm {
    private long departFlightID;
    private long returnFlightID;
    private List<Passenger> passengerList = new ArrayList<>();
    private Payer payer = new Payer();

    @Override
    public String toString() {
        return "BookingForm{" +
                "departFlightID=" + departFlightID +
                ", returnFlightID=" + returnFlightID +
                ", passengerList=" + passengerList.size() +
                ", payer=" + payer +
                '}';
    }
}
