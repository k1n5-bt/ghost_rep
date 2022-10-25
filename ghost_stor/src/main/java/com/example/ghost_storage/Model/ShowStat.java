package com.example.ghost_storage.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Entity
@Table(name = "show_stat")
public class ShowStat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileDesc;

    private String oks;

    private String okpd;

    private Date date;

    public ShowStat(String fileDesc, String oks, String okpd) {
        this.fileDesc = fileDesc;
        this.oks = oks;
        this.okpd = okpd;
        this.date = getTime();
    }

    private Date getTime() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        df.format(date);
        return date;
    }

    public ShowStat() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getOks() {return oks;}

    public void setOks(String oks) {this.oks = oks;}

    public String getOkpd() {return okpd;}

    public void setOkpd(String okpd) {this.okpd = okpd;}

    public Date getDate() {return date;}

    public void setDate(Date date) { this.date = date; }

    public void setFileDesc(String desc) {this.fileDesc = desc;}

    public String getFileDesc() {
        return this.fileDesc;
    }
}
