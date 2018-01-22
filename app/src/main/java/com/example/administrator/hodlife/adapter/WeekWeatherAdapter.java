package com.example.administrator.hodlife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.hodlife.R;
import com.example.administrator.hodlife.model.List;
import com.example.administrator.hodlife.model.WeeklyModel;
import com.example.administrator.hodlife.utility.WeatherUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Administrator on 1/22/2018.
 */

public class WeekWeatherAdapter extends RecyclerView.Adapter<WeekWeatherAdapter.CustomViewHolder> {
    private Context mContext;
    private ArrayList<List> mListModels;
    private OnItemClickListener mItemClickListener;
    public WeekWeatherAdapter(Context context) {
        this.mContext = context;
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, final List mListModel);
    }
    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    public void setModelList(Context context, ArrayList<List> ListModels) {
        this.mListModels = ListModels;
        this.mContext = context;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.week_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }
    private void updateRow(CustomViewHolder holder, List listModel) {
        holder.timeText.setText(WeatherUtils.getTimeStamp(listModel.getDt(), "dd-MM-yyyy HH:mm:ss a"));
        holder.windText.setText(mContext.getString(R.string.wind_lbl)+""+listModel.getWind().getSpeed()+ mContext.getString(R.string.wind_unit_lbl));
        holder.presureText.setText(mContext.getResources().getString(R.string.pressure_lbl)+" "+listModel.getMain().getPressure() + " " +mContext.getString(R.string.pressure_unit_lbl));
        holder.temperatureText.setText(mContext.getResources().getString(R.string.temp_lbl)+" : "+ WeatherUtils.covertKelvinToCelsius(listModel.getMain().getTemp()) +" \u2103");
        holder.humidityText.setText(mContext.getResources().getString(R.string.humidity_lbl) + " " + listModel.getMain().getHumidity() + " %");
    }
    @Override
    public void onBindViewHolder(WeekWeatherAdapter.CustomViewHolder holder, int position) {
        updateRow(holder, mListModels.get(position));
    }

    @Override
    public int getItemCount() {
        if(null != mListModels) {
            return mListModels.size();
        }
        return 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView timeText, windText, presureText, temperatureText, humidityText;

        public CustomViewHolder(View convertView) {
            super(convertView);
            timeText = (TextView) convertView.findViewById(R.id.date_text);
            windText = (TextView) convertView.findViewById(R.id.wind_text);
            presureText = (TextView) convertView.findViewById(R.id.presure_text);
            temperatureText = (TextView) convertView.findViewById(R.id.temp_text);
            humidityText = (TextView) convertView.findViewById(R.id.humidity_text);
            convertView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mItemClickListener != null){
                mItemClickListener.onItemClick(view, mListModels.get(getPosition()));
            }
        }
    }
}
