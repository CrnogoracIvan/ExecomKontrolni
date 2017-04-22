package com.example.ivancrnogorac.execomkontrolni.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import com.example.ivancrnogorac.execomkontrolni.R;


public class AboutDialog extends AlertDialog.Builder{

    public AboutDialog(Context context) {
        super(context);

        setTitle("About aplication");
        setMessage("Application creator: Ivan Crnogorac, email: ivan.crnogorac@yahoo.com, contact phone: 060-363-7673");
        setCancelable(false);

        setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
    }

    public AlertDialog prepareDialog(){
        AlertDialog dialog = create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}