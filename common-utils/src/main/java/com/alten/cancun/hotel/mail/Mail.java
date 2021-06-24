package com.alten.cancun.hotel.mail;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mail {

    private String from;

    private String to;

    private String subject;

    private String content;

    private String contentType;

    private String user;

    private String startDate;

    private String endDate;

}
