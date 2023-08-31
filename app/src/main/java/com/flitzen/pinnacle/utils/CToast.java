package com.flitzen.pinnacle.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.flitzen.pinnacle.R;

public class CToast {

    Context context;
    Toast toast;
    LayoutInflater inflater;
    View toastRoot;
    CardView toastBg;
    ImageView img;
    TextView txtMessage;

    public CToast(Context con) {
        this.context = con;
    }

    public CToast simpleToastSuccess(String message, int length) {
        try {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.simple_toast_success, null);

            txtMessage =  toastRoot.findViewById(R.id.txt_simple_toast_message);
            toastBg =  toastRoot.findViewById(R.id.view_toast_bg);
            txtMessage.setText(message);

            toast = new Toast(context);
            toast.setView(toastRoot);
            toast.setDuration(length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public CToast simpleToastFail(String message, int length) {
        try {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.simple_toast_fail, null);

            txtMessage =  toastRoot.findViewById(R.id.txt_simple_toast_message);
            toastBg =  toastRoot.findViewById(R.id.view_toast_bg);
            txtMessage.setText(message);

            toast = new Toast(context);
            toast.setView(toastRoot);
            toast.setDuration(length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public CToast simpleToastGeneral(String message, int length) {
        try {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastRoot = inflater.inflate(R.layout.simple_toast_general, null);

            txtMessage =  toastRoot.findViewById(R.id.txt_simple_toast_message);
            toastBg =  toastRoot.findViewById(R.id.view_toast_bg);
            txtMessage.setText(message);

            toast = new Toast(context);
            toast.setView(toastRoot);
            toast.setDuration(length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public CToast setBackgroundColor(int color) {
        if (toastBg!=null){
            toastBg.setCardBackgroundColor(context.getResources().getColor(color));
        }
        return this;
    }

    public CToast show() {
        if (toast!=null){
            toast.show();
        }
        return this;
    }

}
