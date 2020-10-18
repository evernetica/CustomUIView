package app.developer.uiview.parameter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

public class ParameterContentBlockView extends LinearLayoutCompat {

    private AppCompatTextView headerText, firstDescription, secondDescription, firstValue, secondValue;

    public ParameterContentBlockView(Context context) {
        super(context);
        init(context, null);
    }

    public ParameterContentBlockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ParameterContentBlockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    public void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_parametr_content_block, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParameterContentBlockView, 0, 0);
        initViews(view);

        headerText.setText(typedArray.getString(R.styleable.ParameterContentBlockView_textHeader));
        firstDescription.setText(typedArray.getString(R.styleable.ParameterContentBlockView_textFirstDescription));
        secondDescription.setText(typedArray.getString(R.styleable.ParameterContentBlockView_textSecondDescription));
        firstValue.setText(typedArray.getString(R.styleable.ParameterContentBlockView_textFirstValue));
        secondValue.setText(typedArray.getString(R.styleable.ParameterContentBlockView_textSecondValue));

        if (typedArray.getFont(R.styleable.ParameterContentBlockView_headerFontFamily) != null) {
            headerText.setTypeface(typedArray.getFont(R.styleable.ParameterContentBlockView_headerFontFamily));
        }

        if (typedArray.getFont(R.styleable.ParameterContentBlockView_descriptionFontFamily) != null) {
            firstDescription.setTypeface(typedArray.getFont(R.styleable.ParameterContentBlockView_descriptionFontFamily));
            secondDescription.setTypeface(typedArray.getFont(R.styleable.ParameterContentBlockView_descriptionFontFamily));
        }

        if (typedArray.getFont(R.styleable.ParameterContentBlockView_valueFontFamily) != null) {
            firstValue.setTypeface(typedArray.getFont(R.styleable.ParameterContentBlockView_valueFontFamily));
            secondValue.setTypeface(typedArray.getFont(R.styleable.ParameterContentBlockView_valueFontFamily));
        }

        if (typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_headerTextSize, 0) != 0)
            headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_headerTextSize, 0));

        if (typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_descriptionTextSize, 0) != 0) {
            firstDescription.setTextSize(typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_descriptionTextSize, 0));
            secondDescription.setTextSize(typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_descriptionTextSize, 0));
        }

        if (typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_valueTextSize, 0) != 0) {
            firstValue.setTextSize(typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_valueTextSize, 0));
            secondValue.setTextSize(typedArray.getDimensionPixelSize(R.styleable.ParameterContentBlockView_valueTextSize, 0));
        }

        headerText.setTextColor(typedArray.getColor(R.styleable.ParameterContentBlockView_headerTextColor, Color.parseColor("#77BC1F")));
        firstDescription.setTextColor(typedArray.getColor(R.styleable.ParameterContentBlockView_descriptionTextColor, Color.parseColor("#8E8E8E")));
        secondDescription.setTextColor(typedArray.getColor(R.styleable.ParameterContentBlockView_descriptionTextColor, Color.parseColor("#8E8E8E")));
        firstValue.setTextColor(typedArray.getColor(R.styleable.ParameterContentBlockView_valueTextColor, Color.WHITE));
        secondValue.setTextColor(typedArray.getColor(R.styleable.ParameterContentBlockView_valueTextColor, Color.WHITE));

        typedArray.recycle();
    }

    public void setHeaderText(String text) {
        headerText.setText(text);
    }

    public void setFirstDescriptionText(String text) {
        firstDescription.setText(text);
    }

    /**
     * Sets second description text.
     *
     * @param text the text
     */
    public void setSecondDescriptionText(String text) {
        secondDescription.setText(text);
    }

    /**
     * Sets first value text.
     *
     * @param text the text
     */
    public void setFirstValueText(String text) {
        firstValue.setText(text);
    }

    /**
     * Sets second value text.
     *
     * @param text the text
     */
    public void setSecondValueText(String text) {
        secondValue.setText(text);
    }

    /**
     * Sets header type face.
     *
     * @param font the font
     */
    public void setHeaderTypeFace(@FontRes Integer font) {
        headerText.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets description type face.
     *
     * @param font the font
     */
    public void setDescriptionTypeFace(@FontRes Integer font) {
        firstDescription.setTypeface(getResources().getFont(font));
        secondDescription.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets vslue type face.
     *
     * @param font the font
     */
    public void setValueTypeFace(@FontRes Integer font) {
        firstValue.setTypeface(getResources().getFont(font));
        secondValue.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets header text size from dimensions.
     *
     * @param textSize the text size
     */
    public void setHeaderTextSize(@DimenRes int textSize) {
        headerText.setTextSize(getResources().getDimension(textSize));
    }

    /**
     * Sets header text size.
     *
     * @param textSize the text size
     */
    public void setHeaderTextSize(float textSize) {
        headerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * Sets description text size from dimensions.
     *
     * @param textSize the text size
     */
    public void setDescriptionTextSize(@DimenRes int textSize) {
        firstDescription.setTextSize(getResources().getDimension(textSize));
        secondDescription.setTextSize(getResources().getDimension(textSize));
    }

    /**
     * Sets description text size.
     *
     * @param textSize the text size
     */
    public void setDescriptionTextSize(float textSize) {
        firstDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        secondDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * Sets value text size from dimensions.
     *
     * @param textSize the text size
     */
    public void setValueTextSize(@DimenRes int textSize) {
        firstValue.setTextSize(getResources().getDimension(textSize));
        secondValue.setTextSize(getResources().getDimension(textSize));
    }

    /**
     * Sets value text size.
     *
     * @param textSize the text size
     */
    public void setValueTextSize(float textSize) {
        firstValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        secondValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * Sets header text color.
     *
     * @param color the color
     */
    public void setHeaderTextColor(@ColorRes int color) {
        headerText.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * Sets description text color.
     *
     * @param color the color
     */
    public void setDescriptionTextColor(@ColorRes int color) {
        firstDescription.setTextColor(ContextCompat.getColor(getContext(), color));
        secondDescription.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * Sets value text color.
     *
     * @param color the color
     */
    public void setValueTextColor(@ColorRes int color) {
        firstValue.setTextColor(ContextCompat.getColor(getContext(), color));
        secondValue.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    private void initViews(View view) {
        headerText = view.findViewById(R.id.headerText);
        firstDescription = view.findViewById(R.id.firstDescription);
        firstValue = view.findViewById(R.id.firstValue);
        secondDescription = view.findViewById(R.id.secondDescription);
        secondValue = view.findViewById(R.id.secondValue);
    }
}
