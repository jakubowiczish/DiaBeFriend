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


    private boolean checkWhetherInputIsValid(EditText carbohydratesInGramsInput, EditText insulinUnitsInput, EditText sugarLevelInput) {
        if (carbohydratesInGramsInput.getText().toString().equals("")
                || insulinUnitsInput.getText().toString().equals("")
                || sugarLevelInput.getText().toString().equals("")
        ) {
            return false;
        }

        Measurement measurement = new Measurement(
                Integer.valueOf(carbohydratesInGramsInput.getText().toString()),
                Float.valueOf(insulinUnitsInput.getText().toString()),
                Integer.valueOf(sugarLevelInput.getText().toString())
        );

        if (measurement.getInsulinInUnits() <= 0 || measurement.getCarbohydratesInGrams() <= 0 || measurement.getSugarLevelBeforeMeal() <= 0) {
            return false;
        }

        return true;
    }


    private void handleUserInput(EditText carbohydratesInGramsInput, EditText insulinUnitsInput, EditText sugarLevelInput) {
        boolean inputIsValid = checkWhetherInputIsValid(carbohydratesInGramsInput, insulinUnitsInput, sugarLevelInput);

        if (inputIsValid) {
            Measurement measurement = new Measurement(
                    Integer.valueOf(carbohydratesInGramsInput.getText().toString()),
                    Float.valueOf(insulinUnitsInput.getText().toString()),
                    Integer.valueOf(sugarLevelInput.getText().toString())
            );
            Intent resultIntent = new Intent();
            resultIntent.putExtra("measurement", measurement);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        } else {
            dialogsManager.showInvalidInputDialog(this);
        }
    }


}
