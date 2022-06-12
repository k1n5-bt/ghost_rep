package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Model.Company;
import com.example.ghost_storage.Model.CompanyRole;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Services.CompanyService;
import com.example.ghost_storage.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final CompanyService companyService;

    public RegistrationController(UserService userService, CompanyService companyService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    @GetMapping
    public String registration(Map<String, Object> model) {
        model.put("companies", companyService.getCompanies());
        return "registration";
    }

    @PostMapping("user")
    public String addUser(User user, @RequestParam String companyName, Map<String, Object> model) throws IOException {
        Company company = companyService.findCompanyByName(companyName);

        if (!companyService.getAllowedEmails(company).contains(user.getEmail()))
            user.setCompany_roles(Collections.singleton(CompanyRole.REQUEST));
        else
            user.setCompany_roles(Collections.singleton(CompanyRole.USER));
        if (!userService.addUser(user, company)) {
            model.put("message", "Username or email exists!");
            model.put("companies", companyService.getCompanies());
            return "registration";
        }

        return "check";
    }

    @PostMapping("company")
    public String addCompany(User user, @RequestParam String createdCompanyName, Map<String, Object> model) {
        if (!companyService.createCompany(createdCompanyName, user)){
            model.put("message", "Company exists!");
            model.put("companies", companyService.getCompanies());
            return "registration";
        }
        Company company = companyService.findCompanyByName(createdCompanyName);
        if (!userService.addUser(user, company)) {
            model.put("message", "Username or email exists!");
            model.put("companies", companyService.getCompanies());
            return "registration";
        }

        return "check";
    }
}
