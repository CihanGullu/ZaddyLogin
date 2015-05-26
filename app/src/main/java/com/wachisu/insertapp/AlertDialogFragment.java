package com.wachisu.insertapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Sochi on 2015/05/06.
 *
 *
 * Gebruik de GETTERS en SETTERS om de errorMessage title, message en knopText te setten ... en te getten xD
 */




public class AlertDialogFragment extends DialogFragment {

    private String _dialogTitle;
    private String _dialogMessage;
    private String _dialogButtonText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(getDialogTitle())
                .setMessage(getDialogMessage())
                .setPositiveButton(getDialogButtonText(), null);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    public void setDialogTitle(String title) {
        _dialogTitle = title;
    }

    public void setDialogMessage(String message) {
        _dialogMessage = message;
    }

    public void setDialogButtonText(String text) {
        _dialogButtonText = text;
    }

    public String getDialogTitle() {
        return _dialogTitle;
    }

    public String getDialogMessage() {
        return _dialogMessage;
    }

    public String getDialogButtonText() {
        return _dialogButtonText;
    }
}