package com.example.diabefriend.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diabefriend.R;

import java.text.DecimalFormat;

public class SummaryActivity extends AppCompatActivity {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Measurement measurement = getIntent().getParcelableExtra("measurement");

        final Button dosageInformationButton = findViewById(R.id.dosageInformationButton);
        dosageInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosageInformationDialog(measurement);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void dosageInformationDialog(Measurement measurement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SummaryActivity.this, R.style.AlertDialogCustom);

        float insulinUnitsPerGrams = measurement.getInsulinInUnits() * 10 / measurement.getCarbohydratesInGrams();
        String insulinUnitsPerGramsString = decimalFormat.format(insulinUnitsPerGrams);

        String dosageMessage = "You gave yourself " + measurement.getCarbohydratesInGrams() +
                " grams of carbohydrates and " + measurement.getInsulinInUnits() + " insulin units" +
                " (" + insulinUnitsPerGramsString + " insulin units for every 10 grams of carbohydrates)";

        builder.setTitle(R.string.dosage_information_title).setMessage(dosageMessage);

        AlertDialog invalidInputDialog = builder.create();
        invalidInputDialog.show();
    }

}
