package com.little.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author pengx
 * @date 2016/11/17
 */
public class DateUtil {

    /**
     * 判断某一天是否是周末（星期一，星期日）
     *
     * @param date
     * @return
     */
    public static boolean isWeekEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }
}
