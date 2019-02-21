package com.example.diabefriend.activities;

import android.os.Bundle;

import com.example.diabefriend.model.Measurement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.diabefriend.R;

public class SummaryActivity extends AppCompatActivity {

    private Measurement measurement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        measurement = getIntent().getParcelableExtra("measurement");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
