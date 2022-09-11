package com.example.ghost_storage.Model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

// таблица с информацией какие госты ссылаются на данный
// dataId - id данного госта
// referralDataId - id госта с сылкой на данный
@Entity
@Table(name = "ghost_relation")
public class GhostRelation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private Integer dataId;
    private Integer referralDataId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDataId() {
        return dataId;
    }

    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Integer getReferralDataId() {
        return referralDataId;
    }

    public void setReferralDataId(Integer referralDataId) {
        this.referralDataId = referralDataId;
    }

    public GhostRelation() {
    }

    public GhostRelation(int dataId, int referralDataId ) {
        this.dataId = dataId;
        this.referralDataId = referralDataId;
    }
}
