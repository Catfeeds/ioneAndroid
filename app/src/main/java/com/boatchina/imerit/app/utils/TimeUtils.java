package com.boatchina.imerit.app.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by fflamingogo on 2016/10/20.
 */

public class TimeUtils  {
    public static String timeFormat(long time) {
        SimpleDateFormat sdfTwo = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒",
                Locale.getDefault());
        long l = time * 1000L;
        return sdfTwo.format(l);
    }
}
