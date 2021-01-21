package com.vnpay.airbooking.model.json;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightData {
    private List<Flight>  departureFlights;
    private List<Flight>  returnFlights;

    @Override
    public String toString() {
        return "FlightData{" +
                "departureFlights=" + departureFlights.size() +
                ", returnFlights=" + returnFlights.size() +
                '}';
    }
}
