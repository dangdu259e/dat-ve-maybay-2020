package com.vnpay.airbooking.controller;

import com.vnpay.airbooking.common.DateFormatter;
import com.vnpay.airbooking.common.JsonConverter;
import com.vnpay.airbooking.config.Config;
import com.vnpay.airbooking.entity.BookingInfor;
import com.vnpay.airbooking.entity.FlightDetail;
import com.vnpay.airbooking.entity.Passenger;
import com.vnpay.airbooking.entity.Payer;
import com.vnpay.airbooking.error_enum.ErrorCodeServer;
import com.vnpay.airbooking.error_enum.ErrorException;
import com.vnpay.airbooking.model.form.BookingForm;
import com.vnpay.airbooking.model.json.Flight;
import com.vnpay.airbooking.model.json.FlightData;
import com.vnpay.airbooking.model.response.BookingResponse;
import com.vnpay.airbooking.model.request.BookingRequest;
import com.vnpay.airbooking.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * @author annv
 */
@Slf4j
@Controller
public class BookController {
    @Autowired
    private AirportService airportService;
    @Autowired
    private PayerService payerService;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private FlightDetailService flightDetailService;
    @Autowired
    private BookingService bookingService;

    private int bookTimeout = 120;

    @Value("${booking-url}")
    private String bookUrl;

