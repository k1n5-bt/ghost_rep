package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Storage.FileRepo;
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
    private final DataService dataService;

    public StatisticService(FileRepo fileRepo, DataService dataService) {
        this.fileRepo = fileRepo;
        this.dataService = dataService;
    }

    public void commitCall(Data file) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ParseException {
        String desc = file.getLastDesc();
        String oks = file.getLastOKS();
        String okpd = file.getLastOKPD();
        Date date = getTime();
        createShowStatRecord(desc, oks, okpd, date);
    }

    public void commitNew(Data file) throws ParseException {
        String action = "new";
        commitAction(file, action);
    }

    public void commitEdit(Data file) throws ParseException {
        String action = "edit";
        commitAction(file, action);
    }

    public void commitArchive(Data file) throws ParseException {
        String action = "archive";
        commitAction(file, action);
    }

    public void commitReplace(int file_id) throws ParseException {
        List<Data> docs = fileRepo.findById(file_id);
        if (docs.size() > 0) {
            Data file = docs.get(0);
            String action = "replace";
            commitAction(file, action);
        }
    }

    private void commitAction(Data file, String act) throws ParseException {
        String action = act;
        int dataId = file.getId();
        Date date = getTime();
        createActionStatRecord(action, dataId, date);
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
