package app.developer.uiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

public class ParameterView extends LinearLayoutCompat {

    private AppCompatTextView textField, valueField;

    /**
     * Instantiates a new Parameter view.
     *
     * @param context the context
     */
    public ParameterView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Parameter view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ParameterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Parameter view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public ParameterView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        View view = inflater.inflate(R.layout.view_parameter, this, true);

        textField = view.findViewById(R.id.textField);
        valueField = view.findViewById(R.id.valueField);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParameterView, 0, 0);

        textField.setText(typedArray.getString(R.styleable.ParameterView_textField));
        valueField.setText(typedArray.getString(R.styleable.ParameterView_valueField));

        textField.setTextColor(typedArray.getColor(R.styleable.ParameterView_colorTextField, ContextCompat.getColor(getContext(), R.color.colorGreen)));
        valueField.setTextColor(typedArray.getColor(R.styleable.ParameterView_colorValueField, ContextCompat.getColor(getContext(), R.color.colorGreen)));

        textField.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ParameterView_sizeTextField, 28));
        valueField.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ParameterView_sizeValueField, 28));

        textField.setAllCaps(typedArray.getBoolean(R.styleable.ParameterView_textAllCaps, false));
        valueField.setAllCaps(typedArray.getBoolean(R.styleable.ParameterView_textAllCaps, false));

        valueField.setPadding(typedArray.getDimensionPixelSize(R.styleable.ParameterView_paddingValueField, 10), 0, 0, 0);

        typedArray.recycle();
    }

    /**
     * Sets text field.
     *
     * @param text the text
     */
    public void setTextField(String text) {
        textField.setText(text);
    }

    /**
     * Sets value field.
     *
     * @param text the text
     */
    public void setValueField(String text) {
        valueField.setText(text);
    }

    /**
     * Sets text field size.
     *
     * @param dimen the dimen
     */
    public void setTextFieldSize(@DimenRes int dimen) {
        textField.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(dimen));
    }

    /**
     * Sets value text size.
     *
     * @param dimen the dimen
     */
    public void setValueTextSize(@DimenRes int dimen) {
        valueField.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(dimen));
    }

    /**
     * Sets text field color.
     *
     * @param color the color
     */
    public void setTextFieldColor(@ColorInt int color) {
        textField.setTextColor(color);
    }

    /**
     * Sets value field color.
     *
     * @param color the color
     */
    public void setValueFieldColor(@ColorInt int color) {
        valueField.setTextColor(color);
    }

    /**
     * Sets text all caps.
     *
     * @param textAllCaps the text all caps
     */
    public void setTextAllCaps(boolean textAllCaps) {
        textField.setAllCaps(textAllCaps);
        valueField.setAllCaps(textAllCaps);
    }

    /**
     * Sets value field padding.
     *
     * @param padding the padding
     */
    public void setValueFieldPadding(@DimenRes int padding) {
        valueField.setPadding(getResources().getDimensionPixelSize(padding), 0, 0 ,0);
    }

}
