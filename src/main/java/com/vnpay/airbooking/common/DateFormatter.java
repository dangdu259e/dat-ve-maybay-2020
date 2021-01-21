package com.vnpay.airbooking.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
/**
 *
 * @author annv
 */
public class DateFormatter {
    public static String format(String date, String inputPattern, String outputPattern){
        try {
            return new SimpleDateFormat(outputPattern).format(new SimpleDateFormat(inputPattern).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}
