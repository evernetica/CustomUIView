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
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import app.developer.uiview.R;

public class MultilineWeldheadButton extends FrameLayout {

    private LinearLayoutCompat button;
    private Type type = Type.normal;
    private AppCompatTextView firstLine, secondLine;
    private View divider;
    private OnButtonClickListener listener;
    private float radius = 0;

    private int[] normalShadow = new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#1A121212")};
    private int[] normalBackground = new int[]{Color.parseColor("#6D6D6D"), Color.parseColor("#454545")};
    private int[] activeShadow = new int[]{Color.parseColor("#012300"), Color.parseColor("#77BC1F")};
    private int[] activeBackground = new int[]{Color.parseColor("#77BC1F"), Color.parseColor("#1A012300")};
    private int[] disableShadow = new int[]{Color.parseColor("#454545"), Color.parseColor("#1A121212")};
    private int[] disableBackground = new int[]{Color.parseColor("#454545"), Color.parseColor("#272727")};
    private int[] stopShadow = new int[]{Color.parseColor("#230000"), Color.parseColor("#BC1F1F")};
    private int[] stopBackground = new int[]{Color.parseColor("#BC1F1F"), Color.parseColor("#121212")};

    public MultilineWeldheadButton(Context context) {
        super(context);
        init(context, null);
    }

    public MultilineWeldheadButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultilineWeldheadButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MultilineWeldheadButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        LayoutInflater  inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_multiline_weldhead_button, this, true);
        initView(view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultilineWeldheadButton, 0, 0);
        button.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                button.setAlpha(0.50f);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                button.setAlpha(1.00f);
            }
            return true;
        });
        radius = typedArray.getFloat(R.styleable.MultilineWeldheadButton_radiusCorners, 0);
        type = Type.values()[typedArray.getInt(R.styleable.MultilineWeldheadButton_type, 0)];
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
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setAlpha(0.85f);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    button.setAlpha(1.00f);
                }
                assert listener != null;
                listener.onButtonClicked();
                return true;
            }
        });
        typedArray.recycle();
    }

    private void initView(View view) {
        button = view.findViewById(R.id.frame);
        firstLine = view.findViewById(R.id.firstLine);
        secondLine = view.findViewById(R.id.secondLine);
        divider = view.findViewById(R.id.divider);
    }

    private void viewWithoutDivider(@Nullable TypedArray typedArray, boolean disabled) {
        assert typedArray != null;
        firstLine.setText(typedArray.getString(R.styleable.MultilineWeldheadButton_firstLineText));
        secondLine.setText(typedArray.getString(R.styleable.MultilineWeldheadButton_secondLineText));
        if (type.name().equals(Type.disable.name())) {
            firstLine.setTextColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_firstLineTextColor, Color.parseColor("#6D6D6D")));
            secondLine.setTextColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_secondLineTextColor, Color.parseColor("#6D6D6D")));
            divider.setBackgroundColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_dividerColor, Color.parseColor("#6D6D6D")));
        } else {
            firstLine.setTextColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_firstLineTextColor, Color.WHITE));
            secondLine.setTextColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_secondLineTextColor, Color.WHITE));
            divider.setBackgroundColor(typedArray.getColor(R.styleable.MultilineWeldheadButton_dividerColor, Color.WHITE));
        }
        if (typedArray.getDimensionPixelSize(R.styleable.MultilineWeldheadButton_firstLineTextSize, 0) != 0)
            firstLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.MultilineWeldheadButton_firstLineTextSize, 0));
        if (typedArray.getDimensionPixelSize(R.styleable.MultilineWeldheadButton_secondLineTextSize, 0) != 0)
            secondLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.MultilineWeldheadButton_secondLineTextSize, 0));
        if (typedArray.getFont(R.styleable.MultilineWeldheadButton_firstLineFontFamily) != null)
            firstLine.setTypeface(typedArray.getFont(R.styleable.MultilineWeldheadButton_firstLineFontFamily));
        if (typedArray.getFont(R.styleable.MultilineWeldheadButton_secondLineFontFamily) != null)
            secondLine.setTypeface(typedArray.getFont(R.styleable.MultilineWeldheadButton_secondLineFontFamily));
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
                break;
            case active:
                button.setEnabled(true);
                button.setBackgroundDrawable(getLayerDrawable(
                        Type.active,
                        new int[]{Color.parseColor("#012300"), Color.parseColor("#77BC1F")},
                        new int[]{Color.parseColor("#77BC1F"), Color.parseColor("#1A012300")},
                        radius));
                break;
            case disable:
                button.setEnabled(false);
                button.setBackground(getLayerDrawable(
                        Type.disable,
                        new int[]{Color.parseColor("#454545"), Color.parseColor("#1A121212")},
                        new int[]{Color.parseColor("#454545"), Color.parseColor("#272727")},
                        radius));
                break;
        }
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
     * Sets on click listener.
     *
     * @param listener the listener
     */
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    /**
     * Sets first line text.
     *
     * @param text the text
     */
    public void setFirstLineText(String text) {
        firstLine.setText(text);
    }

    /**
     * Sets second line text.
     *
     * @param text the text
     */
    public void setSecondLineText(String text) {
        secondLine.setText(text);
    }

    /**
     * Sets first text color.
     *
     * @param color the color
     */
    public void setFirstTextColor(@ColorInt int color) {
        firstLine.setTextColor(color);
    }

    /**
     * Sets second text color.
     *
     * @param color the color
     */
    public void setSecondTextColor(@ColorInt int color) {
        secondLine.setTextColor(color);
    }

    /**
     * Sets first text font.
     *
     * @param font the font
     */
    public void setFirstTextFont(@FontRes int font) {
        firstLine.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets second text font.
     *
     * @param font the font
     */
    public void setSecondTextFont(@FontRes int font) {
        secondLine.setTypeface(getResources().getFont(font));
    }

    private int convertSpToPixels(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }
}
