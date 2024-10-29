package com.example.pfmemo;

import java.sql.Timestamp;

public interface Const {
    String[] SPINNER_ITEMS = {
            "Select","Green","Red","Blue"
    };
    String[] SPINNER_IMAGES = {
            "ic_launcher_round",
            "androidlogot01",
            "androidlogot02",
            "androidlogot03"
    };

    int[] SPINNER_NUM = {
            R.drawable.ic_launcher_round,
            R.drawable.androidlogot01,
            R.drawable.androidlogot02,
            R.drawable.androidlogot03
    };

    public static long getCurrTime(){
        Timestamp curr_time = new Timestamp(System.currentTimeMillis());
        long time_second = curr_time.getTime();

        return time_second;
    }

}
