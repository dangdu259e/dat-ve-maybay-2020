package com.vnpay.airbooking.error_enum;

import lombok.Getter;

@Getter
public enum ErrorException {
    ERROR1 ("100", "Không tìm thấy chuyến bay phù hợp. Xin quý khách vui lòng thử lại"),
    ERROR2 ("404", "Quý khách chưa đặt vé. Vui lòng quay lại trang chủ"),
    ERROR3 ("504", "Không nhận được phản hồi từ máy chủ. Xin Quý khách vui lòng thử lại"),
    ERROR4 ("522", "Đã hết thời gian kết nối phiên giao dịch. Xin quý khách vui lòng thử lại"),
    ERROR5 ("502", "Đã xảy ra lỗi máy chủ. Xin quý khách vui lòng thử lại sau"),

    ;

    private String code;
    private String mess;

    ErrorException (String code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public static String getMessg (String code) {
        for (ErrorException errorException : ErrorException.values()) {
            if (errorException.getCode().equals(code)) {
                return "Lỗi " + errorException.getCode() +": " + errorException.getMess();
            }
        }
        return "";
    }
}
