package com.nexus.backend.controller;

import com.nexus.backend.service.AiService;
import com.nexus.backend.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    private EmailSenderService emailSenderService;

    @GetMapping("/mail")
    public String sendEmail(){
        emailSenderService.sendMail("atharvanighot@gmail.com", "Test", "Mail Sent By JAVA");
        return "Mail Sent";
    }

    @GetMapping("/home")
    public String home(){
        return "Welcome to Compliance Nexus";
    }


}
