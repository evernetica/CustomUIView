package app.developer.uiview.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.text.DecimalFormat;

import app.developer.uiview.R;
import app.developer.uiview.common.RoundedCornerLayout;


/**
 * The type Feedback temperature view.
 */
public class FeedbackTemperatureView extends LinearLayoutCompat {

    private AppCompatTextView headerTitleWidget, temperatureValue, temperatureFormat, minValue, maxValue;
    private AppCompatSeekBar seekBar;
    private RoundedCornerLayout temperatureSeekBarShadow, temperatureBackground;
    private String SPECIAL_CHAR = "º";
    private @Nullable
    ProgressChangeListener listener;

    /**
     * Instantiates a new Feedback temperature view.
     *
     * @param context the context
     */
    public FeedbackTemperatureView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Feedback temperature view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FeedbackTemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    /**
     * Instantiates a new Feedback temperature view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FeedbackTemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    private void init(@Nullable Context context, @Nullable AttributeSet set) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_feedback_temperature, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.FeedbackTemperatureView, 0, 0);
        Typeface typeface = typedArray.getFont(R.styleable.FeedbackTemperatureView_android_fontFamily);
        view.findViewById(R.id.containerView).setBackgroundColor(typedArray.getColor(R.styleable.FeedbackTemperatureView_widgetBackground, Color.parseColor("#1D1D1D")));

        headerTitleWidget = view.findViewById(R.id.headerTitleWidget);
        temperatureFormat = view.findViewById(R.id.temperatureFormat);
        temperatureValue = view.findViewById(R.id.temperatureValue);
        minValue = view.findViewById(R.id.minValue);
        maxValue = view.findViewById(R.id.maxValue);

        if (typeface != null) {
            temperatureValue.setTypeface(typeface);
            temperatureFormat.setTypeface(typeface);
            headerTitleWidget.setTypeface(typeface);
            minValue.setTypeface(typeface);
            maxValue.setTypeface(typeface);
        }

        seekBar = view.findViewById(R.id.temperatureSeekBar);
        seekBar.setMin(typedArray.getInt(R.styleable.FeedbackTemperatureView_minBar, 0));
        seekBar.setMax(typedArray.getInt(R.styleable.FeedbackTemperatureView_maxBar, 200));
        seekBar.setProgress(typedArray.getInt(R.styleable.FeedbackTemperatureView_progressBar, 0));
        minValue.setText(Integer.toString(typedArray.getInt(R.styleable.FeedbackTemperatureView_minBar, 0)));
        maxValue.setText(Integer.toString(typedArray.getInt(R.styleable.FeedbackTemperatureView_maxBar, 200)));

        seekBar.setThumb(ContextCompat.getDrawable(context, R.drawable.ic_seekbar_thumb));
        seekBar.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperatureValue.setText(new DecimalFormat("000").format(progress) + SPECIAL_CHAR);
                if (listener != null) {
                    listener.onProgressChanged(progress);
                }
            }
        });

        temperatureSeekBarShadow = view.findViewById(R.id.temperatureSeekBarShadow);
        temperatureSeekBarShadow.setBackground(
                new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{
                                Color.parseColor("#000000"),
                                Color.parseColor("#525252")
                        }));
        temperatureBackground = view.findViewById(R.id.temperatureBackground);
        temperatureBackground.setBackground(
                new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{
                                Color.parseColor("#2501FF"),
                                Color.parseColor("#78FF1C"),
                                Color.parseColor("#FFEB00"),
                                Color.parseColor("#FF0000")
                        }));

        if (typedArray.getString(R.styleable.FeedbackTemperatureView_titleText) != null) {
            headerTitleWidget.setText(typedArray.getString(R.styleable.FeedbackTemperatureView_titleText));
        }
        headerTitleWidget.setTextColor(typedArray.getColor(R.styleable.FeedbackTemperatureView_titleTextColor, Color.parseColor("#77BC1F")));

        temperatureValue.setText(new DecimalFormat("000").format(typedArray.getInt(R.styleable.FeedbackTemperatureView_progressBar, 0)) + SPECIAL_CHAR);

        if (typedArray.getString(R.styleable.FeedbackTemperatureView_temperatureFormat) != null)
            temperatureFormat.setText(typedArray.getString(R.styleable.FeedbackTemperatureView_temperatureFormat) + SPECIAL_CHAR);
        else
            temperatureFormat.setText("F" + SPECIAL_CHAR);
        typedArray.recycle();
    }

    /**
     * Sets header title.
     *
     * @param headerTitle the header title
     */
    public void setHeaderTitle(String headerTitle) {
        headerTitleWidget.setText(headerTitle);
    }

