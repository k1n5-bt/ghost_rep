package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Storage.FileRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.example.ghost_storage.Model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class MyDocumentsController {
    private final FileRepo fileRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public MyDocumentsController(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    @GetMapping("/document/{documentId}")
    public String showDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException {
        Data file = fileRepo.findById(Integer.parseInt(documentId)).get(0);
        model.put("document", file);
        return "document_show";
    }

    @GetMapping("/document")
    public String newDoc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws IOException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        return "document_form";
    }

    @PostMapping("/document")
    public String createDoc(
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String fileDesc,
            @RequestParam(required = false, defaultValue = "") String codeName,
            @RequestParam(required = false, defaultValue = "") String OKCcode,
            @RequestParam(required = false, defaultValue = "") String OKPDcode,
            @RequestParam(required = false, defaultValue = "") String adoptionDate,
            @RequestParam(required = false, defaultValue = "") String introductionDate,
            @RequestParam(required = false, defaultValue = "") String developer,
            @RequestParam(required = false, defaultValue = "") String predecessor,
            @RequestParam(required = false, defaultValue = "") String contents,
            @RequestParam(required = false, defaultValue = "") String levelOfAcceptance,
            @RequestParam(required = false, defaultValue = "") String changes,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String referencesAmount,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();

        Data doc = new Data(
                name,
                fileDesc,
                user,
                codeName,
                OKCcode,
                OKPDcode,
                adoptionDate,
                introductionDate,
                developer,
                predecessor,
                contents,
                levelOfAcceptance,
                changes,
                status,
                referencesAmount
        );
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFileName = createFile(file);
            doc.setFilename(resultFileName);
        }
        fileRepo.save(doc);
        return "redirect:/document/" + doc.getId();
    }

    private String createFile(MultipartFile file) throws IOException {
        File uploadFolder = new File(uploadPath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        String resultFileName = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + "/" + resultFileName));
        return resultFileName;
    }

    @GetMapping("/document/{documentId}/edit")
    public String editDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            model.put("document", docs.get(0));
            return "document_form";
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @PostMapping("/document/{documentId}/edit")
    public String updateDoc(
            @RequestParam("file") MultipartFile file,
            @PathVariable String documentId,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "") String fileDesc,
            @RequestParam(required = false, defaultValue = "") String codeName,
            @RequestParam(required = false, defaultValue = "") String OKCcode,
            @RequestParam(required = false, defaultValue = "") String OKPDcode,
            @RequestParam(required = false, defaultValue = "") String adoptionDate,
            @RequestParam(required = false, defaultValue = "") String introductionDate,
            @RequestParam(required = false, defaultValue = "") String developer,
            @RequestParam(required = false, defaultValue = "") String predecessor,
            @RequestParam(required = false, defaultValue = "") String contents,
            @RequestParam(required = false, defaultValue = "") String levelOfAcceptance,
            @RequestParam(required = false, defaultValue = "") String changes,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String referencesAmount,

            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
        Data doc = docs.get(0);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFileName = createFile(file);
            doc.setFilename(resultFileName);
        }
        doc.setName(name);
        doc.setFileDesc(fileDesc);
        doc.setCodeName(codeName);
        doc.setOKCcode(OKCcode);
        doc.setOKPDcode(OKPDcode);
        doc.setAdoptionDate(adoptionDate);
        doc.setIntroductionDate(introductionDate);
        doc.setDeveloper(developer);
        doc.setPredecessor(predecessor);
        doc.setContents(contents);
        doc.setLevelOfAcceptance(levelOfAcceptance);
        doc.setChanges(changes);
        doc.setStatus(status);
        doc.setReferencesAmount(referencesAmount);
        fileRepo.save(doc);
        return "redirect:/document/" + doc.getId();
    }

    @GetMapping("/delete/{dataID}")
    public String delete(
            @PathVariable String dataID,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> dataList = fileRepo.findById(Integer.parseInt(dataID));
        if (dataList.isEmpty()) {
            return "redirect:/main";
        }
        Data data = dataList.get(0);
        boolean userIsAuthor = user.getId().equals(data.getAuthor().getId());
        if (!user.isAdminCompany() && !userIsAuthor) {
            return "redirect:/main";
        }
        if (data.getFilename().length() > 0) {
            File file = new File(uploadPath + "/" + data.getFilename());
            if (!file.delete()) throw new FileNotFoundException();
        }
        fileRepo.delete(data);

        model.put("messages", fileRepo.findAll());
        return "redirect:/main";
    }
}
