package com.example.btl_weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtName, txtCountry, txtTemp, txtStatus, txtHumidity, txtWind, txtCloud, txtDay;
    ImageView imgIcon;
    String City = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map();
        GetCurrentWeatherData("Hanoi");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editSearch.getText().toString();
                if (city.equals("")){
                    City = "Hanoi";
                    GetCurrentWeatherData(City);
                }
                else {
                    City = city;
                    GetCurrentWeatherData(City);
                }

            }
        });
        btnChangeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editSearch.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("name", city);
                startActivity(intent);
            }
        });
    }
    public void GetCurrentWeatherData ( String data ) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=53fbf527d52d4d773e828243b90c1f8e";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText("Tên thành phố: "+name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String Day = simpleDateFormat.format(date);

                            txtDay.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietDo = jsonObjectMain.getString("temp");
                            String doAm = jsonObjectMain.getString("humidity");

                            Double a = Double.valueOf(nhietDo);
                            String NhietDo = String .valueOf(a.intValue());

                            txtTemp.setText(NhietDo+"°C");
                            txtHumidity.setText(doAm+"%");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+"m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");
                            txtCloud.setText(may+"%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");
                            txtCountry.setText("Tên quốc gia: "+country);
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
    private void Map() {
        editSearch = (EditText) findViewById(R.id.editTextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtName = (TextView) findViewById(R.id.textViewName);
        txtCountry = (TextView) findViewById(R.id.textViewCountry);
        txtTemp = (TextView) findViewById(R.id.textViewTemp);
        txtStatus = (TextView) findViewById(R.id.textViewStatus);
        txtHumidity = (TextView) findViewById(R.id.textViewHumidity);
        txtCloud = (TextView) findViewById(R.id.textViewCloud);
        txtWind = (TextView) findViewById(R.id.textViewWind);
        txtDay = (TextView) findViewById(R.id.textViewDay);
        imgIcon = (ImageView) findViewById(R.id.imageIcon);
    }
}