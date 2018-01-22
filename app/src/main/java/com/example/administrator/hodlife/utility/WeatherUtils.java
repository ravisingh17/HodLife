package com.example.administrator.hodlife.utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 1/21/2018.
 */

public class WeatherUtils {

    // Converts to celsius
    public static float convertFahrenheitToCelsius(float fahrenheit) {
        return ((fahrenheit - 32) * 5 / 9);
    }
    // Converts to fahrenheit
    public static float convertCelsiusToFahrenheit(float celsius) {
        return ((celsius * 9) / 5) + 32;
    }

    public static int covertKelvinToCelsius(float kelvin){
        // Kelvin to Degree Celsius Conversion
        float celsius = kelvin - 273.15F;
        System.out.println("Celsius: "+ celsius);
        int p = (int)celsius;
        return p;
    }

    public static String getDateFormat(String rawQuestion){
        String formattedDate = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM_yyyy HH:mm:ss");
        TimeZone utcZone = TimeZone.getTimeZone("UTC");
        simpleDateFormat.setTimeZone(utcZone);
        try {
            Date myDate = simpleDateFormat.parse(rawQuestion);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            formattedDate = simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String getTimeStamp(long unixSeconds, String pattern){
// convert seconds to milliseconds
        Date date = new Date();
// the format of your date
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"HH:mm:ss a");//dd-MM-yyyy HH:mm:ss a
// give a timezone reference for formatting (see comment at the bottom)
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
