package com.example.treasurehunt;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.contentcapture.ContentCaptureSession;

public class ProgressService {
    public static ProgressDialog showProgress (Context context, String text) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(true);
        dialog.setMessage(text);
        dialog.show();
        return dialog;
    }
}
