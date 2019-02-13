package com.example.diabefriend.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.diabefriend.R;
import com.example.diabefriend.dialogs.DialogWindow;
import com.example.diabefriend.model.Measurement;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DecimalFormat;

public class StartActivity extends AppCompatActivity {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText carbohydratesInGramsInput = findViewById(R.id.carbohydratesInGramsInput);
        final EditText insulinUnitsInput = findViewById(R.id.insulinInUnitsInput);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonOnClick(carbohydratesInGramsInput, insulinUnitsInput);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private boolean checkWhetherInputIsValid(EditText carbohydratesInGramsInput, EditText insulinUnitsInput) {
        if (carbohydratesInGramsInput.getText().toString().equals("") || insulinUnitsInput.getText().toString().equals("")) {
            return false;
        }

        Measurement measurement = new Measurement(
                Integer.valueOf(carbohydratesInGramsInput.getText().toString()),
                Float.valueOf(insulinUnitsInput.getText().toString())
        );

        if (measurement.getInsulinInUnits() <= 0 || measurement.getCarbohydratesInGrams() <= 0) {
            return false;
        }

        return true;
    }


    public void submitButtonOnClick(EditText carbohydratesInGramsInput, EditText insulinUnitsInput) {
        boolean isInputValid = checkWhetherInputIsValid(carbohydratesInGramsInput, insulinUnitsInput);

        if (isInputValid) {
            startActivity(new Intent(StartActivity.this, SummaryActivity.class));
        } else {
            openDialog();
        }
    }

    private void openDialog() {
        DialogWindow dialogWindow = new DialogWindow();
        dialogWindow.show(getSupportFragmentManager(), "invalid input dialog");
    }

}
