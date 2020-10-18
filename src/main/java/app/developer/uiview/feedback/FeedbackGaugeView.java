package app.developer.uiview.feedback;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

/**
 * The type Feedback gauge view.
 */
public class FeedbackGaugeView extends LinearLayoutCompat {

    private AppCompatTextView titleWidget, valueAmp, minValue, maxValue, format;
    private ProgressBar progressHorizontal;

    /**
     * Instantiates a new Feedback gauge view.
     *
     * @param context the context
     */
    public FeedbackGaugeView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Feedback gauge view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public FeedbackGaugeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Feedback gauge view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public FeedbackGaugeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("SetTextI18n")
    private void init(@Nullable Context context, @Nullable AttributeSet set) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_feedback_guage, this, true);
        titleWidget = view.findViewById(R.id.titleWidget);
        valueAmp = view.findViewById(R.id.valueAmp);
        minValue = view.findViewById(R.id.minValue);
        maxValue = view.findViewById(R.id.maxValue);
        format = view.findViewById(R.id.format);
        progressHorizontal = view.findViewById(R.id.progress_horizontal);
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.FeedbackGaugeView, 0, 0);

        Typeface typeface = typedArray.getFont(R.styleable.FeedbackGaugeView_android_fontFamily);
        if (typeface != null) {
            titleWidget.setTypeface(typeface);
            valueAmp.setTypeface(typeface);
            minValue.setTypeface(typeface);
            maxValue.setTypeface(typeface);
            format.setTypeface(typeface);
        }

        if (typedArray.getString(R.styleable.FeedbackGaugeView_titleWidget) != null) {
            titleWidget.setText(typedArray.getString(R.styleable.FeedbackGaugeView_titleWidget));
        } else {
            titleWidget.setText("AMPS");
        }
        progressHorizontal.setMin(typedArray.getInt(R.styleable.FeedbackGaugeView_minProgress, 0));
        minValue.setText(String.valueOf(typedArray.getInt(R.styleable.FeedbackGaugeView_minProgress, 0)));
        progressHorizontal.setMax(typedArray.getInt(R.styleable.FeedbackGaugeView_maxProgress, 200));
        maxValue.setText(String.valueOf(typedArray.getInt(R.styleable.FeedbackGaugeView_maxProgress, 200)));
        progressHorizontal.setProgress(typedArray.getInt(R.styleable.FeedbackGaugeView_defaultProgress, 150));
        valueAmp.setText(String.valueOf(typedArray.getInt(R.styleable.FeedbackGaugeView_defaultProgress, 150)));

        typedArray.recycle();
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        titleWidget.setText(title);
    }

    /**
     * Sets progress range.
     *
     * @param min the min value
     * @param max the max value
     */
    public void setProgressRange(int min, int max) {
        progressHorizontal.setMin(min);
        progressHorizontal.setMax(max);

        minValue.setText(String.valueOf(min));
        maxValue.setText(String.valueOf(max));
    }

    /**
     * Sets progress default value.
     *
     * @param defaultProgress the default progress
     */
    public void setProgressDefault(int defaultProgress) {
        progressHorizontal.setProgress(defaultProgress);
        valueAmp.setText(String.valueOf(defaultProgress));
    }

    /**
     * Sets progress drawable background.
     *
     * @param drawable the drawable
     */
    public void setProgressDrawable(@DrawableRes int drawable) {
        progressHorizontal.setProgressDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Gets progress value.
     *
     * @return the progress
     */
    public int getProgress() {
        return progressHorizontal.getProgress();
    }
}
