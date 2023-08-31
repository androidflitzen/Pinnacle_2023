package com.flitzen.pinnacle.utils;

import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import com.flitzen.pinnacle.MyApplicationClass;
import com.flitzen.pinnacle.R;

public class GenericKeyEvent implements View.OnKeyListener{

    private EditText currentView;
    private EditText previousView;

    public GenericKeyEvent(EditText currentView, EditText previousView) {
        this.currentView = currentView;
        this.previousView = previousView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        /*if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getText().toString().isEmpty()) {
            if (previousView != null) {
                currentView.setBackground(null);
                previousView.requestFocus();
            }else {
                currentView.setBackground(null);
            }
            return true;
        }
        return false;*/

        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
            if (previousView != null) {
                currentView.setText("");
                currentView.setBackground(null);
                previousView.requestFocus();
            }else {
                currentView.setText("");
                currentView.setBackground(null);
            }
            return true;
        }
        return false;

    }

}
