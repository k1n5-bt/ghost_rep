package com.example.ghost_storage.Services;

import com.example.ghost_storage.Model.Data;
import com.example.ghost_storage.Storage.FileRepo;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    }

    public Date getTime() throws ParseException {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Yekaterinburg"));
        df.format(date);
        return date;
    }
}
