package com.vnpay.airbooking.controller;

import com.vnpay.airbooking.config.Config;
import com.vnpay.airbooking.entity.*;
import com.vnpay.airbooking.error_enum.ErrorCodeServer;
import com.vnpay.airbooking.error_enum.ErrorException;
import com.vnpay.airbooking.model.form.BookingForm;
import com.vnpay.airbooking.model.json.Flight;
import com.vnpay.airbooking.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author annv
 */
@Slf4j
@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private AirportService airportService;

    //haotx
    @Value("${app.config.return-url-local}")
    public String returnUrlLocal;

    @Value("${app.config.return-url-server}")
    public String returnUrlServer;

    //2020-08-13, annv, hien thi thong tin thanh toan
    @PostMapping(value = "/showpayment")
    public String showPayment(HttpServletRequest request, Model model) {
        String bookCode = request.getParameter("bookCode");
        Long amount = Long.parseLong(request.getParameter("amount"));
        model.addAttribute("bookCode", bookCode);
        model.addAttribute("amount", amount);

        return "PaymentInfo";
    }

    //2020-08-13, annv, tao url thanh toan, luu payment xuong db
    @PostMapping(value = "/payment")
    public String payment(HttpServletRequest request,
                          @Value("${vnp-pay-url}") String vnp_PayUrl,
                          @Value("${vnp-version}") String vnp_Version,
                          @Value("${vnp-command}") String vnp_Command,
                          @Value("${order-type}") String orderType,
                          @Value("${vnp-tmn-code}") String vnp_TmnCode,
                          @Value("${locate}") String locate,
                          @Value("${vnp-hash-secret}") String vnp_HashSecret) throws UnknownHostException {
        String vnp_OrderInfo = request.getParameter("orderInfor");
        String vnp_TxnRef = Config.getRandomNumber(8);
        String vnp_IpAddr = Config.getIpAddress(request);

        long amount = Integer.parseInt(request.getParameter("amount")) * 100;

        Payment payment = new Payment();
        payment.setId(Long.parseLong(vnp_TxnRef));
        payment.setAmount(amount / 100);
        payment.setVersion(vnp_Version);
        payment.setCommand(vnp_Command);
        payment.setOrderInfor(vnp_OrderInfo);
        payment.setOrderType(orderType);
        payment.setIpAddr(vnp_IpAddr);
        payment.setTmnCode(vnp_TmnCode);
        payment.setLocale(locate);
        payment.setCurrCode("VND");

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        String bank_code = request.getParameter("bankcode");
        if (bank_code != null && !bank_code.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bank_code);
            payment.setBankcode(bank_code);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", locate);

        vnp_Params.put("vnp_ReturnUrl", returnUrlLocal);
//        vnp_Params.put("vnp_ReturnUrl", returnUrlServer);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Date dt = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(dt);
        String vnp_CreateDate = dateString;
        String vnp_TransDate = vnp_CreateDate;
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        payment.setCreateDate(vnp_CreateDate);

        //Build data to hash and querystring
        List fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(fieldValue);
                //Build query
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = Config.Sha256(vnp_HashSecret + hashData.toString());
        //System.out.println("HashData=" + hashData.toString());
        queryUrl += "&vnp_SecureHashType=SHA256&vnp_SecureHash=" + vnp_SecureHash;

        HttpSession session = request.getSession();

        payment.setSecureHashType("SHA256");
        payment.setSecureHash(vnp_SecureHash);

        payment.setReturnUrl(returnUrlLocal);
//        payment.setReturnUrl(returnUrlServer);

        payment.setStatus(false);
        payment.setBookingInfor((BookingInfor) session.getAttribute("bookingInfor"));
        session.removeAttribute("bookingInfor"); // sua code 120820 - annv

        paymentService.save(payment); // save payment

        String paymentUrl = vnp_PayUrl + "?" + queryUrl;
        log.info("payment url: " + paymentUrl); // 2020-08-18, annv, ghi log

        return "redirect:" + paymentUrl;

    }

    //2020-08-13, annv, get result from IPN url
    @GetMapping(value = "/result")
    public String result(HttpServletRequest request) {
        String s = request.getParameter("vnp_ResponseCode");
        log.info("vnp_ResponseCode: " + s);
        if (s.equals("24")) {
            request.getSession().setAttribute("vnp_ResponseCode", s);
            return "redirect:/paysuccess";
        }
        request.getSession().setAttribute("vnp_ResponseCode", s);
        Payment payment = paymentService.findById(Long.parseLong(request.getParameter("vnp_TxnRef")));
        payment.setStatus(true);
        payment.setBankcode(request.getParameter("vnp_BankCode"));
        paymentService.save(payment);

        return "redirect:/paysuccess";
    }

    //2020-08-13, annv, thong bao ket qua thanh toan toi nguoi dung
    @GetMapping(value = "/paysuccess")
    public String payResult(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("status") == null) {
            model.addAttribute("status", ErrorException.ERROR2.getMess());
            return "error";
        }
        Flight flightDetailDepart = (Flight) session.getAttribute("depart_flight");

        model.addAttribute("type", "1");
        model.addAttribute("orig_place", airportService.findByCode(flightDetailDepart.getOriginal()).getAirport_name() + " (" + flightDetailDepart.getOriginal() + ")");
        model.addAttribute("des_place", airportService.findByCode(flightDetailDepart.getDestination()).getAirport_name() + " (" + flightDetailDepart.getDestination() + ")");
        model.addAttribute("depart_flight", flightDetailDepart);

        if (session.getAttribute("return_flight") != null) {
            model.addAttribute("type", "2");
            Flight flightDetailReturn = (Flight) session.getAttribute("return_flight");
            model.addAttribute("return_flight", flightDetailReturn);
            session.removeAttribute("return_flight");
        }

        BookingForm passengerDetail = (BookingForm) session.getAttribute("passengerDetail");
        model.addAttribute("bookCode", session.getAttribute("bookCode"));
        model.addAttribute("passengerList", passengerDetail.getPassengerList());
        model.addAttribute("payer", passengerDetail.getPayer());
        model.addAttribute("totalPrice_t", session.getAttribute("totalPrice_t"));
        if (session.getAttribute("vnp_ResponseCode").equals("24")) {
            session.setAttribute("status", "canceled");
        } else {
            // remove session data when paysuccess
            session.setAttribute("status", "paid");
            session.removeAttribute("passengerDetail");
            session.removeAttribute("bookCode");
            session.removeAttribute("totalPrice_t");
            session.removeAttribute("depart_flight");
        }


        return "BookingInfo";
    }
}
