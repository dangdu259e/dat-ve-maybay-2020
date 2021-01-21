package com.vnpay.airbooking.model.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@Setter
@Getter
@JsonIgnoreProperties({"ticketConds", "ticketCondition"})
public class  Flight {

    private long flight_fare_id;
    private String airlineCode;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String depart_date;
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String arrival_date;
    private String original;
    private String destination;
    private int stops;
    private String duration;
    private String currency;
    private int adult;
    private int child;
    private int infant;
    private long priceAdult;
    private long priceChild;
    private long priceInfant;
    private long feeAdult;
    private long feeChild;
    private long feeInfant;
    private long taxAdult;
    private long taxChild;
    private long taxInfant;
    private long feeVnpay;
    private long totalPrice;
    @JsonProperty("FlightSegments")
    private FlightSegment[] FlightSegments;
    private int isDeparture;
    @JsonProperty("isSelected")
    private boolean isSelected;
    private long pay_now;
    private long feeSevAdult;
    private long feeSevChild;
    private long feeSevInfant;
    private String classOfService;
    private String additionalData;

    public String priceToString(){
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("en", "EN"));
        return numberFormat.format(totalPrice) + " VNĐ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return flight_fare_id == flight.flight_fare_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(flight_fare_id);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flight_fare_id=" + flight_fare_id +
                ", airlineCode='" + airlineCode + '\'' +
                ", depart_date='" + depart_date + '\'' +
                ", arrival_date='" + arrival_date + '\'' +
                ", original='" + original + '\'' +
                ", destination='" + destination + '\'' +
                ", stops=" + stops +
                ", duration='" + duration + '\'' +
                ", currency='" + currency + '\'' +
                ", adult=" + adult +
                ", child=" + child +
                ", infant=" + infant +
                ", priceAdult=" + priceAdult +
                ", priceChild=" + priceChild +
                ", priceInfant=" + priceInfant +
                ", feeAdult=" + feeAdult +
                ", feeChild=" + feeChild +
                ", feeInfant=" + feeInfant +
                ", taxAdult=" + taxAdult +
                ", taxChild=" + taxChild +
                ", taxInfant=" + taxInfant +
                ", feeVnpay=" + feeVnpay +
                ", totalPrice=" + totalPrice +
                ", FlightSegments=" + Arrays.toString(FlightSegments) +
                '}';
    }
}
