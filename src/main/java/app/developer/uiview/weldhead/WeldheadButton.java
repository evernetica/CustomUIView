package app.developer.uiview.weldhead;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;


/**
 * The type Weldhead button.
 */
public class WeldheadButton extends LinearLayoutCompat {

    private AppCompatButton button;
    private float radius = 0;
    private Type type;
    private OnButtonClickListener listener;
    private int[] normalShadow = new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#1A121212")};
    private int[] normalBackground = new int[]{Color.parseColor("#6D6D6D"), Color.parseColor("#454545")};
    private int[] activeShadow = new int[]{Color.parseColor("#012300"), Color.parseColor("#77BC1F")};
    private int[] activeBackground = new int[]{Color.parseColor("#77BC1F"), Color.parseColor("#1A012300")};
    private int[] disableShadow = new int[]{Color.parseColor("#454545"), Color.parseColor("#1A121212")};
    private int[] disableBackground = new int[]{Color.parseColor("#454545"), Color.parseColor("#272727")};
    private int[] stopShadow = new int[]{Color.parseColor("#230000"), Color.parseColor("#BC1F1F")};
    private int[] stopBackground = new int[]{Color.parseColor("#BC1F1F"), Color.parseColor("#121212")};

    /**
     * Instantiates a new Weldhead button.
     *
     * @param context the context
     */
    public WeldheadButton(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Weldhead button.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public WeldheadButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Weldhead button.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public WeldheadButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    @SuppressLint("ClickableViewAccessibility")
    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_weldhead_button, this, true);
        button = view.findViewById(R.id.frame);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WeldheadButton, 0, 0);
        radius = typedArray.getFloat(R.styleable.WeldheadButton_radiusCorners, 0);
        button.requestLayout();

        type = Type.values()[typedArray.getInt(R.styleable.WeldheadButton_type, 0)];
        switch (type) {
            case normal:
                button.setBackground(getLayerDrawable(
                        Type.normal,
                        normalShadow,
                        normalBackground,
                        radius));
                viewWithoutDivider(typedArray, false);
                break;
            case active:
                button.setBackgroundDrawable(getLayerDrawable(
                        Type.active,
                        activeShadow,
                        activeBackground,
                        radius));
                viewWithoutDivider(typedArray, false);
                break;
            case disable:
                button.setBackground(getLayerDrawable(
                        Type.disable,
                        disableShadow,
                        disableBackground,
                        radius));
                viewWithoutDivider(typedArray, true);
                break;
            case stop:
                button.setBackground(getLayerDrawable(
                        Type.stop,
                        stopShadow,
                        stopBackground,
                        radius));
                viewWithoutDivider(typedArray, false);
                break;
        }
        button.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                button.setAlpha(0.85f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                button.setAlpha(1.00f);
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                assert listener != null;
                listener.onButtonClicked();
            }
            return true;
        });

        typedArray.recycle();
    }

    private LayerDrawable getLayerDrawable(Type type,
                                           int[] shadowColors,
                                           int[] backgroundColor,
                                           float cornerRadius) {
        GradientDrawable shadow;
        GradientDrawable backColor;
        Drawable[] layers = new Drawable[2];
        if (!type.name().equals(Type.disable.name()) && !type.name().equals(Type.normal.name())) {
            backColor = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, backgroundColor);
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            layers[0] = backColor;
            layers[1] = shadow;
        } else {
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            backColor = new GradientDrawable();
            backColor.setColors(backgroundColor);
            backColor.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            backColor.setGradientRadius(100f);
            layers[1] = backColor;
            layers[0] = shadow;
        }
        shadow.setCornerRadius(cornerRadius);
        backColor.setCornerRadius(cornerRadius);

        LayerDrawable layerList = new LayerDrawable(layers);
        layerList.setLayerInset(0, 0, 0, 0, 0);
        layerList.setLayerInset(1, 0, convertSpToPixels(getContext(), 3), 0, convertSpToPixels(getContext(), 4));
        return layerList;
    }

    /**
     * Gets layer drawable.
     *
     * @param shadowColors    the shadow colors
     * @param backgroundColor the background color
     * @param cornerRadius    the corner radius
     */
    public void getLayerDrawable(int[] shadowColors,
                                 int[] backgroundColor,
                                 float cornerRadius) {
        GradientDrawable shadow;
        GradientDrawable backColor;
        Drawable[] layers = new Drawable[2];
        if (!type.name().equals(Type.disable.name()) && !type.name().equals(Type.normal.name())) {
            backColor = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, backgroundColor);
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            layers[0] = backColor;
            layers[1] = shadow;
        } else {
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            backColor = new GradientDrawable();
            backColor.setColors(backgroundColor);
            backColor.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            backColor.setGradientRadius(100f);
            layers[1] = backColor;
            layers[0] = shadow;
        }
        shadow.setCornerRadius(cornerRadius);
        backColor.setCornerRadius(cornerRadius);

        LayerDrawable layerList = new LayerDrawable(layers);
        layerList.setLayerInset(0, 0, 0, 0, 0);
        layerList.setLayerInset(1, 0, convertSpToPixels(getContext(), 3), 0, convertSpToPixels(getContext(), 4));
        button.setBackground(layerList);
    }

    /**
     * Sets on click listener.
     *
     * @param listener the listener
     */
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    /**
     * Gets drawable.
     *
     * @param drawable the drawable
     */
    public void getDrawable(@DrawableRes int drawable) {
        button.setBackground(ContextCompat.getDrawable(getContext(), drawable));
    }

    /**
     * Sets corners radius.
     *
     * @param cornersRadius the corners radius
     */
    public void setCornersRadius(float cornersRadius) {
        GradientDrawable shadow;
        GradientDrawable backColor;
        Drawable[] layers = new Drawable[2];
        if (type.name().equals(Type.disable.name())) {
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, disableShadow);
            backColor = new GradientDrawable();
            backColor.setColors(disableBackground);
            backColor.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            backColor.setGradientRadius(100f);
            layers[1] = backColor;
            layers[0] = shadow;
        } else if (!type.name().equals(Type.normal.name())) {
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, normalShadow);
            backColor = new GradientDrawable();
            backColor.setColors(normalBackground);
            backColor.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            backColor.setGradientRadius(100f);
            layers[1] = backColor;
            layers[0] = shadow;
        } else {
            backColor = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, activeBackground);
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, activeShadow);
            layers[0] = backColor;
            layers[1] = shadow;
        }
        shadow.setCornerRadius(cornersRadius);
        backColor.setCornerRadius(cornersRadius);

        LayerDrawable layerList = new LayerDrawable(layers);
        layerList.setLayerInset(0, 0, 0, 0, 0);
        layerList.setLayerInset(1, 0, convertSpToPixels(getContext(), 3), 0, convertSpToPixels(getContext(), 4));
        button.setBackground(layerList);
    }

    /**
     * Sets type button.
     *
     * @param type the type
     */
    public void setTypeView(Type type) {
        switch (type) {
            case normal:
                button.setEnabled(true);
                button.setBackground(getLayerDrawable(
                        Type.normal,
                        new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#1A121212")},
                        new int[]{Color.parseColor("#6D6D6D"), Color.parseColor("#454545")},
                        radius));
                button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                break;
            case active:
                button.setEnabled(true);
                button.setBackgroundDrawable(getLayerDrawable(
                        Type.active,
                        new int[]{Color.parseColor("#012300"), Color.parseColor("#77BC1F")},
                        new int[]{Color.parseColor("#77BC1F"), Color.parseColor("#1A012300")},
                        radius));
                button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhite));

                break;
            case disable:
                button.setEnabled(false);
                button.setBackground(getLayerDrawable(
                        Type.disable,
                        new int[]{Color.parseColor("#454545"), Color.parseColor("#1A121212")},
                        new int[]{Color.parseColor("#454545"), Color.parseColor("#272727")},
                        radius));
                button.setTextColor(ContextCompat.getColor(getContext(), R.color.colorLightGrey));
                break;
        }
    }

    /**
     * Sets text without divider.
     *
     * @param text the text
     */
    public void setText(String text) {
        button.setText(text);
    }

    /**
     * Sets text color without divider view.
     *
     * @param color the color
     */
    public void setTextColorView(@ColorRes int color) {
        button.setTextColor(getResources().getColor(color, null));
    }

    private void viewWithoutDivider(@Nullable TypedArray typedArray, boolean disabled) {
        if (typedArray != null) {
            button.setText(typedArray.getString(R.styleable.WeldheadButton_android_text));
            if (typedArray.getFont(R.styleable.WeldheadButton_android_fontFamily) != null)
                button.setTypeface(typedArray.getFont(R.styleable.WeldheadButton_android_fontFamily));
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.WeldheadButton_android_textSize, convertSpToPixels(getContext(), 21)));
            if (disabled)
                button.setTextColor(typedArray.getColor(R.styleable.WeldheadButton_disableTextColor, Color.parseColor("#6D6D6D")));
            else
                button.setTextColor(typedArray.getColor(R.styleable.WeldheadButton_android_textColor, Color.WHITE));
        }
    }

    private int convertSpToPixels(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }
}