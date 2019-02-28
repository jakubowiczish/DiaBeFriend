package com.example.diabefriend.activities;


import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diabefriend.R;
import com.example.diabefriend.model.ResultMeasurement;
import com.example.diabefriend.model.Utils;

public class SummaryActivity extends AppCompatActivity {

    private Measurement measurement;
    private ResultMeasurement resultMeasurement;
    private EditText sugarLevelInputAfterMeal;
    private Button summarizeButton;

    private TextView carbohydratesText;
    private TextView insulinText;
    private TextView sugarBeforeText;
    private TextView sugarAfterText;

    private ImageView carbohydratesImage;
    private ImageView insulinImage;
    private ImageView sugarBeforeImage;
    private ImageView sugarAfterImage;
    private ImageView faceImageView;

    private DialogsManager dialogsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getMeasurementFromPreferences();
        assignAndSetComponents();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getMeasurementFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(MeasurementFragment.preferencesString, MODE_PRIVATE);
        measurement = Utils.createMeasurementFromJson(preferences, MeasurementFragment.measurementString);
    }

    private void assignAndSetComponents() {
        dialogsManager = new DialogsManager();

        sugarLevelInputAfterMeal = findViewById(R.id.sugarLevelInputAfterMeal);

        summarizeButton = findViewById(R.id.summarizeButton);
        summarizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput();
            }
        });

        carbohydratesText = findViewById(R.id.carbohydratesTextSummary);
        carbohydratesText.setVisibility(View.INVISIBLE);

        insulinText = findViewById(R.id.insulinTextSummary);
        insulinText.setVisibility(View.INVISIBLE);

        sugarBeforeText = findViewById(R.id.sugarBeforeTextSummary);
        sugarBeforeText.setVisibility(View.INVISIBLE);

        sugarAfterText = findViewById(R.id.sugarAfterTextSummary);
        sugarAfterText.setVisibility(View.INVISIBLE);

        carbohydratesImage = findViewById(R.id.carbohydratesImageViewSummary);
        carbohydratesImage.setVisibility(View.INVISIBLE);

        insulinImage = findViewById(R.id.insulinImageViewSummary);
        insulinImage.setVisibility(View.INVISIBLE);

        sugarBeforeImage = findViewById(R.id.sugarBeforeImageViewSummary);
        sugarBeforeImage.setVisibility(View.INVISIBLE);

        sugarAfterImage = findViewById(R.id.sugarAfterImageViewSummary);
        sugarAfterImage.setVisibility(View.INVISIBLE);

        faceImageView = findViewById(R.id.faceImageView);
        faceImageView.setVisibility(View.INVISIBLE);
    }

    private void updateVisibilityOfComponents() {
        carbohydratesText.setVisibility(View.VISIBLE);
        insulinText.setVisibility(View.VISIBLE);
        sugarBeforeText.setVisibility(View.VISIBLE);
        sugarAfterText.setVisibility(View.VISIBLE);

        carbohydratesImage.setVisibility(View.VISIBLE);
        insulinImage.setVisibility(View.VISIBLE);
        sugarBeforeImage.setVisibility(View.VISIBLE);
        sugarAfterImage.setVisibility(View.VISIBLE);
        faceImageView.setVisibility(View.VISIBLE);

    }

    private boolean checkSugarLevelInput() {
        if (sugarLevelInputAfterMeal.getText().toString().equals("")) {
            return false;
        }

        if (Integer.valueOf(sugarLevelInputAfterMeal.getText().toString()) <= 0) {
            return false;
        }

        return true;
    }


    private void handleUserInput() {
        boolean inputIsValid = checkSugarLevelInput();

        if (inputIsValid) {
            resultMeasurement = new ResultMeasurement(
                    measurement,
                    Integer.valueOf(sugarLevelInputAfterMeal.getText().toString())
            );

            carbohydratesText.setText(determineCarbohydratesText(resultMeasurement));
            insulinText.setText(determineInulinText(resultMeasurement));
            sugarBeforeText.setText(determineSugarBeforeText(resultMeasurement));
            sugarAfterText.setText(determineSugarAfterText(resultMeasurement));

            if (sugarLevelStandard(false, resultMeasurement.getSugarLevelAfterMeal()) == 1) {
                showHappyFace();
            } else {
                showSadFace();
            }

            updateVisibilityOfComponents();

        } else {
            dialogsManager.showInvalidInputDialog(this);
        }
    }


    private void showHappyFace() {
        faceImageView.setImageResource(R.drawable.ic_happy_face);
        faceImageView.setVisibility(View.VISIBLE);

    }

    private void showSadFace() {
        faceImageView.setImageResource(R.drawable.ic_sad_face);
        faceImageView.setVisibility(View.VISIBLE);
    }


    private String determineCarbohydratesText(ResultMeasurement resultMeasurement) {
        int carbohydratesInGrams = resultMeasurement.getMeasurement().getCarbohydratesInGrams();
        return "Carbohydrates eaten: " + carbohydratesInGrams + " grams\n";
    }


    private String determineInulinText(ResultMeasurement resultMeasurement) {
        float insulinInUnits = resultMeasurement.getMeasurement().getInsulinInUnits();
        return "Insulin taken: " + insulinInUnits + " units\n";
    }


    private String determineSugarBeforeText(ResultMeasurement resultMeasurement) {
        int sugarLevelBeforeMeal = resultMeasurement.getMeasurement().getSugarLevelBeforeMeal();
        return "Sugar level before meal: " + sugarLevelBeforeMeal + " mg/dL\n";
    }


    private String determineSugarAfterText(ResultMeasurement resultMeasurement) {
        int sugarLevelAfterMeal = resultMeasurement.getSugarLevelAfterMeal();
        return "Sugar level after meal: " + sugarLevelAfterMeal + " mg/dL\n";
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
