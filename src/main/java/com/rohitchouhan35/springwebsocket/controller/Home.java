package com.rohitchouhan35.springwebsocket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RestController
@RequestMapping("/")
public class Home {

    @GetMapping
    public String home(){
        return "Hi, time now is: " + LocalDateTime.now();
    }

}
