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

//    @PostMapping("/main")
//    public String add(
//            @RequestParam("file") MultipartFile uploadData,
//            @RequestParam String name,
//            @RequestParam String fileDesc,
//            @RequestParam String codeName,
//            @RequestParam String OKCcode,
//            @RequestParam String OKPDcode,
//            @RequestParam String adoptionDate,
//            @RequestParam String introductionDate,
//            @RequestParam String developer,
//            @RequestParam String predecessor,
//            @RequestParam String contents,
//            @RequestParam String levelOfAcceptance,
//            @RequestParam String changes,
//            @RequestParam String status,
//            @RequestParam String referencesAmount,
//            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
//        Data data = new Data(
//                name,
//                fileDesc,
//                user,
//                codeName,
//                OKCcode,
//                OKPDcode,
//                adoptionDate,
//                introductionDate,
//                developer,
//                predecessor,
//                contents,
//                levelOfAcceptance,
//                changes,
//                status,
//                referencesAmount
//        );
//        if (uploadData != null && !uploadData.getOriginalFilename().isEmpty()) {
//            File uploadFolder = new File(uploadPath);
//            if (!uploadFolder.exists()) {
//                uploadFolder.mkdir();
//            }
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFileName = uuidFile + "." + uploadData.getOriginalFilename();
//            uploadData.transferTo(new File(uploadPath + "/" + resultFileName));
//            data.setFilename(resultFileName);
//        }
//        fileRepo.save(data);
//        Iterable<Data> messages = fileRepo.findAll();
//        model.put("messages", messages);
//        return "main";
//    }

    private String li(String str) {
        return '%' + str + '%';
    }
}
