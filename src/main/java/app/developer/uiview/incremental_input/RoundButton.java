package app.developer.uiview.incremental_input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import app.developer.uiview.R;

/**
 * The type Round button.
 */
public class RoundButton extends FrameLayout {

    private AppCompatImageView button, image;
    private FrameLayout container;
    private OnClickListener listener;

    /**
     * Instantiates a new Round button.
     *
     * @param context the context
     */
    public RoundButton(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Round button.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public RoundButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Round button.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public RoundButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Instantiates a new Round button.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    public RoundButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_rounded_button, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundButton, 0, 0);
        button = view.findViewById(R.id.button);
        container = view.findViewById(R.id.container);
        image = view.findViewById(R.id.image);
        button.setLayoutParams(new LayoutParams(
                typedArray.getDimensionPixelSize(R.styleable.RoundButton_android_layout_width, 96),
                typedArray.getDimensionPixelSize(R.styleable.RoundButton_android_layout_height, 96)
        ));
        image.setImageDrawable(typedArray.getDrawable(R.styleable.RoundButton_image));
        setLayerDrawable(GradientDrawable.RADIAL_GRADIENT,
                new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#8E8E8E")},
                new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#121212")});

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick();
                }
            }
        });

        typedArray.recycle();
    }

    /**
     * Sets image.
     *
     * @param drawable the drawable
     */
    public void setImage(Drawable drawable) {
        image.setImageDrawable(drawable);
    }

    /**
     * Sets layer drawable.
     *
     * @param typeGradient    the type gradient
     * @param shadowColors    the shadow colors
     * @param backgroundColor the background color
     */
    public void setLayerDrawable(int typeGradient, int[] shadowColors, int[] backgroundColor) {
        GradientDrawable shadow;
        GradientDrawable backColor;
        Drawable[] layers = new Drawable[2];
        if (typeGradient == GradientDrawable.LINEAR_GRADIENT) {
            backColor = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, backgroundColor);
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            shadow.setShape(GradientDrawable.OVAL);
            backColor.setShape(GradientDrawable.OVAL);
            layers[0] = backColor;
            layers[1] = shadow;
        } else {
            shadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, shadowColors);
            backColor = new GradientDrawable();
            backColor.setColors(backgroundColor);
            backColor.setGradientType(GradientDrawable.RADIAL_GRADIENT);
            backColor.setGradientRadius(150f);
            shadow.setShape(GradientDrawable.OVAL);
            backColor.setShape(GradientDrawable.OVAL);
            layers[1] = backColor;
            layers[0] = shadow;
        }
        LayerDrawable layerList = new LayerDrawable(layers);
        layerList.setLayerInset(0, 0, 0, 0, 0);
        layerList.setLayerInset(1, 0, 3, 0, 0);
        container.setBackground(layerList);
        container.requestLayout();
    }

    /**
     * Sets on click listener.
     *
     * @param listener the OnClickListener
     */
    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Sets tint color.
     *
     * @param color the color
     */
    public void setTintColor(int color) {
        image.setImageTintList(ColorStateList.valueOf(color));
    }

    interface OnClickListener {
        void onClick();
    }

}
