package io.dcloud.H58E83894.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeUtils {

//    private static final long ONE_DAY = TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);

//    /**
//     * 比较日期
//     */
//    public static String compareDate(Context context, String date) {
//        return compareDate(context, date, "yyyy-MM-dd");
//    }

//    public static String compareDate(Context context, String date, String formatType) {
//        Date currentDate = longToDate(new Date().getTime(), formatType);
//        Date anotherDate = stringToDate(date, formatType);
//        if (currentDate.compareTo(anotherDate) == 0) {
//            return context.getString(R.string.str_today);
//        } else {
//            if (anotherDate.getTime() - currentDate.getTime() == ONE_DAY) {
//                return context.getString(R.string.str_tomorrow);
//            } else if (anotherDate.getTime() - currentDate.getTime() == 2 * ONE_DAY) {
//                return context.getString(R.string.str_day_after_tomorrow);
//            }
//        }
//        return "";
//    }

    /**
     * 格式化日期，由old格式向new格式转化
     */
    public static String formatDate(String date, String oldFormat, String newFormat) {
        long longTime = stringToLong(date, oldFormat);
        return longToString(longTime, newFormat);
    }

    public static long ONE_DAY = 24 * 60 * 60 * 1000;

    public static int dateLess(long oldTime, long nowTime) {
        long less = nowTime - oldTime;
        return (int) (less / ONE_DAY);
    }

    public static long stringToLong(String strTime, String formatType) {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }


    private static SimpleDateFormat getDateFormat(String formatType) {
        return new SimpleDateFormat(formatType, Locale.CHINA);
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    public static Date stringToDate(String strTime, String formatType) {
        try {
            SimpleDateFormat formatter = getDateFormat(formatType);
            return formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    // formatType格式为yyyy-MM-dd HH:mm:ss ==> yyyy年MM月dd日 HH时mm分ss秒
    public static String dateToString(Date data, String formatType) {
        return getDateFormat(formatType).format(data);
    }

    public static String longToString(long currentTime, String formatType) {
        Date date = longToDate(currentTime, formatType);
        return dateToString(date, formatType);
    }

    public static Date longToDate(long currentTime, String formatType) {
        Date dateOld = new Date(currentTime);
        String sDateTime = dateToString(dateOld, formatType);
        return stringToDate(sDateTime, formatType);
    }

    /**
     * 返回分钟数
     */
    public static String getSafeTimeNumStr(int num) {
        if (num > 9) {
            return String.valueOf(num);
        } else {
            return "0" + String.valueOf(num);
        }
    }
}