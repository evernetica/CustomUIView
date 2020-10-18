package app.developer.uiview.incremental_input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

public class InnerShadowView extends View {

    @Nullable
    private Integer shadow, background;

    /**
     * Instantiates a new Inner shadow view.
     *
     * @param context the context
     */
    public InnerShadowView(Context context) {
        super(context);
    }

    /**
     * Instantiates a new Inner shadow view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public InnerShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Instantiates a new Inner shadow view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public InnerShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Instantiates a new Inner shadow view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    public InnerShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        int radius = 20;
        int padding = 2;
        int bleed = 20;
        RectF frame = new RectF(padding, padding, getWidth(), getHeight());
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (shadow != null)
            mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), shadow, Color.WHITE, Shader.TileMode.MIRROR));
        else
            mPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));
        canvas.drawRoundRect(frame, radius, radius, mPaint);
        Bitmap mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        if (background != null)
            mBitmap.eraseColor(background);
        else
            mBitmap.eraseColor(Color.parseColor("#DEDEDE"));
        Shader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF000000);
        mPaint.setMaskFilter(new BlurMaskFilter(bleed, BlurMaskFilter.Blur.INNER));
        mPaint.setShader(bitmapShader);
        canvas.drawRoundRect(frame, radius, radius, mPaint);
    }

    /**
     * Sets background.
     *
     * @param shadow     the shadow
     * @param background the background
     */
    public void setBackground(@ColorInt int shadow, @ColorInt int background) {
        this.shadow = shadow;
        this.background = background;
    }
}