    private Flight getFlight(Long id, List<Flight> flights) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getFlight_fare_id() == id) {
                return flights.get(i);
            }
        }
        return null;
    }

    /**
     * @Date: 2020-08-02
     * @Created by: @thuna
     * @desc: Xu ly tim kiem thong tin chuyen bay va dat ve
     */
    @PostMapping(value = "/passengerdetail")
    public String showPassengerForm(@RequestParam(name = "flight_fare_id_Depart") Long flight_fare_id_Depart,
                                    @RequestParam(name = "flight_fare_id_Return", required = false) Long flight_fare_id_Return,
                                    Model model, HttpSession session) {
        log.info("depart flight id: " + String.valueOf(flight_fare_id_Depart)); // 2020-08-18, annv, ghi log
        log.info("return flight id: " + String.valueOf(flight_fare_id_Return)); // 2020-08-18, annv, ghi log
        FlightData flightData = (FlightData) session.getAttribute("flightData");

        session.removeAttribute("flightData"); // remove flight data attribute

        // departure flight
        Flight flightDetailDepart = getFlight(flight_fare_id_Depart, flightData.getDepartureFlights());
        session.setAttribute("depart_flight", flightDetailDepart); // 2020-08-12, annv, sua session data
        String flightNumber = flightDetailDepart.getFlightSegments()[0].getFlightNumber();

        // Tim kiem chuyen bay chieu di
        model.addAttribute("flightDetailDepart", flightDetailDepart);
        model.addAttribute("orig_place", airportService.findByCode(flightDetailDepart.getOriginal()));
        model.addAttribute("des_place", airportService.findByCode(flightDetailDepart.getDestination()));
        model.addAttribute("number", flightNumber);

        long totalPrice = flightDetailDepart.getTotalPrice();
        String type = "0";
        //  Tim kiem chuyen bay chieu ve (2 chieu)
        if (flight_fare_id_Return != null) {
            type = "1";
            Flight flightDetailReturn = getFlight(flight_fare_id_Return, flightData.getReturnFlights());
            session.setAttribute("return_flight", flightDetailReturn); // 2020-08-12, annv, sua session data
            totalPrice += flightDetailReturn.getTotalPrice();
            model.addAttribute("flightDetailReturn", flightDetailReturn);
            String flightNumber_t = flightDetailReturn.getFlightSegments()[0].getFlightNumber();
            model.addAttribute("number_t", flightNumber_t);
            model.addAttribute("original_t", airportService.findByCode(flightDetailReturn.getOriginal()));
            model.addAttribute("destination_t", airportService.findByCode(flightDetailReturn.getDestination()));

        }

        model.addAttribute("totalPrice_t", String.valueOf(totalPrice));
        model.addAttribute("type_t", type);
        session.setAttribute("totalPrice_t", totalPrice);

        /**
         * @Created by: @dudt
         * @desc: get input infor from form and add to list
         */
        BookingForm bookingForm = new BookingForm();
        int n = flightDetailDepart.getAdult() + flightDetailDepart.getChild() + flightDetailDepart.getInfant();
        for (int i = 0; i < flightDetailDepart.getAdult(); i++) {
            Passenger passenger = new Passenger();
            passenger.setType("ADT");
            bookingForm.getPassengerList().add(passenger);
        }
        for (int i = 0; i < flightDetailDepart.getChild(); i++) {
            Passenger passenger = new Passenger();
            passenger.setType("CHD");
            bookingForm.getPassengerList().add(passenger);
        }
        for (int i = 0; i < flightDetailDepart.getInfant(); i++) {
            Passenger passenger = new Passenger();
            passenger.setType("INF");
            bookingForm.getPassengerList().add(passenger);
        }

        model.addAttribute("bookingForm", bookingForm);
        return "Booking";
    }

    //2020-08-03, dudt, xu ly thong tin khach hang dat ve
    @PostMapping(value = "/paymentinfor")
    public String showPaymentInfor(Model model, @ModelAttribute("bookingForm") BookingForm bookingForm, HttpSession session) {
        log.info("Recevie booking form: " + bookingForm); // 2020-08-18, annv, ghi log
        String inputPattern = "dd/MM/yyyy";
        String outputPattern = "yyyy-MM-dd";

        int n = bookingForm.getPassengerList().size();
        for (int i = 0; i < n; i++) {
            if (!bookingForm.getPassengerList().get(i).getType().equals("ADT")) {
                bookingForm.getPassengerList().get(i).setBirthday(DateFormatter.format(bookingForm.getPassengerList().get(i).getBirthday(), inputPattern, outputPattern));
            }
        }
        BookingRequest bookingRequest = new BookingRequest();

        model.addAttribute("type", "1");
        Flight flightDetailDepart = (Flight) session.getAttribute("depart_flight");
        model.addAttribute("orig_place", airportService.findByCode(flightDetailDepart.getOriginal()).getAirport_name() + " (" + flightDetailDepart.getOriginal() + ")");
        model.addAttribute("des_place", airportService.findByCode(flightDetailDepart.getDestination()).getAirport_name() + " (" + flightDetailDepart.getDestination() + ")");
        model.addAttribute("depart_flight", flightDetailDepart);
        bookingForm.setDepartFlightID(flightDetailDepart.getFlight_fare_id());

        FlightDetail returnFlightDetail = null;
        if (session.getAttribute("return_flight") != null) {
            model.addAttribute("type", "2");
            Flight flightDetailReturn = (Flight) session.getAttribute("return_flight");
            model.addAttribute("return_flight", flightDetailReturn);
            bookingForm.setReturnFlightID(flightDetailReturn.getFlight_fare_id());
            returnFlightDetail = new FlightDetail(flightDetailReturn.getFlight_fare_id(), flightDetailReturn.getFlightSegments()[0].getFlightNumber(), flightDetailReturn.getOriginal(), flightDetailReturn.getDestination(), flightDetailReturn.getDepart_date(), flightDetailReturn.getArrival_date(), "2");

        }

        bookingRequest.setData(bookingForm, Long.parseLong((String) session.getAttribute("query_id")));

        session.removeAttribute("query_id"); // 2020-08-12, annv, sua session data

//        String bookUrl = "https://vnticket.vnpaytest.vn/vnticketapi/make_booking?data={data}";   //url booking==> property

        String bookJson = JsonConverter.toJson(bookingRequest);
        log.info("Send book request: " + bookJson); // 2020-08-18, annv, ghi log

//        long startTime = System.nanoTime();
        // 2020-08-13, annv, bat loi tu api dat ve
        // 2020-08-19, dutdt, bat them loi tu api dat ve
        String result;
        try {
            RestTemplate restTemplate = new RestTemplate(Config.getClientHttpRequestFactory(bookTimeout));
            result = restTemplate.postForObject(bookUrl, bookingRequest, String.class, bookJson);

        } catch (HttpServerErrorException.GatewayTimeout e) {     //GetwayTimeout code 504
            ErrorException errorException = ErrorException.ERROR3;
            model.addAttribute("status", errorException.getMess());
            log.error(errorException.getCode()+":"+errorException.getMess());
            log.error( e.getMessage());
            return "error";

        } catch (HttpServerErrorException.BadGateway e) {       //BadGetway code 502
            ErrorException errorException = ErrorException.ERROR5;
            model.addAttribute("status", errorException.getMess());
            log.error(errorException.getMess());
            log.error(e.getMessage());
            return "error";

        } catch (Exception e) {
            ErrorException errorException = ErrorException.ERROR4; //Connect timeout code 522
            model.addAttribute("status", errorException.getMess());
            log.error(errorException.getMess());
            log.error(e.getMessage());
            return "error";
        }

//        long stopTime = System.nanoTime();
//        System.out.println(">>> log time: " +(stopTime - startTime));
        //check status response -> show result
        BookingResponse response = BookingResponse.toObject(result);
        log.info("Recevie seach response: " + response.toString()); // 2020-08-18, annv, ghi log));

        //2020-08-17, thuna, sua code
        if (!response.getCode().equals("00")) {
            model.addAttribute("status", ErrorCodeServer.getMessg(response.getCode()));
            ErrorCodeServer errorCodeServer;
            if (response.getCode().equals(ErrorCodeServer.getMessg(response.getCode()))) {
                model.addAttribute("status", ErrorCodeServer.getMessg(response.getCode()));
                log.error(ErrorCodeServer.getMessg(response.getCode()));
                return "error";
            }
        }


        model.addAttribute("bookCode", response.getBook_code());
        model.addAttribute("passengerList", bookingForm.getPassengerList());
        model.addAttribute("payer", bookingForm.getPayer());
        model.addAttribute("totalPrice_t", session.getAttribute("totalPrice_t"));

        session.setAttribute("status", "booked");
        session.setAttribute("bookCode", response.getBook_code());
        session.setAttribute("passengerDetail", bookingForm);
        session.setAttribute("depart_flight", flightDetailDepart);

        BookingInfor bookingInfor = new BookingInfor();
        bookingService.save(bookingInfor); // save booking infor
        FlightDetail departFlightDeatil = new FlightDetail(flightDetailDepart.getFlight_fare_id(), flightDetailDepart.getFlightSegments()[0].getFlightNumber(), flightDetailDepart.getOriginal(), flightDetailDepart.getDestination(), flightDetailDepart.getDepart_date(), flightDetailDepart.getArrival_date(), "1");
        flightDetailService.save(departFlightDeatil); // save depart flight
        if (returnFlightDetail != null) {
            flightDetailService.save(returnFlightDetail); // save return flight
            bookingInfor.getFlightDetails().add(returnFlightDetail);
            returnFlightDetail.getBookingInfors().add(bookingInfor);
        }

        bookingInfor.setBookCode(response.getBook_code());
        bookingInfor.setAmount((Long) session.getAttribute("totalPrice_t"));
        bookingInfor.getFlightDetails().add(departFlightDeatil);

        Payer payer = bookingForm.getPayer();
        payerService.save(payer); // save payer
        bookingInfor.setPayer(payer);

        List<Passenger> passengers = bookingForm.getPassengerList();
        passengerService.saveAll(passengers); // save passengers

        for (int i = 0; i < passengers.size(); i++) {

            passengers.get(i).getBookingInfors().add(bookingInfor);
            bookingInfor.getPassengerDetails().add(passengers.get(i));
        }

        departFlightDeatil.getBookingInfors().add(bookingInfor);
        payer.getBookingInfors().add(bookingInfor);

        payerService.save(payer); // update payer
        bookingService.save(bookingInfor); // update booking infor

        passengerService.saveAll(passengers); // update passengers detail
        flightDetailService.save(departFlightDeatil); // update depart flight


        session.setAttribute("bookingInfor", bookingInfor);
        return "BookingInfo";
    }
}
