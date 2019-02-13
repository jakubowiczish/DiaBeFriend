package com.example.diabefriend.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diabefriend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.Locale;

public class SummaryActivity extends AppCompatActivity {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final long TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS = 2000; // 2 hours = 7200000 millis

    private Button dosageInformationButton;
    private Measurement measurement;

    private TextView countDownTextView;
    private FloatingActionButton startButton;
    private FloatingActionButton resetButton;

    private CountDownTimer countDownTimer;

    private long timeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        measurement = getIntent().getParcelableExtra("measurement");

        dosageInformationButton = findViewById(R.id.dosageInformationButton);
        dosageInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dosageInformationDialog(measurement);
            }
        });

        countDownTextView = findViewById(R.id.countdownView);

        startButton = findViewById(R.id.startButtonInSummary);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        resetButton = findViewById(R.id.resetButtonInSummary);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountdownTextView();
            }

            @Override
            public void onFinish() {
                startButton.setVisibility(View.VISIBLE);
            }
        }.start();

        startButton.setVisibility(View.INVISIBLE);

    }

    private void resetTimer() {
        countDownTimer.cancel();
        startButton.setVisibility(View.VISIBLE);
        timeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
        updateCountdownTextView();
    }

    private void updateCountdownTextView() {
        int hours = (int) timeLeftInMillis / 1000 / 60 / 60;
        int seconds = (int) timeLeftInMillis / 1000 % 60;

        int minutes;
        if (hours > 0) {
            minutes = (int) timeLeftInMillis / 1000 / 60 - hours * 60;
        } else {
            minutes = (int) timeLeftInMillis / 1000 / 60;
        }

        String timeLeftFormattedForTextView = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        countDownTextView.setText(timeLeftFormattedForTextView);
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
