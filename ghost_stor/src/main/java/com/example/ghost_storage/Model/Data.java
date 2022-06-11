package com.example.ghost_storage.Model;

import javax.persistence.*;

@Entity
public class Data {
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
//    @Transient
//    private List<String> keywords;
//    @Transient
//    private List<String> keyPhrases;
//    @Transient
//    private List<String> links;

    public Data() {
    }

//    public Data(String name, String fileDesc, User user) {
//        this.author = user;
//        this.name = name;
//        this.fileDesc = fileDesc;
//    }

    public Data(String name,
                String fileDesc,
                User author,
                String codeName,
                String OKCcode,
                String OKPDcode,
                String adoptionDate,
                String introductionDate,
                String developer,
                String predecessor,
                String contents,
                String levelOfAcceptance,
                String changes,
                String status,
                String referencesAmount) {
        this.name = name;
        this.fileDesc = fileDesc;
        this.author = author;
        this.codeName = codeName;
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

    public String getFilename() {
        boolean f = filename != null && filename.length() > 0;
        return f ? filename : "";
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
