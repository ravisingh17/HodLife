package com.example.administrator.hodlife.utility;

import com.example.administrator.hodlife.model.WeatherModel;
import com.example.administrator.hodlife.model.WeeklyModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 1/19/2018.
 */

public class WeatherAPI {

    public static final String appId = "4ceca0ab265c5d8284d7003c268d9889";
    private static final String url = "http://api.openweathermap.org/data/2.5/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public interface APIInterface{

        @GET("weather")
        Call<WeatherModel> getCurrentWeater(@Query("lat") String latitude, @Query("lon") String longitude, @Query("APPID") String appId);
        @GET("weather")
        Call<WeatherModel> getCityWeater(@Query("q") String city, @Query("APPID") String appId);
        @GET("forecast")
        Call<WeeklyModel> getWeeklyWeather(@Query("q") String city, @Query("APPID") String appId);
        @GET("forecast")
        Call<WeeklyModel> getWeeklyCurrentWeather(@Query("lat") String latitude, @Query("lon") String longitude, @Query("APPID") String appId);
    }
}
