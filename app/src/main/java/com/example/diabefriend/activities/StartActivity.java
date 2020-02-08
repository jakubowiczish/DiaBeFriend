package com.example.diabefriend.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.diabefriend.R;
import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.lang3.StringUtils;

public class StartActivity extends AppCompatActivity {

    private EditText carbohydratesInGramsInput;
    private EditText insulinUnitsInput;
    private EditText sugarLevelInput;

    private DialogsManager dialogsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeComponents();
    }

    private void initializeComponents() {
        dialogsManager = new DialogsManager();

        carbohydratesInGramsInput = findViewById(R.id.carbohydratesInGramsInput);
        insulinUnitsInput = findViewById(R.id.insulinInUnitsInput);
        sugarLevelInput = findViewById(R.id.sugarLevelInput);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(carbohydratesInGramsInput, insulinUnitsInput, sugarLevelInput);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void handleUserInput(EditText carbohydratesInGramsInput, EditText insulinUnitsInput, EditText sugarLevelInput) {
        boolean isInputValid = isInputValid(carbohydratesInGramsInput, insulinUnitsInput, sugarLevelInput);
        if (!isInputValid) {
            dialogsManager.showInvalidInputDialog(this);
            return;
        }

        final Measurement measurement = createMeasurementFromInput(carbohydratesInGramsInput, insulinUnitsInput, sugarLevelInput);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(getResources().getString(R.string.measurement_string), measurement);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private boolean isInputValid(EditText carbohydratesInGramsInput, EditText insulinUnitsInput, EditText sugarLevelInput) {
        if (StringUtils.isAnyEmpty(
                carbohydratesInGramsInput.getText().toString(),
                insulinUnitsInput.getText().toString(),
                sugarLevelInput.getText().toString())) {
            return false;
        }

        final Measurement measurement = createMeasurementFromInput(carbohydratesInGramsInput, insulinUnitsInput, sugarLevelInput);

        return !(measurement.getInsulinInUnits() <= 0)
                && measurement.getCarbohydratesInGrams() > 0
                && measurement.getSugarLevelBeforeMeal() > 0;
    }

    private Measurement createMeasurementFromInput(EditText carbohydratesInGramsInput, EditText insulinUnitsInput, EditText sugarLevelInput) {
        return Measurement.builder()
                .carbohydratesInGrams(Integer.valueOf(carbohydratesInGramsInput.getText().toString()))
                .insulinInUnits(Float.valueOf(insulinUnitsInput.getText().toString()))
                .sugarLevelBeforeMeal(Integer.valueOf(sugarLevelInput.getText().toString()))
                .build();
    }
}
