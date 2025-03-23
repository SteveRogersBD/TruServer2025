package com.example.greenpulse.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.WeatherResponse;
import com.squareup.picasso.Picasso;

import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{

    Context context;
    List<WeatherResponse.Datum>days;

    public WeatherAdapter(Context context, List<WeatherResponse.Datum> days) {
        this.context = context;
        this.days = days;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherResponse.Datum day = days.get(position);

        holder.percip.setText(day.precip+"");
        holder.humi.setText(day.rh+"");
        holder.wind.setText(day.wind_spd+"");
        holder.temp.setText(day.temp+" F");
        holder.desc.setText(day.weather.description);
        holder.date.setText(day.datetime);

    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sunny;
        AppCompatButton percip,humi,wind;
        TextView temp,desc,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sunny = itemView.findViewById(R.id.weather_poster);
            percip = itemView.findViewById(R.id.percip_btn);
            humi = itemView.findViewById(R.id.humidity_btn);
            wind = itemView.findViewById(R.id.wind_btn);
            temp = itemView.findViewById(R.id.temp_tv);
            desc = itemView.findViewById(R.id.description_tv);
            date = itemView.findViewById(R.id.date_tv);
        }
    }
}