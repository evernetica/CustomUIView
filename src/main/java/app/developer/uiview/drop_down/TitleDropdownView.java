package app.developer.uiview.drop_down;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
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

public class TitleDropdownView extends LinearLayoutCompat {

    private LinearLayoutCompat containerView;
    private AppCompatTextView title;
    private AppCompatImageView leftImageView, rightImageView;
    private State state = State.expand_down;
    private SidesClickListener sidesClickListener;
    private ArrowClickListener arrowClickListener;
    private ItemClickListener itemClickListener;
    private View leftDivider, rightDivider;
    private GradientDrawable backgroundDrawable;

    public TitleDropdownView(Context context) {
        super(context);
        init(context, null);
    }

    public TitleDropdownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TitleDropdownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_title_dropdown, this, true);
        initViews(view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleDropdownView, 0, 0);
        float radius = typedArray.getFloat(R.styleable.TitleDropdownView_dropDownRadiusCorners, 8);

        state = State.values()[typedArray.getInt(R.styleable.TitleDropdownView_state, 0)];
        setState(state);

        if (typedArray.getBoolean(R.styleable.TitleDropdownView_visibilityLeftDivider, false)) {
            leftDivider.setVisibility(View.VISIBLE);
        } else {
            leftDivider.setVisibility(View.GONE);
        }

        if (typedArray.getBoolean(R.styleable.TitleDropdownView_visibilityRightDivider, false)) {
            rightDivider.setVisibility(View.VISIBLE);
        } else {
            rightDivider.setVisibility(View.GONE);
        }

        rightDivider.setBackgroundColor(typedArray.getColor(R.styleable.TitleDropdownView_dividerColor, Color.TRANSPARENT));
        leftDivider.setBackgroundColor(typedArray.getColor(R.styleable.TitleDropdownView_dividerColor, Color.TRANSPARENT));

        if (typedArray.getBoolean(R.styleable.TitleDropdownView_sides, false)) {
            rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_down_right));
            leftImageView.setVisibility(View.VISIBLE);
            title.setGravity(Gravity.CENTER);
        } else {
            title.setGravity(Gravity.START);
            leftImageView.setVisibility(View.GONE);
        }
        containerView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_light_black_rounded_top_corners_with_top_line));
        title.setText(typedArray.getString(R.styleable.TitleDropdownView_text));
        title.setTextSize(typedArray.getDimensionPixelSize(R.styleable.TitleDropdownView_textSize, 22));
        title.setTextColor(typedArray.getColor(R.styleable.TitleDropdownView_textColor, Color.WHITE));
        if (typedArray.getInt(R.styleable.TitleDropdownView_android_gravity, 0) != 0)
            title.setGravity(typedArray.getInt(R.styleable.TitleDropdownView_android_gravity, 0));
        if (typedArray.getFont(R.styleable.TitleDropdownView_textFontFamily) != null)
            title.setTypeface(typedArray.getFont(R.styleable.TitleDropdownView_textFontFamily));
        LinearLayoutCompat.LayoutParams params = (LayoutParams) title.getLayoutParams();
        params.setMargins(
                typedArray.getDimensionPixelSize(R.styleable.TitleDropdownView_titleMarginStart, 0),
                0,
                typedArray.getDimensionPixelSize(R.styleable.TitleDropdownView_titleMarginEnd, 0),
                0);
        title.setLayoutParams(params);

        rightImageView.setImageTintList(ColorStateList.valueOf(typedArray.getColor(R.styleable.TitleDropdownView_arrowColor, Color.WHITE)));
        leftImageView.setImageTintList(ColorStateList.valueOf(typedArray.getColor(R.styleable.TitleDropdownView_arrowColor, Color.WHITE)));
        typedArray.recycle();
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        title.setText(text);
    }

    /**
     * Sets text color.
     *
     * @param color the color
     */
    public void setTextColor(@ColorInt int color) {
        title.setTextColor(color);
    }

    /**
     * Sets font family.
     *
     * @param font the font
     */
    public void setFontFamily(@FontRes int font) {
        title.setTypeface(getResources().getFont(font));
    }

    public void setBackground(@DrawableRes int drawable) {
        containerView.setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    public void setBackgroundViewColor(int color) {
        containerView.setBackgroundColor(ContextCompat.getColor(getContext(), color));
    }

    /**
     * Sets arrow color.
     *
     * @param color the color
     */
    public void setArrowColor(@ColorInt int color) {
        leftImageView.setImageTintList(ColorStateList.valueOf(color));
        rightImageView.setImageTintList(ColorStateList.valueOf(color));
    }

    /**
     * Sets state.
     *
     * @param state the state
     */
    public void setState(State state) {
        switch (state) {
            case expand_down: {
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down));
                break;
            }
            case expand_up: {
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down_up));
                break;
            }
            case expand_right: {
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_down_right));
                break;
            }
            case none: {
                rightImageView.setVisibility(GONE);
                break;
            }
        }
    }


    /**
     * Returns state.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets corners radius.
     *
     * @param radius the radius
     */
    public void setCornersRadius(float radius) {
        GradientDrawable drawable = backgroundDrawable;
        drawable.setCornerRadii(new float[]{radius, radius, radius, radius, 0, 0, 0, 0});
        containerView.setBackground(drawable);
    }

    /**
     * Sets sides.
     *
     * @param sides the sides
     */
    public void setSides(Boolean sides) {
        if (sides) {
            rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_down_right));
            leftImageView.setVisibility(View.VISIBLE);
            leftDivider.setVisibility(View.VISIBLE);
            title.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        } else {
            title.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            leftImageView.setVisibility(View.GONE);
            leftDivider.setVisibility(View.GONE);
        }
    }

    public void setDividerColor(@ColorInt int color) {
        leftDivider.setBackgroundColor(color);
        rightDivider.setBackgroundColor(color);
    }

    private void initViews(View view) {
        containerView = view.findViewById(R.id.containerView);
        title = view.findViewById(R.id.title);
        leftImageView = view.findViewById(R.id.leftImageView);
        rightImageView = view.findViewById(R.id.rightImageView);
        leftDivider = view.findViewById(R.id.leftDivider);
        rightDivider = view.findViewById(R.id.rightDivider);
    }

    /**
     * Sets visible right divider.
     *
     * @param visible the visible
     */
    public void setVisibleRightDivider(Boolean visible) {
        if (visible)
            rightDivider.setVisibility(View.VISIBLE);
        else
            rightDivider.setVisibility(View.GONE);
    }

    /**
     * Sets visible left divider.
     *
     * @param visible the visible
     */
    public void setVisibleLeftDivider(Boolean visible) {
        if (visible)
            leftDivider.setVisibility(View.VISIBLE);
        else
            leftDivider.setVisibility(View.GONE);
    }

    /**
     * Sets visible all divider.
     *
     * @param visible the visible
     */
    public void setVisibleAllDivider(Boolean visible) {
        if (visible) {
            rightDivider.setVisibility(View.VISIBLE);
            leftDivider.setVisibility(View.VISIBLE);
        } else {
            rightDivider.setVisibility(View.GONE);
            leftDivider.setVisibility(View.GONE);
        }
    }

    /**
     * Sets sides click listener.
     *
     * @param listener the listener
     */
    public void setSidesClickListener(SidesClickListener listener) {
        sidesClickListener = listener;
        leftImageView.setOnClickListener(v -> sidesClickListener.onCLickLeft());
        rightImageView.setOnClickListener(v -> sidesClickListener.onClickRight());
    }

    /**
     * Sets arrow click listener.
     *
     * @param listener the arrow click listener
     */
    public void setArrowClickListener(final ArrowClickListener listener) {
        arrowClickListener = listener;
        rightImageView.setOnClickListener(v -> {
            if (state == State.expand_down) {
                state = State.expand_up;
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down_up));
                arrowClickListener.onClick(true);
            } else if (state == State.expand_up) {
                state = State.expand_down;
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down));
                arrowClickListener.onClick(false);
            }
        });
    }

    public void setActive(boolean isActive) {
        rightImageView.setImageDrawable(
                isActive
                        ? ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down_up)
                        : ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down)
        );
    }

    /**
     * Sets item click listener.
     *
     * @param itemClickListener the item click listener
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        containerView.setOnClickListener(v -> {
            rightImageView.setBackground(null);
            leftImageView.setBackground(null);
            itemClickListener.onClick(true, getId());
        });
    }

    public void callOnCLick() {
        containerView.callOnClick();
    }

    /**
     * Sets right arrow drawable.
     *
     * @param drawable the drawable
     */
    public void setRightArrowDrawable(@DrawableRes int drawable) {
        rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets left arrow drawable.
     *
     * @param drawable the drawable
     */
    public void setLeftArrowDrawable(@DrawableRes int drawable) {
        leftImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Call this view's ItemClickListener, if it is defined.
     */
    public void performItemSelection() {
        if (itemClickListener != null && containerView != null) {
            containerView.setOnClickListener(v -> {

            });
        }
    }

    /**
     * Call this view's ItemClickListener, if it is defined.
     */
    public void performArrowSelection() {
        if (arrowClickListener != null && containerView != null) {
            rightImageView.performClick();
        }
    }

    public interface SidesClickListener {
        void onCLickLeft();

        void onClickRight();
    }

    public interface ArrowClickListener {
        void onClick(Boolean isOpened);
    }

    public interface ItemClickListener {
        void onClick(Boolean isSelected, int id);
    }
}
