package com.example.diabefriend.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.diabefriend.R;
import com.example.diabefriend.activities.SummaryActivity;

public class Alarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, SummaryActivity.class);
        intent1.putExtra(
                context.getResources().getString(R.string.measurement_string),
                intent.getParcelableExtra(context.getResources().getString(R.string.measurement_string))
        );
        context.startActivity(intent1);

        Toast.makeText(context, "IT IS TIME TO TEST YOUR SUGAR LEVEL!", Toast.LENGTH_LONG).show();
    }
}
