package com.vnpay.airbooking.error_enum;

import lombok.Getter;

@Getter
public enum ErrorCodeServer {
//        CODE0("00", "Thành công"),
        CODE1("01", "Mã ứng dụng không hợp lệ"),
        CODE2("02", "Thông tin đầu vào không hợp lệ"),
        CODE3("03", "Không có phản hồi từ hãng hàng không"),
        CODE4("04", "Hết phiên giao dịch. Vui lòng thao tác lại từ đầu"),
        CODE5("05", "Dữ liệu QueryId thực sự đã tồn tại trong một Booking"),
        CODE6("06", "Không đặt được chỗ. Cần kiểm tra thêm tại API đối tác"),
        CODE7("07", "Không đặt được chỗ. Cần kiểm tra thêm tại API Vnpay"),
        CODE8("08", "Hệ thống đang bảo trì"),
        CODE10("10", "Không tìm thấy giao dịch"),
        CODE11("11", "Giao dịch đã/đang xuất hóa đơn hoặc đã hủy, ko được cập nhật thông tin hóa đơn"),
        CODE12("12", "Thông tin xuất hóa đơn không hợp lệ"),
        CODE13("13", "Đơn hàng không được giữ chỗ do thời gian để giữ chỗ trên hãng không hợp lệ"),
        CODE14("14", "Đơn hàng không được giữ chỗ do thời gian để giữ chỗ trên hãng không hợp lệ"),
        CODE15("15", "Ngày sinh của trẻ em hoặc em bé không đặt được chuyến 2 chiều (paxType chiều đi và chiều về khác nhau)"),
        CODE16("16", "Chuyến bay đã tăng giá"),
        CODE17("17", "Hãng hàng không đã thay đổi giá"),
        CODE18("18", "Số điệnthoại nhận Promotion code không hợp lệ--không có hoặc chuỗi GUID"),
        CODE19("19", "Mã Promotion code không hợp lệ"),
        CODE1001("1001", "Mã voucher không được tìm thấy hoặc đã hết hạn"),
        CODE1002("1002", "Số lượng mã giảm giá này đã được sử dụng hết"),
        CODE1003("1003", "Không tìm thấy chương trình khuyến mại phù hợp"),
        CODE1004("1004", "Giảm giá không được phép trong khoảng thời gian này"),
        CODE1005("1005", "Không tìm thấy chương trình khuyến mại phù hợp cho loại hành khách này."),
        CODE1006("1006", "Không tìm thấy chương trình khuyến mại phù hợp cho đường bay này"),
        CODE1007("1007", "Không tìm thấy chương trình khuyến mại phù hợp cho ngày khởi hành này"),
        CODE1008("1008", "Đã vượt quá ngân sách khuyến mại của đối tác"),
        CODE22("24", "Không giữ được chỗ chuyến bay Vietjet, JetStar trong 24h"),
        CODE50("50", "Mã thanh toán không tồn tại"),
        CODE55("55", "Hãng không cho phép giữ chỗ với các chuyến bay giá khuyến mãi (hạng Promo). Cần liên hệ VnTicket để biết thêm thông tin"),
        CODE77("77", "Sai định dạng họ tên hành khách"),
        CODE97("97", "Mã checksum không hợp lệ"),
        CODE99("99", "Lỗi ngoại lệ khác"),
        CODE100("100", "Không tìm thấy chuyến bay phù hợp"),
        ;

        private String code;
        private String mess;

        ErrorCodeServer(String code, String mess) {
            this.code = code;
            this.mess = mess;
        }

        public String getMess() {
                return mess + ".\n Xin quý khách vui lòng thử lại!";
        }

        public static String getMessg (String code) {
                for (ErrorCodeServer errorCode : ErrorCodeServer.values()) {
                        if (errorCode.getCode().equals(code)) {
                                return errorCode.getCode() + ": " + errorCode.getMess()+".\n Xin quý khách vui lòng thử lại!";
                        }
                }
                return CODE99.getMess()+".\n Xin quý khách vui lòng thử lại!";
        }
}
