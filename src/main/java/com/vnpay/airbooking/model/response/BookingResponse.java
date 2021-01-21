package com.vnpay.airbooking.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingResponse {
    private String code;
    private String book_id;
    private String book_code;
    private String message;
    private String checksum;
    private String expire_date;
    private int pay_now;
    private int promotion_amount;
    private long total_amount;
    private long total_amount_payment;
    private String action;

    public static BookingResponse toObject(String json){
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.readValue(json, BookingResponse.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
        return new Gson().fromJson(json, BookingResponse.class);

    }

    @Override
    public String toString() {
        return "BookingResponse{" +
                "code='" + code + '\'' +
                ", book_id='" + book_id + '\'' +
                ", book_code='" + book_code + '\'' +
                ", message='" + message + '\'' +
                ", checksum='" + checksum + '\'' +
                ", expire_date='" + expire_date + '\'' +
                ", pay_now=" + pay_now +
                ", promotion_amount=" + promotion_amount +
                ", total_amount=" + total_amount +
                ", total_amount_payment=" + total_amount_payment +
                ", action='" + action + '\'' +
                '}';
    }
}
