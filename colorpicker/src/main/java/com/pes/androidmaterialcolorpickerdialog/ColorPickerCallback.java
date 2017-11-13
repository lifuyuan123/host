package com.pes.androidmaterialcolorpickerdialog;

import android.support.annotation.ColorInt;

public interface ColorPickerCallback {
    void onColorChosen(String color);
    void onColorChosen(@ColorInt int color);
}

