package app.developer.uiview.parameter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

/**
 * The type Parameter toggle view.
 */
public class ParameterToggleView extends LinearLayoutCompat implements CompoundButton.OnCheckedChangeListener {

    private LinearLayoutCompat switchBackground;
    private Switch switchView;
    private AppCompatTextView headerText, inactiveText, activeText;
    @Nullable
    private Drawable activeDrawable, inactiveDrawable;

    @Nullable
    private OnCheckedListener listener;

    /**
     * Instantiates a new Parameter toggle view.
     *
     * @param context the context
     */
    public ParameterToggleView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Parameter toggle view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ParameterToggleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Parameter toggle view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public ParameterToggleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        View view = inflater.inflate(R.layout.view_parameter_toggle, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ParameterToggleView, 0, 0);
        initViews(view);

        headerText.setText(typedArray.getString(R.styleable.ParameterToggleView_headerText));
        if (typedArray.getFont(R.styleable.ParameterToggleView_headerFontFamily) != null)
            headerText.setTypeface(typedArray.getFont(R.styleable.ParameterToggleView_headerFontFamily));

        activeDrawable = typedArray.getDrawable(R.styleable.ParameterToggleView_activeDrawable);
        inactiveDrawable = typedArray.getDrawable(R.styleable.ParameterToggleView_inactiveDrawable);

        if (typedArray.getFont(R.styleable.ParameterToggleView_switchFontFamily) != null) {
            activeText.setTypeface(typedArray.getFont(R.styleable.ParameterToggleView_switchFontFamily));
            inactiveText.setTypeface(typedArray.getFont(R.styleable.ParameterToggleView_switchFontFamily));
        }

        if (typedArray.getString(R.styleable.ParameterToggleView_activeText) != null)
            activeText.setText(typedArray.getString(R.styleable.ParameterToggleView_activeText));
        activeText.setTextColor(typedArray.getColor(R.styleable.ParameterToggleView_activeTextColor, Color.WHITE));

        if (typedArray.getString(R.styleable.ParameterToggleView_inactiveText) != null)
            inactiveText.setText(typedArray.getString(R.styleable.ParameterToggleView_inactiveText));
        inactiveText.setTextColor(typedArray.getColor(R.styleable.ParameterToggleView_inactiveTextColor, Color.WHITE));

        if (typedArray.getBoolean(R.styleable.ParameterToggleView_android_checked, false)) {
            if (activeDrawable != null)
                switchBackground.setBackground(activeDrawable);
            else
                switchBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_track_active));
            switchView.setChecked(true);
            activeText.setVisibility(View.VISIBLE);
            inactiveText.setVisibility(View.GONE);
        } else {
            if (inactiveDrawable != null)
                switchBackground.setBackground(inactiveDrawable);
            else
                switchBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_track_inactive));
            switchView.setChecked(false);
            activeText.setVisibility(View.GONE);
            inactiveText.setVisibility(View.VISIBLE);
        }

        switchView.setOnCheckedChangeListener(this);

        typedArray.recycle();
    }

    private void initViews(View view) {
        switchBackground = view.findViewById(R.id.switchBackground);
        switchView = view.findViewById(R.id.switchView);
        headerText = view.findViewById(R.id.headerText);
        inactiveText = view.findViewById(R.id.inactiveText);
        activeText = view.findViewById(R.id.activeText);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (listener != null) {
            listener.onCheckedChanged(isChecked);
        }
        updateSwitchState(isChecked);
    }

    private void updateSwitchState(boolean isChecked) {
        if (isChecked) {
            switchBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_track_active));
            activeText.setVisibility(View.VISIBLE);
            inactiveText.setVisibility(View.GONE);
        } else {
            switchBackground.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_track_inactive));
            activeText.setVisibility(View.GONE);
            inactiveText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets on changed listener.
     *
     * @param listener the listener
     */
    public void setOnChangedListener(OnCheckedListener listener) {
        this.listener = listener;
    }

    /**
     * Sets header text.
     *
     * @param text the text
     */
    public void setHeaderText(String text) {
        headerText.setText(text);
    }

    /**
     * Sets header text color.
     *
     * @param color the color
     */
    public void setHeaderTextColor(@ColorInt int color) {
        headerText.setTextColor(color);
    }

    /**
     * Sets header font family.
     *
     * @param font the font
     */
    public void setHeaderFontFamily(@FontRes int font) {
        headerText.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets active text.
     *
     * @param activeText the active text
     */
    public void setActiveText(String activeText) {
        this.activeText.setText(activeText);
    }

    /**
     * Sets inactive text.
     *
     * @param inactiveText the inactive text
     */
    public void setInactiveText(String inactiveText) {
        this.inactiveText.setText(inactiveText);
    }

    /**
     * Sets active text color.
     *
     * @param activeColor the active color
     */
    public void setActiveTextColor(@ColorInt int activeColor) {
        activeText.setTextColor(activeColor);
    }

    /**
     * Sets inactive text color.
     *
     * @param inactiveColor the inactive color
     */
    public void setInactiveTextColor(@ColorInt int inactiveColor) {
        inactiveText.setTextColor(inactiveColor);
    }


    /**
     * Sets active drawable.
     *
     * @param activeDrawable the active drawable
     */
    public void setActiveDrawable(@DrawableRes int activeDrawable) {
        this.activeDrawable = ContextCompat.getDrawable(getContext(), activeDrawable);
    }

    /**
     * Sets inactive drawable.
     *
     * @param inactiveDrawable the inactive drawable
     */
    public void setInactiveDrawable(@DrawableRes int inactiveDrawable) {
        this.inactiveDrawable = ContextCompat.getDrawable(getContext(), inactiveDrawable);
    }

    /**
     * Sets switch state font family.
     *
     * @param font the font
     */
    public void setSwitchStateFontFamily(@FontRes int font) {
        activeText.setTypeface(getResources().getFont(font));
        inactiveText.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets checked.
     *
     * @param checked the checked
     */
    public void setChecked(boolean checked) {
        switchView.setChecked(checked);
    }

    public interface OnCheckedListener {
        void onCheckedChanged(boolean isChecked);
    }

    private float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
