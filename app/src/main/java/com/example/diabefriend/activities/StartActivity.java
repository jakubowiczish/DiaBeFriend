package com.example.diabefriend.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.diabefriend.R;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StartActivity extends AppCompatActivity {

    private EditText carbohydratesInGramsInput;
    private EditText insulinUnitsInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        carbohydratesInGramsInput = findViewById(R.id.carbohydratesInGramsInput);
        insulinUnitsInput = findViewById(R.id.insulinInUnitsInput);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(carbohydratesInGramsInput, insulinUnitsInput);
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


    public void handleUserInput(EditText carbohydratesInGramsInput, EditText insulinUnitsInput) {
        boolean isInputValid = checkWhetherInputIsValid(carbohydratesInGramsInput, insulinUnitsInput);

        if (isInputValid) {
            Measurement measurement = new Measurement(
                    Integer.valueOf(carbohydratesInGramsInput.getText().toString()),
                    Float.valueOf(insulinUnitsInput.getText().toString())
            );

            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra("measurement", measurement);
            startActivity(intent);
        } else {
            openDialog();
        }
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.AlertDialogCustom);
        builder.setTitle(R.string.invalid_input_dialog).setMessage(R.string.try_again_dialog);

        AlertDialog invalidInputDialog = builder.create();
        invalidInputDialog.show();
    }

}
