package app.developer.uiview.drop_down;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

/**
 * The type Dropdown view.
 */
public class DropdownView extends LinearLayoutCompat {

    private LinearLayoutCompat containerView;
    private AppCompatTextView text;
    private StateDropDown state;
    private Drawable collapsedImage, expandedImage;
    private AppCompatImageView stateImage;
    @Nullable
    private OnStateChangeListener listener;
    private Boolean isExpanded;

    private int[] backgroundList = new int[]{Color.parseColor("#6D6D6D"), Color.parseColor("#272727")};

    /**
     * Instantiates a new Dropdown view.
     *
     * @param context the context
     */
    public DropdownView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Dropdown view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DropdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Dropdown view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DropdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        View view = inflater.inflate(R.layout.view_dropdown, this, true);
        initViews(view);

        containerView.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, backgroundList));

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropdownView, 0, 0);
        text.setText(typedArray.getString(R.styleable.DropdownView_text));

        text.setTextColor(typedArray.getColor(R.styleable.DropdownView_textColor, Color.WHITE));
        if (typedArray.getFont(R.styleable.DropdownView_textFontFamily) != null)
            text.setTypeface(typedArray.getFont(R.styleable.DropdownView_textFontFamily));

        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.DropdownView_android_textSize, 28));
        if (typedArray.getBoolean(R.styleable.DropdownView_textCaps, false)) {
            text.setAllCaps(true);
        } else {
            text.setAllCaps(false);
        }
        if (typedArray.getDrawable(R.styleable.DropdownView_collapsedImage) != null)
            collapsedImage = typedArray.getDrawable(R.styleable.DropdownView_collapsedImage);
        else
            collapsedImage = ContextCompat.getDrawable(getContext(), R.drawable.ic_collapsed);

        if (typedArray.getDrawable(R.styleable.DropdownView_expandedImage) != null)
            expandedImage = typedArray.getDrawable(R.styleable.DropdownView_expandedImage);
        else
            expandedImage = ContextCompat.getDrawable(getContext(), R.drawable.ic_expanded);

        stateImage.setImageTintList(ColorStateList.valueOf(typedArray.getColor(R.styleable.DropdownView_imageTint, Color.WHITE)));

        state = StateDropDown.values()[typedArray.getInt(R.styleable.DropdownView_stateDropDown, 0)];

        switch (state) {
            case expanded:
                isExpanded = false;
                stateImage.setImageDrawable(expandedImage);
                break;
            case collapsed:
                isExpanded = true;
                stateImage.setImageDrawable(collapsedImage);
                break;
            case none:
                stateImage.setImageDrawable(null);
                break;
        }

        stateImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpanded = !isExpanded;
                if (isExpanded) {
                    stateImage.setImageDrawable(collapsedImage);
                } else {
                    stateImage.setImageDrawable(expandedImage);
                }
                if (listener != null) {
                    listener.onStateChanged(isExpanded);
                }
            }
        });

        typedArray.recycle();
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text.setText(text);
    }

    /**
     * Sets text color.
     *
     * @param color the color
     */
    public void setTextColor(@ColorInt int color) {
        text.setTextColor(color);
    }

    /**
     * Sets typeface.
     *
     * @param font the font
     */
    public void setTypeface(@FontRes int font) {
        text.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets text size.
     *
     * @param textSize the text size
     */
    public void setTextSize(@DimenRes int textSize) {
        text.setTextSize(getResources().getDimensionPixelSize(textSize));
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(StateDropDown state) {
        this.state = state;
        switch (state) {
            case expanded:
                stateImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expanded));
                break;
            case collapsed:
                stateImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_collapsed));
                break;
            case none:
                stateImage.setImageDrawable(null);
                break;
        }
    }

    public StateDropDown getState(){
        return state;
    }

    public void performStateChangingClick() {
        if (stateImage != null) {
            stateImage.performClick();
        }
    }

    /**
     * Sets collapsed image.
     *
     * @param drawable the drawable
     */
    public void setCollapsedImage(@DrawableRes int drawable) {
        collapsedImage = ContextCompat.getDrawable(getContext(), drawable);
    }

    /**
     * Sets expanded image.
     *
     * @param drawable the drawable
     */
    public void setExpandedImage(@DrawableRes int drawable) {
        expandedImage = ContextCompat.getDrawable(getContext(), drawable);
    }

    /**
     * Sets image tint.
     *
     * @param color the color
     */
    public void setImageTint(@ColorInt int color) {
        stateImage.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets state change listener.
     *
     * @param listener the listener
     */
    public void setStateChangeListener(OnStateChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Sets background.
     *
     * @param drawable the drawable
     */
    public void setBackground(@DrawableRes int drawable) {
        containerView.setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets background.
     *
     * @param color the color
     */
    public void setBackground(int[] color) {
        containerView.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, color));
    }

    private void initViews(View view) {
        containerView = view.findViewById(R.id.containerView);
        stateImage = view.findViewById(R.id.stateImage);
        text = view.findViewById(R.id.text);
    }

    public interface OnStateChangeListener {
        void onStateChanged(Boolean isOpened);
    }
}
