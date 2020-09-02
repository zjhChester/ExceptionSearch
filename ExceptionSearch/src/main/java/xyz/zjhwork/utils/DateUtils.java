package xyz.zjhwork.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
//    public static String getDatePoor(Date endDate, Date nowDate) {
//
//        long nd = 1000 * 24 * 60 * 60;//每天毫秒数
//
//        long nh = 1000 * 60 * 60;//每小时毫秒数
//
//        long nm = 1000 * 60;//每分钟毫秒数
//
//        long diff = endDate.getTime() - nowDate.getTime(); // 获得两个时间的毫秒时间差异
//
//        long day = diff / nd;   // 计算差多少天
//
//        long hour = diff % nd / nh; // 计算差多少小时
//
//        long min = diff % nd % nh / nm;  // 计算差多少分钟
//
//        return day + "天" + hour + "小时" + min + "分钟";
//    }
//
//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
//        String date1= "2001-08-01 08:10:14";
//        String date2= "2002-07-01 08:10:14";
//        Date d1 = simpleDateFormat.parse(date1);
//        Date d2 = simpleDateFormat.parse(date2);
//        long l = System.currentTimeMillis();
//        System.out.println(l/1000/60/24/60);
//        String time = DateUtils.getDatePoor(d2, d1);
//        System.out.println(time);
//    }
    public static String  getFormat(Date date){
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return time;
    }

//    public static void main(String[] args) {
//        System.out.println(DigestUtils.sha1Hex(DigestUtils.md5Hex("123")));
//    }
}
