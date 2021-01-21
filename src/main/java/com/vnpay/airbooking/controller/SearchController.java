package com.vnpay.airbooking.controller;

import com.vnpay.airbooking.common.DateFormatter;
import com.vnpay.airbooking.common.JsonConverter;
import com.vnpay.airbooking.config.Config;
import com.vnpay.airbooking.error_enum.ErrorCodeServer;
import com.vnpay.airbooking.error_enum.ErrorException;
import com.vnpay.airbooking.model.json.Flight;
import com.vnpay.airbooking.entity.*;
import com.vnpay.airbooking.model.form.SearchForm;
import com.vnpay.airbooking.model.response.SearchResponse;
import com.vnpay.airbooking.model.request.SearchRequest;
import com.vnpay.airbooking.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.net.ConnectException;
import java.util.*;
/**
 *
 * @author annv
 */
@Slf4j
@Controller
public class  SearchController {

    @Autowired
    private AirportService airportService;

    private int searchTimeout = 120;

    @Value("${search-url}")
    private String search_url;

    @GetMapping(value = "/")
    public String home(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("status");
        List<Airport> airports = airportService.getAllAirport();
        SearchForm searchForm = new SearchForm();
        model.addAttribute("searchForm", searchForm);
        model.addAttribute("airports",airports);
        return "SeachFlight";
    }
    /**
     * @Created by: @thuna
     * @desc: Xu ly tim kiem ve
     */
    @PostMapping(value = "/search")
    public String searchFlight(Model model, @ModelAttribute("searchForm") SearchForm searchForm, HttpSession session) throws ConnectException {
        log.info("Receive searchForm: " + searchForm.toString()); // 2020-08-17, annv, ghi log
        RestTemplate restTemplate = new RestTemplate(Config.getClientHttpRequestFactory(searchTimeout));

        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";
        searchForm.formatDate(inputPattern, outputPattern);

        SearchRequest request = new SearchRequest();
        request.setData(searchForm);

        //Gui du lieu thong tin request va lay du lieu tim kiem ve tu API
        String data = JsonConverter.toJson(request); // convert request object thanh chuoi json
        log.info("Send search data: " + data); // 2020-08-17, annv, ghi log

        //2020-08-17, dudt, bat exception va ghi log, sua phan tra ket qua cho nguoi dung
        String result;
        try {
            result = restTemplate.getForObject(search_url, String.class, data); // gui json va lay ket qua ve tu API
        }catch (RestClientException e){
            ErrorException errorException = ErrorException.ERROR5;
            model.addAttribute("status", errorException.getMess());
            log.error(errorException.getCode()+ ": "+ errorException.getMess());
            log.error(e.getMessage());

            return "error";
        }catch (Exception e) {
            ErrorException errorException = ErrorException.ERROR4;      //connect timeout code 522
            model.addAttribute("status", errorException.getMess());
            log.error(errorException.getMess());
            log.error(e.getMessage());
            return "error";
        }

        SearchResponse searchResponse = SearchResponse.toObject(result);    // convert chuoi tra ve thanh object
        log.info("Receive search response: " + searchResponse.toString()); // 2020-08-17, annv, ghi log
        if(searchResponse.getFlightData() == null|| searchResponse.getFlightData().getDepartureFlights().isEmpty()){
            ErrorCodeServer errorCodeServer = ErrorCodeServer.CODE100;
            model.addAttribute("status", errorCodeServer.getMess() + ".\n Quý khách vui lòng thử lại.");
            log.error("Flight not found");
            return "error";
        }
        log.info("query id: " + searchResponse.getQuery_id()); // 2020-08-17, annv, ghi log
        session.setAttribute("query_id", searchResponse.getQuery_id());
        formatFlightDate(searchResponse.getFlightData().getDepartureFlights());
        formatFlightDate(searchResponse.getFlightData().getReturnFlights());
        session.setAttribute("flightData", searchResponse.getFlightData());

        model.addAttribute("type_fight", searchForm.getType_fight());

        model.addAttribute("flightData", searchResponse.getFlightData());

        model.addAttribute("orig_place", airportService.findByCode(searchResponse.getFlightData().getDepartureFlights().get(0).getOriginal()).getAirport_name());
        model.addAttribute("des_place", airportService.findByCode(searchResponse.getFlightData().getDepartureFlights().get(0).getDestination()).getAirport_name());

        return "FlightDetails";
    }
    // annv, dinh dang ngay cho cac chuyen bay
    private void formatFlightDate(List<Flight> flights) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd/MM/yyyy/HH:mm/ss";
        for(int i = 0; i < flights.size(); i++) {
            flights.get(i).setDepart_date(DateFormatter.format(flights.get(i).getDepart_date(), inputPattern,outputPattern));
            flights.get(i).setArrival_date(DateFormatter.format(flights.get(i).getArrival_date(), inputPattern, outputPattern));
        }
    }
 }