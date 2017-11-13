package com.pes.androidmaterialcolorpickerdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import static com.pes.androidmaterialcolorpickerdialog.ColorFormatHelper.assertColorValueInRange;
import static com.pes.androidmaterialcolorpickerdialog.ColorFormatHelper.formatColorValues;

/**
 * This is the only class of the project. It consists in a custom dialog that shows the GUI
 * used for choosing a color using three sliders or an input field.
 *
 * @author Simone Pessotto
 */
public class ColorPicker extends Dialog implements SeekBar.OnSeekBarChangeListener {

    private final Activity activity;

    private View colorView;
    private SeekBar alphaSeekBar, redSeekBar, greenSeekBar, blueSeekBar;
    private TextView hexCode;
    private int alpha, red, green, blue;
    private ColorPickerCallback callback;
    private LinearLayout mLLAlpha;
    private boolean withAlpha;


    public ColorPicker(Activity activity) {
        super(activity);

        this.activity = activity;

        if (activity instanceof ColorPickerCallback) {
            callback = (ColorPickerCallback) activity;
        }

        this.alpha = 255;
        this.red = 0;
        this.green = 0;
        this.blue = 0;

        this.withAlpha = false;
    }

    public ColorPicker(Activity activity,
                       @IntRange(from = 0, to = 255) int red,
                       @IntRange(from = 0, to = 255) int green,
                       @IntRange(from = 0, to = 255) int blue) {
        this(activity);

        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);

    }

    public ColorPicker(Activity activity,
                       @IntRange(from = 0, to = 255) int alpha,
                       @IntRange(from = 0, to = 255) int red,
                       @IntRange(from = 0, to = 255) int green,
                       @IntRange(from = 0, to = 255) int blue) {
        this(activity);

        this.alpha = assertColorValueInRange(alpha);
        this.red = assertColorValueInRange(red);
        this.green = assertColorValueInRange(green);
        this.blue = assertColorValueInRange(blue);

        this.withAlpha = true;

    }

    public void setCallback(ColorPickerCallback listener) {
        callback = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        //预览色
        setContentView(R.layout.materialcolorpicker__layout_color_picker);

        colorView = findViewById(R.id.colorView);
        colorView.setBackgroundColor(getColor());

        hexCode = (TextView) findViewById(R.id.hexCode);

        alphaSeekBar = (SeekBar) findViewById(R.id.alphaSeekBar);
        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);

        alphaSeekBar.setOnSeekBarChangeListener(this);
        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);

        alphaSeekBar.setProgress(alpha);
        redSeekBar.setProgress(red);
        greenSeekBar.setProgress(green);
        blueSeekBar.setProgress(blue);

        mLLAlpha= (LinearLayout) findViewById(R.id.ll_alpha);
        //是否需要透明度
        if (!withAlpha) {
            mLLAlpha.setVisibility(View.GONE);
        }else {
            mLLAlpha.setVisibility(View.VISIBLE);
        }

        hexCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(withAlpha ? 8 : 6)});

        hexCode.setText(withAlpha
                ? formatColorValues(alpha, red, green, blue)
                : formatColorValues(red, green, blue)
        );

        //点击选择
        findViewById(R.id.okColorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendColor();
                dismiss();
            }
        });

        //点击取消
        findViewById(R.id.cancelColorButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



    }

    private void sendColor() {
        if (callback != null)
            callback.onColorChosen(getColor());
            callback.onColorChosen(hexCode.getText().toString());
    }

    private void updateColorView(String input) {
        try {
            final int color = Color.parseColor('#' + input);
            alpha = Color.alpha(color);
            red = Color.red(color);
            green = Color.green(color);
            blue = Color.blue(color);

            colorView.setBackgroundColor(getColor());

            alphaSeekBar.setProgress(alpha);
            redSeekBar.setProgress(red);
            greenSeekBar.setProgress(green);
            blueSeekBar.setProgress(blue);
        } catch (IllegalArgumentException ignored) {
            hexCode.setError(activity.getResources().getText(R.string.materialcolorpicker__errHex));
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.alphaSeekBar) {

            alpha = progress;

        } else if (seekBar.getId() == R.id.redSeekBar) {

            red = progress;

        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;

        }

        colorView.setBackgroundColor(getColor());

        //Setting the inputText hex color
        hexCode.setText(withAlpha
                ? formatColorValues(alpha, red, green, blue)
                : formatColorValues(red, green, blue)
        );

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public int getAlpha() {
        return alpha;
    }


    public int getRed() {
        return red;
    }


    public int getGreen() {
        return green;
    }


    public int getBlue() {
        return blue;
    }

    public int getColor() {
        return withAlpha ? Color.argb(alpha, red, green, blue) : Color.rgb(red, green, blue);
    }
}