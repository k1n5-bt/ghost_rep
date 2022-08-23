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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
        return doc;
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
            String newValue = params.get(fieldName);
            String oldValue = lastValues.get(fieldName);

            if (!newValue.equals(oldValue)) {
                setLastName(doc, fieldName, newValue);
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
        Object obj = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "SecondRedaction").invoke(file);
        if (obj == null) {
            obj = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "FirstRedaction").invoke(file);
            if (obj == null) {
                obj = file.getClass().getMethod("get" + maxFieldNames().get(fieldName)).invoke(file);
                if (obj == null) {
                    file.getClass().getMethod("set" + maxFieldNames().get(fieldName), value.getClass()).invoke(file, value);
                } else {
                    file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "FirstRedaction", value.getClass()).invoke(file, value);
                }
            } else {
                file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "SecondRedaction", value.getClass()).invoke(file, value);
            }
        } else {
            String value2 = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "FirstRedaction").invoke(file).toString();
            String value3 = file.getClass().getMethod("get" + maxFieldNames().get(fieldName) + "SecondRedaction").invoke(file).toString();
            file.getClass().getMethod("set" + maxFieldNames().get(fieldName), value2.getClass()).invoke(file, value2);
            file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "FirstRedaction", value3.getClass()).invoke(file, value3);
            file.getClass().getMethod("set" + maxFieldNames().get(fieldName) + "SecondRedaction", value.getClass()).invoke(file, value);
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
        List<Data> replacedData = fileRepo.findByStateId(Data.State.REPLACED.getValue());
        replacedData.addAll(canceledData);
        return replacedData;
    }
}
