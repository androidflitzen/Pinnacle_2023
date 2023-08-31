package com.flitzen.pinnacle.utils;

import android.text.InputFilter;
import android.text.Spanned;

import static com.flitzen.pinnacle.MyApplicationClass.context;

public class InputFilterMinMax implements InputFilter {

    private Float min, max;

    public InputFilterMinMax(Float min, Float max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Float.parseFloat(min);
        this.max = Float.parseFloat(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            Float input = Float.parseFloat(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) {
        }
        return "";
    }

    private boolean isInRange(Float a, Float b, Float c) {
        boolean ans = b > a ? c >= a && c <= b : c >= b && c <= a;
        if(ans==false){
            AppUtil.showToastFail(context, "You can not select grater than Stock quantity");
        }else {

        }
        return ans;
    }
}
