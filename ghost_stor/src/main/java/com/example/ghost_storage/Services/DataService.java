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
public class DataService {
    
    private FileRepo fileRepo;
    private RelationRepo relationRepo;

    @Value("${upload.path}")
    private String uploadPath;

    public DataService(FileRepo fileRepo, RelationRepo relationRepo, UserRepo userRepo) {
        this.fileRepo = fileRepo;
        this.relationRepo = relationRepo;
    }

    public Data createDoc(MultipartFile file, Map<String, String> params) throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Data doc = new Data();
        for (String fieldName : Data.fieldNames()) {
            if (!fieldName.equals("normReferences")) {
                String param = params.get(fieldName);
                if (param.equals("")) {
                    doc.getClass().getMethod("set" + maxFieldNames().get(fieldName), "-".getClass()).invoke(doc, "-");
                } else {
                    doc.getClass().getMethod("set" + maxFieldNames().get(fieldName), param.getClass()).invoke(doc, param);
                }
            }
        }

        List<Integer> activeLinksIds = saveLinks(doc, params);

        if (file != null && !file.getOriginalFilename().isEmpty()) {
            String resultFileName = createFile(file);
            doc.setFilename(resultFileName);
        }
        fileRepo.save(doc);
        createRelations(doc, activeLinksIds);
        if (params.containsKey("parentDocId")) {
            int parentDocId = Integer.parseInt(params.get("parentDocId"));
            replaceDoc(parentDocId, doc.getId(), doc.getLastValues().get("fileDesc"));
        }
        return doc;
    }

    public void replaceDoc(int oldDocId, int newDocId, String newDocDesc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findById(oldDocId);
        if (docs.size() > 0) {
            Data file = docs.get(0);
            file.setState(Data.State.CANCELED);
            setLastName(file, "status", "Заменен " + newDocDesc);
            fileRepo.save(file);

            List<GhostRelation> relations = relationRepo.findByDataId(oldDocId);
            for (GhostRelation relation : relations) {
                relation.setDataId(newDocId);
                relationRepo.save(relation);
                replaceActiveLink(relation.getReferralDataId(), oldDocId, newDocId);
            }

            List<GhostRelation> selfRelations = relationRepo.findByReferralDataId(oldDocId);
            for (GhostRelation relation : selfRelations) {
                List<Data> files = fileRepo.findById(relation.getDataId());
                if (files.size() > 0) {
                    Data ghost = files.get(0);
                    deactivateActiveLink(file, ghost.getId(), ghost.getFileDesc());
                    fileRepo.save(file);
                }
                relationRepo.delete(relation);
            }
        }


    }

    public void replaceActiveLink(int docId, int oldId, int newId) {
        List<Data> docs = fileRepo.findById(docId);
        if (docs.size() > 0) {
            Data doc = docs.get(0);

            int[] oldIds = doc.getActiveLinks();
            List<Integer> newIds = new ArrayList<>();
            for (int id : oldIds) {
                if (id != oldId) {
                    newIds.add(id);
                } else {
                    newIds.add(newId);
                }
            }
            doc.setActiveLinks(newIds);
            fileRepo.save(doc);
        }
    }

    public List<Integer> saveLinks(Data file, Map<String, String> params) {
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

        return activeLinksIds;
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
                String parVal = params.get(fieldName);
                String newValue = parVal.equals("-") ? "" : parVal;
                String lastValue = lastValues.get(fieldName);
                String oldValue = lastValue.equals("-") ? "" : lastValue;

                if (!newValue.equals(oldValue)) {
                    setLastName(doc, fieldName, newValue);
                }
            }
        }

        updateLinks(doc, params);

        fileRepo.save(doc);
        return doc;
    }

    public void updateLinks(Data file, Map<String, String> params) {
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
        updateLinks(file,
                    activeLinksIds.stream().mapToInt(i->i).toArray(),
                    inactiveLinks.toArray(new String[0]));
//        updateActiveLinks(file, activeLinksIds);
//        updateInactiveLinks(file, inactiveLinks);

//        file.setActiveLinks(activeLinksIds);
//        file.setInactiveLinks(inactiveLinks);
    }

    public void updateLinks(Data file,
                            int[] newActiveLinks,
                            String[] newInactiveLinks) {
        int[] oldActiveLinks = getLastActiveLinkValue(file);
        String[] oldInactiveLinks = getLastInactiveLinkValue(file);
        boolean updateLinks = checkLinkChanges(newActiveLinks, oldActiveLinks) &&
                checkLinkChanges(newInactiveLinks, oldInactiveLinks);

    }

    // возвращает сортированный массив
    public int[] getLastActiveLinkValue(Data file) {
        int[] activeLinksLast;
        int[] activeLinks = file.getActiveLinks();
        int[] activeLinksFR = file.getActiveLinksFirstRedaction();

        if (activeLinksFR.length == 1 && activeLinksFR[0] == 0) { // перевая редакция не инициализирована еще
            activeLinksLast = activeLinks;
        } else {
            activeLinksLast = activeLinksFR;
        }
        Arrays.sort(activeLinksLast);
        return activeLinksLast;
    }

    // возвращает сортированный массив
    public String[] getLastInactiveLinkValue(Data file) {
        String[] inactiveLinksLast;
        String[] inactiveLinks = file.getInactiveLinks();
        String[] inactiveLinksFR = file.getInactiveLinksFirstRedaction();

        if (inactiveLinksFR.length == 1 && inactiveLinksFR[0].equals("-")) { // перевая редакция не инициализирована еще
            inactiveLinksLast = inactiveLinks;
        } else {
            inactiveLinksLast = inactiveLinksFR;
        }
        Arrays.sort(inactiveLinksLast);
        return inactiveLinksLast;
    }

    public boolean checkLinkChanges(Object newArr, Object oldArr) {
        return newArr.equals(oldArr);
    }

    public void updateActiveLinks(Data file, List<Integer> newActiveLinksIds) {
        Collections.sort(newActiveLinksIds);
        int[] activeLinksLast;
        int[] activeLinks = file.getActiveLinks();
        int[] activeLinksFR = file.getActiveLinksFirstRedaction();

        if (activeLinksFR.length == 1 && activeLinksFR[0] == 0) { // перевая редакция не инициализирована еще
            activeLinksLast = activeLinks;
            List<Integer> lastLinks = Arrays.stream(activeLinksLast).boxed().collect(Collectors.toList());
            Collections.sort(lastLinks);
            if (!newActiveLinksIds.equals(lastLinks)) {
                if (activeLinksLast.length != 0) { // записывать в начальное значение
                    file.setActiveLinks(newActiveLinksIds);
                } else { // записывать в первую редакцию
                    file.setActiveLinksFirstRedaction(newActiveLinksIds);
                }
            }
        } else { // записывать в первую редакцию и смещать
            activeLinksLast = activeLinksFR;
            List<Integer> lastLinks = Arrays.stream(activeLinksLast).boxed().collect(Collectors.toList());
            Collections.sort(lastLinks);
            if (!newActiveLinksIds.equals(lastLinks)) {
                file.setActiveLinks(Arrays.stream(activeLinksFR).boxed().collect(Collectors.toList()));
                file.setActiveLinksFirstRedaction(newActiveLinksIds);
            }
        }
        createAndRemoveRelations(file, Arrays.stream(activeLinksLast).boxed().collect(Collectors.toList()), newActiveLinksIds);
    }

    public void updateInactiveLinks(Data file, List<String> newInactiveLinks) {
        Collections.sort(newInactiveLinks);
        String[] inactiveLinksLast;
        String[] inactiveLinks = file.getInactiveLinks();
        String[] inactiveLinksFR = file.getInactiveLinksFirstRedaction();

        if (inactiveLinksFR.length == 1 && inactiveLinksFR[0].equals("-")) { // перевая редакция не инициализирована еще
            inactiveLinksLast = inactiveLinks;
            List<String> lastLinks = new ArrayList<>(Arrays.asList(inactiveLinksLast));
            Collections.sort(lastLinks);
            if (!newInactiveLinks.equals(inactiveLinksLast)) {
                if (inactiveLinksLast.length == 0) { // записывать в начальное значение
                    file.setInactiveLinks(newInactiveLinks);
                } else { // записывать в первую редакцию
                    file.setInactiveLinksFirstRedaction(newInactiveLinks);
                }
            }
        } else { // записывать в первую редакцию и смещать
            inactiveLinksLast = inactiveLinksFR;
            List<String> lastLinks = new ArrayList<>(Arrays.asList(inactiveLinksLast));
            Collections.sort(lastLinks);
            if (!newInactiveLinks.equals(lastLinks)) {
                file.setInactiveLinks(new ArrayList<>(Arrays.asList(inactiveLinksFR)));
                file.setInactiveLinksFirstRedaction(newInactiveLinks);
            }
        }
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

    public void archiveDocument(Data doc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        doc.setState(Data.State.CANCELED);
        setLastName(doc, "status", "Отменен");

        fileRepo.save(doc);

        List<GhostRelation> relations = relationRepo.findByDataId(doc.getId());
        for (GhostRelation relation : relations) {
            List<Data> files = fileRepo.findById(relation.getReferralDataId());
            if (files.size() > 0) {
                Data file = files.get(0);
                removeActiveLink(file, doc.getId(), doc.getFileDesc());
                fileRepo.save(file);
            }
            relationRepo.delete(relation);
        }

        List<GhostRelation> selfRelations = relationRepo.findByReferralDataId(doc.getId());
        for (GhostRelation relation : selfRelations) {
            List<Data> files = fileRepo.findById(relation.getDataId());
            if (files.size() > 0) {
                Data file = files.get(0);
                deactivateActiveLink(doc, file.getId(), file.getFileDesc());
                fileRepo.save(doc);
            }
            relationRepo.delete(relation);
        }
    }

    public void deactivateActiveLink(Data doc, int linkId, String linkDesc) {
        int[] oldIds = doc.getActiveLinks();
        List<Integer> newIds = new ArrayList<>();
        for (int id : oldIds) {
            if (id != linkId) {
                newIds.add(id);
            }
        }
        doc.setActiveLinks(newIds);

        String[] oldDesc = doc.getInactiveLinks();
        List<String> newDesc = new ArrayList<>(List.of(oldDesc));
        newDesc.add(linkDesc);
        doc.setInactiveLinks(newDesc);
    }

    public void removeActiveLink(Data doc, int linkId, String linkDesc) {
        int[] oldIds = doc.getActiveLinks();
        List<Integer> newIds = new ArrayList<>();
        for (int id : oldIds) {
            if (id != linkId) {
                newIds.add(id);
            }
        }
        doc.setActiveLinks(newIds);
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
            if (obj.equals("-")) {
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

    public void deleteDoc(String dataID, User user) throws FileNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> dataList = fileRepo.findById(Integer.parseInt(dataID));
        if (dataList.isEmpty()) {
            return;
        }
        Data data = dataList.get(0);
        if (!user.isAdmin()) {
            return;
        }
        if (data.getFilename().length() > 0) {
            File file = new File(uploadPath + "/" + data.getFilename());
            if (!file.delete()) throw new FileNotFoundException();
        }
        archiveDocument(data);
        fileRepo.delete(data);
    }

    public List<Data> getArchiveData(String descFilter){
        List<Data> canceledData;
        if (descFilter.equals("")) {
            canceledData = fileRepo.findByStateId(Data.State.CANCELED.getValue());
        } else {
            canceledData = fileRepo.findByStateIdAndFileDescLike(Data.State.CANCELED.getValue(), li(descFilter));
        }
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

    public Map<String, Integer> getActiveLinkNames(Data file) {
        List<Data> docs = fileRepo.findByStateId(Data.State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        int[] ids = file.getActiveLinks();

        for (Data doc : docs) {
            if (Arrays.stream(ids).anyMatch(i -> i == doc.getId())) {
                dict.put(doc.getFileDesc(), doc.getId());
            }
        }
        return  dict;
    }

    public static String[] searchFields() {
        return new String[] {
                "fileDesc",
                "name",
                "OKCcode",
                "OKPDcode",
                "adoptionDate",
                "introductionDate",
                "developer",
                "predecessor",
                "headContent",
                "keywords",
                "keyPhrases",
                "levelOfAcceptance",
                "contents",
                "modifications",
                "status"
        };
    }

    public Map<String, String> searchRuFields() {
        return new HashMap<>() {{
            put("name", "Наименование");
            put("fileDesc", "Обозначение");
            put("OKCcode", "Код ОКС");
            put("OKPDcode", "Код ОКПД 2");
            put("adoptionDate", "Дата принятия");
            put("introductionDate", "Дата введения");
            put("developer", "Разработчик");
            put("predecessor", "Принят взамен");
            put("contents", "Ссылка на документ");
            put("levelOfAcceptance", "Уровень принятия");
            put("status", "Статус");
            put("headContent", "Содержание");
            put("keywords", "Ключевые слова");
            put("keyPhrases", "Ключевые фразы");
            put("modifications", "Поправки");
        }};
    }

    public List<Data> findByParams(Map<String, String> params) {
        return fileRepo.search(
            li(params.get("fileDesc")),
            li(params.get("name")),
            li(params.get("OKCcode")),
            li(params.get("OKPDcode")),
            li(params.get("adoptionDate")),
            li(params.get("introductionDate")),
            li(params.get("developer")),
            li(params.get("predecessor")),
            li(params.get("headContent")),
            li(params.get("keywords")),
            li(params.get("keyPhrases")),
            li(params.get("levelOfAcceptance")),
            li(params.get("contents")),
            li(params.get("modifications")),
            li(params.get("status"))
        );
    }

    public String li(String str) {
        if (str.equals("")) {
            return "%";
        }
        else {
            return "%" + str + "%";
        }
    }

    public static String queryStr(Map<String, String> params) {
        return "aaaaa";
    }
}
