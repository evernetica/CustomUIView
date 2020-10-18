package app.developer.uiview.steps;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.List;

import app.developer.uiview.R;

public class StepsView extends View {

    private List<StepItem> stepItems;
    private Paint numberTextPaint;
    private TextPaint titleTextPaint;
    private Paint circlePaint;
    private Paint selectedCirclePaint;
    private Paint linePaint;
    private Paint selectedLinePaint;
    private int horizontalLineWidth = 172;
    private int horizontalLineHeight = 6;
    private int strokeWidth = 6;
    private int circleRadius = 40;
    private int circleDiameter = circleRadius * 2;
    private int numberTextSize = 40;
    private int titleTextSize = 24;
    private int titleTextMargin = 16;
    private Drawable iconDrawable;

    public StepsView(Context context) {
        super(context);
        init(context, null);
    }

    public StepsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StepsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (stepItems != null && stepItems.size() > 0) {
            for (int i = 0; i < stepItems.size(); i++) {
                StepItem currentStep = stepItems.get(i);
                StepItem previousStep = null;
                if (i > 0) {
                    previousStep = stepItems.get(i - 1);
                }
                int count = currentStep.getStepNumber();
                drawHorizontalLine(canvas, count, linePaint);
                drawNumberText(canvas, count);
                drawTitleText(canvas, count, currentStep.getStepTitle());

                if (currentStep.isSelected()) {
                    canvas.drawCircle(count * (circleDiameter + horizontalLineWidth), (float) getHeight() / 3, circleRadius, selectedCirclePaint);
                } else {
                    canvas.drawCircle(count * (circleDiameter + horizontalLineWidth), (float) getHeight() / 3, circleRadius, circlePaint);
                }

                if (previousStep != null && previousStep.isCompleted()) {
                    drawCompletedItem(canvas, previousStep);
                }
            }
        }
    }

    private void drawCompletedItem(Canvas canvas, StepItem previousStep) {
        drawHorizontalLine(canvas, previousStep.getStepNumber(), selectedLinePaint);
        canvas.drawCircle(previousStep.getStepNumber() * (2 * circleRadius + horizontalLineWidth), (float) getHeight() / 3, circleRadius, selectedCirclePaint);
        iconDrawable.setBounds(
                previousStep.getStepNumber() * (2 * circleRadius + horizontalLineWidth) - iconDrawable.getIntrinsicWidth() / 2,
                getHeight() / 3 - iconDrawable.getIntrinsicHeight() / 2,
                previousStep.getStepNumber() * (2 * circleRadius + horizontalLineWidth) + iconDrawable.getIntrinsicWidth() / 2,
                getHeight() / 3 + iconDrawable.getIntrinsicHeight() / 2
        );
        iconDrawable.draw(canvas);
    }

    private void drawHorizontalLine(Canvas canvas, int count, Paint paint) {
        if (count > 0 && count < stepItems.size()) {
            canvas.drawLine(
                    count * (circleDiameter + horizontalLineWidth) + circleRadius,
                    (float) getHeight() / 3,
                    count * (circleDiameter + horizontalLineWidth) + horizontalLineWidth + circleRadius,
                    (float) getHeight() / 3,
                    paint);
        }
    }

    private void drawTitleText(Canvas canvas, int count, String text) {
        StaticLayout mTextLayout = new StaticLayout(text, titleTextPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(count * (circleDiameter + horizontalLineWidth), (float) getHeight() / 4 + circleRadius + titleTextPaint.getTextSize() + titleTextMargin);
        mTextLayout.draw(canvas);
        canvas.restore();
    }

    private void drawNumberText(Canvas canvas, int count) {
        Rect bounds = new Rect();
        String text = String.valueOf(count);
        numberTextPaint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.height();
        canvas.drawText(text, count * (circleDiameter + horizontalLineWidth), (float) getHeight() / 3 + (float) height / 2, numberTextPaint);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet set) {
        assert context != null;
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.ModeView, 0, 0);
        typedArray.recycle();

        selectedCirclePaint = new Paint();
        selectedCirclePaint.setAntiAlias(true);
        selectedCirclePaint.setStyle(Paint.Style.STROKE);
        selectedCirclePaint.setStrokeWidth(strokeWidth);
        selectedCirclePaint.setColor(ContextCompat.getColor(context, R.color.colorGreen));

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(strokeWidth);
        circlePaint.setColor(ContextCompat.getColor(context, R.color.colorGrey));

        selectedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedLinePaint.setStyle(Paint.Style.STROKE);
        selectedLinePaint.setColor(ContextCompat.getColor(context, R.color.colorGreen));
        selectedLinePaint.setStrokeWidth(horizontalLineHeight);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(horizontalLineHeight);
        linePaint.setColor(ContextCompat.getColor(context, R.color.colorGrey));

        numberTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberTextPaint.setTextAlign(Paint.Align.CENTER);
        numberTextPaint.setStyle(Paint.Style.FILL);
        numberTextPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
        numberTextPaint.setTextSize(numberTextSize);
        numberTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.rajdhani_bold));

        titleTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        titleTextPaint.setTextAlign(Paint.Align.CENTER);
        titleTextPaint.setStyle(Paint.Style.FILL);
        titleTextPaint.setColor(ContextCompat.getColor(context, R.color.colorWhite));
        titleTextPaint.setTextSize(titleTextSize);
        titleTextPaint.setTypeface(ResourcesCompat.getFont(context, R.font.rajdhani_bold));

        iconDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_step_completed);
    }

    public List<StepItem> getStepItems() {
        return stepItems;
    }

    public void setStepItems(List<StepItem> stepItems) {
        this.stepItems = stepItems;
        invalidate();
    }

    public void increaseStep() {
        for (int i = 0; i < stepItems.size(); i++) {
            if (stepItems.get(i).isSelected()) {
                stepItems.get(i).setSelected(false);
                stepItems.get(i).setCompleted(true);
                if (i + 1 < stepItems.size()) {
                    stepItems.get(i + 1).setSelected(true);
                    invalidate();
                    return;
                }
            }
        }
    }
}
