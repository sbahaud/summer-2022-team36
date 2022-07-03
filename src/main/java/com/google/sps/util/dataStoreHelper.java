package com.google.sps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class dataStoreHelper {
    static public Date parseInputDate(String textDate){
        Date date;
        try {
            date = DateFormat.getDateInstance().parse(textDate);
        } catch (ParseException e) {
            return null;
        }
        return date;
    }
}
