package xyz.zjhwork.utils;


import xyz.zjhwork.common.NotificationType;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public final static String CURRENT_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    public final static String TODAY = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    public final static DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String  getFormat(Date date){
        String time = df.format(date);
        return time;
    }
    public static String  formatYearMonthDay(String time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
           time=dateFormat.format( dateFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


}
