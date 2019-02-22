package com.example.diabefriend.activities;


import android.graphics.Color;
import android.os.Bundle;

import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.diabefriend.R;
import com.example.diabefriend.model.ResultMeasurement;

public class SummaryActivity extends AppCompatActivity {

    private Measurement measurement;
    private ResultMeasurement resultMeasurement;
    private EditText sugarLevelInputAfterMeal;
    private Button summarizeButton;
    private TextView summaryTextView;
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
                handleUserInput(sugarLevelInputAfterMeal, summaryTextView);
            }
        });

        summaryTextView = findViewById(R.id.summaryTextView);

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

    private void handleUserInput(EditText sugarLevelInputAfterMeal, TextView summaryTextView) {
        boolean inputIsValid = checkSugarLevelInput(sugarLevelInputAfterMeal);

        if (inputIsValid) {
            resultMeasurement = new ResultMeasurement(
                    measurement,
                    Integer.valueOf(sugarLevelInputAfterMeal.getText().toString())
            );


            String summaryString = createTextForSummary(resultMeasurement);
            summaryTextView.setText(summaryString);

            if (sugarLevelStandard(false, resultMeasurement.getSugarLevelAfterMeal()) == 1) {
                summaryTextView.setTextColor(Color.GREEN);
            } else {
                summaryTextView.setTextColor(Color.RED);
            }

        } else {
            dialogsManager.openInvalidInputDialog(this);
        }
    }

    private String createTextForSummary(ResultMeasurement resultMeasurement) {
        int carbohydratesInGrams = resultMeasurement.getMeasurement().getCarbohydratesInGrams();
        float insulinInUnits = resultMeasurement.getMeasurement().getInsulinInUnits();
        int sugarLevelBeforeMeal = resultMeasurement.getMeasurement().getSugarLevelBeforeMeal();
        int sugarLevelAfterMeal = resultMeasurement.getSugarLevelAfterMeal();

        String result = "You have eaten "
                + carbohydratesInGrams + " grams of carbohydrates,"
                + " your insulin dosage was " + insulinInUnits + " units"
                + " and your blood sugar level was " + sugarLevelBeforeMeal;

        String sugarLevelBeforeMealString;

        if (sugarLevelStandard(true, sugarLevelBeforeMeal) == 1) {
            sugarLevelBeforeMealString = ", which was a normal sugar level for a diabetic before meal.\n";
        } else if (sugarLevelStandard(true, sugarLevelBeforeMeal) == 2) {
            sugarLevelBeforeMealString = ", which was too high sugar level for a diabetic before meal.\n";
        } else {
            sugarLevelBeforeMealString = ", which was too low sugar level for a diabetic before meal.\n";
        }

        result += sugarLevelBeforeMealString +
                "Your sugar level after meal is " + sugarLevelAfterMeal;

        String sugarLevelAfterMealString;

        if (sugarLevelStandard(false, sugarLevelAfterMeal) == 1) {
            sugarLevelAfterMealString = ", which is a normal sugar level for a diabetic after meal\n";
        } else if (sugarLevelStandard(false, sugarLevelBeforeMeal) == 2) {
            sugarLevelAfterMealString = ", which is too high sugar level for a diabetic after meal\n";
        } else {
            sugarLevelAfterMealString = ", which is too low sugar level for a diabetic after meal\n";
        }

        result += sugarLevelAfterMealString;

        return result;
    }

    private int sugarLevelStandard(boolean measurementBeforeMeal, int sugarLevel) {
        if (measurementBeforeMeal) {
            if (sugarLevel >= 70 && sugarLevel <= 130) {
                return 1;
            } else if (sugarLevel > 130) {
                return 2;
            } else {
                return 0;
            }
        } else {
            if (sugarLevel >= 70 && sugarLevel <= 170) {
                return 1;
            } else if (sugarLevel > 170) {
                return 2;
            } else {
                return 0;
            }
        }
    }
}
