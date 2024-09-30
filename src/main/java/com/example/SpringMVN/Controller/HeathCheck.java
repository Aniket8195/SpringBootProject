package com.example.SpringMVN.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeathCheck {

    @GetMapping("/Heath-Check")
    public String heathCheck(){
        return "OK";
    }
}
