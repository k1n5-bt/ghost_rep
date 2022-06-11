package com.example.ghost_storage.Controllers;


import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Services.CompanyService;
import com.example.ghost_storage.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private final UserService userService;
    private final CompanyService companyService;

    public CompanyController(UserService userService, CompanyService companyService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping("requests")
    public String requestsList(@AuthenticationPrincipal User user, Map<String, Object> model) {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        model.put("users", companyService.getUsersRequests(user.getCompany()));
        return "requests";
    }

    @GetMapping("requests/{req_user}")
    public String requests(@AuthenticationPrincipal User user, @PathVariable String req_user, Map<String, Object> model) {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        userService.addUserInCompany(req_user);
        return "requests";
    }

    @GetMapping("allow")
    public String allow(@AuthenticationPrincipal User user, Map<String, Object> model) {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();

        return "requests";
    }
}
