package com.example.ghost_storage.Model;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Method;

@Entity
public class Data {
    public String getFilename() {
        boolean f = filename != null && filename.length() > 0;
        return f ? filename : "";
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public Map<String, String[]> getAllValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String[]> dict = new HashMap<String, String[]>();
        for (String str : fieldNames())
        {
            Object obj1 = this.getClass().getMethod("get" + maxFieldNames().get(str)).invoke(this);
            String value1 = obj1 != null ? obj1.toString() : "";

            Object obj2 = this.getClass().getMethod("get" + maxFieldNames().get(str) + "FirstRedaction").invoke(this);
            String value2 = obj2 != null ? obj2.toString() : "";

            Object obj3 = this.getClass().getMethod("get" + maxFieldNames().get(str) + "SecondRedaction").invoke(this);
            String value3 = obj3 != null ? obj3.toString() : "";

            dict.put(str, new String[] {value1, value2, value3});
        }
        return dict;
    }

    public Map<String, String> getLastValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> dict = new HashMap<String, String>();
        for (String str : fieldNames())
        {
            Object obj = this.getClass().getMethod("get" + maxFieldNames().get(str) + "SecondRedaction").invoke(this);
            if (obj == null) {
                obj = this.getClass().getMethod("get" + maxFieldNames().get(str) + "FirstRedaction").invoke(this);
                if (obj == null) {
                    obj = this.getClass().getMethod("get" + maxFieldNames().get(str)).invoke(this);
                }
            }
            String value = obj != null ? obj.toString() : "";
            dict.put(str, value);
        }
        return dict;
    }

    public Data() {}

    public Data(String name, String fileDesc, String OKCcode, String OKPDcode, String adoptionDate, String introductionDate, String developer, String predecessor, String contents, String levelOfAcceptance, String changes, String status, String referencesAmount) {
        this.name = name;
        this.fileDesc = fileDesc;
        this.OKCcode = OKCcode;
        this.OKPDcode = OKPDcode;
        this.adoptionDate = adoptionDate;
        this.introductionDate = introductionDate;
        this.developer = developer;
        this.predecessor = predecessor;
        this.contents = contents;
        this.levelOfAcceptance = levelOfAcceptance;
        this.changes = changes;
        this.status = status;
        this.referencesAmount = referencesAmount;
    }

    public static String[] fieldNames() {
        String[] arr = {
                "name",
                "fileDesc",
                "OKCcode",
                "OKPDcode",
                "adoptionDate",
                "introductionDate",
                "developer",
                "predecessor",
                "contents",
                "levelOfAcceptance",
                "changes",
                "status",
                "referencesAmount"
        };
        return arr;
    }

    public static Map<String, String> ruFieldNames() {
        Map<String, String> map  = new HashMap<>() {{
            put("name", "Название документа");
            put("fileDesc", "Описание документа");
            put("OKCcode", "Код ОКС");
            put("OKPDcode", "Код ОКПД 2");
            put("adoptionDate", "Дата принятия");
            put("introductionDate", "Дата введения");
            put("developer", "Разработчик");
            put("predecessor", "Принят взамен");
            put("contents", "Текст документа");
            put("levelOfAcceptance", "Уровень принятия");
            put("changes", "Изменения");
            put("status", "Статус документа");
            put("referencesAmount", "Количество обращений");
        }};
        return map;
    }

    public static Map<String, String> maxFieldNames() {
        Map<String, String> map  = new HashMap<>() {{
            put("name", "Name");
            put("fileDesc", "FileDesc");
            put("OKCcode", "OKCcode");
            put("OKPDcode", "OKPDcode");
            put("adoptionDate", "AdoptionDate");
            put("introductionDate", "IntroductionDate");
            put("developer", "Developer");
            put("predecessor", "Predecessor");
            put("contents", "Contents");
            put("levelOfAcceptance", "LevelOfAcceptance");
            put("changes", "Changes");
            put("status", "Status");
            put("referencesAmount", "ReferencesAmount");
        }};
        return map;
    }

    public static Map<String, String> emptyFieldValues() {
        Map<String, String> map  = new HashMap<>() {{
            put("name", "");
            put("fileDesc", "");
            put("OKCcode", "");
            put("OKPDcode", "");
            put("adoptionDate", "");
            put("introductionDate", "");
            put("developer", "");
            put("predecessor", "");
            put("contents", "");
            put("levelOfAcceptance", "");
            put("changes", "");
            put("status", "");
            put("referencesAmount", "");
        }};
        return map;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String name;
    private String fileDesc;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String filename = "";
    private String codeName;
    private String OKCcode;
    private String OKPDcode;
    private String adoptionDate;
    private String introductionDate;
    private String developer;
    private String predecessor;
    private String contents;
    private String levelOfAcceptance;
    private String changes;
    private String status;
    private String referencesAmount;
    private String nameFirstRedaction;
    private String fileDescFirstRedaction;
    private String OKCcodeFirstRedaction;
    private String OKPDcodeFirstRedaction;
    private String adoptionDateFirstRedaction;
    private String introductionDateFirstRedaction;
    private String developerFirstRedaction;
    private String predecessorFirstRedaction;
    private String contentsFirstRedaction;
    private String levelOfAcceptanceFirstRedaction;
    private String changesFirstRedaction;
    private String statusFirstRedaction;
    private String referencesAmountFirstRedaction;
    private String nameSecondRedaction;
    private String fileDescSecondRedaction;
    private String OKCcodeSecondRedaction;
    private String OKPDcodeSecondRedaction;
    private String adoptionDateSecondRedaction;
    private String introductionDateSecondRedaction;
    private String developerSecondRedaction;
    private String predecessorSecondRedaction;
    private String contentsSecondRedaction;
    private String levelOfAcceptanceSecondRedaction;
    private String changesSecondRedaction;
    private String statusSecondRedaction;
    private String referencesAmountSecondRedaction;

    public String getNameFirstRedaction() {
        return nameFirstRedaction;
    }

    public void setNameFirstRedaction(String nameFirstRedaction) {
        this.nameFirstRedaction = nameFirstRedaction;
    }

    public String getFileDescFirstRedaction() {
        return fileDescFirstRedaction;
    }

    public void setFileDescFirstRedaction(String fileDescFirstRedaction) {
        this.fileDescFirstRedaction = fileDescFirstRedaction;
    }

    public String getOKCcodeFirstRedaction() {
        return OKCcodeFirstRedaction;
    }

    public void setOKCcodeFirstRedaction(String OKCcodeFirstRedaction) {
        this.OKCcodeFirstRedaction = OKCcodeFirstRedaction;
    }

    public String getOKPDcodeFirstRedaction() {
        return OKPDcodeFirstRedaction;
    }

    public void setOKPDcodeFirstRedaction(String OKPDcodeFirstRedaction) {
        this.OKPDcodeFirstRedaction = OKPDcodeFirstRedaction;
    }

    public String getAdoptionDateFirstRedaction() {
        return adoptionDateFirstRedaction;
    }

    public void setAdoptionDateFirstRedaction(String adoptionDateFirstRedaction) {
        this.adoptionDateFirstRedaction = adoptionDateFirstRedaction;
    }

    public String getIntroductionDateFirstRedaction() {
        return introductionDateFirstRedaction;
    }

    public void setIntroductionDateFirstRedaction(String introductionDateFirstRedaction) {
        this.introductionDateFirstRedaction = introductionDateFirstRedaction;
    }

    public String getDeveloperFirstRedaction() {
        return developerFirstRedaction;
    }

    public void setDeveloperFirstRedaction(String developerFirstRedaction) {
        this.developerFirstRedaction = developerFirstRedaction;
    }

    public String getPredecessorFirstRedaction() {
        return predecessorFirstRedaction;
    }

    public void setPredecessorFirstRedaction(String predecessorFirstRedaction) {
        this.predecessorFirstRedaction = predecessorFirstRedaction;
    }

    public String getContentsFirstRedaction() {
        return contentsFirstRedaction;
    }

    public void setContentsFirstRedaction(String contentsFirstRedaction) {
        this.contentsFirstRedaction = contentsFirstRedaction;
    }

    public String getLevelOfAcceptanceFirstRedaction() {
        return levelOfAcceptanceFirstRedaction;
    }

    public void setLevelOfAcceptanceFirstRedaction(String levelOfAcceptanceFirstRedaction) {
        this.levelOfAcceptanceFirstRedaction = levelOfAcceptanceFirstRedaction;
    }

    public String getChangesFirstRedaction() {
        return changesFirstRedaction;
    }

    public void setChangesFirstRedaction(String changesFirstRedaction) {
        this.changesFirstRedaction = changesFirstRedaction;
    }

    public String getStatusFirstRedaction() {
        return statusFirstRedaction;
    }

    public void setStatusFirstRedaction(String statusFirstRedaction) {
        this.statusFirstRedaction = statusFirstRedaction;
    }

    public String getReferencesAmountFirstRedaction() {
        return referencesAmountFirstRedaction;
    }

    public void setReferencesAmountFirstRedaction(String referencesAmountFirstRedaction) {
        this.referencesAmountFirstRedaction = referencesAmountFirstRedaction;
    }

    public String getNameSecondRedaction() {
        return nameSecondRedaction;
    }

    public void setNameSecondRedaction(String nameSecondRedaction) {
        this.nameSecondRedaction = nameSecondRedaction;
    }

    public String getFileDescSecondRedaction() {
        return fileDescSecondRedaction;
    }

    public void setFileDescSecondRedaction(String fileDescSecondRedaction) {
        this.fileDescSecondRedaction = fileDescSecondRedaction;
    }

    public String getOKCcodeSecondRedaction() {
        return OKCcodeSecondRedaction;
    }

    public void setOKCcodeSecondRedaction(String OKCcodeSecondRedaction) {
        this.OKCcodeSecondRedaction = OKCcodeSecondRedaction;
    }

    public String getOKPDcodeSecondRedaction() {
        return OKPDcodeSecondRedaction;
    }

    public void setOKPDcodeSecondRedaction(String OKPDcodeSecondRedaction) {
        this.OKPDcodeSecondRedaction = OKPDcodeSecondRedaction;
    }

    public String getAdoptionDateSecondRedaction() {
        return adoptionDateSecondRedaction;
    }

    public void setAdoptionDateSecondRedaction(String adoptionDateSecondRedaction) {
        this.adoptionDateSecondRedaction = adoptionDateSecondRedaction;
    }

    public String getIntroductionDateSecondRedaction() {
        return introductionDateSecondRedaction;
    }

    public void setIntroductionDateSecondRedaction(String introductionDateSecondRedaction) {
        this.introductionDateSecondRedaction = introductionDateSecondRedaction;
    }

    public String getDeveloperSecondRedaction() {
        return developerSecondRedaction;
    }

    public void setDeveloperSecondRedaction(String developerSecondRedaction) {
        this.developerSecondRedaction = developerSecondRedaction;
    }

    public String getPredecessorSecondRedaction() {
        return predecessorSecondRedaction;
    }

    public void setPredecessorSecondRedaction(String predecessorSecondRedaction) {
        this.predecessorSecondRedaction = predecessorSecondRedaction;
    }

    public String getContentsSecondRedaction() {
        return contentsSecondRedaction;
    }

    public void setContentsSecondRedaction(String contentsSecondRedaction) {
        this.contentsSecondRedaction = contentsSecondRedaction;
    }

    public String getLevelOfAcceptanceSecondRedaction() {
        return levelOfAcceptanceSecondRedaction;
    }

    public void setLevelOfAcceptanceSecondRedaction(String levelOfAcceptanceSecondRedaction) {
        this.levelOfAcceptanceSecondRedaction = levelOfAcceptanceSecondRedaction;
    }

    public String getChangesSecondRedaction() {
        return changesSecondRedaction;
    }

    public void setChangesSecondRedaction(String changesSecondRedaction) {
        this.changesSecondRedaction = changesSecondRedaction;
    }

    public String getStatusSecondRedaction() {
        return statusSecondRedaction;
    }

    public void setStatusSecondRedaction(String statusSecondRedaction) {
        this.statusSecondRedaction = statusSecondRedaction;
    }

    public String getReferencesAmountSecondRedaction() {
        return referencesAmountSecondRedaction;
    }

    public void setReferencesAmountSecondRedaction(String referencesAmountSecondRedaction) {
        this.referencesAmountSecondRedaction = referencesAmountSecondRedaction;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getOKCcode() {
        return OKCcode;
    }

    public void setOKCcode(String OKCcode) {
        this.OKCcode = OKCcode;
    }

    public String getOKPDcode() {
        return OKPDcode;
    }

    public void setOKPDcode(String OKPDcode) {
        this.OKPDcode = OKPDcode;
    }

    public String getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(String adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public String getIntroductionDate() {
        return introductionDate;
    }

    public void setIntroductionDate(String introductionDate) {
        this.introductionDate = introductionDate;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLevelOfAcceptance() {
        return levelOfAcceptance;
    }

    public void setLevelOfAcceptance(String levelOfAcceptance) {
        this.levelOfAcceptance = levelOfAcceptance;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferencesAmount() {
        return referencesAmount;
    }

    public void setReferencesAmount(String referencesAmount) {
        this.referencesAmount = referencesAmount;
    }
}