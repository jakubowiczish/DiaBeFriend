package com.example.diabefriend.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.diabefriend.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.sql.Time;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartClick();
            }
        });

        if (TimerActivity.mTimerIsRunning) {
            startButton.setEnabled(false);
        }

        Button lastMeasurement = findViewById(R.id.lastMeasurementButton);
        lastMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLastMeasurementClick();
            }
        });
//        Class<?> activityClass;
//
//        try {
//            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
//            activityClass = Class.forName(
//                    Objects.requireNonNull(prefs.getString("lastActivity", TimerActivity.class.getName())));
//        } catch (ClassNotFoundException ex) {
//            activityClass = MainActivity.class;
//        }
//
//        startActivity(new Intent(this, activityClass));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onStartClick() {
        startActivity(new Intent(this, StartActivity.class));
    }

    private void onLastMeasurementClick() {
        startActivity(new Intent(this, TimerActivity.class));
    }
}
