package com.example.diabefriend.activities;


import android.os.Bundle;

import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.diabefriend.R;
import com.example.diabefriend.model.ResultMeasurement;

public class SummaryActivity extends AppCompatActivity {

    private Measurement measurement;
    private ResultMeasurement resultMeasurement;
    private EditText sugarLevelInputAfterMeal;
    private Button summarizeButton;
    private DialogsManager dialogsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialogsManager = new DialogsManager();

        measurement = getIntent().getParcelableExtra("measurement");

        sugarLevelInputAfterMeal = findViewById(R.id.sugarLevelInputAfterMeal);

        summarizeButton = findViewById(R.id.summarizeButton);
        summarizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput(sugarLevelInputAfterMeal);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private boolean checkSugarLevelInput(EditText sugarLevelInputAfterMeal) {
        if (sugarLevelInputAfterMeal.getText().toString().equals("")) {
            return false;
        }

        if (Integer.valueOf(sugarLevelInputAfterMeal.getText().toString()) <= 0) {
            return false;
        }

        return true;
    }

    private void handleUserInput(EditText sugarLevelInputAfterMeal) {
        boolean inputIsValid = checkSugarLevelInput(sugarLevelInputAfterMeal);

        if (inputIsValid) {
            resultMeasurement = new ResultMeasurement(
                    measurement,
                    Integer.valueOf(sugarLevelInputAfterMeal.getText().toString())
            );
        } else {
            dialogsManager.openInvalidInputDialog(this);
        }
    }


}
