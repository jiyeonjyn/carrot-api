package com.umc.courtking.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Processing {

    public static String processAddress(String rawAddress) {
        if (rawAddress.contains("동 ")) {
            rawAddress = rawAddress.substring(0, rawAddress.indexOf("동 ") + 1); //서울특별시 광진구 화양동
        } else if (rawAddress.contains("면 ")) {
            rawAddress = rawAddress.substring(0, rawAddress.indexOf("면 ") + 1);
        } else if (rawAddress.contains("읍 ")) {
            rawAddress = rawAddress.substring(0, rawAddress.indexOf("읍 ") + 1);
        }
        return rawAddress.substring(rawAddress.indexOf(" ") + 1); //광진구 화양동
    }

    public static String processTime(String rawTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        long timeMillis = 0;
        try {
            timeMillis = format.parse(rawTime).getTime();
        } catch (ParseException e) {
            return rawTime;
        }
        long diff = now-timeMillis;
        if (diff/1000 < 60)
            return diff/1000 + "초 전";
        else if (diff/(1000*60) < 60)
            return diff/(1000*60) + "분 전";
        else if (diff/(1000*60*60) < 24 )
            return diff/(1000*60) + "시간 전";
        else if (diff/(1000*60*60*24) < 7 )
            return diff/(1000*60*60*24) + "일 전";
        else
            diff = diff/(1000*60*60*24);

        if (diff/7 < 5)
            return diff/7 + "주 전";
        else if (diff/30 < 12)
            return diff/30 + "달 전";
        else
            return diff/365 + "년 전";
    }
}
