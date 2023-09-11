package com.example.havadurumu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.havadurumu.adapters.RecyclerViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewID;
    EditText sehirTxt;
    Button btn;
    TextView havaDurumuTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewID  = (RecyclerView)findViewById(R.id.recyclerViewID);
        sehirTxt        = (EditText)findViewById(R.id.sehirTxt);
        btn             = (Button)findViewById(R.id.btnID);
        havaDurumuTxt = (TextView)findViewById(R.id.havaDurumuTxt);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sehir=sehirTxt.getText().toString();
                OkHttpClient client = new OkHttpClient();
                String url="https://api.weatherapi.com/v1/forecast.json?key=f5aaca1af2fd4dbe97a62651232108&q="+sehir+"&days=3&aqi=yes&alerts=no\n";
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                client.newCall(request).enqueue(new Callback()

                {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e)
                    {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull Call call,@NonNull Response response) throws IOException

                    {

                        if (response.isSuccessful())
                        {
                            String myResponse = response.body().string() ;

                            List<Weather> listWeather = new ArrayList<>();
                            try {
                                JSONObject jsonResponse = new JSONObject(myResponse);
                                JSONObject jsonForecast = jsonResponse.getJSONObject("forecast");
                                JSONArray jsonArrayDays = jsonForecast.getJSONArray("forecastday");

                                for(int i = 0; i<jsonArrayDays.length();i++){
                                    JSONObject day = jsonArrayDays.getJSONObject(i);
                                    String date = day.getString("date");
                                    JSONObject temps = day.getJSONObject("day");
                                    double avgTemp = temps.getDouble("avgtemp_c");
                                    JSONObject condition = temps.getJSONObject("condition");
                                    String iconUrl  = condition.getString("icon");

                                    Weather weather = new Weather();
                                    weather.date = date;
                                    weather.avgTemp=avgTemp;
                                    weather.iconUrl="https:"+iconUrl;
                                    listWeather.add(weather);


                                }
                                havaDurumuTxt.setText(sehir.toUpperCase()+ "  için 3 Günlük Ortalama Sıcaklık");


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(MainActivity.this, listWeather);
                                        recyclerViewID.setAdapter(adapter);
                                        recyclerViewID.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                    }
                                });


                            }
                            catch (JSONException e) {
                                e.printStackTrace();

                            }


                        }

                    }
                });

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        });



    }
}