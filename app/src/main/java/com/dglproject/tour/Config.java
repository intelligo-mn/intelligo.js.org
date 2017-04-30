package com.dglproject.tour;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

/**
 * Created by Tortuvshin Byambaa on 3/3/2017.
 */

public class Config {
    public static String WebURL = "https://www.dglproject.com";
    public static String UserService= "https://www.dglproject.com/applications/UserService.php";

    public static long generateAccessKey(){
        long y = Calendar.getInstance().get(Calendar.YEAR), m = Calendar.getInstance().get(Calendar.MONTH) + 1, d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH), a = 4, b = 7, c = 12;
        return (y+m+d)*a*b*c*(y*c+m*b+d*a);
    }
}
