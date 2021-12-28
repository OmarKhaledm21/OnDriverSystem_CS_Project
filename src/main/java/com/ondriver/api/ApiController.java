package com.ondriver.api;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/ondriver")
@RestController
public class ApiController {
    @GetMapping
    public String testConnection(){
        return "It Works :D";
    }
}
