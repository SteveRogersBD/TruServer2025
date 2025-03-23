package com.example.greenpulse;

import android.os.Build;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {
    public static String formatter(LocalDateTime past)
    {
        LocalDateTime now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            now = LocalDateTime.now();
        }
        Duration duration = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            duration = Duration.between(past, now);
        }
        String ans=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(duration.toMinutes()<=0)
            {
                ans = "Just now";
            }
            else if(duration.toMinutes()<60)
            {
                ans = duration.toMinutes() + " mins ago";
            }
            else if(duration.toHours()<24)
            {
                ans = duration.toHours() + " hours ago";
            }
            else{
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                ans = past.format(formatter);
            }
        }
        return ans;
    }
}
