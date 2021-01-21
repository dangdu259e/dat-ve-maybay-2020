package com.vnpay.airbooking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "payment")
public class Payment {
    @Id
    @Column(name ="vnp_txn_ref")
    private Long id;

    @Column(name = "vnp_amount")
    private long amount;

    @Column(name = "vnp_bank_code")
    private String bankcode;

    @Column(name = "vnp_command")
    private String command;

    @Column(name = "vnp_create_date")
    private String createDate;

    @Column(name = "vnp_curr_code")
    private String currCode;

    @Column(name = "vnp_ip_addr")
    private String ipAddr;

    @Column(name = "vnp_locale")
    private String locale;

    @Column(name = "vnp_order_info")
    private String orderInfor;

    @Column(name = "vnp_order_typ")
    private String orderType;

    @Column(name = "vnp_return_url")
    private String returnUrl;

    @Column(name = "vnp_tmn_code")
    private String tmnCode;

    @Column(name = "vnp_version")
    private String version;

    @Column(name = "vnp_secure_hash_type")
    private String secureHashType;

    @Column(name = "vnp_secure_hash")
    private String secureHash;

    @Column(name = "status")
    private Boolean status = false;

    @OneToOne
    @JoinColumn(name = "book_id")
    private BookingInfor bookingInfor;

}
