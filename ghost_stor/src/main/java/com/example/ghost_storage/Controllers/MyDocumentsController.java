package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Services.DataService;
import com.example.ghost_storage.Storage.FileRepo;
import com.example.ghost_storage.Storage.RelationRepo;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import com.example.ghost_storage.Model.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Controller
public class MyDocumentsController {
    private final FileRepo fileRepo;

    private final DataService dataService;
    private final RelationRepo relationRepo;

    public MyDocumentsController(FileRepo fileRepo, DataService dataService, RelationRepo relationRepo) {
        this.fileRepo = fileRepo;
        this.dataService = dataService;
        this.relationRepo = relationRepo;
    }

    @GetMapping("/archive")
    public String main(
            @RequestParam(defaultValue = "") String descFilter,
            @RequestParam(defaultValue = "") String nameFilter,
            Map<String, Object> model) {
//      Iterable<Data> messages = fileRepo.findByFileDescLikeAndNameLike(li(descFilter), li(nameFilter));
        Iterable<Data> messages = dataService.getArchiveData();
        model.put("messages", messages);
        model.put("levels", Data.acceptanceLevels());
        model.put("formAction", "/main");
        model.put("descFilter", descFilter);
        model.put("nameFilter", nameFilter);

        return "archived_docs";
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
        model.put("levels", Data.acceptanceLevels());
        model.put("fields", fields);
        model.put("fieldNames", file.fieldNames());
        model.put("ruFieldNames", file.ruFieldNames());

        model.put("activeLinks", dataService.getActiveLinkNames(file));
        model.put("inactiveLinks", file.getInactiveLinks());

        return "document_show";
    }


    @GetMapping("/archived_doc/{documentId}")
    public String showArchDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data file = fileRepo.findById(Integer.parseInt(documentId)).get(0);
        Map<String, String[]> fields = file.getAllValues();

        if (file.getState() != Data.State.CANCELED)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        model.put("document", file);
        model.put("fileName", Data.fieldNames());
        model.put("fields", fields);
        model.put("fieldNames", file.fieldNames());
        model.put("levels", Data.acceptanceLevels());
        model.put("ruFieldNames", file.ruFieldNames());
        model.put("activeLinks", dataService.getActiveLinkNames(file));
        model.put("inactiveLinks", file.getInactiveLinks());
        return "archived_document";
    }

    @GetMapping("/document")
    public String newDoc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws IOException {
        model.put("fieldNames", Data.fieldNames());
        model.put("ruFieldNames", Data.ruFieldNames());
        model.put("levels", Data.acceptanceLevels());
        model.put("ghostDescs", dataService.getGhostDesc().keySet().toArray(new String[0]));

        return "document_form";
    }

    @PostMapping("/document")
    public String createDoc(
            @RequestParam Map<String, String> params,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data doc = dataService.createDoc(file, params);
        return "redirect:/document/" + doc.getId();
    }

    @GetMapping("/document/{documentId}/edit")
    public String editDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            Map<String, String> lastFields = file.getLastValues();
            model.put("document", file);
            model.put("levels", Data.acceptanceLevels());
            model.put("lastFields", lastFields);
            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());

            model.put("ghostDescs", dataService.getGhostDesc().keySet().toArray(new String[0]));
            model.put("activeLinks", dataService.getActiveLinkNames(file));
            model.put("inactiveLinks", file.getInactiveLinks());
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
        try {
            Data doc = dataService.updateDoc(documentId, file, params);
            return "redirect:/document/" + doc.getId();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @GetMapping("/delete/{dataID}")
    public String delete(
            @PathVariable String dataID,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException {
        dataService.deleteDoc(dataID, user);
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
            dataService.archiveDocument(file);
            return "redirect:/archive";
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
            if (file.getState() == Data.State.CANCELED)
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
