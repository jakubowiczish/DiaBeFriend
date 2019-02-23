package com.example.diabefriend.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.diabefriend.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

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

        Button currentMeasurement = findViewById(R.id.currentMeasurementButton);
        currentMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLastMeasurementClick();
            }
        });

        Button searchForProducts = findViewById(R.id.searchForProductsButton);
        searchForProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchForProductsClick();
            }
        });

        Button informationButton = findViewById(R.id.informationButton);
        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInformationClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onStartClick() {
        if (!TimerActivity.mTimerIsRunning) {
            startActivity(new Intent(this, StartActivity.class));
        } else {
            showThereIsAlreadyExistingMeasurementDialog();
        }

    }

    private void onLastMeasurementClick() {
        if (TimerActivity.mTimerIsRunning) {
            startActivity(new Intent(this, TimerActivity.class));
        } else {
            showThereIsNoExistingMeasurementAtTheMomentDialog();
        }
    }

    private void onSearchForProductsClick() {
        startActivity(new Intent(this, SearchForProductsActivity.class));
    }


    private void onInformationClick() {
        startActivity(new Intent(this, InformationActivity.class));
    }

    private void showThereIsAlreadyExistingMeasurementDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle(R.string.unable_to_start_measurement_title).setMessage(R.string.existing_measurement_in_the_background);

        AlertDialog invalidInputDialog = builder.create();
        invalidInputDialog.show();
    }

    private void showThereIsNoExistingMeasurementAtTheMomentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setTitle(R.string.unable_to_find_current_measurement_title).setMessage(R.string.no_existing_measurement);

        AlertDialog invalidInputDialog = builder.create();
        invalidInputDialog.show();
    }


}
