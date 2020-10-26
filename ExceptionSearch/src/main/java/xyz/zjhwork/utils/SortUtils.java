package xyz.zjhwork.utils;


import xyz.zjhwork.entity.Exception;
import xyz.zjhwork.entity.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SortUtils {
     public static List sort(List list){
        Collections.sort(list, new Comparator<Exception>() {
            @Override
            public int compare(Exception e1, Exception e2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = format.parse(e1.getCreateTime());
                    Date dt2 = format.parse(e2.getCreateTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }  catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return list;
    }


}
