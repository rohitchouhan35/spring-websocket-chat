package com.rohitchouhan35.springwebsocket.controller;

import com.rohitchouhan35.springwebsocket.model.Message;
import com.rohitchouhan35.springwebsocket.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.time.LocalDateTime;

@CrossOrigin("*")
@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        logger.info("Executed chatroom");
        message.setTimestamp(LocalDateTime.now());
        return message;
    }

    @MessageMapping("/private-message")
    public ResponseEntity<String> recMessage(@Payload Message message) {
        logger.info("Executed private");
        chatService.forwardMessage(message);
        return new ResponseEntity<>("Message sent to " + message.getReceiverName(), HttpStatus.OK);
    }

    @MessageMapping("/join-group")
    public ResponseEntity<String> joinGroup(@Payload String groupName, Principal principal) {
        chatService.joinGroup(groupName, principal);
        return new ResponseEntity<>("User added to group successfully", HttpStatus.OK);
    }

    @MessageMapping("/leave-group")
    public ResponseEntity<String> leaveGroup(@Payload String groupName, Principal principal) {
        chatService.leaveGroup(groupName, principal);
        return new ResponseEntity<>("User removed from group successfully", HttpStatus.OK);
    }

}
