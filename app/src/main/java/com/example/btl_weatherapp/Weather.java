package com.example.btl_weatherapp;

public class Weather {
    public String Day;
    public String Status;
    public String Image;
    public String MaxTemp;
    public String MinTemp;

    public Weather(String day, String status, String image, String maxTemp, String minTemp) {
        Day = day;
        Status = status;
        Image = image;
        MaxTemp = maxTemp;
        MinTemp = minTemp;
    }
}
