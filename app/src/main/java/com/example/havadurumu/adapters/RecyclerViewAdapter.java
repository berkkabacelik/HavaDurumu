package com.example.havadurumu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.havadurumu.R;
import com.example.havadurumu.Weather;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    Context context;
    List<Weather> listWeather;

    public RecyclerViewAdapter(Context context, List<Weather> listWeather)
    {
        this.context=  context;
        this.listWeather= listWeather;


    }

    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String ilkTarih = listWeather.get(position).date;
        SimpleDateFormat ilkTarihFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat guncelFormat= new SimpleDateFormat("dd.MM.yyyy");

        try{Date date = ilkTarihFormat.parse(ilkTarih);


            String formattedDate = guncelFormat.format(date);
            holder.date.setText(formattedDate);

        }
        catch (ParseException e)
        {
            e.printStackTrace();

        }

        holder.temp.setText(String.valueOf(listWeather.get(position).avgTemp+"°C"));
        String iconUrl = listWeather.get(position).iconUrl;
        Picasso.get().load(iconUrl).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return listWeather.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        ImageView imageView;
        TextView date,temp;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView   = itemView.findViewById(R.id.iconID);
            date        = itemView.findViewById(R.id.txtTarih);
            temp        = itemView.findViewById(R.id.txtSicaklik);

        }


    }
}
