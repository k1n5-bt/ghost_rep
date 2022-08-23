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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.ghost_storage.Model.Data.maxFieldNames;

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
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data file = fileRepo.findById(Integer.parseInt(documentId)).get(0);
        Map<String, String[]> fields = file.getAllValues();
        model.put("document", file);
        model.put("fileName", Data.fieldNames());

        model.put("fields", fields);
        model.put("fieldNames", file.fieldNames());
        model.put("ruFieldNames", file.ruFieldNames());
        return "document_show";
    }


    @GetMapping("/archived_doc/{documentId}")
    public String showArchDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data file = fileRepo.findById(Integer.parseInt(documentId)).get(0);
        Map<String, String[]> fields = file.getAllValues();
        if (!file.getArchivalStatus())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        model.put("document", file);
        model.put("fileName", Data.fieldNames());
        model.put("fields", fields);
        model.put("fieldNames", file.fieldNames());
        model.put("ruFieldNames", file.ruFieldNames());
        return "archived_document";
    }

    @GetMapping("/document")
    public String newDoc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws IOException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        model.put("fieldNames", Data.fieldNames());
        model.put("ruFieldNames", Data.ruFieldNames());
        return "document_form";
    }

    @PostMapping("/document")
    public String createDoc(
            @RequestParam Map<String, String> params,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        Data doc = new Data();
        Map<String, String> emptyValues = doc.emptyFieldValues();
        for (String fieldName : Data.fieldNames()) {
            String newValue = params.get(fieldName);
            String oldValue = emptyValues.get(fieldName);
            if (!newValue.equals(oldValue)) {
                setLastName(doc, fieldName, newValue);
            }
        }

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
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            Map<String, String> lastFields = file.getLastValues();
            model.put("document", file);
            model.put("lastFields", lastFields);
            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());
            return "document_form";
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @PostMapping("/document/{documentId}/edit")
    public String updateDoc(
            @RequestParam("file") MultipartFile file,
            @PathVariable String documentId,
            @RequestParam Map<String, String> params,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdminCompany())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() == 0 || docs.get(0).getArchivalStatus())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        Data doc = docs.get(0);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            if (doc.getFilename().length() > 0) {
                File oldFile = new File(uploadPath + "/" + doc.getFilename());
                if (!oldFile.delete()) throw new FileNotFoundException();
            }
            String resultFileName = createFile(file);
            doc.setFilename(resultFileName);
        }
        Map<String, String> lastValues = doc.getLastValues();

        for (String fieldName : Data.fieldNames()) {
            String newValue = params.get(fieldName);
            String oldValue = lastValues.get(fieldName);

            if (!newValue.equals(oldValue)) {
                setLastName(doc, fieldName, newValue);
            }
        }
        fileRepo.save(doc);
        return "redirect:/document/" + doc.getId();
    }

    public void setLastName(Data file, String fieldName, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "FirstRedaction").invoke(file);
        if (obj == null) {
            obj = file.getClass().getMethod("get" + maxFieldNames().get(fieldName)).invoke(file);
            if (obj == null) {
                file.getClass().getMethod("set" + maxFieldNames().get(fieldName), value.getClass()).invoke(file, value);
            } else {
                file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "FirstRedaction", value.getClass()).invoke(file, value);
            }
        } else {
            String value2 = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "FirstRedaction").invoke(file).toString();
            file.getClass().getMethod("set" + maxFieldNames().get(fieldName), value2.getClass()).invoke(file, value2);
            file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "FirstRedaction", value.getClass()).invoke(file, value);
        }
    }

    @GetMapping("/delete/{dataID}")
    public String delete(
            @PathVariable String dataID,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> dataList = fileRepo.findById(Integer.parseInt(dataID));
        if (dataList.isEmpty()) {
            return "redirect:/main";
        }
        Data data = dataList.get(0);
        boolean userIsAuthor = true;
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

    @GetMapping("/document/{documentId}/archive")
    public String archiveDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            file.setArchived();
            fileRepo.save(file);
            return "redirect:/archived";
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @GetMapping("/document/{documentId}/replace")
    public String replaceDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            if (file.getArchivalStatus())
                return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();


            Map<String, String> lastFields = file.getLastValues();
            model.put("document", file);
            model.put("lastFields", lastFields);
            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());
            return "document_form";

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }
}
