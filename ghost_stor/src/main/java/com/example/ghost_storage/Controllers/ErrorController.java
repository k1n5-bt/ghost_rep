package com.example.ghost_storage.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class ErrorController {

    @PostMapping("/errorActivation")
    public String errorActivation(){
        return "errorActivation";
    }
}
