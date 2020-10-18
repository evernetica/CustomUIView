package app.developer.uiview.parameter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

/**
 * The type Parameter text entry view.
 */
public class ParameterTextEntryView extends LinearLayoutCompat {

    private AppCompatTextView header;
    private AppCompatEditText value;
    private ConstraintLayout constraint;

    /**
     * Instantiates a new Parameter text entry view.
     *
     * @param context the context
     */
    public ParameterTextEntryView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Parameter text entry view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ParameterTextEntryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Parameter text entry view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public ParameterTextEntryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_parameter_text_entry, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParameterTextEntryView, 0, 0);
        initViews(view);
        header.setText(typedArray.getString(R.styleable.ParameterTextEntryView_headerText));
        header.setTextColor(typedArray.getColor(R.styleable.ParameterTextEntryView_headerTextColor, Color.parseColor("#77BC1F")));
        if (typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_headerTextSize, 0) != 0)
            header.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_headerTextSize, 0));
        if (typedArray.getFont(R.styleable.ParameterTextEntryView_headerFontFamily) != null)
            header.setTypeface(typedArray.getFont(R.styleable.ParameterTextEntryView_headerFontFamily));

        value.setText(typedArray.getString(R.styleable.ParameterTextEntryView_valueText));
        value.setTextColor(typedArray.getColor(R.styleable.ParameterTextEntryView_valueTextColor, Color.WHITE));
        if (typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_valueTextSize, 0) != 0)
            value.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_valueTextSize, 0));
        if (typedArray.getFont(R.styleable.ParameterTextEntryView_valueFontFamily) != null)
            header.setTypeface(typedArray.getFont(R.styleable.ParameterTextEntryView_valueFontFamily));

        value.setImeOptions(typedArray.getInt(R.styleable.ParameterTextEntryView_android_imeOptions, 0));
        value.setInputType(typedArray.getInt(R.styleable.ParameterTextEntryView_android_inputType, EditorInfo.TYPE_TEXT_VARIATION_NORMAL));

        if (typedArray.getDrawable(R.styleable.ParameterTextEntryView_valueContainerDrawable) != null)
            value.setBackground(typedArray.getDrawable(R.styleable.ParameterTextEntryView_valueContainerDrawable));

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraint);

        constraintSet.connect(value.getId(), ConstraintSet.START, constraint.getId(), ConstraintSet.START, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_fieldMarginStart, 106));
        constraintSet.connect(value.getId(), ConstraintSet.TOP, header.getId(), ConstraintSet.BOTTOM, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_fieldMarginTop, 12));
        constraintSet.connect(value.getId(), ConstraintSet.BOTTOM, constraint.getId(), ConstraintSet.BOTTOM, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_fieldMarginBottom, 25));
        constraintSet.connect(value.getId(), ConstraintSet.END, constraint.getId(), ConstraintSet.END, typedArray.getDimensionPixelSize(R.styleable.ParameterTextEntryView_fieldMarginEnd, 106));
        constraintSet.applyTo(constraint);

        typedArray.recycle();
    }

    private void initViews(View view) {
        header = view.findViewById(R.id.header);
        value = view.findViewById(R.id.value);
        constraint = view.findViewById(R.id.constraint);
    }

    /**
     * Sets header text.
     *
     * @param text the text
     */
    public void setHeaderText(String text) {
        header.setText(text);
    }

    /**
     * Sets value text.
     *
     * @param text the text
     */
    public void setValueText(String text) {
        value.setText(text);
    }

    /**
     * Sets header text color.
     *
     * @param color the color
     */
    public void setHeaderTextColor(@ColorRes int color) {
        header.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * Sets value text color.
     *
     * @param color the color
     */
    public void setValueTextColor(@ColorRes int color) {
        value.setTextColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * Sets header typeface.
     *
     * @param font the font
     */
    public void setHeaderTypeface(@FontRes int font) {
        header.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets value typeface.
     *
     * @param font the font
     */
    public void setValueTypeface(@FontRes int font) {
        value.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets header text size from dimension.
     *
     * @param size the size
     */
    public void setHeaderTextSize(@DimenRes int size) {
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(size));
    }

    /**
     * Sets value text size from dimension.
     *
     * @param size the size
     */
    public void setValueTextSize(@DimenRes int size) {
        value.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(size));
    }

    /**
     * Sets header text size.
     *
     * @param size the size
     */
    public void setHeaderTextSize(float size) {
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * Sets value text size.
     *
     * @param size the size
     */
    public void setValueTextSize(float size) {
        value.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value.getText().toString();
    }

    /**
     * Sets ime options.
     *
     * @param imeOptions the ime options
     */
    public void setImeOptions(int imeOptions) {
        value.setImeOptions(imeOptions);
    }

    /**
     * Sets input type.
     *
     * @param inputType the input type
     */
    public void setInputType(int inputType) {
        value.setInputType(inputType);
    }

    /**
     * Sets background drawable.
     *
     * @param drawable the drawable
     */
    public void setBackgroundDrawable(@DrawableRes int drawable) {
        value.setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

}
