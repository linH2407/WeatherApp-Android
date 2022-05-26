package com.example.btl_weatherapp;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity2 extends AppCompatActivity {
    String tenThanhPho = "";
    ImageView imageBack;
    TextView txtName;
    ListView lv;

    CustomAdapter customAdapter;
    ArrayList<Weather> listWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Map();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("Ketqua","Du lieu truyen qua: " + city);
        if(city.equals("")) {
            tenThanhPho = "Hanoi";
            Get7DayData(tenThanhPho);
        }else {
            tenThanhPho=city;
            Get7DayData(tenThanhPho);

        }
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }


        });

    }

    private void Map() {
        imageBack = (ImageView) findViewById(R.id.imageViewBack);
        txtName = (TextView) findViewById(R.id.textViewTenThanhPho);
        lv = (ListView) findViewById(R.id.listView);
        listWeather = new ArrayList<Weather>();
        customAdapter = new CustomAdapter(this,MainActivity2.this.listWeather);
        lv.setAdapter(customAdapter);
    }

    private void Get7DayData(String data) {
        String url = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+data+"&units=metric&cnt=7&appid=53fbf527d52d4d773e828243b90c1f8e";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Ketqua",response);
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                            String name = jsonObjectCity.getString("name");
                            txtName.setText(name);

                            JSONArray jsonArrayList = jsonObject.getJSONArray("list");
                            for(int i = 0; i<jsonArrayList.length();i++){
                                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                                String ngay = jsonObjectList.getString("dt");

                                long l = Long.valueOf(ngay);
                                Date date = new Date(l*1000);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp = jsonObjectList.getJSONObject("temp");
                                String max = jsonObjectTemp.getString("max");
                                String min = jsonObjectTemp.getString("min");

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String NhietDoMax = String.valueOf(a.intValue());
                                String NhietDoMin = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObjectWeather.getString("description");
                                String icon = jsonObjectWeather.getString("icon");

                                listWeather.add(new Weather(Day, status,icon,NhietDoMax, NhietDoMin));
                            }
                            customAdapter.setList(listWeather);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}