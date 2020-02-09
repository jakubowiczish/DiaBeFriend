package com.example.diabefriend.utils;

import android.content.SharedPreferences;

import com.example.diabefriend.model.measurement.Measurement;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class Utils {
    public static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public static String createMeasurementJsonString(Measurement measurement) {
        return new Gson().toJson(measurement);
    }

    public static Measurement createMeasurementFromJson(SharedPreferences preferences, String measurementString) {
        String measurementJson = preferences.getString(measurementString, "");
        return new Gson().fromJson(measurementJson, Measurement.class);
    }
}
