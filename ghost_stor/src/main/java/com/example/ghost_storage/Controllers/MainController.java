package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Services.UserService;
import com.example.ghost_storage.Storage.FileRepo;
import com.example.ghost_storage.Storage.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.example.ghost_storage.Model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
class MainController {
    private final FileRepo fileRepo;
    private final UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    public MainController(FileRepo fileRepo, UserService userService) {
        this.userService = userService;
        this.fileRepo = fileRepo;
    }

    @GetMapping("/")
    public String hello(){
        return "hellopage";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(defaultValue = "") String descFilter,
            @RequestParam(defaultValue = "") String nameFilter,
            Map<String, Object> model) {
//        Iterable<Data> messages = fileRepo.findByFileDescLikeAndNameLike(li(descFilter), li(nameFilter));
        Iterable<Data> messages = fileRepo.findByStateId(Data.State.ACTIVE.getValue());
        model.put("messages", messages);
        model.put("formAction", "/main");
        model.put("descFilter", descFilter);
        model.put("nameFilter", nameFilter);

        return "main";
    }

    @GetMapping("/archived")
    public String archived(
            Map<String, Object> model) {
        model.put("messages", fileRepo.findByArchived(true));
        return "archived_docs";
    }

    private String li(String str) {
        return '%' + str + '%';
    }
}
