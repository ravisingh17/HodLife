package com.example.administrator.hodlife.fragment;

import android.app.ProgressDialog;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hodlife.R;
import com.example.administrator.hodlife.model.WeatherModel;
import com.example.administrator.hodlife.screen.HomeActivity;
import com.example.administrator.hodlife.utility.GPSInterface;
import com.example.administrator.hodlife.utility.GetGPSLocation;
import com.example.administrator.hodlife.utility.WeatherAPI;
import com.example.administrator.hodlife.utility.WeatherUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dell on 7/31/2017.
 */

public class HomeFragment extends Fragment implements GPSInterface {

    private TextView locationText, temperatureMaxText, temperatureMinText, windSpeedText, sunriseTimeText, sunsetTimeText;
    LocationManager mLocationManager;
    private GetGPSLocation mGPSLocation = null;
    private ProgressDialog dialog;
    private LinearLayout mainLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);

        mainLayout = (LinearLayout) view.findViewById(R.id.main_layout);
        locationText = (TextView) view.findViewById(R.id.locationText);
        sunriseTimeText = (TextView) view.findViewById(R.id.sunrise_time);
        sunsetTimeText = (TextView) view.findViewById(R.id.sunset_time);
        temperatureMaxText = (TextView) view.findViewById(R.id.max_temp);
        temperatureMinText = (TextView) view.findViewById(R.id.min_temp);
        windSpeedText = (TextView) view.findViewById(R.id.wind_speed);
        mainLayout.setVisibility(View.VISIBLE);

        mGPSLocation = new GetGPSLocation(getActivity(), HomeFragment.this);
        dialog.show();
        return view;
    }
    @Override
    public void onCurrentLocation(double latitude, double longitude, String address) {
        locationText.setText(getActivity().getResources().getString(R.string.your_location)+ mGPSLocation.getAddress());
        if(latitude != 0
                && longitude != 0) {
            getData(""+ latitude, ""+ longitude);//"22.2093", "75.7619");
        }
    }
    private void getData(String latitude, String longitude){
        WeatherAPI.APIInterface apiInterface = WeatherAPI.getClient().create(WeatherAPI.APIInterface.class);
        Call<WeatherModel> call = apiInterface.getCurrentWeater(latitude, longitude, WeatherAPI.appId);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
//                mainLayout.setVisibility(View.VISIBLE);
                String name = response.body().getName();
                temperatureMaxText.setText(getActivity().getResources().getString(R.string.temp_max_lbl)+" : "+ WeatherUtils.covertKelvinToCelsius(Float.valueOf(String.valueOf(response.body().getMain().getTempMax()))) +" \u2103");
                temperatureMinText.setText(getActivity().getResources().getString(R.string.temp_min_lbl)+" : "+ WeatherUtils.covertKelvinToCelsius(Float.valueOf(String.valueOf(response.body().getMain().getTempMin()))) +" \u2103");
                windSpeedText.setText(getActivity().getResources().getString(R.string.wind_lbl)+ response.body().getWind().getSpeed() + getActivity().getResources().getString(R.string.wind_unit_lbl));
                sunriseTimeText.setText(getActivity().getResources().getString(R.string.sunrise_lbl)+WeatherUtils.getTimeStamp(response.body().getSys().getSunrise().longValue(), "HH:mm:ss a"));
                sunsetTimeText.setText(getActivity().getResources().getString(R.string.sunset_lbl)+WeatherUtils.getTimeStamp(response.body().getSys().getSunrise().longValue(), "HH:mm:ss a"));
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
