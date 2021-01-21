package com.vnpay.airbooking.model.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSegment {

    private String depart_date;
    private String arrival_date;
    private String original;
    private String destination;
    private String flightNumber;
    private String plane;
    private String planeCode;
    private String duration;
    private String classify;
    private String classCode;
    private String seatsRemaining;
    private String flight_id;
    private String layover;

    @Override
    public String toString() {
        return "FlightSegment{" +
                "depart_date='" + depart_date + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                ", original='" + original + '\'' +
                ", destination='" + destination + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", plane='" + plane + '\'' +
                ", planeCode='" + planeCode + '\'' +
                ", duration='" + duration + '\'' +
                ", classify='" + classify + '\'' +
                ", classCode='" + classCode + '\'' +
                ", seatsRemaining='" + seatsRemaining + '\'' +
                ", flight_id='" + flight_id + '\'' +
                ", layover='" + layover + '\'' +
                '}';
    }
}
