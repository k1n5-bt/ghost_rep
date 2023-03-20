package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Model.Role;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Storage.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    private final UserRepo userRepo;

    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public String userList(@AuthenticationPrincipal User currentUser, Model model) {
        if (!currentUser.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user, Model model) {
        if (!currentUser.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @GetMapping("/delete/{user}")
    public String deleteUser(@AuthenticationPrincipal User currentUser,
                             @PathVariable User user, Model model){
        if (!currentUser.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        userRepo.delete(user);
        return "redirect:/user";
    }

    @PostMapping
    public String userSave(
            @AuthenticationPrincipal User currentUser,
            @RequestParam String username,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String patronymic,
            @RequestParam String field,
            @RequestParam String division,
            @RequestParam String company,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        if (!currentUser.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        if (userRepo.findByUsername(username) == null)
            user.setUsername(username);
        user.setName(name);
        user.setSurname(surname);
        user.setPatronymic(patronymic);
        user.setField(field);
        user.setDivision(division);
        user.setCompany(company);

        user.getRoles().clear();

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        for (String key : form.keySet()){
            if (roles.contains(key))
                user.getRoles().add(Role.valueOf(key));
        }

        userRepo.save(user);
        return "redirect:/user";
    }
}
