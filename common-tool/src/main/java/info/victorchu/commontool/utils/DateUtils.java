package info.victorchu.commontool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description:
 * @Date:2022/12/6 15:11
 * @Author:victorchutian
 */
public class DateUtils {
    /**
     * 判断日期是否处于 24H 制的时间区间,可以跨天
     *
     * @param date            待判断时间
     * @param strDateBegin    起始时间字符串，例如10:00
     * @param strDateEnd      结束时间字符串
     * @param dateFormatStr   格式（HH:mm 或 HH:mm:ss）
     * @return
     */
    public static boolean isDateInDayTime(Date date, String strDateBegin, String strDateEnd, String dateFormatStr) throws ParseException {

        SimpleDateFormat afterSdf = new SimpleDateFormat(dateFormatStr);
        Date dBegin = null;
        Date dEnd = null;
        dBegin = afterSdf.parse(strDateBegin);
        dEnd = afterSdf.parse(strDateEnd);

        Calendar calPlayer = Calendar.getInstance();
        calPlayer.setTime(date);

        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        calBegin.set(calPlayer.get(Calendar.YEAR) , calPlayer.get(Calendar.MONTH), calPlayer.get(Calendar.DAY_OF_MONTH));

        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        calEnd.set(calPlayer.get(Calendar.YEAR) , calPlayer.get(Calendar.MONTH), calPlayer.get(Calendar.DAY_OF_MONTH));

        if(calEnd.before(calBegin)){
            // 结束时间在第二天
            if (calPlayer.before(calBegin) && calPlayer.after(calEnd)) {
                return false;
            }else {
                return true;
            }
        }else {
            if(calPlayer.before(calBegin)|| calPlayer.after(calEnd)) {
                return false;
            }
            else {
                return true;
            }
        }
    }
}
