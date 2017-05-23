package com.spring.websocket.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by Je.vinci.Inc. on 2017. 4. 24..
 */

@Data
public class Message {
    private String name;
    private String content;
    private Date sendDate;
    private double lng;
    private double lat;
}
