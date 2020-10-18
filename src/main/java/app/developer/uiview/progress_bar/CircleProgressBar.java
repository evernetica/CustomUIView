package app.developer.uiview.progress_bar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import app.developer.uiview.R;

/**
 * The type Circle progress bar.
 */
public class CircleProgressBar extends View {
    private static final int DEFAULT_MAX = 100;
    private static final float MAX_DEGREE = 360.0f;
    private static final float LINEAR_START_DEGREE = 90.0f;

    /**
     * The constant LINE.
     */
    public static final int LINE = 0;
    /**
     * The constant SOLID.
     */
    public static final int SOLID = 1;
    /**
     * The constant SOLID_LINE.
     */
    public static final int SOLID_LINE = 2;
    /**
     * The constant TEXT.
     */
    public static final int TEXT = 3;
    /**
     * The constant SINGLE_LINE.
     */
    public static final int SINGLE_LINE = 4;

    private static final int LINEAR = 0;
    private static final int RADIAL = 1;
    private static final int SWEEP = 2;

    private static final int DEFAULT_START_DEGREE = -90;

    private static final int DEFAULT_LINE_COUNT = 45;

    private static final float DEFAULT_LINE_WIDTH = 4.0f;
    private static final float DEFAULT_PROGRESS_TEXT_SIZE = 11.0f;
    private static final float DEFAULT_PROGRESS_STROKE_WIDTH = 1.0f;

    private static final String COLOR_FFF2A670 = "#fff2a670";
    private static final String COLOR_FFD3D3D5 = "#454545";

    private final RectF mProgressRectF = new RectF();

    private final Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mProgressBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Paint mProgressTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private float mRadius;
    private float mCenterX;
    private float mCenterY;

    private int mProgress;
    private int mMax = DEFAULT_MAX;

    private int mLineCount;
    private float mLineWidth;

    private float mProgressStrokeWidth;

    private float mProgressTextSize;

    private int mProgressStartColor;
    private int mProgressEndColor;
    private int mProgressTextColor;
    private int mProgressBackgroundColor;

    private int mStartDegree;

    private int textSize = 0;
    private Typeface typeface = getContext().getResources().getFont(R.font.rajdhani_semibold);
    private int color = Color.WHITE;

    @Nullable
    private static ArrayList<DegreeIcon> active = null;
    @Nullable
    private static ArrayList<DegreeIcon> inactive = null;

