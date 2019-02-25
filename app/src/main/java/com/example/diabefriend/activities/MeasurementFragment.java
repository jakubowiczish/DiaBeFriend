package com.example.diabefriend.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.diabefriend.R;
import com.example.diabefriend.dialogs.DialogsManager;
import com.example.diabefriend.model.Alarm;
import com.example.diabefriend.model.Measurement;
import com.example.diabefriend.model.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;


public class MeasurementFragment extends Fragment {

    private static final int START_ACTIVITY_REQUEST_CODE = 1;

    public MeasurementFragment() {
    }

    private static final int TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS = 10000; // 2 hours = 7200000 millis
    private static final String millisLeftString = "millisLeftString";
    private static final String endTimeString = "endTime";
    public static final String preferencesString = "preferences";
    private static final String progressBarStatusString = "progressBarStatus";
    private static final String timerIsRunningString = "timerIsRunning";
    public static final String measurementString = "measurement";
    private static final String countdownIsFinishedString = "countdownIsFinished";
    private DialogsManager dialogsManager;

    private Button mDosageInformationButton;
    private Measurement measurement;

    private TextView countDownTextView;
    private FloatingActionButton mStartButton;
    private FloatingActionButton mResetButton;

    private CountDownTimer mCountDownTimer;
    public static boolean mTimerIsRunning;
    private boolean mCountDownIsFinished;

    private MaterialProgressBar mProgressBar;
    private int mProgressBarStatus = 0;

    private long mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
    private long mEndTime;

    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_timer, container, false);

        v.findViewById(R.id.startActivityFloatingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), StartActivity.class), START_ACTIVITY_REQUEST_CODE);
            }
        });

        dialogsManager = new DialogsManager();

        mDosageInformationButton = v.findViewById(R.id.dosageInformationButton);
        mDosageInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogsManager.showDosageInformationDialog(measurement, getContext());
            }
        });

        countDownTextView = v.findViewById(R.id.countdownView);

        mStartButton = v.findViewById(R.id.startButtonInSummary);
        measurementChanged();
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                setAlarm();
            }
        });

        mResetButton = v.findViewById(R.id.resetButtonInSummary);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerResetButtonDialog();
            }
        });

        mProgressBar = v.findViewById(R.id.progressBar);

        if (!mTimerIsRunning) {
            mTimeLeftInMillis = TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS;
            resetProgressBar();
            updateCountDownTextView();
        }
        updateCountDownTextView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (START_ACTIVITY_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    measurement = data.getParcelableExtra(measurementString);
                    measurementChanged();
                }
                break;
            }
        }
    }

    private void measurementChanged() {
        mStartButton.setEnabled(measurement != null);
    }

    private void setAlarm() {
        Intent intent = new Intent(getContext(), Alarm.class);

        SharedPreferences preferences = getActivity().getSharedPreferences(preferencesString, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(measurementString, Utils.createMeasurementJsonString(measurement));
        editor.apply();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS, pendingIntent);
        }
    }

    private void cancelAlarm() {
        Intent intent = new Intent(getContext(), Alarm.class);
        intent.putExtra(measurementString, measurement);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences preferences = getActivity().getSharedPreferences(preferencesString, MODE_PRIVATE);

        mTimerIsRunning = preferences.getBoolean(timerIsRunningString, false);
        mTimeLeftInMillis = preferences.getLong(millisLeftString, TIME_TO_TEST_SUGAR_LEVEL_IN_MILLIS);
        mCountDownIsFinished = preferences.getBoolean(countdownIsFinishedString, false);

        updateCountDownTextView();

        if (mTimerIsRunning) {
            measurement = Utils.createMeasurementFromJson(preferences, measurementString);
            mEndTime = preferences.getLong(endTimeString, 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            mProgressBarStatus = countProgressBarStatus(mTimeLeftInMillis);
            mProgressBar = v.findViewById(R.id.progressBar);
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
    public void onStop() {
        super.onStop();

        SharedPreferences preferences = getActivity().getSharedPreferences(preferencesString, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(measurementString, Utils.createMeasurementJsonString(measurement));
        editor.putBoolean(timerIsRunningString, mTimerIsRunning);
        editor.putBoolean(countdownIsFinishedString, mCountDownIsFinished);
        editor.putLong(millisLeftString, mTimeLeftInMillis);
        editor.putLong(endTimeString, mEndTime);
        editor.putInt(progressBarStatusString, mProgressBarStatus);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
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
                mCountDownIsFinished = true;
            }
        }.start();

        mTimerIsRunning = true;
        mCountDownIsFinished = false;
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


    private void timerResetButtonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
