package com.vnpay.airbooking.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vnpay.airbooking.model.form.SearchForm;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class SearchRequest {

    private String original;
    private String destination;
    private String itinerary_type;
    private String depart_date;
    private String return_date;
    private int adult;
    private int child;
    private int infant;
    private String[] prefer_airline = {"VN"};
    private String[] prefer_classify = {"VN-ECO", "VJ-ECO", "QH-ECO"};

    private String mobile= "0975966872";
    private String app_id= "THUCTAP|EN";
    private String request_id;
    private String request_date;
    private String checksum="0";
    private String version ="2.0";

    public void setData(SearchForm searchForm){
        this.itinerary_type = searchForm.getType_fight();
        this.original = searchForm.getOrig_place();
        this.destination = searchForm.getDes_place();
        this.depart_date = searchForm.getOrig_date();
        this.return_date = searchForm.getReturn_date();
        this.adult = searchForm.getAdult_number();
        this.child = searchForm.getChild_number();
        this.infant = searchForm.getBaby_number();
    }

    public SearchRequest(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime now = LocalDateTime.now();
        this.request_id = "An_request_at_" + dtf.format(now);
        this.request_date = dtf.format(now);
    }


}
