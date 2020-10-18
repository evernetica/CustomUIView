package app.developer.uiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class SectionTitleView extends AppCompatTextView {

    private int radius = 0;
    @Nullable
    private Integer backgroundColor, borderColor;
    private int topPadding = 0;

    public SectionTitleView(Context context) {
        super(context);
        init(context, null);
    }

    public SectionTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SectionTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        this.setMinWidth(384);
        this.setMinHeight(76);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SectionTitleView, 0, 0);
        this.setText(typedArray.getString(R.styleable.SectionTitleView_android_text));
        radius = typedArray.getInt(R.styleable.SectionTitleView_radiusCorners, 15);
        backgroundColor = typedArray.getColor(R.styleable.SectionTitleView_backgroundColor, Color.TRANSPARENT);
        borderColor = typedArray.getColor(R.styleable.SectionTitleView_topBorderColor, Color.TRANSPARENT);
        topPadding = typedArray.getDimensionPixelSize(R.styleable.SectionTitleView_topBorderWidth, 2);
        typedArray.recycle();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (backgroundColor != null) {
            mPaint.setColor(backgroundColor);
        }
        RectF frame, frameLine;
        int height = getHeight();
        int width = getWidth();
        frame = new RectF(0, topPadding, width, height);
        frameLine = new RectF(0, 0, getWidth(), getHeight());


        float[] corners = new float[]{
                radius, radius,
                radius, radius,
                0, 0,
                0, 0
        };


        final Path pathLine = new Path();
        Paint paint = new Paint();
        if (borderColor != null) {
            paint.setColor(borderColor);
        }
        pathLine.addRoundRect(frameLine, corners, Path.Direction.CCW);
        canvas.drawPath(pathLine, paint);

        final Path path = new Path();
        path.addRoundRect(frame, corners, Path.Direction.CW);
        canvas.drawPath(path, mPaint);

        super.onDraw(canvas);
    }
}
