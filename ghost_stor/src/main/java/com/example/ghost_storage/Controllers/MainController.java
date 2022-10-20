package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Services.DataService;
import com.example.ghost_storage.Services.UserService;
import com.example.ghost_storage.Storage.FileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.example.ghost_storage.Model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@Controller
class MainController {
    private final FileRepo fileRepo;
    private final UserService userService;
    private final DataService dataService;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(FileRepo fileRepo, UserService userService, DataService dataService) {
        this.userService = userService;
        this.fileRepo = fileRepo;
        this.dataService = dataService;
    }

    @GetMapping("/")
    public String hello(){return "hellopage";}

    @GetMapping("/main")
    public String main(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "false") Boolean isFavorites,
            @RequestParam(defaultValue = "") String descFilter,
            Map<String, Object> model) {
        Iterable<Data> messages;
        if (isFavorites) {
            messages = fileRepo.findFavoritesData(user.getId().toString());
        } else if (descFilter.equals("")) {
            messages = fileRepo.findByStateId(State.ACTIVE.getValue());
        } else {
            messages = fileRepo.findByStateIdAndFileDescLike(State.ACTIVE.getValue(), dataService.li(descFilter));
        }
        model.put("messages", messages);
        model.put("formAction", "/main");
        model.put("descFilter", descFilter);
        return "main";
    }

    private String li(String str) {
        return '%' + str + '%';
    }
}
