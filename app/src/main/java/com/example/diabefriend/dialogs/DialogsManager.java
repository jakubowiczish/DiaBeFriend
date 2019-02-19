package com.example.diabefriend.dialogs;

import android.app.AlertDialog;

public class DialogsManager {

    private void createDialogAndShow(AlertDialog.Builder builder) {
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
