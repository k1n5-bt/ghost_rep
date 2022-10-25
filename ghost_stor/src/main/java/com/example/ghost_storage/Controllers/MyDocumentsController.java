package com.example.ghost_storage.Controllers;

import com.example.ghost_storage.Services.DataService;
import com.example.ghost_storage.Services.MailSender;
import com.example.ghost_storage.Services.RawEditService;
import com.example.ghost_storage.Services.StatisticService;
import com.example.ghost_storage.Storage.ActionStatRepo;
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
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Controller
public class MyDocumentsController {
    private final FileRepo fileRepo;
    private final DataService dataService;
    private final RawEditService rawEditService;
    private final MailSender mailSender;
    private final RelationRepo relationRepo;
    private final StatisticService statisticService;

    public MyDocumentsController(FileRepo fileRepo, DataService dataService,
                                 RelationRepo relationRepo, MailSender mailSender, RawEditService rawEditService,
                                 StatisticService statisticService) {
        this.fileRepo = fileRepo;
        this.mailSender = mailSender;
        this.dataService = dataService;
        this.relationRepo = relationRepo;
        this.rawEditService = rawEditService;
        this.statisticService = statisticService;
    }

    @GetMapping("/archive")
    public String main(
            @RequestParam(defaultValue = "") String descFilter,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        Iterable<Data> messages = dataService.getArchiveData(descFilter);
        model.put("messages", messages);
        model.put("levels", Data.acceptanceLevels());
        model.put("descFilter", descFilter);

        return "archived_docs";
    }

    @GetMapping("/document/{documentId}")
    public String showDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        int id =  Integer.parseInt(documentId);
        Data file = fileRepo.findById(id).get(0);
        if (file.getState() == State.CANCELED)
            return "redirect:/main";

        if (!user.isAdmin())
            statisticService.commitCall(file);

        Map<String, String[]> fields = file.getAllValues();
        model.put("document", file);
        model.put("fileName", Data.fieldNames());
        model.put("levels", Data.acceptanceLevels());
        model.put("fields", fields);
        model.put("fieldNames", file.fieldNames());
        model.put("ruFieldNames", file.ruFieldNames());

        model.put("activeLinks", dataService.getActiveLinkNames(file));
        model.put("activeLinks_f", dataService.getActiveLinkFRNames(file));
        model.put("inactiveLinks", dataService.getInactiveLinkNames(file));
        model.put("inactiveLinks_f", dataService.getInactiveLinkFRNames(file));

        return "document_show";
    }


    @GetMapping("/archived_doc/{documentId}")
    public String showArchDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
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
        return "archived_document";
    }

    @GetMapping("/document")
    public String newDoc(
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        model.put("fieldNames", Data.fieldNames());
        model.put("ruFieldNames", Data.ruFieldNames());
        model.put("levels", Data.acceptanceLevels());
        model.put("ghostDescs", dataService.getGhostDesc());
        return "document_form";
    }

    @PostMapping("/document")
    public String createDoc(
            @RequestParam Map<String, String> params,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        Data doc = dataService.createDoc(file, params);
        return "redirect:/document/" + doc.getId();
    }

    @GetMapping("/document/{documentId}/raw_edit")
    public String rawEditDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            if (file.getState() == State.CANCELED)
                return "redirect:/main";


