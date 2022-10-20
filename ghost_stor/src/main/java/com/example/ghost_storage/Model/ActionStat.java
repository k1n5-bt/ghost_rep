package com.example.ghost_storage.Model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "action_stat")
public class ActionStat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ACTION_ID")
    private int actionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "data_id")
    private Data data;

    private Date date;

    public ActionStat(){ }

    public ActionStat(Action action, Data data){
        this.data = data;
        this.setAction(action);
        this.date = new Date();
    }

    public Action getAction () {
        return Action.parse(this.actionId);
    }

    public void setAction(Action right) {
        this.actionId = right.getValue();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
