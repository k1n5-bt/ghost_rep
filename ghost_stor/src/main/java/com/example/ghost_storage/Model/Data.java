package com.example.ghost_storage.Model;

import com.example.ghost_storage.Storage.FileRepo;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

            dict.put(str, new String[] {value1, value2});
        }
        return dict;
    }

    public Map<String, String> getLastValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> dict = new HashMap<String, String>();
        for (String str : fieldNames())
        {
            Object obj = this.getClass().getMethod("get" + maxFieldNames().get(str) + "FirstRedaction").invoke(this);
            if (obj == null) {
                obj = this.getClass().getMethod("get" + maxFieldNames().get(str)).invoke(this);
            }
            String value = obj != null ? obj.toString() : "";
            dict.put(str, value);
        }
        return dict;
    }

    public Data() {
        setState(State.ACTIVE);
    }

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
                "status"
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
        }};
        return map;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Boolean archivated = false;
    private String name;
    private String fileDesc;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;
    private String filename = "";

    public enum State {
        ACTIVE(100), CANCELED(200), REPLACED(300);

        private int value;

        State(int value) { this.value = value; }

        public int getValue() { return value; }

        public static State parse(int id) {
            State state = null; // Default
            for (State item : State.values()) {
                if (item.getValue()==id) {
                    state = item;
                    break;
                }
            }
            return state;
        }

    };

    @Column(name = "STATE_ID")
    private int stateId;

    public State getState () {
        return State.parse(this.stateId);
    }

    public void setState(State right) {
        this.stateId = right.getValue();
    }

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

    public boolean getArchivalStatus() {
        return archivated;
    }

    public void setArchived() {
        this.archivated = true;
    }

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
}