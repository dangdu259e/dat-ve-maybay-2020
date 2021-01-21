package com.vnpay.airbooking.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 *
 * @author annv
 */
public class JsonConverter {

    private static ObjectMapper mapper = new ObjectMapper();
    public static String toJson(Object o){
        mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
