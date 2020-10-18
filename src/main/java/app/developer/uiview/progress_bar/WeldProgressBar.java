package app.developer.uiview.progress_bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import app.developer.uiview.R;

/**
 * The type Weld progress bar.
 */
public class WeldProgressBar extends LinearLayoutCompat {

    @Nullable
    private OnChangeProgressListener listener;

    CircleProgressBar innerProgress, externalProgress, solidProgress, lineProgress, axisSignatures, progressActiveBackgroundDrawable, progressInactiveBackgroundDrawable, axisActiveDrawable, axisInactiveDrawable;
    AppCompatTextView value, titleProgress, extraValue, extraLabel;
    CircularSeekBar innerSeekBar, externalSeekBar;
    RoundBackground shadow;
    ProgressBar progressBar;
    LinearLayoutCompat extraDataContainer;
    TypeProgress type;
    AttributeSet attributeSet;

    Integer innerMax, innerDefault, externalMax, externalDefault, anomaly;

    private int lastExternalProgress = 1;

    /**
     * Instantiates a new Weld progress bar.
     *
     * @param context the context
     */
    public WeldProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Weld progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public WeldProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Weld progress bar.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public WeldProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    // region Init
    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        attributeSet = attrs;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initViews(inflater.inflate(R.layout.view_weld_progress, this, true));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeldProgressBar, 0, 0);

        innerMax = typedArray.getInt(R.styleable.WeldProgressBar_innerProgressMax, 360);
        innerDefault = typedArray.getInt(R.styleable.WeldProgressBar_innerProgressDefault, 0);
        externalMax = typedArray.getInt(R.styleable.WeldProgressBar_externalProgressMax, 8);
        externalDefault = typedArray.getInt(R.styleable.WeldProgressBar_externalProgressDefault, 0);

        anomaly = typedArray.getInt(R.styleable.WeldProgressBar_anomaly, 0);
        innerProgress.setMax(innerMax);
        externalProgress.setLineCount(externalMax);
        externalProgress.setMax(externalMax);
        axisSignatures.setLineCount(externalMax);
        axisSignatures.setMax(externalMax);

        shadow.setColors(
                typedArray.getColor(R.styleable.WeldProgressBar_colorStartShadow, Color.parseColor("#327527")),
                typedArray.getColor(R.styleable.WeldProgressBar_colorEndShadow, Color.TRANSPARENT)
        );

        extraDataContainer.setVisibility(typedArray.getBoolean(R.styleable.WeldProgressBar_extraDataEnabled, false) ? View.VISIBLE : View.GONE);
        if (typedArray.getString(R.styleable.WeldProgressBar_extraDataValue) != null) {
            extraValue.setText(typedArray.getString(R.styleable.WeldProgressBar_extraDataValue));
        } else {
            extraValue.setText("");
        }

        if (typedArray.getString(R.styleable.WeldProgressBar_extraDataLabel) != null) {
            extraLabel.setText(typedArray.getString(R.styleable.WeldProgressBar_extraDataLabel));
        } else {
            extraLabel.setText("");
        }

        extraValue.setTextSize(typedArray.getDimensionPixelSize(R.styleable.WeldProgressBar_extraDataValueTextSize, 24));
        extraLabel.setTextSize(typedArray.getDimensionPixelSize(R.styleable.WeldProgressBar_extraDataLabelTextSize, 20));

        extraValue.setTextColor(typedArray.getColor(R.styleable.WeldProgressBar_extraColor, Color.WHITE));
        extraLabel.setTextColor(typedArray.getColor(R.styleable.WeldProgressBar_extraColor, Color.WHITE));

        if (typedArray.getFont(R.styleable.WeldProgressBar_extraFontFamily) != null) {
            extraValue.setTypeface(typedArray.getFont(R.styleable.WeldProgressBar_extraFontFamily));
            extraLabel.setTypeface(typedArray.getFont(R.styleable.WeldProgressBar_extraFontFamily));
        }

        if (typedArray.getBoolean(R.styleable.WeldProgressBar_axis_signatures, false)) {
            axisSignatures.setRotation(externalProgress.getRotation() + ((360 / externalMax) / 2));
            axisSignatures.setVisibility(View.VISIBLE);
            axisSignatures.setStyleAxis(
                    typedArray.getDimensionPixelSize(R.styleable.WeldProgressBar_axis_textSize, 30),
                    typedArray.getFont(R.styleable.WeldProgressBar_axis_fontface),
                    typedArray.getColor(R.styleable.WeldProgressBar_axis_textColor, Color.WHITE)
            );
        } else {
            axisSignatures.setVisibility(View.GONE);
        }

        type = TypeProgress.values()[typedArray.getInt(R.styleable.WeldProgressBar_typeProgress, 0)];
        shadow.setColorBackground(typedArray.getColor(R.styleable.WeldProgressBar_colorBackground, getResources().getColor(R.color.colorBlack, null)));

        initProgressBars(typedArray);

        typedArray.recycle();
    }

    private void initSingleInnerProgress(int innerColor, int lineColor) {
        innerProgress.setMax(innerMax);
        innerProgress.setProgressStartColor(innerColor);
        if (0 <= innerDefault && innerDefault <= innerMax) {
            titleProgress.setText("Level " + (innerDefault / (innerMax / externalMax) + 1));
            innerProgress.setProgress(innerDefault);
            value.setText(String.valueOf(innerDefault));
        } else {
            if (innerDefault > 0) {
                innerProgress.setProgress(innerMax);
                value.setText(String.valueOf(innerMax));
            } else {
                innerProgress.setProgress(0);
                value.setText(String.valueOf(0));
            }
        }
    }

    public void setTypeProgress(TypeProgress type) {
        this.type = type;
        initProgressBars(getContext().obtainStyledAttributes(attributeSet, R.styleable.WeldProgressBar, 0, 0));
    }

    private void setSingleExternalProgressWithAnomaly(int progress) {
        lineProgress.setProgress(progress);
        solidProgress.setProgress(progress / (innerMax / externalMax) + 1);
        value.setText(String.valueOf(progress));
        if (progress < innerMax) {
            titleProgress.setText("Level " + (progress / (innerMax / externalMax) + 1));
            progressBar.setProgress(progress % (innerMax / externalMax));
        }
        if (lastExternalProgress < progress / (innerMax / externalMax) + 1) {
            if (listener != null) listener.changeExternalProgress(progress);
        }
        if (listener != null) listener.changeExternalProgress(progress);
    }

    private void initSingleExternalProgressWithAnomaly(int externalColor, int solidColor) {
        externalProgress.setMax(externalMax);
        solidProgress.setMax(externalMax);
        externalProgress.setProgressStartColor(externalColor);
        solidProgress.setProgressEndColor(Color.parseColor("#FF0F170C"));
        solidProgress.setProgressStartColor(solidColor);
        externalSeekBar.setMax(externalMax);
        lineProgress.setMax(innerMax);
        lineProgress.setProgress(innerDefault);
        lineProgress.setProgressStartColor(externalColor);
        externalSeekBar.setMax(innerMax);
        externalSeekBar.setProgress(innerDefault);
        if (externalDefault == 0) {
            externalDefault = innerDefault / (innerMax / externalMax) + 1;
        }
        titleProgress.setText("Level " + externalDefault);
        progressBar.setMax(innerMax / externalMax);
        progressBar.setProgress(innerDefault % (innerMax / externalMax));
        if (0 <= externalDefault && externalDefault <= externalMax) {
            externalProgress.setProgress(externalDefault);
            solidProgress.setProgress(externalDefault);
        } else {
            if (externalDefault > 0) {
                externalProgress.setProgress(externalMax);
                solidProgress.setProgress(externalMax);
            } else {
                externalProgress.setProgress(0);
                solidProgress.setProgress(0);
            }
        }
    }

    private void setSingleExternalProgress(int progress) {
        lineProgress.setProgress(progress);
        value.setText(String.valueOf(progress));
        solidProgress.setProgress(progress);
        if (externalMax >= (progress / (innerMax / externalMax) + 1)) {
            titleProgress.setText("Level " + (progress / (innerMax / externalMax) + 1));
        }
    }

    public void setProgress(
            @Nullable Integer innerDefault,
            @Nullable Integer externalDefault
    ) {
        if (innerDefault != null)
            this.innerDefault = innerDefault;
        if (externalDefault != null)
            this.externalDefault = externalDefault;
        else {
            if (innerDefault != null)
                this.externalDefault = innerDefault / (innerMax / externalMax) + 1;
            else
                this.externalDefault = this.innerDefault / (innerMax / externalMax) + 1;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.WeldProgressBar, 0, 0);
        initProgressBars(typedArray);
        typedArray.recycle();
    }

    private void initProgressBars(TypedArray typedArray) {
        switch (type) {
            case defaultProgress:
                externalSeekBar.setVisibility(View.GONE);
                innerSeekBar.setVisibility(View.GONE);
                initInnerProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                initExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));
                break;
            case innerProgress:
                innerSeekBar.setProgress(this.innerDefault);
                innerSeekBar.setMax(this.innerMax);
                innerSeekBar.setVisibility(View.VISIBLE);
                externalSeekBar.setVisibility(View.GONE);
                initInnerProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                initExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));
                innerSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setInnerProgress((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                break;
            case externalProgress:
                externalSeekBar.setMax(this.externalMax);
                externalSeekBar.setProgress(this.externalDefault);
                externalSeekBar.setVisibility(View.VISIBLE);
                innerSeekBar.setVisibility(View.GONE);
                externalSeekBar.setProgress(typedArray.getInt(R.styleable.WeldProgressBar_innerProgressDefault, 0) / (innerMax / externalMax));
                initInnerProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                initExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                externalSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setExternalProgress((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                break;
            case singleExternalProgressDefault:
                externalSeekBar.setMax(this.innerMax);
                externalSeekBar.setProgress(this.innerDefault);
                externalSeekBar.setVisibility(View.GONE);
                innerSeekBar.setVisibility(View.GONE);

                value.setText(String.valueOf(this.innerDefault));
                initSingleExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                externalSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setSingleExternalProgress((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                break;
            case singleInnerProgressDefault:
                innerSeekBar.setProgress(this.innerDefault);
                innerSeekBar.setMax(this.innerMax);
                innerSeekBar.setVisibility(View.GONE);
                externalSeekBar.setVisibility(View.GONE);
                initSingleInnerProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.default_outer_bar_color, null)));
                break;
            case singleExternalProgress:
                externalSeekBar.setMax(this.innerMax);
                externalSeekBar.setProgress(this.innerDefault);
                externalSeekBar.setVisibility(View.VISIBLE);
                innerSeekBar.setVisibility(View.GONE);
                value.setText(String.valueOf(this.innerDefault));
                initSingleExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                externalSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setSingleExternalProgress((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                break;
            case singleExternalProgressWithAnomaly:
                externalSeekBar.setMax(this.innerMax);
                externalSeekBar.setProgress(this.innerDefault);
                externalSeekBar.setVisibility(View.VISIBLE);
                innerSeekBar.setVisibility(View.GONE);
                innerProgress.setStyle(CircleProgressBar.SINGLE_LINE);
                innerProgress.setMax(this.innerMax);
                innerProgress.setProgress(this.anomaly);
                value.setText(String.valueOf(this.innerDefault));
                initSingleExternalProgressWithAnomaly(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                externalSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setSingleExternalProgressWithAnomaly((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {

                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                break;
            case trackingIndicators:
                externalDefault = 0;
                innerDefault = 0;
                externalSeekBar.setVisibility(View.GONE);
                innerSeekBar.setVisibility(View.GONE);
                initInnerProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.default_outer_bar_color, null)));

                initExternalProgress(
                        typedArray.getColor(R.styleable.WeldProgressBar_colorExternalProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                        typedArray.getColor(R.styleable.WeldProgressBar_colorSolidProgress, getResources().getColor(R.color.default_outer_bar_color, null)));
                break;
            case upslope:
                externalSeekBar.setMax(this.innerMax);
                externalSeekBar.setProgress(0);
                externalSeekBar.setVisibility(View.VISIBLE);
                innerSeekBar.setVisibility(View.GONE);
                value.setText(String.valueOf(this.innerDefault));
                initSingleExternalProgress(
                        getContext().getColor(R.color.colorBlack),
                        getContext().getColor(R.color.colorDarkGrey));
                setSingleExternalProgress(0);

                externalSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                        setSingleExternalProgress((int) progress);
                    }

                    @Override
                    public void onStopTrackingTouch(CircularSeekBar seekBar) {
                        innerDefault = (int) seekBar.getProgress();
                        innerProgress.setStartDegree(innerDefault);
                        initInnerProgress(
                                typedArray.getColor(R.styleable.WeldProgressBar_colorInnerProgress, getResources().getColor(R.color.default_outer_bar_color, null)),
                                typedArray.getColor(R.styleable.WeldProgressBar_colorLineProgress, getResources().getColor(R.color.colorBlack, null)));
                    }

                    @Override
                    public void onStartTrackingTouch(CircularSeekBar seekBar) {

                    }
                });
                titleProgress.setText("Upslope");
                break;
        };
    }

    private void initSingleExternalProgress(@ColorInt int externalColor, @ColorInt int solidColor) {
        externalProgress.setMax(externalMax);
        solidProgress.setMax(innerMax);
        solidProgress.setProgress(innerDefault);
        externalProgress.setProgressStartColor(externalColor);
        solidProgress.setProgressEndColor(Color.parseColor("#FF0F170C"));
        solidProgress.setProgressStartColor(solidColor);
        lineProgress.setProgressStartColor(externalColor);
        lineProgress.setProgress(innerDefault);
        lineProgress.setMax(innerMax);
        if (externalDefault == 0) {
            externalDefault = innerDefault / (innerMax / externalMax) + 1;
        }
        titleProgress.setText("Level " + externalDefault);
        progressBar.setMax(innerMax / externalMax);
        progressBar.setProgress(innerDefault % (innerMax / externalMax));
        if (0 <= externalDefault && externalDefault <= externalMax) {
            externalProgress.setProgress(externalDefault);
            solidProgress.setProgress(innerDefault);
        } else {
            if (externalDefault > 0) {
                externalProgress.setProgress(externalMax);
                solidProgress.setProgress(innerDefault);
            } else {
                externalProgress.setProgress(0);
                solidProgress.setProgress(0);
            }
        }
    }

    private void setExternalProgress(int progress) {
        solidProgress.setProgress(progress + 1);
        innerProgress.setProgress((innerMax / externalMax) * progress);
        lineProgress.setProgress((innerMax / externalMax) * progress);
        value.setText(String.valueOf((innerMax / externalMax) * progress));
        if (progress < innerMax) {
            if (progress <= externalMax) {
                titleProgress.setText("Level " + (progress + 1));
            }
        }
        if (lastExternalProgress < progress / (innerMax / externalMax) + 1) {
            if (listener != null) listener.changeExternalProgress(progress);
        }
    }

    private void setInnerProgress(int progress) {
        innerProgress.setProgress(progress);
        lineProgress.setProgress(progress);
        solidProgress.setProgress(progress / (innerMax / externalMax) + 1);
        value.setText(String.valueOf(progress));
        if (progress < innerMax) {
            titleProgress.setText("Level " + (progress / (innerMax / externalMax) + 1));
            progressBar.setProgress(progress % (innerMax / externalMax));
        }
        if (lastExternalProgress < progress / (innerMax / externalMax) + 1) {
            if (listener != null) listener.changeExternalProgress(progress);
        }
        if (listener != null) listener.changeExternalProgress(progress);
    }

    @SuppressLint("SetTextI18n")
    private void initExternalProgress(@ColorInt int externalColor, @ColorInt int solidColor) {
        externalProgress.setMax(externalMax);
        solidProgress.setMax(externalMax);
        externalProgress.setProgressStartColor(externalColor);
        solidProgress.setProgressEndColor(Color.parseColor("#FF0F170C"));
        solidProgress.setProgressStartColor(solidColor);
        externalSeekBar.setMax(externalMax);

        if (externalDefault == 0) {
            if (type != TypeProgress.trackingIndicators) {
                if ((float) ((float) innerDefault / (innerMax / externalMax)) == 0) {
                    externalDefault = 0;
                } else {
                    externalDefault = innerDefault / (innerMax / externalMax) + 1;
                }
            }
        }
        if (innerDefault == 0) {
            titleProgress.setText("Ready");
        } else {
            titleProgress.setText("Level " + externalDefault);
        }
        progressBar.setMax(innerMax / externalMax);
        progressBar.setProgress(innerDefault % (innerMax / externalMax));
        if (0 <= externalDefault && externalDefault <= externalMax) {
            externalProgress.setProgress(externalDefault);
            solidProgress.setProgress(externalDefault);
        } else {
            if (externalDefault > 0) {
                externalProgress.setProgress(externalMax);
                solidProgress.setProgress(externalMax);
            } else {
                externalProgress.setProgress(0);
                solidProgress.setProgress(0);
            }
        }
    }

    public void setText(String text) {
        titleProgress.setText(text);
    }

    private void initInnerProgress(@ColorInt int innerColor, @ColorInt int lineColor) {
        innerProgress.setMax(innerMax);
        innerProgress.setProgressStartColor(innerColor);
        lineProgress.setMax(innerMax);
        lineProgress.setProgressStartColor(lineColor);
        if (0 <= innerDefault && innerDefault <= innerMax) {
            innerProgress.setProgress(innerDefault);
            lineProgress.setProgress(innerDefault);
            value.setText(String.valueOf(innerDefault));
        } else {
            if (innerDefault > 0) {
                innerProgress.setProgress(innerMax);
                lineProgress.setProgress(innerMax);
                value.setText(String.valueOf(innerMax));
            } else {
                innerProgress.setProgress(0);
                lineProgress.setProgress(0);
                value.setText(String.valueOf(0));
            }
        }
    }

    private void initViews(View view) {
        innerProgress = view.findViewById(R.id.innerProgress);
        externalProgress = view.findViewById(R.id.progress);
        solidProgress = view.findViewById(R.id.progressBackground);
        lineProgress = view.findViewById(R.id.progressBackgroundLine);
        axisSignatures = view.findViewById(R.id.axisSignatures);
        value = view.findViewById(R.id.value);
        innerSeekBar = view.findViewById(R.id.innerSeekBar);
        shadow = view.findViewById(R.id.shadow);
        titleProgress = view.findViewById(R.id.titleProgress);
        progressBar = view.findViewById(R.id.progressBar);
        externalSeekBar = view.findViewById(R.id.externalSeekBar);
        extraDataContainer = view.findViewById(R.id.extraDataContainer);
        extraValue = view.findViewById(R.id.extraValue);
        extraLabel = view.findViewById(R.id.extraLabel);
        progressActiveBackgroundDrawable = view.findViewById(R.id.progressActiveBackgroundDrawable);
        progressInactiveBackgroundDrawable = view.findViewById(R.id.progressInactiveBackgroundDrawable);

        axisActiveDrawable = view.findViewById(R.id.axisActiveDrawable);
        axisInactiveDrawable = view.findViewById(R.id.axisInactiveDrawable);
    }
    // endregion

    // region Inner progress

    /**
     * Set inner progress color to Inner progress.
     *
     * @param color the color
     */
    public void setInnerProgressColor(@ColorInt int color) {
        innerProgress.setProgressStartColor(color);
    }

    /**
     * Sets anomaly.
     *
     * @param anomaly the anomaly
     */
    public void setAnomaly(int anomaly) {
        innerProgress.setProgress(anomaly);
    }


    /**
     * Sets inner default value.
     *
     * @param defaultValue the default value to Inner progress
     */
    public void setInnerDefaultValue(int defaultValue) {
        innerProgress.setProgress(defaultValue);
        lineProgress.setProgress(defaultValue);
        value.setText(defaultValue);
    }

    /**
     * Sets inner max value.
     *
     * @param maxValue the max value to Inner progress
     */
    public void setInnerMaxVaue(int maxValue) {
        innerProgress.setMax(maxValue);
        lineProgress.setMax(maxValue);
    }
    //endregion

    // region External progress

    /**
     * Sets external progress color.
     *
     * @param color the color to External progress
     */
    public void setExternalProgressColor(@ColorInt int color) {
        externalProgress.setProgressStartColor(color);
    }

    /**
     * Sets external default value to External progress
     *
     * @param defaultValue the default value
     */
    public void setExternalDefaultValue(int defaultValue) {
        externalProgress.setProgress(defaultValue);
        solidProgress.setProgress(defaultValue);
    }

    /**
     * Sets external max value.
     *
     * @param maxValue the max value to External progress
     */
    public void setExternalMaxValue(int maxValue) {
        externalProgress.setMax(maxValue);
        solidProgress.setMax(maxValue);
    }
    // endregion

    // region Solid progress

    /**
     * Sets solid progress color.
     *
     * @param color the color to Solid progress
     */
    public void setSolidProgressColor(@ColorInt int color) {
        solidProgress.setProgressStartColor(color);
    }
    // endregion

    // region Line progress

    /**
     * Sets line progress color.
     *
     * @param color the color to Line progress
     */
    public void setLineProgressColor(@ColorInt int color) {
        lineProgress.setProgressStartColor(color);
    }
    // endregion

    /**
     * Sets on change progress listener.
     *
     * @param listener the listener
     */
    public void setOnChangeProgressListener(OnChangeProgressListener listener) {
        this.listener = listener;
    }

    /**
     * Sets touch event enabled.
     *
     * @param touchEventEnabled the default value to Inner progress
     */
    public void setExternalTouchEventEnabled(boolean touchEventEnabled) {
        externalSeekBar.setTouchEventEnabled(touchEventEnabled);
    }

    /**
     * Sets text size.
     *
     * @param size the size axis
     */
    public void setTextSize(int size) {
        axisSignatures.setTextSize(size);
    }

    /**
     * Sets typeface.
     *
     * @param font the font axis
     */
    public void setTypeface(@FontRes int font) {
        axisSignatures.setTypeface(font);
    }

    /**
     * Sets text color.
     *
     * @param color the color axis
     */
    public void setTextColor(@ColorInt int color) {
        axisSignatures.setTextColor(color);
    }

    /**
     * Sets style axis.
     *
     * @param textSize  the text size
     * @param textFont  the text font
     * @param textColor the text color
     */

    public void setStyleAxisDrawable(@Nullable Integer textSize,
                                     @Nullable Typeface textFont,
                                     @Nullable @ColorInt Integer textColor) {
        axisActiveDrawable.setStyleAxis(textSize, textFont, textColor);
    }


    public void setStyleAxis(@Nullable Integer textSize,
                             @Nullable Typeface textFont,
                             @Nullable @ColorInt Integer textColor) {
        axisSignatures.setStyleAxis(textSize, textFont, textColor);
    }

    /**
     * Sets extra data value.
     *
     * @param value the value
     */
    public void setExtraDataValue(String value) {
        extraValue.setText(value);
    }

    /**
     * Sets extra data label.
     *
     * @param label the label
     */
    public void setExtraDataLabel(String label) {
        extraLabel.setText(label);
    }

    /**
     * Sets extra value text size.
     *
     * @param valueSize the value size
     */
    public void setExtraValueTextSize(@DimenRes int valueSize) {
        extraValue.setTextSize(getResources().getDimensionPixelSize(valueSize));
    }

    /**
     * Sets extra label text size.
     *
     * @param labelSize the label size
     */
    public void setExtraLabelTextSize(@DimenRes int labelSize) {
        extraLabel.setTextSize(getResources().getDimensionPixelSize(labelSize));
    }

    /**
     * Sets extras text size.
     *
     * @param valueSize the value size
     * @param labelSize the label size
     */
    public void setExtrasTextSize(@DimenRes int valueSize, @DimenRes int labelSize) {
        extraValue.setTextSize(getResources().getDimensionPixelSize(valueSize));
        extraLabel.setTextSize(getResources().getDimensionPixelSize(labelSize));
    }

    /**
     * Sets extras text color.
     *
     * @param color the color
     */
    public void setExtrasTextColor(@ColorInt int color) {
        extraValue.setTextColor(color);
        extraLabel.setTextColor(color);
    }

    /**
     * Sets extra drawable tint.
     *
     * @param color the color
     */
    public void setExtraDrawableTint(@ColorInt int color) {
        Drawable drawable = getResources().getDrawable(R.drawable.bg_extra_value, null);
        drawable.setTintList(ColorStateList.valueOf(color));
        extraValue.setBackgroundDrawable(drawable);
    }

    /**
     * Sets extra typeface.
     *
     * @param font the font
     */
    public void setExtraTypeface(@FontRes int font) {
        extraValue.setTypeface(getResources().getFont(font));
        extraLabel.setTypeface(getResources().getFont(font));
    }

    ArrayList<DegreeIcon> active = new ArrayList<>();
    ArrayList<DegreeIcon> inactive = new ArrayList<>();
    ArrayList<DegreeIcon> degreeIcons = new ArrayList<>();

    /**
     * Sets start point in the tracking progress mode.
     *
     * @param degreeIcons the degree icons
     */
    public void setStartPoint(ArrayList<DegreeIcon> degreeIcons) {

        ArrayList<DegreeIcon> employees = degreeIcons;

        Comparator<DegreeIcon> compareById = (DegreeIcon o1, DegreeIcon o2) -> {
            assert o1.getAxis() != null;
            assert o2.getAxis() != null;
            return o1.getAxis().compareTo(o2.getAxis());
        };

        Collections.sort(employees, compareById);

        this.degreeIcons = employees;
        active.add(degreeIcons.get(0));
        this.degreeIcons.remove(0);
        inactive.addAll(this.degreeIcons);
        progressActiveBackgroundDrawable.setIconToDegree(active, inactive);
        axisActiveDrawable.setIconToDegree(active, inactive);
        progressInactiveBackgroundDrawable.setIconToDegree(active, inactive);
        axisInactiveDrawable.setIconToDegree(active, inactive);
    }

    /**
     * To next point in the tracking progress mode.
     */
    public void toNextPoint() {
        if (!degreeIcons.isEmpty()) {
            active.add(degreeIcons.get(0));
            this.degreeIcons.remove(0);
            inactive = this.degreeIcons;
            progressActiveBackgroundDrawable.setIconToDegree(active, inactive);
            axisActiveDrawable.setIconToDegree(active, inactive);
            progressInactiveBackgroundDrawable.setIconToDegree(active, inactive);
            axisInactiveDrawable.setIconToDegree(active, inactive);
            invalidate();
        } else {
            active.add(new DegreeIcon(0, R.drawable.ic_drawable_round, "finish", true));
            progressActiveBackgroundDrawable.setIconToDegree(active, inactive);
            axisActiveDrawable.setIconToDegree(active, inactive);
        }
    }

    // region Callback
    public interface OnChangeProgressListener {
        void changeExternalProgress(int progress);
    }
    // endregion

}
