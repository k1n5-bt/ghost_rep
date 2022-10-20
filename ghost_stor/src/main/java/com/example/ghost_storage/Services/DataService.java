package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.GhostRelation;
import com.example.ghost_storage.Model.State;
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
            file.setState(State.CANCELED);
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
                    deactivateActiveLink(file, ghost.getId(), ghost.getLastDesc());
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

            int[] oldIds = getLastActiveLinkValue(doc);
            List<Integer> newIds = new ArrayList<>();
            for (int id : oldIds) {
                if (id != oldId) {
                    newIds.add(id);
                } else {
                    newIds.add(newId);
                }
            }
            int stage = checkActiveUpdatedStage(doc);
            if (stage == 2) {
                doc.setActiveLinks(newIds);
            } else if (stage == 3) {
                doc.setActiveLinksFirstRedaction(newIds);
            }
            fileRepo.save(doc);
        }
    }

    public List<Integer> saveLinks(Data file, Map<String, String> params) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, Integer> descs = this.getGhostDescMap();
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

        changeLinks(doc, params);

        fileRepo.save(doc);
        return doc;
    }

    public void changeLinks(Data file, Map<String, String> params) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, Integer> descs = this.getGhostDescMap();
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
    }

    public void updateLinks(Data file,
                            int[] newActiveLinks,
                            String[] newInactiveLinks) {
        int[] oldActiveLinks = getLastActiveLinkValue(file);
        String[] oldInactiveLinks = getLastInactiveLinkValue(file);
//        boolean updateLinks = checkLinkChanges(newActiveLinks, oldActiveLinks) &&
//                checkLinkChanges(newInactiveLinks, oldInactiveLinks);
        int res = checkLinkChanges(file, newActiveLinks, oldActiveLinks, newInactiveLinks, oldInactiveLinks);
        if (res != 0) {
            updateActiveLinks(file, newActiveLinks, res, oldActiveLinks);
            updateInactiveLinks(file, newInactiveLinks, res);
        }
        String stage = "";
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

    public int checkLinkChanges(Data file,
                                int[] newActArr, int[] oldActiveArr,
                                String[] newInArr, String[] oldInactiveArr) {
        int[] oldActArr;
        if (Arrays.equals(oldActiveArr, new int[]{0})) {
            oldActArr = new int[0];
        } else {
            oldActArr = oldActiveArr;
        }
        String[] oldInArr;
//        if (Arrays.equals(oldInactiveArr, new String[]{"-"})) {
        if (oldInactiveArr.length == 1 && oldInactiveArr[0].equals("-")) {
            oldInArr = new String[0];
        } else {
            oldInArr = oldInactiveArr;
        }

//        0 - без обновления
//        1 - записать результат в начальное значение
//        2 - записать результат в первую редакцию
//        3 - записать результат в первую редакцию с ротацией

        boolean actUpdate = !Arrays.equals(newActArr, oldActArr);
        boolean inactUpdate = !Arrays.equals(newInArr, oldInArr);

        if (actUpdate || inactUpdate) {
            int actRes;
            int[] activeLinks = file.getActiveLinks();
            int[] activeLinksFR = file.getActiveLinksFirstRedaction();
            if (activeLinksFR.length == 1 && activeLinksFR[0] == 0) { // перевая редакция не инициализирована еще
                if (activeLinks.length == 1 && activeLinks[0] == 0) {
                    actRes = 1;
                } else {
                    actRes = 2;
                }
            } else {
                actRes = 3;
            }

            int inactRes;
            String[] inactiveLinks = file.getInactiveLinks();
            String[] inactiveLinksFR = file.getInactiveLinksFirstRedaction();
            if (inactiveLinksFR.length == 1 && inactiveLinksFR[0].equals("-")) { // перевая редакция не инициализирована еще
                if (inactiveLinks.length == 1 && inactiveLinks[0].equals("-")) { // записывать в начальное значение
                    inactRes = 1;
                } else { // записывать в первую редакцию
                    inactRes = 2;
                }
            } else {
                inactRes = 3;
            }

            return Math.max(actRes, inactRes);
        } else {
            return 0;
        }
    }

    public int checkActiveUpdatedStage(Data file) {
        int actRes;
        int[] activeLinks = file.getActiveLinks();
        int[] activeLinksFR = file.getActiveLinksFirstRedaction();
        if (activeLinksFR.length == 1 && activeLinksFR[0] == 0) { // перевая редакция не инициализирована еще
            if (activeLinks.length == 1 && activeLinks[0] == 0) {
                actRes = 1;
            } else {
                actRes = 2;
            }
        } else {
            actRes = 3;
        }
        return actRes;
    }

    public int checkInactiveUpdatedStage(Data file) {
        int inactRes;
        String[] inactiveLinks = file.getInactiveLinks();
        String[] inactiveLinksFR = file.getInactiveLinksFirstRedaction();
        if (inactiveLinksFR.length == 1 && inactiveLinksFR[0].equals("-")) { // перевая редакция не инициализирована еще
            if (inactiveLinks.length == 1 && inactiveLinks[0].equals("-")) { // записывать в начальное значение
                inactRes = 1;
            } else { // записывать в первую редакцию
                inactRes = 2;
            }
        } else {
            inactRes = 3;
        }

        return inactRes;
    }

    public void updateActiveLinks(Data file, int[] newActiveLinksIds, int stage, int[] lastValue) {
        switch (stage) {
            case (1):
                file.setActiveLinks(newActiveLinksIds);
                break;
            case (2):
                file.setActiveLinksFirstRedaction(newActiveLinksIds);
                break;
            case (3):
                int[] activeLinksFR = file.getActiveLinksFirstRedaction();
                if (activeLinksFR.length == 1 && activeLinksFR[0] == 0) {
                    activeLinksFR = new int[0];
                }
                file.setActiveLinks(activeLinksFR);
                file.setActiveLinksFirstRedaction(newActiveLinksIds);
                break;
        }
        if (!Arrays.equals(lastValue, newActiveLinksIds)) {
            createAndRemoveRelations(file, Arrays.stream(lastValue).boxed().collect(Collectors.toList()),
                    Arrays.stream(newActiveLinksIds).boxed().collect(Collectors.toList()));
        }
    }

    public void updateInactiveLinks(Data file, String[] newInactiveLinks, int stage) {
        switch (stage) {
            case (1):
                file.setInactiveLinks(newInactiveLinks);
                break;
            case (2):
                file.setInactiveLinksFirstRedaction(newInactiveLinks);
                break;
            case (3):
                String[] inactiveLinksFR = file.getInactiveLinksFirstRedaction();
                if (inactiveLinksFR.length == 1 && inactiveLinksFR[0].equals("-")) {
                    inactiveLinksFR = new String[0];
                }
                file.setInactiveLinks(inactiveLinksFR);
                file.setInactiveLinksFirstRedaction(newInactiveLinks);
                break;
        }
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
                if (inactiveLinks.length == 1 && inactiveLinks[0].equals("-")) { // записывать в начальное значение
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

    public void archiveDocument(Data doc) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        doc.setState(State.CANCELED);
        setLastName(doc, "status", "Отменен");

        fileRepo.save(doc);

        List<GhostRelation> relations = relationRepo.findByDataId(doc.getId());
        for (GhostRelation relation : relations) {
            List<Data> files = fileRepo.findById(relation.getReferralDataId());
            if (files.size() > 0) {
                Data file = files.get(0);
                removeActiveLink(file, doc.getId(), doc.getLastDesc());
                fileRepo.save(file);
            }
            relationRepo.delete(relation);
        }

        List<GhostRelation> selfRelations = relationRepo.findByReferralDataId(doc.getId());
        for (GhostRelation relation : selfRelations) {
            List<Data> files = fileRepo.findById(relation.getDataId());
            if (files.size() > 0) {
                Data file = files.get(0);
                deactivateActiveLink(doc, file.getId(), file.getLastDesc());
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

        int[] oldIdsFR = doc.getActiveLinks();
        List<Integer> newIdsFR = new ArrayList<>();
        for (int id : oldIdsFR) {
            if (id != linkId) {
                newIdsFR.add(id);
            }
        }
        doc.setActiveLinksFirstRedaction(newIdsFR);
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
            canceledData = fileRepo.findByStateId(State.CANCELED.getValue());
        } else {
            canceledData = fileRepo.findByStateIdAndFileDescLike(State.CANCELED.getValue(), li(descFilter));
        }
        return canceledData;
    }

    public Map<String, Integer> getGhostDescMap() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> files = fileRepo.findByStateId(State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        for (Data file : files) {
            dict.put(file.getLastDesc(), file.getId());
        }
        return  dict;
    }

    public String[] getGhostDesc() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> files = fileRepo.findByStateId(State.ACTIVE.getValue());
        String[] descArr = new String[files.size()];
        for (int i = 0; i < descArr.length; i++) {
            descArr[i] = files.get(i).getLastDesc();
        }
        return  descArr;
    }

    public Map<String, Integer> getActiveLinkNames(Data file) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findByStateId(State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        int[] ids = file.getActiveLinks();

        for (Data doc : docs) {
            if (Arrays.stream(ids).anyMatch(i -> i == doc.getId())) {
                dict.put(doc.getLastDesc(), doc.getId());
            }
        }
        return  dict;
    }

    public Map<String, Integer> getActiveLinkFRNames(Data file) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findByStateId(State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        int[] ids = file.getActiveLinksFirstRedaction();

        for (Data doc : docs) {
            if (Arrays.stream(ids).anyMatch(i -> i == doc.getId())) {
                dict.put(doc.getLastDesc(), doc.getId());
            }
        }
        return  dict;
    }

    public Map<String, Integer> getLastActiveLinkNames(Data file) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        List<Data> docs = fileRepo.findByStateId(State.ACTIVE.getValue());
        Map<String, Integer> dict = new HashMap<>();
        int[] ids = getLastActiveLinkValue(file);

        for (Data doc : docs) {
            if (Arrays.stream(ids).anyMatch(i -> i == doc.getId())) {
                dict.put(doc.getLastDesc(), doc.getId());
            }
        }
        return  dict;
    }

    public String[] getInactiveLinkNames(Data file) {
        String[] strArr = file.getInactiveLinks();
        if (Arrays.equals(strArr, new String[] {"-"})) {
            return new String[0];
        } else {
            return strArr;
        }
    }

    public String[] getInactiveLinkFRNames(Data file) {
        String[] strArr = file.getInactiveLinksFirstRedaction();
        if (Arrays.equals(strArr, new String[] {"-"})) {
            return new String[0];
        } else {
            return strArr;
        }
    }

    public String[] getLastInactiveLinkNames(Data file) {
        String[] strArr = getLastInactiveLinkValue(file);
        if (Arrays.equals(strArr, new String[] {"-"})) {
            return new String[0];
        } else {
            return strArr;
        }
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
