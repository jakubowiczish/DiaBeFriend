package com.example.diabefriend.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabefriend.model.Measurement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import com.example.diabefriend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final int TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS = 5000; // 2 hours = 7200000 millis
    private static final String millisLeftString = "millisLeftString";
    private static final String endTimeString = "endTime";


    private Button mDosageInformationButton;
    private Measurement measurement;

    private TextView countDownTextView;
    private FloatingActionButton mStartButton;
    private FloatingActionButton mResetButton;

    private CountDownTimer mCountDownTimer;
//    private boolean mTimerIsRunning;

    private MaterialProgressBar materialProgressBar;

    private long mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        measurement = getIntent().getParcelableExtra("measurement");

        mDosageInformationButton = findViewById(R.id.dosageInformationButton);
        mDosageInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDosageInformationDialog(measurement);
            }
        });

        countDownTextView = findViewById(R.id.countdownView);

        mStartButton = findViewById(R.id.startButtonInSummary);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        mResetButton = findViewById(R.id.resetButtonInSummary);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        materialProgressBar = findViewById(R.id.progressBar);

        updateCountDownTextView();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(millisLeftString, mTimeLeftInMillis);
        outState.putLong(endTimeString, mEndTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mTimeLeftInMillis = savedInstanceState.getLong(millisLeftString);
        updateCountDownTextView();

            mEndTime = savedInstanceState.getLong(endTimeString);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
    }

    private int mProgressBarStatus = 0;

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownTextView();
                mProgressBarStatus++;
                materialProgressBar.setProgress(mProgressBarStatus * 100 / (TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS / 1000));
            }

            @Override
            public void onFinish() {
                mStartButton.setVisibility(View.VISIBLE);
                resetProgressBar();
                resetTimer();

            }
        }.start();

        mStartButton.setVisibility(View.INVISIBLE);
    }


    private void resetTimer() {
        mCountDownTimer.cancel();
        mStartButton.setVisibility(View.VISIBLE);
        mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
        updateCountDownTextView();
        resetProgressBar();
    }

    private void resetProgressBar() {
        mProgressBarStatus = 0;
        materialProgressBar.setProgress(0);
    }


    private void updateCountDownTextView() {
        int hours = (int) mTimeLeftInMillis / 1000 / 60 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;

        int minutes;
        if (hours > 0) {
            minutes = (int) mTimeLeftInMillis / 1000 / 60 - hours * 60;
        } else {
            minutes = (int) mTimeLeftInMillis / 1000 / 60;
        }

        String timeLeftFormattedForTextView = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        countDownTextView.setText(timeLeftFormattedForTextView);
    }


    private void showDosageInformationDialog(Measurement measurement) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TimerActivity.this, R.style.AlertDialogCustom);

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
