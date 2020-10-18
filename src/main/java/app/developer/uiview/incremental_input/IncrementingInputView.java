package app.developer.uiview.incremental_input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

/**
 * The type Incrementing input view.
 */
public class IncrementingInputView extends LinearLayoutCompat {

    private InnerShadowView valueContainer;
    private AppCompatTextView valueTextView, maxValue, minValue;
    private RoundButton incrementalButton, decrementalButton;
    private AppCompatImageView imageView;

    @Nullable
    private OnChangeValueListener listener;

    @Nullable
    private Drawable indicatorIncremental, indicatorDecremental;

    private int min, max;
    private float incrementStep;

    /**
     * Instantiates a new Incrementing input view.
     *
     * @param context the context
     */
    public IncrementingInputView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Incrementing input view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public IncrementingInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Incrementing input view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public IncrementingInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_incrementing_input, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IncrementingInputView, 0, 0);
        initViews(view);

        if (typedArray.getDrawable(R.styleable.IncrementingInputView_imageIncremental) != null) {
            incrementalButton.setImage(typedArray.getDrawable(R.styleable.IncrementingInputView_imageIncremental));
        }
        if (typedArray.getDrawable(R.styleable.IncrementingInputView_imageDecremental) != null) {
            decrementalButton.setImage(typedArray.getDrawable(R.styleable.IncrementingInputView_imageDecremental));
        }

        min = typedArray.getInt(R.styleable.IncrementingInputView_setMin, 0);
        max = typedArray.getInt(R.styleable.IncrementingInputView_setMax, 0);
        incrementStep = typedArray.getFloat(R.styleable.IncrementingInputView_setIncrementSteps, 0.1f);

        indicatorIncremental = typedArray.getDrawable(R.styleable.IncrementingInputView_indicatorIncremental);
        indicatorDecremental = typedArray.getDrawable(R.styleable.IncrementingInputView_indicatorDecremental);

        minValue.setText("min " + min + "%");
        maxValue.setText("max " + max + "%");
        valueTextView.setText(typedArray.getFloat(R.styleable.IncrementingInputView_setDefaultValue, 0.0f) + "%");
        if (typedArray.getDimensionPixelSize(R.styleable.IncrementingInputView_valueTextSize, 0) != 0)
            valueTextView.setTextSize(typedArray.getDimensionPixelSize(R.styleable.IncrementingInputView_valueTextSize, 0));
        if (typedArray.getDimensionPixelSize(R.styleable.IncrementingInputView_minMaxTextSize, 0) != 0) {
            minValue.setTextSize(typedArray.getDimensionPixelSize(R.styleable.IncrementingInputView_minMaxTextSize, 0));
            maxValue.setTextSize(typedArray.getDimensionPixelSize(R.styleable.IncrementingInputView_minMaxTextSize, 0));
        }

        incrementalButton.setOnClickListener(new RoundButton.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick() {
                Float value = Float.valueOf(valueTextView.getText().toString().replace("%", ""));
                if (indicatorIncremental != null)
                    imageView.setImageDrawable(indicatorIncremental);
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_up_value));
                if ((value + incrementStep) <= max) {
                    valueTextView.setText(String.format("%.1f", (value + incrementStep)) + "%");
                    if (listener != null) {
                        listener.onValueChanged(value + incrementStep);
                    }
                }
            }
        });

        valueContainer.setBackground(typedArray.getColor(R.styleable.IncrementingInputView_shadowColorValueView, Color.BLACK),
                typedArray.getColor(R.styleable.IncrementingInputView_backgroundColorValueView, Color.parseColor("#DEDEDE")));

        setImageTintColor(typedArray.getColor(R.styleable.IncrementingInputView_imageTint, Color.parseColor("#1D1D1D")));

        decrementalButton.setOnClickListener(new RoundButton.OnClickListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onClick() {
                Float value = Float.valueOf(valueTextView.getText().toString().replace("%", ""));
                if (indicatorDecremental != null)
                    imageView.setImageDrawable(indicatorDecremental);
                else
                    imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_down_value));
                if ((value - incrementStep) >= min) {
                    valueTextView.setText(String.format("%.1f", (value - incrementStep)) + "%");
                    if (listener != null) {
                        listener.onValueChanged(value - incrementStep);
                    }
                }
            }
        });
        typedArray.recycle();
    }

    private void initViews(View view) {
        valueContainer = view.findViewById(R.id.valueContainer);
        valueTextView = view.findViewById(R.id.valueTextView);
        maxValue = view.findViewById(R.id.maxValue);
        minValue = view.findViewById(R.id.minValue);
        imageView = view.findViewById(R.id.imageView);
        incrementalButton = view.findViewById(R.id.incrementalButton);
        decrementalButton = view.findViewById(R.id.decrementalButton);
    }

    /**
     * Sets default value.
     *
     * @param defaultValue the default value
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void setDefaultValue(Float defaultValue) {
        valueTextView.setText(String.format("%.1f", defaultValue) + "%");
    }

    /**
     * Sets increment step.
     *
     * @param incrementStep the increment step
     */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void setIncrementStep(Float incrementStep) {
        this.incrementStep = incrementStep;
    }

    /**
     * Sets button backgraund color.
     *
     * @param typeGradient the type gradient
     * @param shadow       the shadow colors
     * @param background   the background color
     */
    public void setButtonBackgroundColor(int typeGradient, int[] shadow, int[] background) {
        incrementalButton.setLayerDrawable(typeGradient, shadow, background);
        decrementalButton.setLayerDrawable(typeGradient, shadow, background);
    }

    /**
     * Sets image tint color.
     *
     * @param color the color
     */
    public void setImageTintColor(@ColorInt int color) {
        incrementalButton.setTintColor(color);
        decrementalButton.setTintColor(color);
    }

    /**
     * Sets indicator incremental.
     *
     * @param drawable the indicator incremental image
     */
    public void setIndicatorIncremental(@DrawableRes int drawable) {
        indicatorIncremental = ContextCompat.getDrawable(getContext(), drawable);
    }

    /**
     * Sets indicator decremental.
     *
     * @param drawable the indicator decremental image
     */
    public void setIndicatorDecremental(@DrawableRes int drawable) {
        indicatorDecremental = ContextCompat.getDrawable(getContext(), drawable);
    }

    /**
     * Sets incremental image.
     *
     * @param drawable the drawable incremental button
     */
    public void setIncrementalImage(@DrawableRes int drawable) {
        incrementalButton.setImage(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets decremental image.
     *
     * @param drawable the drawable decremental button
     */
    public void setDecrementalImage(@DrawableRes int drawable) {
        decrementalButton.setImage(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets max value.
     *
     * @param maxValue the max value
     */
    @SuppressLint("SetTextI18n")
    public void setMaxValue(Integer maxValue) {
        max = maxValue;
        this.maxValue.setText("max " + maxValue + "%");
    }

    /**
     * Sets min value.
     *
     * @param minValue the min value
     */
    @SuppressLint("SetTextI18n")
    public void setMinValue(Integer minValue) {
        min = minValue;
        this.minValue.setText("min " + minValue + "%");
    }

    /**
     * Sets typeface.
     *
     * @param font the font for min and max TextView
     */
    public void setTypeface(@FontRes int font) {
        minValue.setTypeface(getResources().getFont(font));
        maxValue.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets value typeface.
     *
     * @param font the font for value TextView
     */
    public void setValueTypeface(@FontRes int font) {
        valueTextView.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets on change value listrener.
     *
     * @param listener the listener for change value
     */
    public void setOnChangeValueListener(OnChangeValueListener listener) {
        this.listener = listener;
    }

    interface OnChangeValueListener {
        void onValueChanged(float value);
    }
}