    /**
     * Gets header title.
     *
     * @return the header title
     */
    public String getHeaderTitle() {
        return headerTitleWidget.getText().toString();
    }

    /**
     * Sets temperature value.
     *
     * @param temperatureValue the temperature value
     */
    @SuppressLint("SetTextI18n")
    public void setTemperatureValue(String temperatureValue) {
        if (temperatureValue.contains(SPECIAL_CHAR))
            this.temperatureValue.setText(temperatureValue);
        else
            this.temperatureValue.setText(temperatureValue + SPECIAL_CHAR);
    }

    public String getTemperatureValue() {
        return this.temperatureValue.getText().toString();
    }

    /**
     * Sets temperature format.
     *
     * @param temperatureFormat the temperature format
     */
    @SuppressLint("SetTextI18n")
    public void setTemperatureFormat(String temperatureFormat) {
        if (temperatureFormat.contains(SPECIAL_CHAR))
            this.temperatureFormat.setText(temperatureFormat);
        else
            this.temperatureFormat.setText(temperatureFormat + SPECIAL_CHAR);
    }

    /**
     * Gets temperature format.
     *
     * @return the temperature format
     */
    public String getTemperatureFormat() {
        return this.temperatureFormat.getText().toString();
    }

    public void setFontface(@FontRes Integer font) {
        Typeface typeface = ResourcesCompat.getFont(getContext(), font);
        headerTitleWidget.setTypeface(typeface);
        temperatureValue.setTypeface(typeface);
        temperatureFormat.setTypeface(typeface);
    }

    /**
     * Sets progress shadow.
     *
     * @param topColor    the top color
     * @param bottomColor the bottom color
     */
    void setProgressShadow(int topColor, int bottomColor) {
        temperatureSeekBarShadow.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{
                        topColor,
                        bottomColor
                }));
    }

    /**
     * Sets progress shadow.
     *
     * @param shadow the shadow
     */
    void setProgressShadow(GradientDrawable shadow) {
        temperatureSeekBarShadow.setBackground(shadow);
    }

    /**
     * Sets progress backgroud.
     *
     * @param startColor       the start color
     * @param leftCenterColor  the left center color
     * @param rightCenterColor the right center color
     * @param endColor         the end color
     */
    void setProgressBackgroud(int startColor, int leftCenterColor, int rightCenterColor, int endColor) {
        temperatureBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{
                        startColor,
                        leftCenterColor,
                        rightCenterColor,
                        endColor
                }));
    }

    /**
     * Sets progress backgroud.
     *
     * @param colorArray the color array for temperature SeekBar
     */
    void setProgressBackgroud(int[] colorArray) {
        temperatureBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                colorArray));
    }

    /**
     * Sets progress backgroud.
     *
     * @param shadow the shadow
     */
    void setProgressBackgroud(GradientDrawable shadow) {
        temperatureBackground.setBackground(shadow);
    }

    /**
     * Sets thump.
     *
     * @param drawable the drawable
     */
    public void setThump(@DrawableRes int drawable) {
        seekBar.setThumb(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets range bar.
     *
     * @param minValue the min value
     * @param maxValue the max value
     */
    @SuppressLint("SetTextI18n")
    public void setRangeBar(int minValue, int maxValue) {
        seekBar.setMin(minValue);
        seekBar.setMax(maxValue);
        this.minValue.setText(Integer.toString(minValue));
        this.maxValue.setText(Integer.toString(maxValue));
    }

    /**
     * Sets default bar.
     *
     * @param defaultValue the default value
     */
    public void setDefaultBar(int defaultValue) {
        seekBar.setProgress(defaultValue);
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return seekBar.getProgress();
    }

    /**
     * Sets progress change listener.
     *
     * @param progressChangeListener the progress change listener
     */
    public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
        listener = progressChangeListener;
    }

    public interface ProgressChangeListener {
        void onProgressChanged(int progress);
    }

}
