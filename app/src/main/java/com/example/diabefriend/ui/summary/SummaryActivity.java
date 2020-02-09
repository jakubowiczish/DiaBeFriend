package com.example.diabefriend.ui.summary;


import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diabefriend.R;
import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.measurement.Measurement;
import com.example.diabefriend.model.measurement.ResultMeasurement;
import com.example.diabefriend.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SummaryActivity extends AppCompatActivity {

    private Measurement measurement;
    private EditText sugarLevelInputAfterMeal;

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
    private MediaPlayer mediaPlayer;
    private FloatingActionButton pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startAlarmSound();

        getMeasurementFromPreferences();
        assignAndSetComponents();

        setStopAlarmSoundButton();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startAlarmSound() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer = MediaPlayer.create(this, notification);
        mediaPlayer.start();
    }

    private void setStopAlarmSoundButton() {
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mediaPlayer.stop();
            }
        });
    }

    private void getMeasurementFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(
                getResources().getString(R.string.preferences_string),
                MODE_PRIVATE
        );
        measurement = Utils.createMeasurementFromJson(
                preferences,
                getResources().getString(R.string.measurement_string)
        );
    }

    private void assignAndSetComponents() {
        dialogsManager = new DialogsManager();

        sugarLevelInputAfterMeal = findViewById(R.id.sugarLevelInputAfterMeal);

        Button summarizeButton = findViewById(R.id.summarizeButton);
        summarizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUserInput();
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    Log.wtf("Keyboard hide", "Problem with hiding keyboard");
                    e.printStackTrace();
                }
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

        pauseButton = findViewById(R.id.pauseButton);
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

        return Integer.valueOf(sugarLevelInputAfterMeal.getText().toString()) > 0;
    }


    private void handleUserInput() {
        boolean inputIsValid = checkSugarLevelInput();

        if (inputIsValid) {
            ResultMeasurement resultMeasurement = new ResultMeasurement(
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
