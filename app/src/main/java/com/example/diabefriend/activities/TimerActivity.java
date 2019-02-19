package com.example.diabefriend.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabefriend.model.Alarm;
import com.example.diabefriend.model.Measurement;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import com.example.diabefriend.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static final int TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS = 20000; // 2 hours = 7200000 millis
    private static final String millisLeftString = "millisLeftString";
    private static final String endTimeString = "endTime";
    private static final String preferencesString = "preferences";
    private static final String progressBarStatusString = "progressBarStatus";
    private static final String timerIsRunningString = "timerIsRunning";
    private static final String measurementString = "measurement";

    private Button mDosageInformationButton;
    private Measurement measurement;

    private TextView countDownTextView;
    private FloatingActionButton mStartButton;
    private FloatingActionButton mResetButton;

    private CountDownTimer mCountDownTimer;
    public static boolean mTimerIsRunning;

    private MaterialProgressBar mProgressBar;
    private int mProgressBarStatus = 0;

    private long mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
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
                setAlarm();
            }
        });

        mResetButton = findViewById(R.id.resetButtonInSummary);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerResetButtonDialog();
            }
        });

        mProgressBar = findViewById(R.id.progressBar);

        if (!mTimerIsRunning) {
            mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
            resetProgressBar();
            updateCountDownTextView();
        }
        updateCountDownTextView();

    }

    private void setAlarm() {
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS, pendingIntent);

    }

    private void cancelAlarm() {
        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(preferencesString, MODE_PRIVATE);

        mTimerIsRunning = preferences.getBoolean(timerIsRunningString, false);
        mTimeLeftInMillis = preferences.getLong(millisLeftString, TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS);

        updateCountDownTextView();

        if (mTimerIsRunning) {
            measurement = createMeasurementFromJson(preferences);
            mEndTime = preferences.getLong(endTimeString, 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            mProgressBarStatus = countProgressBarStatus(mTimeLeftInMillis);
            mProgressBar = findViewById(R.id.progressBar);
            mProgressBar.setProgress(mProgressBarStatus);

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerIsRunning = false;
                updateCountDownTextView();
            } else {
                startTimer();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences preferences = getSharedPreferences(preferencesString, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(measurementString, createMeasurementJsonString());
        editor.putBoolean(timerIsRunningString, mTimerIsRunning);
        editor.putLong(millisLeftString, mTimeLeftInMillis);
        editor.putLong(endTimeString, mEndTime);
        editor.putInt(progressBarStatusString, mProgressBarStatus);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }

    private String createMeasurementJsonString() {
        return new Gson().toJson(measurement);
    }

    private Measurement createMeasurementFromJson(SharedPreferences preferences) {
        String measurementJson = preferences.getString(measurementString, "");
        return new Gson().fromJson(measurementJson, Measurement.class);
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateProgressBar();
                updateCountDownTextView();
            }

            @Override
            public void onFinish() {
                mStartButton.setVisibility(View.VISIBLE);
                resetProgressBar();
                resetTimer();
            }
        }.start();

        mTimerIsRunning = true;
        mStartButton.setVisibility(View.INVISIBLE);
    }


    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimerIsRunning = false;
        mStartButton.setVisibility(View.VISIBLE);
        mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
        updateCountDownTextView();
        resetProgressBar();
    }

    private int countProgressBarStatus(long timeLeftInMillis) {
        return (int) (((TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS - timeLeftInMillis) / 1000));
    }

    private void updateProgressBar() {
        mProgressBarStatus++;
        mProgressBar.setProgress(mProgressBarStatus * 100 / (TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS / 1000));
    }

    private void resetProgressBar() {
        mProgressBarStatus = 0;
        mProgressBar.setProgress(0);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);

        String dosageMessage;
        if (measurement == null) {
            dosageMessage = "Error! No information about the measurement has been found";
        } else {
            float insulinUnitsPerGrams = measurement.getInsulinInUnits() * 10 / measurement.getCarbohydratesInGrams();
            String insulinUnitsPerGramsString = decimalFormat.format(insulinUnitsPerGrams);

            dosageMessage = "You gave yourself " + measurement.getCarbohydratesInGrams() +
                    " grams of carbohydrates and " + measurement.getInsulinInUnits() + " insulin units" +
                    " (" + insulinUnitsPerGramsString + " insulin units for every 10 grams of carbohydrates)";

        }

        builder.setTitle(R.string.dosage_information_title).setMessage(dosageMessage);

        AlertDialog invalidInputDialog = builder.create();
        invalidInputDialog.show();
    }

    private void timerResetButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure? Measurement data will be lost");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                resetTimer();
                cancelAlarm();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
