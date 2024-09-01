package com.autoboardaws.autoboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ViewController {

    @PostMapping("/auth/login")
    public String loginPage() {
        return "/static/index.html";
    }

}
