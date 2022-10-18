package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.GhostRelation;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Storage.FileRepo;
import com.example.ghost_storage.Storage.RelationRepo;
import com.example.ghost_storage.Storage.UserRepo;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.ghost_storage.Model.Data.maxFieldNames;

@Service
public class RawEditService {
    private FileRepo fileRepo;
    private RelationRepo relationRepo;
    private DataService dataService;
    private Map<String, String> maxMap = Data.maxFieldNames();
    private String fr = "FirstRedaction";

    public RawEditService(FileRepo fileRepo, RelationRepo relationRepo, DataService dataService) {
        this.dataService = dataService;
        this.fileRepo = fileRepo;
        this.relationRepo = relationRepo;
    }

    public void setValue(Data file, String field, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String setMethodName = "set" + maxMap.get(field);
        file.getClass().getMethod(setMethodName, value.getClass()).invoke(file, value);
    }

    public void setFRValue(Data file, String field, String value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String setMethodName = "set" + maxMap.get(field) + fr;
        file.getClass().getMethod(setMethodName, value.getClass()).invoke(file, value);
    }

    public String getValue(Data file, String field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getMethodName = "get" + maxMap.get(field);
        String result = (String) file.getClass().getMethod(getMethodName).invoke(file);
        return result;
    }

    public String getFRValue(Data file, String field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String getMethodName = "get" + maxMap.get(field) + fr;
        String result = (String) file.getClass().getMethod(getMethodName).invoke(file);
        return result;
    }


    public void editDoc(String documentId, Map<String, String> params) throws NotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() == 0) {
            throw new NotFoundException("Files not found");
        }
        Data file = docs.get(0);

        for (String fieldName : Data.fieldNames()) {
            if (!fieldName.equals("normReferences")) {
                String newValue = params.get(fieldName);
                String oldValue = getValue(file, fieldName);
                if (!newValue.equals(oldValue)) {
                    setValue(file, fieldName, newValue);
                }

                String newValueFR = params.get(fieldName + fr);
                String oldValueFR = getFRValue(file, fieldName);
                if (!newValueFR.equals(oldValueFR)) {
                    setFRValue(file, fieldName, newValueFR);
                }
            }
        }

        changeLinks(file, params);

        fileRepo.save(file);
    }

    public void changeLinks(Data file, Map<String, String> params) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        updateLinks

//        updateLinks(file,
//                activeLinksIds.stream().mapToInt(i->i).toArray(),
//                inactiveLinks.toArray(new String[0]));
    }

    public void updateLinks(Data file, Map<String, String> params) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, Integer> descs = dataService.getGhostDescMap();
        List<Integer> activeLinksIds = new ArrayList<>();
        List<String> inactiveLinks = new ArrayList<>();

        for (String key : params.keySet().toArray(new String[0])) {
            if (key.matches("normReferences.+")) {
                if (!params.get(key).equals("")) {
                    if (descs.containsKey(params.get(key))) {
                        activeLinksIds.add(descs.get(params.get(key)));
                    } else {
                        inactiveLinks.add(params.get(key));
                    }
                }
            }
        }

        int[] newLinksIds = activeLinksIds.stream().mapToInt(i->i).toArray();
        Arrays.sort(newLinksIds);
        int[] oldLinksIds = file.getActiveLinks();
        Arrays.sort(oldLinksIds);

        int[] lastLinksIds = dataService.getLastActiveLinkValue(file);
        Arrays.sort(lastLinksIds);

        if (!Arrays.equals(newLinksIds, oldLinksIds)) {
            if (Arrays.equals(lastLinksIds, oldLinksIds)) {
                createAndRemoveRelations(file,
                        Arrays.stream(oldLinksIds).boxed().collect(Collectors.toList()),
                        Arrays.stream(newLinksIds).boxed().collect(Collectors.toList()));
            }
            file.setActiveLinks(newLinksIds);
        }

        String[] newLinks = inactiveLinks.toArray(new String[0]);
        Arrays.sort(newLinks);
        String[] oldLinks = file.getInactiveLinks();
        Arrays.sort(oldLinks);

        if (!Arrays.equals(newLinks, oldLinks)) {

        }
    }

    public void updateFRLinks() {

    }

    public void createAndRemoveRelations(Data file, List<Integer> oldIds, List<Integer> newIds) {
        List<Integer> removedIds = new ArrayList<>();
        for (int id : oldIds) {
            if (!newIds.contains(id)) {
                removedIds.add(id);
            }
        }
        List<Integer> createdIds = new ArrayList<>();
        for (int id : newIds) {
            if (!oldIds.contains(id)) {
                createdIds.add(id);
            }
        }
        createRelations(file, createdIds);
        removeRelations(file, removedIds);
    }

    public void createRelations(Data file, List<Integer> activeLinksIds) {
        for (int linkId : activeLinksIds) {
            GhostRelation relation = new GhostRelation(linkId, file.getId());
            relationRepo.save(relation);
        }
    }

    public void removeRelations(Data file, List<Integer> activeLinksIds) {
        for (int linkId : activeLinksIds) {
            List<GhostRelation> relations = relationRepo.findByDataIdAndReferralDataId(linkId, file.getId());
            if (relations.size() > 0) {
                relationRepo.delete(relations.get(0));
            }
        }
    }


    public Map<String, String> frMap() {
        Map<String, String> map  = new HashMap<>() {{
            put("name", "nameFirstRedaction");
            put("fileDesc", "fileDescFirstRedaction");
            put("OKCcode", "OKCcodeFirstRedaction");
            put("OKPDcode", "OKPDcodeFirstRedaction");
            put("adoptionDate", "adoptionDateFirstRedaction");
            put("introductionDate", "introductionDateFirstRedaction");
            put("developer", "developerFirstRedaction");
            put("predecessor", "predecessorFirstRedaction");
            put("contents", "contentsFirstRedaction");
            put("levelOfAcceptance", "levelOfAcceptanceFirstRedaction");
            put("status", "statusFirstRedaction");
            put("headContent", "headContentFirstRedaction");
            put("keywords", "keywordsFirstRedaction");
            put("keyPhrases", "keyPhrasesFirstRedaction");
            put("modifications", "modificationsFirstRedaction");
        }};
        return map;
    }







}
