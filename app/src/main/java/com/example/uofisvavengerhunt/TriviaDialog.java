package com.example.uofisvavengerhunt;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

public class TriviaDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Trivia!")
                .setMessage(MapsActivity.getTriviaMessage())
                .setPositiveButton("True!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (MapsActivity.correctAnswer()) {
                            MapsActivity.upScore();
                        }
                    }
                })
                .setNegativeButton("False!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!MapsActivity.correctAnswer()) {
                            MapsActivity.upScore();
                        }
                    }
                });
        return builder.create();
    }
}
