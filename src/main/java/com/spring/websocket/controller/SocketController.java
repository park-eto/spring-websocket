package com.spring.websocket.controller;

import com.spring.websocket.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Je.vinci.Inc. on 2017. 4. 24..
 */

@Controller
public class SocketController {

    @Autowired
    HttpSession session;

    Logger logger = LoggerFactory.getLogger(SocketController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chats/{chatRoomId}")
    public void handleChat(@Payload Message message, @DestinationVariable("chatRoomId") String chatRoomId, MessageHeaders messageHeaders){
        logger.info(messageHeaders.toString());
        message.setSendDate(getTimestamp());
        this.simpMessagingTemplate.convertAndSend("/topic/chats."+chatRoomId, message);
    }

    private Date getTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    @ResponseBody
    @RequestMapping("/sessionId")
    public String sessionId() {
        return this.session.getId();
    }

    @MessageMapping("/hello/{chatRoomId}")
    public void helloChat(@Payload Message message, @DestinationVariable("chatRoomId") String chatRoomId){
        this.simpMessagingTemplate.convertAndSend("/topic/chats."+chatRoomId, message);
    }

    @MessageMapping("/bye/{chatRoomId}")
    public void byeChat(@Payload Message message, @DestinationVariable("chatRoomId") String chatRoomId){
        this.simpMessagingTemplate.convertAndSend("/topic/chats."+chatRoomId, message);
    }

}
