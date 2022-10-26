package com.example.ghost_storage.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        this.date = getTime();
    }

    private Date getTime() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        df.format(date);
        return date;
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
