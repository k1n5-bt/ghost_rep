package com.example.ghost_storage.Model;

import com.example.ghost_storage.Storage.FileRepo;

import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
            if (!str.equals("normReferences")) {
                Object obj1 = this.getClass().getMethod("get" + maxFieldNames().get(str)).invoke(this);
                String value1 = obj1 != null ? obj1.toString() : "";

                Object obj2 = this.getClass().getMethod("get" + maxFieldNames().get(str) + "FirstRedaction").invoke(this);
                String value2 = obj2 != null ? obj2.toString() : "";

                dict.put(str, new String[] {value1, value2});
            }
        }
        return dict;
    }

    public Map<String, String> getLastValues() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, String> dict = new HashMap<String, String>();
        for (String str : fieldNames())
        {
            if (!str.equals("normReferences")) {
                Object obj = this.getClass().getMethod("get" + maxFieldNames().get(str) + "FirstRedaction").invoke(this);
                if (obj == null) {
                    obj = this.getClass().getMethod("get" + maxFieldNames().get(str)).invoke(this);
                }
                String value = obj != null ? obj.toString() : "";
                dict.put(str, value);
            }
        }
        return dict;
    }

    public Data() {
        setState(State.ACTIVE);
    }

//    public Data(String name, String fileDesc, String OKCcode, String OKPDcode, String adoptionDate, String introductionDate, String developer, String predecessor, String contents, String levelOfAcceptance, String changes, String status, String referencesAmount) {
//        this.name = name;
//        this.fileDesc = fileDesc;
//        this.OKCcode = OKCcode;
//        this.OKPDcode = OKPDcode;
//        this.adoptionDate = adoptionDate;
//        this.introductionDate = introductionDate;
//        this.developer = developer;
//        this.predecessor = predecessor;
//        this.contents = contents;
//        this.levelOfAcceptance = levelOfAcceptance;
//        this.changes = changes;
//        this.status = status;
//    }

    public static String[] fieldNames() {
        String[] arr = {
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
                "normReferences",
                "changes",
                "modifications",
                "status"
        };
        return arr;
    }

    public static Map<String, String> ruFieldNames() {
        Map<String, String> map  = new HashMap<>() {{
            put("name", "Наименование стандарта");
            put("fileDesc", "Обозначение стандарта");
            put("OKCcode", "Код ОКС");
            put("OKPDcode", "Код ОКПД 2");
            put("adoptionDate", "Дата принятия");
            put("introductionDate", "Дата введения");
            put("developer", "Разработчик");
            put("predecessor", "Принят взамен");
            put("contents", "Текст документа");
            put("levelOfAcceptance", "Уровень принятия");
            put("changes", "Изменения");
            put("status", "Действующий/отменен/заменен");
            put("headContent", "Содержание");
            put("keywords", "Ключевые слова");
            put("keyPhrases", "Ключевые фразы");
            put("normReferences", "Нормативные ссылки");
            put("modifications", "Поправки");
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
            put("headContent", "HeadContent");
            put("keywords", "Keywords");
            put("keyPhrases", "KeyPhrases");
//            put("normReferences", "NormReferences");
            put("modifications", "Modifications");
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
            put("headContent", "");
            put("keywords", "");
            put("keyPhrases", "");
//            put("normReferences", "");
            put("modifications", "");
        }};
        return map;
    }

    public static Map<String, String> acceptanceLevels() {
        Map<String, String> map  = new HashMap<>() {{
            put("", "");
            put("a", "Международный");
            put("b", "Иностранный");
            put("c", "Стандарт организации");
            put("d", "Национальный/межгосударственный");
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


//    public String getNormReferences() {
//        return normReferences;
//    }
//
//    public void setNormReferences(String normReferences) {
//        this.normReferences = normReferences;
//    }
//
//    public String getNormReferencesFirstRedaction() {
//        return normReferencesFirstRedaction;
//    }
//
//    public void setNormReferencesFirstRedaction(String normReferencesFirstRedaction) {
//        this.normReferencesFirstRedaction = normReferencesFirstRedaction;
//    }

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
    private String headContent;
    private String keywords;
    private String keyPhrases;
    private String modifications;
    private String headContentFirstRedaction;
    private String keywordsFirstRedaction;
    private String keyPhrasesFirstRedaction;
    private String modificationsFirstRedaction;


    private String activeLinks;
    private String inactiveLinks;
    private String activeLinksFirstRedaction;
    private String inactiveLinksFirstRedaction;

    public int[] getActiveLinks() {
        String strLinksIds = activeLinks;
        if (!strLinksIds.equals("[]")) {
            return Arrays.stream(strLinksIds.substring(1, strLinksIds.length()-1).split(","))
                    .map(String::trim).mapToInt(Integer::parseInt).toArray();
        } else {
            return new int[0];
        }
    }
    public void setActiveLinks(List<Integer> activeLinksIds) {
        this.activeLinks = activeLinksIds.toString();
    }

    public String[] getInactiveLinks() {
        return inactiveLinks.split("#");
    }
    public void setInactiveLinks(List<String> inactiveLinks) {
        this.inactiveLinks = String.join("#", inactiveLinks);
    }



    public String getActiveLinksFirstRedaction() {
        return activeLinksFirstRedaction;
    }
    public void setActiveLinksFirstRedaction(String activeLinksFirstRedaction) {
        this.activeLinksFirstRedaction = activeLinksFirstRedaction;
    }
    public String getInactiveLinksFirstRedaction() {
        return inactiveLinksFirstRedaction;
    }
    public void setInactiveLinksFirstRedaction(String inactiveLinksFirstRedaction) {
        this.inactiveLinksFirstRedaction = inactiveLinksFirstRedaction;
    }



    //    private String normReferences;
    //    private String normReferencesFirstRedaction;

    public String getHeadContent() {
        return headContent;
    }

    public void setHeadContent(String headContent) {
        this.headContent = headContent;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeyPhrases() {
        return keyPhrases;
    }

    public void setKeyPhrases(String keyPhrases) {
        this.keyPhrases = keyPhrases;
    }

    public String getModifications() {
        return modifications;
    }

    public void setModifications(String modifications) {
        this.modifications = modifications;
    }

    public String getHeadContentFirstRedaction() {
        return headContentFirstRedaction;
    }

    public void setHeadContentFirstRedaction(String headContentFirstRedaction) {
        this.headContentFirstRedaction = headContentFirstRedaction;
    }

    public String getKeywordsFirstRedaction() {
        return keywordsFirstRedaction;
    }

    public void setKeywordsFirstRedaction(String keywordsFirstRedaction) {
        this.keywordsFirstRedaction = keywordsFirstRedaction;
    }

    public String getKeyPhrasesFirstRedaction() {
        return keyPhrasesFirstRedaction;
    }

    public void setKeyPhrasesFirstRedaction(String keyPhrasesFirstRedaction) {
        this.keyPhrasesFirstRedaction = keyPhrasesFirstRedaction;
    }

    public String getModificationsFirstRedaction() {
        return modificationsFirstRedaction;
    }

    public void setModificationsFirstRedaction(String modificationsFirstRedaction) {
        this.modificationsFirstRedaction = modificationsFirstRedaction;
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