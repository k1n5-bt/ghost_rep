package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Action;
import com.example.ghost_storage.Model.ActionStat;
import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Model.ShowStat;
import com.example.ghost_storage.Storage.ActionStatRepo;
import com.example.ghost_storage.Storage.FileRepo;
import com.example.ghost_storage.Storage.ShowStatRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Service
public class StatisticService {

    private final FileRepo fileRepo;
    private final ShowStatRepo showStatRepo;
    private final ActionStatRepo actionStatRepo;

    public StatisticService(FileRepo fileRepo, ShowStatRepo showStatRepo, ActionStatRepo actionStatRepo) {
        this.fileRepo = fileRepo;
        this.showStatRepo = showStatRepo;
        this.actionStatRepo = actionStatRepo;
    }

    public void commitCall(Data file) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        String desc = file.getLastDesc();
        String oks = file.getLastOKS();
        String okpd = file.getLastOKPD();
        ShowStat showStat = new ShowStat(desc, oks, okpd);
        showStatRepo.save(showStat);
    }

    public void commitNew(Data file) throws ParseException {
        actionStatRepo.save(new ActionStat(Action.New, file));
    }

    public void commitEdit(Data file) throws ParseException {
        actionStatRepo.save(new ActionStat(Action.Edit, file));
    }

    public void commitArchive(Data file) throws ParseException {
        actionStatRepo.save(new ActionStat(Action.Archive, file));
    }

    public void commitReplace(int file_id) throws ParseException {
        List<Data> docs = fileRepo.findById(file_id);
        if (docs.size() > 0) {
            Data file = docs.get(0);
            actionStatRepo.save(new ActionStat(Action.Replace, file));
        }
    }


    private Date getTime() throws ParseException {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        df.format(date);
        return date;
    }

    private void createShowStatRecord(String desc, String oks, String okpd, Date date) {

    }

    private void createActionStatRecord(String action, int dataId, Date date) {

    }
}
