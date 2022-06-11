package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Storage.FileRepo;
import org.springframework.beans.factory.annotation.Value;
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

//    @GetMapping("/document")
//    public String newDoc() throws IOException {
//        return "document_form";
//    }
//
//    @PostMapping("/document")
//    public String createDoc(
//            @RequestParam String name,
//            @RequestParam String fileDesc,
////            @RequestParam("file") MultipartFile file,
//            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
//
////        if (uploadData != null && !uploadData.getOriginalFilename().isEmpty()) {}
//        Data doc = new Data(name, fileDesc, user);
//        fileRepo.save(doc);
//        return "redirect:/document/" + doc.getId();
//    }

//    @GetMapping("/document/{documentId}/edit")
//    public String editDoc(
//            @PathVariable String documentId,
//            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
//        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
//        if (docs.size() > 0) {
//            model.put("document", docs.get(0));
//            return "document_form";
//        } else {
//            return "not_found";
//        }
//    }
//
//    @PostMapping("/document/{documentId}/edit")
//    public String updateDoc(
////            @RequestParam("file") MultipartFile uploadData,
//            @PathVariable String documentId,
//            @RequestParam String name,
//            @RequestParam String fileDesc,
//            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
//        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
//        if (docs.size() == 0) {
//            return "not_found";
//        }
//        Data doc = docs.get(0);
//
//        doc.setName(name);
//        doc.setFileDesc(fileDesc);
//
//        fileRepo.save(doc);
//        return "redirect:/document/" + doc.getId();
//    }

//    public String adda(
//            @RequestParam("file") MultipartFile uploadData,
//            @RequestParam String name,
//            @RequestParam String fileDesc,
//            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
//        if (uploadData != null && !uploadData.getOriginalFilename().isEmpty()) {
//            Data data = new Data(name, fileDesc, user);
//            File uploadFolder = new File(uploadPath);
//            if (!uploadFolder.exists()) {
//                uploadFolder.mkdir();
//            }
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFileName = uuidFile + "." + uploadData.getOriginalFilename();
//            uploadData.transferTo(new File(uploadPath + "/" + resultFileName));
//            data.setFilename(resultFileName);
//            fileRepo.save(data);
//        }
//        model.put("messages", fileRepo.findByAuthor(user));
////        return "my_docs";
//        return "redirect:/document/" + "id";
//    }

//    @GetMapping("/my_docs")
//    public String my_docs(
//            @RequestParam(required = false, defaultValue = "") String descFilter,
//            @AuthenticationPrincipal User user,
//            Map<String, Object> model) throws FileNotFoundException {
//        Data file = fileRepo.findById(Integer.parseInt(documentId)).get(0);
//        model.put("document", file);
//        return "document_show";
//    }

    @GetMapping("/document")
    public String newDoc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws IOException {
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
//            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {

//        if (uploadData != null && !uploadData.getOriginalFilename().isEmpty()) {}
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
        fileRepo.save(doc);
        return "redirect:/document/" + doc.getId();
    }


    @GetMapping("/document/{documentId}/edit")
    public String editDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            model.put("document", docs.get(0));
            return "document_form";
        } else {
            return "not_found";
        }
    }

    @PostMapping("/document/{documentId}/edit")
    public String updateDoc(
//            @RequestParam("file") MultipartFile uploadData,
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
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() == 0) {
            return "not_found";
        }
        Data doc = docs.get(0);
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
        List<Data> dataList = fileRepo.findById(Integer.parseInt(dataID));
        if (dataList.isEmpty()) {
            return "redirect:/main";
        }
        Data data = dataList.get(0);
        boolean userIsAuthor = user.getId().equals(data.getAuthor().getId());
        if (!user.isAdmin() && !userIsAuthor) {
            return "redirect:/main";
        }
        if ( data.getFilename().length() > 0) {
            File file = new File(uploadPath + "/" + data.getFilename());
            if (!file.delete()) throw new FileNotFoundException();
        }
        fileRepo.delete(data);

        model.put("messages", fileRepo.findAll());
        return "redirect:/main";
    }
}
