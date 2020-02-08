package com.example.diabefriend.dialogs;

import android.content.Context;
import android.content.DialogInterface;

import com.example.diabefriend.R;
import com.example.diabefriend.model.Measurement;
import com.example.diabefriend.model.Utils;

import androidx.appcompat.app.AlertDialog;

public class DialogsManager {

    private void createDialogAndShow(AlertDialog.Builder builder) {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showInvalidInputDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.invalid_input_dialog).setMessage(R.string.try_again_dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        createDialogAndShow(builder);
    }

    public void showNoMeasurementFoundDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.no_measurement_found_dialog_title).setMessage(R.string.try_again_dialog);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        createDialogAndShow(builder);
    }


    public void showDosageInformationDialog(Measurement measurement, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        String dosageMessage;
        if (measurement == null) {
            dosageMessage = "Error! No information about the measurement has been found";
        } else {
            float insulinUnitsPerGrams = measurement.getInsulinInUnits() * 10 / measurement.getCarbohydratesInGrams();
            String insulinUnitsPerGramsString = Utils.decimalFormat.format(insulinUnitsPerGrams);

            dosageMessage = "You gave yourself "
                    + measurement.getCarbohydratesInGrams() + " grams of carbohydrates and "
                    + measurement.getInsulinInUnits() + " insulin units\n" +
                    "(" + insulinUnitsPerGramsString + " insulin units for every 10 grams of carbohydrates).\n" +
                    "Your blood sugar level before the meal was " + measurement.getSugarLevelBeforeMeal();
        }


        builder.setTitle(R.string.dosage_information_title).setMessage(dosageMessage);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        createDialogAndShow(builder);
    }

}
