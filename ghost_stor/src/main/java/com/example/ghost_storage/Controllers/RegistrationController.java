package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registration(Map<String, Object> model) {
        return "registration";
    }

    @PostMapping("user")
    public String addUser(User user, Map<String, Object> model) throws IOException {
        if (!userService.addUser(user)) {
            model.put("message", "Username or email exists!");
            return "registration";
        }

        return "check";
    }
}
