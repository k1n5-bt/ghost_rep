package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.User;
import com.example.ghost_storage.Storage.FileRepo;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.example.ghost_storage.Model.Data.maxFieldNames;

@Service
public class DataService {
    
    private FileRepo fileRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public DataService(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    public Data createDoc(MultipartFile file, Map<String, String> params) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data doc = new Data();
        Map<String, String> emptyValues = doc.emptyFieldValues();
        for (String fieldName : Data.fieldNames()) {
            if (!fieldName.equals("normReferences")) {
                String newValue = params.get(fieldName);
                String oldValue = emptyValues.get(fieldName);
                if (!newValue.equals(oldValue)) {
                    setLastName(doc, fieldName, newValue);
                }
            }
        }

        saveLinks(doc, params);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFileName = createFile(file);
            doc.setFilename(resultFileName);
        }
        fileRepo.save(doc);
        return doc;
    }

    public void saveLinks(Data file, Map<String, String> params) {
        Map<String, Integer> descs = this.getGhostDesc();
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
        file.setActiveLinks(activeLinksIds);
        file.setInactiveLinks(inactiveLinks);
    }

    public Data updateDoc(String documentId, MultipartFile file, Map<String, String> params) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, NotFoundException {
        List<Data> docs = fileRepo.findById(Integer.parseInt(documentId));
        if (docs.size() == 0) {
            throw new NotFoundException("Files not found");
        }
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
            if (!fieldName.equals("normReferences")) {
                String newValue = params.get(fieldName);
                String oldValue = lastValues.get(fieldName);

                if (!newValue.equals(oldValue)) {
                    setLastName(doc, fieldName, newValue);
                }
            }
        }
        fileRepo.save(doc);
        return doc;
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

    public void deleteDoc(String dataID, User user) throws FileNotFoundException {
        List<Data> dataList = fileRepo.findById(Integer.parseInt(dataID));
        if (dataList.isEmpty()) {
            return;
        }
        Data data = dataList.get(0);
        var userIsAuthor = Objects.equals(data.getAuthor(), user);
        if (!user.isAdmin() && !userIsAuthor) {
            return;
        }
        if (data.getFilename().length() > 0) {
            File file = new File(uploadPath + "/" + data.getFilename());
            if (!file.delete()) throw new FileNotFoundException();
        }
        fileRepo.delete(data);
    }

    public List<Data> getArchiveData(){
        List<Data> canceledData = fileRepo.findByStateId(Data.State.CANCELED.getValue());
//        replacedData.addAll(canceledData);
        return canceledData;
    }

    public Map<String, Integer> getGhostDesc() {
        List<Data> files = fileRepo.findByStateId(Data.State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        for (Data file : files) {
            dict.put(file.getFileDesc(), file.getId());
        }
        return  dict;
    }
}
