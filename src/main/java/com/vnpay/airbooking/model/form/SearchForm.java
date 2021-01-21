package com.vnpay.airbooking.model.form;


import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class SearchForm {

    private String orig_place;
    private String des_place;
    private String type_fight = "2";
    private String orig_date;
    private String return_date;
    private int adult_number = 1;
    private int child_number;
    private int baby_number;

    public void formatDate(String inputPattern, String outputPattern) {
        try {
            this.orig_date = new SimpleDateFormat(outputPattern).format(new SimpleDateFormat(inputPattern).parse(orig_date));
            if (!return_date.equals("")) {
                this.return_date = new SimpleDateFormat(outputPattern).format(new SimpleDateFormat(inputPattern).parse(return_date));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String toString() {
        return "SearchForm{" +
                "orig_place='" + orig_place + '\'' +
                ", des_place='" + des_place + '\'' +
                ", orig_date='" + orig_date + '\'' +
                ", return_date='" + return_date + '\'' +
                ", adult_number=" + adult_number +
                ", child_number=" + child_number +
                ", baby_number=" + baby_number +
                '}';
    }
}