//            Map<String, String> lastFields = file.getLastValues();
//            model.put("lastFields", lastFields);
//            model.put("activeLinks", dataService.getLastActiveLinkNames(file));
//            model.put("inactiveLinks", dataService.getLastInactiveLinkNames(file));


            Map<String, String[]> allFields = file.getAllValues();
            model.put("document", file);
            model.put("allFields", allFields);
            model.put("levels", Data.acceptanceLevels());
            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());
            model.put("ghostDescs", dataService.getGhostDesc());

            model.put("frMap", rawEditService.frMap());

            model.put("activeLinks", dataService.getActiveLinkNames(file));
            model.put("activeLinks_f", dataService.getActiveLinkFRNames(file));
            model.put("inactiveLinks", dataService.getInactiveLinkNames(file));
            model.put("inactiveLinks_f", dataService.getInactiveLinkFRNames(file));


            return "raw_edit";
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @PostMapping("/document/{documentId}/raw_edit")
    public String rawUpdateDoc(
            @PathVariable String documentId,
            @RequestParam Map<String, String> params,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NotFoundException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        rawEditService.editDoc(documentId, params);
        return "redirect:/document/" + documentId;
    }

    @GetMapping("/document/{documentId}/edit")
    public String editDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            if (file.getState() == State.CANCELED)
                return "redirect:/main";
            Map<String, String> lastFields = file.getLastValues();
            model.put("document", file);
            model.put("levels", Data.acceptanceLevels());
            model.put("lastFields", lastFields);
            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());

            model.put("ghostDescs", dataService.getGhostDesc());
            model.put("activeLinks", dataService.getLastActiveLinkNames(file));
            model.put("inactiveLinks", dataService.getLastInactiveLinkNames(file));
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
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        try {
            Data doc = dataService.updateDoc(documentId, file, params);
            statisticService.commitEdit(doc);
            sendMessage(doc);
            return "redirect:/document/" + doc.getId();
        } catch (NotFoundException | ParseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }



    @GetMapping("/favorite/{documentId}")
    public String addToFavorite(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws Exception {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            file.addFavorite(user);
            fileRepo.save(file);
            return "redirect:/document/" + file.getId();
            }
         else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @GetMapping("/favorite/remove/{documentId}")
    public String removeFromFavorite(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws Exception {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            var fav = file.getFavorites();
            for (var i = 0; i < fav.size(); i++ ){
                User us = fav.get(i);
                if (us.getId().equals(user.getId()))
                    fav.remove(us);
            }
            file.setFavorites(fav);
            fileRepo.save(file);
            return "redirect:/document/" + file.getId();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    @GetMapping("/delete/{dataID}")
    public String delete(
            @PathVariable String dataID,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        dataService.deleteDoc(dataID, user);
        return "redirect:/main";
    }

    @GetMapping("/document/{documentId}/archive")
    public String archiveDoc(
            @PathVariable String documentId,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        if (!user.isAdmin())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).toString();
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() > 0) {
            Data file = docs.get(0);
            dataService.archiveDocument(file);
            statisticService.commitArchive(file);
            sendMessage(file);
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
            if (file.getState() == State.CANCELED)
                return "redirect:/main";
            sendMessage(file);
            Map<String, String> lastFields = file.getLastValues();
            model.put("parentDocDesc", "Взамен " + file.getLastDesc());
            model.put("parentDocId", file.getId());

            model.put("fieldNames", Data.fieldNames());
            model.put("ruFieldNames", Data.ruFieldNames());
            model.put("levels", Data.acceptanceLevels());
            model.put("ghostDescs", dataService.getGhostDesc());
            return "document_form";

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).toString();
        }
    }

    private void sendMessage(Data file){
        if (file.getFavorites().size() > 0){
            for (User us: file.getFavorites()){
                mailSender.send(us.getEmail(), "Обновление документа", "Добрый день! Документ был обновлен.");
            }
        }
    }

    @GetMapping("/search")
    public String search(
            @RequestParam Map<String, String> params,
            @AuthenticationPrincipal User user, Map<String, Object> model) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Iterable<Data> messages;
        String[] fields = DataService.searchFields();
        if (params.size() == 0) {
            messages = fileRepo.findByStateId(State.ACTIVE.getValue());
            for (String field : fields) {
                params.put(field, "");
            }
        } else {
            messages = dataService.findByParams(params);
        }
        model.put("params", params);
        model.put("ruFields", dataService.searchRuFields());
        model.put("fields", fields);
        model.put("messages", messages);
        model.put("levels", Data.acceptanceLevels());
        return "search_page";
    }
}