    private boolean mDrawBackgroundOutsideProgress;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINE, SOLID, SOLID_LINE, TEXT, SINGLE_LINE})
    private @interface Style {
    }

    @Style
    private int mStyle;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LINEAR, RADIAL, SWEEP})
    private @interface ShaderMode {
    }

    @ShaderMode
    private int mShader;
    private Paint.Cap mCap;
    private Canvas canvas;

    /**
     * Instantiates a new Circle progress bar.
     *
     * @param context the context
     */
    public CircleProgressBar(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Circle progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFromAttributes(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentHeight, parentHeight);
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }

    /**
     * Basic data initialization
     */
    @SuppressWarnings("ResourceType")
    private void initFromAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        mLineCount = a.getInt(R.styleable.CircleProgressBar_line_count, DEFAULT_LINE_COUNT);

        mStyle = a.getInt(R.styleable.CircleProgressBar_style, LINE);
        mShader = a.getInt(R.styleable.CircleProgressBar_progress_shader, LINEAR);
        mCap = a.hasValue(R.styleable.CircleProgressBar_progress_stroke_cap) ?
                Paint.Cap.values()[a.getInt(R.styleable.CircleProgressBar_progress_stroke_cap, 0)] : Paint.Cap.BUTT;

        mLineWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_line_width, UnitUtils.dip2px(getContext(), DEFAULT_LINE_WIDTH));
        mProgressTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_text_size, UnitUtils.dip2px(getContext(), DEFAULT_PROGRESS_TEXT_SIZE));
        mProgressStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progress_stroke_width, UnitUtils.dip2px(getContext(), DEFAULT_PROGRESS_STROKE_WIDTH));

        mProgressStartColor = a.getColor(R.styleable.CircleProgressBar_progress_start_color, Color.parseColor(COLOR_FFF2A670));
        mProgressEndColor = a.getColor(R.styleable.CircleProgressBar_progress_end_color, Color.parseColor(COLOR_FFF2A670));
        mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_progress_text_color, Color.parseColor(COLOR_FFF2A670));
        mProgressBackgroundColor = a.getColor(R.styleable.CircleProgressBar_progress_background_color, Color.parseColor(COLOR_FFD3D3D5));

        mStartDegree = a.getInt(R.styleable.CircleProgressBar_progress_start_degree, DEFAULT_START_DEGREE);
        mDrawBackgroundOutsideProgress = a.getBoolean(R.styleable.CircleProgressBar_drawBackgroundOutsideProgress, false);

        a.recycle();
    }

    /**
     * Paint initialization
     */
    private void initPaint() {
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setColor(mProgressStartColor);
        mProgressPaint.setStrokeCap(mCap);

        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressBackgroundPaint.setColor(mProgressBackgroundColor);
        mProgressBackgroundPaint.setStrokeCap(mCap);
    }

    /**
     * The progress bar color gradient,
     * need to be invoked in the {@link #onSizeChanged(int, int, int, int)}
     */
    private void updateProgressShader() {
        if (mProgressStartColor != mProgressEndColor) {
            Shader shader = null;
            switch (mShader) {
                case LINEAR: {
                    if (mStyle != 3) {
                        shader = new LinearGradient(mProgressRectF.left, mProgressRectF.top,
                                mProgressRectF.left, mProgressRectF.bottom,
                                mProgressStartColor, mProgressStartColor, Shader.TileMode.CLAMP);
                        Matrix matrix = new Matrix();
                        matrix.setRotate(LINEAR_START_DEGREE, mCenterX, mCenterY);
                        shader.setLocalMatrix(matrix);
                    } else {
                        Log.d("", "");
                    }
                    break;
                }
                case RADIAL: {
                    shader = new RadialGradient(mCenterX, mCenterY, mRadius,
                            mProgressStartColor, mProgressEndColor, Shader.TileMode.CLAMP);
                    break;
                }
                case SWEEP: {
                    //arc = radian * radius
                    float radian = (float) (mProgressStrokeWidth / Math.PI * 2.0f / mRadius);
                    float rotateDegrees = (float) (
                            -(mCap == Paint.Cap.BUTT && mStyle == SOLID_LINE ? 0 : Math.toDegrees(radian)));

                    shader = new SweepGradient(mCenterX, mCenterY, new int[]{mProgressStartColor, mProgressEndColor},
                            new float[]{0.0f, 1.0f});
                    Matrix matrix = new Matrix();
                    matrix.setRotate(rotateDegrees, mCenterX, mCenterY);
                    shader.setLocalMatrix(matrix);
                    break;
                }
                default:
                    break;
            }

            mProgressPaint.setShader(shader);
        } else {
            if (mStyle != 3) {
                mProgressPaint.setShader(null);
                mProgressPaint.setColor(mProgressStartColor);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.rotate(mStartDegree, mCenterX, mCenterY);
        drawProgress(canvas);
        canvas.restore();
    }

    private void drawProgress(Canvas canvas) {
        this.canvas = canvas;

        switch (mStyle) {
            case SOLID:
                Log.d("TAG", "onDraw drawSolidProgress");
                drawSolidProgress(canvas);
                break;
            case SOLID_LINE:
                Log.d("TAG", "onDraw drawSolidLineProgress");
                drawSolidLineProgress(canvas, active, inactive);
                break;
            case TEXT:
                drawTextProgress(canvas);
            case LINE:
                drawLineProgress(canvas);
            case SINGLE_LINE:
                drawSingleLineProgress(canvas);
                break;
            default:
                break;
        }
    }

    private void drawSingleLineProgress(Canvas canvas) {
        float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
        float outerCircleRadius = mRadius;
        float interCircleRadius = mRadius - mLineWidth;

        int progressLineCount = (int) ((float) mProgress / (float) mMax * mLineCount);

        for (int i = 0; i < mLineCount; i++) {
            float rotateDegrees = i * -unitDegrees;

            float startX = mCenterX + (float) Math.cos(rotateDegrees) * interCircleRadius;
            float startY = mCenterY - (float) Math.sin(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.cos(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterY - (float) Math.sin(rotateDegrees) * outerCircleRadius;

            if (mDrawBackgroundOutsideProgress) {
                if (i >= progressLineCount) {
                    canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
                }
            } else {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
            }
            if (mStyle == 4) {
                if (i == progressLineCount) {

                    mProgressPaint.setColor(Color.RED);
                    canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint);
                }
            } else {
                if (i < progressLineCount) {
                    canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint);
                }
            }
        }
        if (mStyle != 3) {
            Paint innerBarShadowPaint = new Paint();
            float[] stops = {0.92f, 0.96f, 1.0f};
            RadialGradient gradient = new RadialGradient(mCenterX, mCenterY, outerCircleRadius,
                    new int[]{Color.TRANSPARENT, Color.parseColor("#33000000"), Color.parseColor("#B7000000")}, stops, Shader.TileMode.CLAMP);
            innerBarShadowPaint.setShader(gradient);
            canvas.drawCircle(mCenterX, mCenterY, outerCircleRadius, innerBarShadowPaint);
        }
    }


    public void setIconToDegree(ArrayList<DegreeIcon> active, ArrayList<DegreeIcon> inactive) {
        CircleProgressBar.active = active;
        CircleProgressBar.inactive = inactive;
        postInvalidate();
    }

    private void drawTextProgress(Canvas canvas) {
        float outerCircleRadius = mRadius - (mLineWidth / 2);
        double slice = 2 * Math.PI / mLineCount;
        for (int i = 0; i < mLineCount; i++) {
            double angle = slice * i;
            int newX = (int) (mCenterX + outerCircleRadius * Math.cos(angle));
            int newY = (int) (mCenterY + outerCircleRadius * Math.sin(angle));
            Paint paint = new Paint();
            paint.setColor(color);
            if (textSize != 0) {
                paint.setTextSize(textSize);
            } else {
                paint.setTextSize(30f);
            }

            paint.setTypeface(typeface);
            canvas.save();
            canvas.rotate(-((float) (360 / mLineCount)) / 2, newX, newY);
            canvas.drawText(String.valueOf(i + 1), newX, newY, paint);
            canvas.restore();
        }
    }

    /**
     * In the center of the drawing area as a reference point , rotate the canvas
     */
    private void drawLineProgress(Canvas canvas) {

        float unitDegrees = (float) (2.0f * Math.PI / mLineCount);
        float outerCircleRadius = mRadius;
        float interCircleRadius = mRadius - mLineWidth;

        int progressLineCount = (int) ((float) mProgress / (float) mMax * mLineCount);

        for (int i = 0; i < mLineCount; i++) {
            float rotateDegrees = i * -unitDegrees;

            float startX = mCenterX + (float) Math.cos(rotateDegrees) * interCircleRadius;
            float startY = mCenterY - (float) Math.sin(rotateDegrees) * interCircleRadius;

            float stopX = mCenterX + (float) Math.cos(rotateDegrees) * outerCircleRadius;
            float stopY = mCenterY - (float) Math.sin(rotateDegrees) * outerCircleRadius;

            if (mDrawBackgroundOutsideProgress) {
                if (i >= progressLineCount) {
                    canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
                }
            } else {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint);
            }

            if (i < progressLineCount) {
                canvas.drawLine(startX, startY, stopX, stopY, mProgressPaint);
            }
        }
        if (mStyle != 3) {
            Paint innerBarShadowPaint = new Paint();
            float[] stops = {0.92f, 0.96f, 1.0f};
            RadialGradient gradient = new RadialGradient(mCenterX, mCenterY, outerCircleRadius,
                    new int[]{Color.TRANSPARENT, Color.parseColor("#33000000"), Color.parseColor("#B7000000")}, stops, Shader.TileMode.CLAMP);
            innerBarShadowPaint.setShader(gradient);
            canvas.drawCircle(mCenterX, mCenterY, outerCircleRadius, innerBarShadowPaint);
        }
    }

    private void drawSolidProgress(Canvas canvas) {
        if (mDrawBackgroundOutsideProgress) {
            float startAngle = MAX_DEGREE * mProgress / mMax;
            float sweepAngle = MAX_DEGREE - startAngle;

            canvas.drawArc(mProgressRectF, startAngle, sweepAngle, false, mProgressBackgroundPaint);
        } else {
            canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE, false, mProgressBackgroundPaint);
        }
        float[] stops = {0.85f, 1.0f};

        mProgressPaint.setShader(new RadialGradient(mCenterX, mCenterY, mRadius, new int[]{mProgressStartColor, mProgressEndColor}, stops, Shader.TileMode.CLAMP));
        canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE * mProgress / mMax, false, mProgressPaint);
    }

    private void drawSolidLineProgress(Canvas canvas, ArrayList<DegreeIcon> active, ArrayList<DegreeIcon> inactive) {
        if (this.getId() == R.id.axisActiveDrawable) {
            drawAxisDrawable(canvas, active, true);
        } else if (this.getId() == R.id.axisInactiveDrawable) {
            drawAxisDrawable(canvas, inactive, false);
        } else if (this.getId() == R.id.progressActiveBackgroundDrawable) {
            drawProgressWithDrawable(canvas, active, true);
        } else if (this.getId() == R.id.progressInactiveBackgroundDrawable) {
            drawProgressWithDrawable(canvas, inactive, false);
        } else {
            drawDefaultSolidLineProgress(canvas);
        }
    }

    private void drawProgressWithDrawable(Canvas canvas, ArrayList<DegreeIcon> degreeIcons, Boolean active) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        if (degreeIcons != null) {
            float outerCircleRadius = mRadius - (mProgressStrokeWidth / 2);
            for (int i = 0; i < degreeIcons.size(); i++) {
                DegreeIcon degreeIcon = degreeIcons.get(i);
                if(!degreeIcon.getLast()) {
                    canvas.save();
                    float angle = (float) -Math.toRadians(degreeIcon.getDegree());
                    Drawable icon;
                    if (i != degreeIcons.size() - 1 && active) {
                        icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_done_progress);
                        assert icon != null;
                    } else if (i == degreeIcons.size() - 1 && active) {
                        icon = ContextCompat.getDrawable(getContext(), degreeIcon.getDrawable());
                        assert icon != null;
                        icon.setTint(Color.RED);
                    } else {
                        icon = ContextCompat.getDrawable(getContext(), degreeIcon.getDrawable());
                        assert icon != null;
                        icon.setTint(Color.WHITE);
                    }

                    float x = (mCenterX + (float) Math.cos(angle) * outerCircleRadius);
                    float y = (mCenterY - (float) Math.sin(angle) * outerCircleRadius);
                    icon.setBounds(-Math.round(mProgressStrokeWidth) / 2, -Math.round(mProgressStrokeWidth) / 2, Math.round(mProgressStrokeWidth) / 2, Math.round(mProgressStrokeWidth) / 2);

                    canvas.translate(x, y);
                    icon.draw(canvas);

                    canvas.restore();
                }
            }
        }
    }

    private void drawDefaultSolidLineProgress(Canvas canvas) {
        if (mDrawBackgroundOutsideProgress) {
            float startAngle = MAX_DEGREE * mProgress / mMax;
            float sweepAngle = MAX_DEGREE - startAngle;
            canvas.drawArc(mProgressRectF, startAngle, sweepAngle, false, mProgressBackgroundPaint);
        } else {
            canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE, false, mProgressBackgroundPaint);
        }
        if (mProgressEndColor != Color.parseColor(COLOR_FFF2A670)) {
            float[] stops = {0.85f, 1.0f};
            mProgressPaint.setShader(new RadialGradient(mCenterX, mCenterY, mRadius, new int[]{mProgressStartColor, mProgressEndColor}, stops, Shader.TileMode.CLAMP));
        }
        canvas.drawArc(mProgressRectF, 0.0f, MAX_DEGREE * mProgress / mMax, false, mProgressPaint);
    }

    private void drawAxisDrawable(Canvas canvas, ArrayList<DegreeIcon> degreeIcons, Boolean active) {
        Paint paint = new Paint();

        if (degreeIcons != null) {
            for (int i = 0; i < degreeIcons.size(); i++) {
                DegreeIcon degreeIcon = degreeIcons.get(i);
                if (degreeIcon.getAxis() != null) {
                    if(!degreeIcon.getLast()) {
                        canvas.save();
                        float outerCircleRadius = mRadius - (int) (mLineWidth / 1.2);

                        float angle = (float) Math.toRadians(degreeIcon.getDegree());
                        float x = (int) (mCenterX + outerCircleRadius * Math.cos(angle));
                        float y = (int) (mCenterY + outerCircleRadius * Math.sin(angle));

                        if (active && i < degreeIcons.size() - 1) {
                            paint.setColor(getResources().getColor(R.color.colorGreen, null));
                        } else {
                            paint.setColor(Color.WHITE);
                        }

                        if (textSize != 0) {
                            paint.setTextSize(textSize);
                        } else {
                            paint.setTextSize(30f);
                        }
                        paint.setTypeface(typeface);
                        canvas.save();
                        canvas.drawText(degreeIcon.getAxis(), x, y, paint);
                        canvas.restore();
                    }
                }
            }
        }
    }

    /**
     * When the size of CircleProgressBar changed, need to re-adjust the drawing area
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;

        mRadius = Math.min(mCenterX, mCenterY);
        mProgressRectF.top = mCenterY - mRadius;
        mProgressRectF.bottom = mCenterY + mRadius;
        mProgressRectF.left = mCenterX - mRadius;
        mProgressRectF.right = mCenterX + mRadius;

        updateProgressShader();

        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
    }

    /**
     * Sets progress stroke width.
     *
     * @param progressStrokeWidth the progress stroke width
     */
    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.mProgressStrokeWidth = progressStrokeWidth;
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2);
        invalidate();
    }

    /**
     * Sets progress start color.
     *
     * @param progressStartColor the progress start color
     */
    public void setProgressStartColor(int progressStartColor) {
        this.mProgressStartColor = progressStartColor;
        updateProgressShader();
        invalidate();
    }

    /**
     * Sets progress end color.
     *
     * @param progressEndColor the progress end color
     */
    public void setProgressEndColor(int progressEndColor) {
        this.mProgressEndColor = progressEndColor;
        updateProgressShader();
        invalidate();
    }

    /**
     * Sets line count.
     *
     * @param lineCount the line count
     */
    public void setLineCount(int lineCount) {
        this.mLineCount = lineCount;
        invalidate();
    }

    /**
     * Sets text size.
     *
     * @param size the size axis
     */
    public void setTextSize(int size) {
        textSize = size;
        invalidate();
    }

    /**
     * Sets typeface.
     *
     * @param font the font axis
     */
    public void setTypeface(@FontRes int font) {
        typeface = getResources().getFont(font);
        invalidate();
    }

    /**
     * Sets text color.
     *
     * @param color the color axis
     */
    public void setTextColor(@ColorInt int color) {
        this.color = color;
        invalidate();
    }

    /**
     * Sets style axis.
     *
     * @param textSize  the text size
     * @param textFont  the text font
     * @param textColor the text color
     */
    public void setStyleAxis(@Nullable Integer textSize,
                             @Nullable Typeface textFont,
                             @Nullable @ColorInt Integer textColor) {
        if (textSize != null)
            this.textSize = textSize;
        if (textFont != null)
            this.typeface = textFont;
        if (textColor != null)
            this.color = textColor;
        invalidate();
    }

    /**
     * Sets line width.
     *
     * @param lineWidth the line width
     */
    public void setLineWidth(float lineWidth) {
        this.mLineWidth = lineWidth;
        invalidate();
    }

    /**
     * Sets style.
     *
     * @param style the style
     */
    public void setStyle(@Style int style) {
        this.mStyle = style;
        mProgressPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        mProgressBackgroundPaint.setStyle(mStyle == SOLID ? Paint.Style.FILL : Paint.Style.STROKE);
        invalidate();
    }

    /**
     * Sets shader.
     *
     * @param shader the shader
     */
    public void setShader(@ShaderMode int shader) {
        mShader = shader;
        updateProgressShader();
        invalidate();
    }

    /**
     * Sets cap.
     *
     * @param cap the cap
     */
    public void setCap(Paint.Cap cap) {
        mCap = cap;
        mProgressPaint.setStrokeCap(cap);
        mProgressBackgroundPaint.setStrokeCap(cap);
        invalidate();
    }

    /**
     * Sets progress.
     *
     * @param progress the progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    /**
     * Sets max.
     *
     * @param max the max
     */
    public void setMax(int max) {
        this.mMax = max;
        invalidate();
    }

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * Gets max.
     *
     * @return the max
     */
    public int getMax() {
        return mMax;
    }

    /**
     * Gets start degree.
     *
     * @return the start degree
     */
    public int getStartDegree() {
        return mStartDegree;
    }

    /**
     * Sets start degree.
     *
     * @param startDegree the start degree
     */
    public void setStartDegree(int startDegree) {
        this.mStartDegree = startDegree;
        invalidate();
    }

    /**
     * Is draw background outside progress boolean.
     *
     * @return the boolean
     */
    public boolean isDrawBackgroundOutsideProgress() {
        return mDrawBackgroundOutsideProgress;
    }

    /**
     * Sets draw background outside progress.
     *
     * @param drawBackgroundOutsideProgress the draw background outside progress
     */
    public void setDrawBackgroundOutsideProgress(boolean drawBackgroundOutsideProgress) {
        this.mDrawBackgroundOutsideProgress = drawBackgroundOutsideProgress;
        invalidate();
    }

    /**
     * The interface Progress formatter.
     */
    public interface ProgressFormatter {
        /**
         * Format char sequence.
         *
         * @param progress the progress
         * @param max      the max
         * @return the char sequence
         */
        CharSequence format(int progress, int max);
    }

    private static final class DefaultProgressFormatter implements ProgressFormatter {
        private static final String DEFAULT_PATTERN = "%d%%";

        @SuppressLint("DefaultLocale")
        @Override
        public CharSequence format(int progress, int max) {
            return String.format(DEFAULT_PATTERN, (int) ((float) progress / (float) max * 100));
        }
    }

    private static final class SavedState extends BaseSavedState {
        /**
         * The Progress.
         */
        int progress;

        /**
         * Instantiates a new Saved state.
         *
         * @param superState the super state
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        /**
         * The constant CREATOR SavedState.
         */
        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = mProgress;

        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        setProgress(ss.progress);
    }

}
