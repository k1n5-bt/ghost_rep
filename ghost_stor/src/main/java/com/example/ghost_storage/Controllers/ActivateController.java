package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Model.Company;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Services.CompanyService;
import com.example.ghost_storage.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.Map;

@Controller
public class ActivateController {
    private final UserService userService;
    private final CompanyService companyService;

    public ActivateController(UserService userService, CompanyService companyService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated)
            model.addAttribute("message", "User successfully activated!");
        else
            model.addAttribute("message", "Activation code is not found!");

        return "login";
    }
}
