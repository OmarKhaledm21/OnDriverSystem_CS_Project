package com.ondriver.api;

import com.ondriver.OnDriverSystem;
import com.ondriver.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("api/v1/ondriver")
@RestController
public class ApiController {
    @GetMapping
    public String testConnection(){
        return "It Works :D";
    }

    @GetMapping(path = "/db")
    public ArrayList<User> selectAll(){
        return OnDriverSystem.getSystem().selectAll();
    }
}
