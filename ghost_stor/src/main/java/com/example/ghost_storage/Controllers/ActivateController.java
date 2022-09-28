package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ActivateController {
    private final UserService userService;

    public ActivateController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);

        if (isActivated)
            model.addAttribute("message", "Активация прошла успешно!");
        else
            model.addAttribute("message", "Код активации не найден!");

        return "login";
    }
}
