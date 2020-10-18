package app.developer.uiview.progress_bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import app.developer.uiview.R;

public class RoundBackground extends View {

    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    @ColorInt
    private int colorBackground, startShadowColor, endShadowColor;

    /**
     * Instantiates a new Round background.
     *
     * @param context the context
     */
    public RoundBackground(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Round background.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public RoundBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Round background.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public RoundBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * Instantiates a new Round background.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    public RoundBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundBackground, 0, 0);

        colorBackground = typedArray.getColor(R.styleable.RoundBackground_colorProgressBackground, getResources().getColor(R.color.colorBlack, null));
        startShadowColor = typedArray.getColor(R.styleable.RoundBackground_startShadowColor, Color.parseColor("#327527"));
        endShadowColor = typedArray.getColor(R.styleable.RoundBackground_endShadowColor, Color.TRANSPARENT);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = w /  2;
        mCenterY = h / 2;
        mRadius = Math.min(mCenterX, mCenterY);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentHeight, parentHeight);
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new RadialGradient(mCenterX, mCenterY, mRadius,
                new int[]{colorBackground, startShadowColor, endShadowColor}, new float[]{0.65f, 0.71f, 1.0f}, Shader.TileMode.CLAMP));
        canvas.drawCircle(mCenterX, mCenterY, mRadius, paint);
        super.onDraw(canvas);
    }

    /**
     * Sets colors.
     *
     * @param startColor the gradient start color
     * @param endColor   the dradient end color
     */
    public void setColors(@ColorInt int startColor, @ColorInt int endColor) {
        startShadowColor = startColor;
        endShadowColor = endColor;
    }

    /**
     * Sets color background.
     *
     * @param backgroundColor the background color
     */
    public void setColorBackground(@ColorInt int backgroundColor) {
        colorBackground = backgroundColor;
    }
}
