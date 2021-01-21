package com.vnpay.airbooking.model.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.vnpay.airbooking.model.json.FlightData;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponse {
    private String code;
    private String message;
    private String app_id;
    private String request_id;
    private String query_id;
    private FlightData flightData;
    private String checksum;
    private String action;

    public static SearchResponse toObject(String json){
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            return mapper.readValue(json, SearchResponse.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
        return new Gson().fromJson(json, SearchResponse.class);
    }
    @Override
    public String toString() {
        return "SearchResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", query_id='" + query_id + '\'' +
                ", flightData=" + flightData +
                '}';
    }
}
