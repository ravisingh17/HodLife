package com.example.administrator.hodlife.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hodlife.R;
import com.example.administrator.hodlife.adapter.WeekWeatherAdapter;
import com.example.administrator.hodlife.model.List;
import com.example.administrator.hodlife.model.WeatherModel;
import com.example.administrator.hodlife.model.WeeklyModel;
import com.example.administrator.hodlife.utility.GetGPSLocation;
import com.example.administrator.hodlife.utility.WeatherAPI;
import com.example.administrator.hodlife.utility.WeatherUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Dell on 7/31/2017.
 */

public class WeekWeatherFragment extends Fragment implements View.OnClickListener, WeekWeatherAdapter.OnItemClickListener{

    private RecyclerView mListView;
    private WeekWeatherAdapter rAdapter;
    private TextView mTextView;
    private ProgressDialog dialog;
    private EditText mCityEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait..");
        rAdapter = new WeekWeatherAdapter(getActivity());
        rAdapter.SetOnItemClickListener(WeekWeatherFragment.this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View convertView =  inflater.inflate(R.layout.week_weather_fragment, container, false);

        mListView = (RecyclerView) convertView.findViewById(R.id.recycler_view);
        //mListView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setNestedScrollingEnabled(false);
        mListView.setHasFixedSize(false);
        mCityEditText = (EditText) convertView.findViewById(R.id.city_edt_id);

        mTextView = (TextView) convertView.findViewById(R.id.textView);
        Button sendBt = (Button) convertView.findViewById(R.id.send_bt);
        sendBt.setOnClickListener(this);

        if(GetGPSLocation.getAddress()!= null && GetGPSLocation.getAddress().length() > 0)
            mCityEditText.setText(GetGPSLocation.getAddress().toString().trim());
        getCurrentLocationWeather();
        return convertView;
    }
    private void updateData(ArrayList<List> mList ){
        if(mList != null
                && mList.size() > 0) {
            rAdapter.setModelList(getActivity(), mList);
            mListView.setAdapter(rAdapter);
            //rAdapter.notifyDataSetChanged();
            mListView.setVisibility(View.VISIBLE);
        }else{
            mListView.setVisibility(View.GONE);
        }
    }

    private void getCurrentLocationWeather(){
        if(GetGPSLocation.getLongitude() != 0 || GetGPSLocation.getLatitude() != 0) {
            dialog.show();
            WeatherAPI.APIInterface apiInterface = WeatherAPI.getClient().create(WeatherAPI.APIInterface.class);
            Call<WeeklyModel> call = apiInterface.getWeeklyCurrentWeather("" + GetGPSLocation.getLatitude(), "" + GetGPSLocation.getLongitude(), WeatherAPI.appId);
            call.enqueue(new Callback<WeeklyModel>() {
                @Override
                public void onResponse(Call<WeeklyModel> call, Response<WeeklyModel> response) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    ArrayList<List> mList = response.body().getList();
                    updateData(mList);
                    // mTextView.setText(response.body().getCnt()+"");
                    try {
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<WeeklyModel> call, Throwable t) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_msg), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClick(View view){
        switch(view.getId()) {
            case R.id.send_bt:
                dialog.show();

                String str = mCityEditText.getText().toString().trim();
                if(str == null || str.equals("")|| str.length() < 2){
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.city_msg), Toast.LENGTH_SHORT).show();
                }else {
                    dialog.show();
                    hideKeyboard();
                    getWeeklyWeather(str);
                }
                break;
        }
    }
    private void getWeeklyWeather(String mCity){
        WeatherAPI.APIInterface apiInterface = WeatherAPI.getClient().create(WeatherAPI.APIInterface.class);
        Call<WeeklyModel> call = apiInterface.getWeeklyWeather(mCity, WeatherAPI.appId);
        call.enqueue(new Callback<WeeklyModel>() {
            @Override
            public void onResponse(Call<WeeklyModel> call, Response<WeeklyModel> response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                ArrayList<List> mList = response.body().getList();
                updateData(mList);
                // mTextView.setText(response.body().getCnt()+"");

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<WeeklyModel> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.server_msg), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, List mListModel) {

    }
    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }
}
