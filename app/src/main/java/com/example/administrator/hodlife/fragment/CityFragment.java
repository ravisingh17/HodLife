package com.example.administrator.hodlife.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hodlife.R;
import com.example.administrator.hodlife.model.WeatherModel;
import com.example.administrator.hodlife.utility.WeatherAPI;
import com.example.administrator.hodlife.utility.WeatherUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dell on 7/31/2017.
 */

public class CityFragment extends Fragment implements View.OnClickListener{

    private TextView locationText, temperatureMaxText, temperatureMinText, windSpeedText;
    private EditText mCityEditText;
    private ProgressDialog dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView =  inflater.inflate(R.layout.city_fragment, container, false);

        temperatureMaxText = (TextView) convertView.findViewById(R.id.max_temp);
        temperatureMinText = (TextView) convertView.findViewById(R.id.min_temp);
        windSpeedText = (TextView) convertView.findViewById(R.id.wind_speed);
        mCityEditText = (EditText) convertView.findViewById(R.id.city_edt_id);

        ((Button) convertView.findViewById(R.id.weather_button)).setOnClickListener(this);;
        return convertView;
    }

    public void onClick(View view){
        switch(view.getId()) {
            case R.id.weather_button:
                String str = mCityEditText.getText().toString().trim();
                if(str == null || str.equals("")|| str.length() < 2){
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.city_msg), Toast.LENGTH_SHORT).show();
                }else {
                    dialog.show();
                    hideKeyboard();
                    getCityWeather(str);
                }
                break;
        }
    }
    private void getCityWeather(String mCity){
        WeatherAPI.APIInterface apiInterface = WeatherAPI.getClient().create(WeatherAPI.APIInterface.class);
        Call<WeatherModel> call = apiInterface.getCityWeater(mCity, WeatherAPI.appId);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                try {
                    temperatureMaxText.setText(getActivity().getResources().getString(R.string.temp_max_lbl) + " : " + WeatherUtils.covertKelvinToCelsius(Float.valueOf(String.valueOf(response.body().getMain().getTempMax()))) + " \u2103");
                    temperatureMinText.setText(getActivity().getResources().getString(R.string.temp_min_lbl) + " : " + WeatherUtils.covertKelvinToCelsius(Float.valueOf(String.valueOf(response.body().getMain().getTempMin()))) + " \u2103");
                    windSpeedText.setText(getActivity().getResources().getString(R.string.wind_lbl) + response.body().getWind().getSpeed() + getActivity().getResources().getString(R.string.wind_unit_lbl));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
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
    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
