package com.example.springSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/show")
    public @ResponseBody String show() {

        return "<h1>/admin/show</h1>";
    }
}
