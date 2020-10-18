package app.developer.uiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class EditStepHistoryView extends LinearLayoutCompat {

    private AppCompatTextView firstLine, secondLine, previousText, newText;

    public EditStepHistoryView(Context context) {
        super(context);
        init(context, null);
    }

    public EditStepHistoryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EditStepHistoryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        View view = inflater.inflate(R.layout.view_edit_step_history, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditStepHistoryView, 0, 0);
        initView(view);
        firstLine.setText(typedArray.getString(R.styleable.EditStepHistoryView_firstLineText));
        firstLine.setTextColor(typedArray.getColor(R.styleable.EditStepHistoryView_firstLineTextColor, Color.parseColor("#77BC1F")));
        if (typedArray.getFont(R.styleable.EditStepHistoryView_firstLineFontFamily) != null)
            firstLine.setTypeface(typedArray.getFont(R.styleable.EditStepHistoryView_firstLineFontFamily));

        secondLine.setText(typedArray.getString(R.styleable.EditStepHistoryView_secondLineText));
        secondLine.setTextColor(typedArray.getColor(R.styleable.EditStepHistoryView_secondLineTextColor, Color.parseColor("#77BC1F")));
        if (typedArray.getFont(R.styleable.EditStepHistoryView_secondLineFontFamily) != null)
            secondLine.setTypeface(typedArray.getFont(R.styleable.EditStepHistoryView_secondLineFontFamily));

        previousText.setText(typedArray.getString(R.styleable.EditStepHistoryView_previousText));
        previousText.setTextColor(typedArray.getColor(R.styleable.EditStepHistoryView_previousTextColor, Color.parseColor("#8E8E8E")));
        if (typedArray.getFont(R.styleable.EditStepHistoryView_previousFontFamily) != null)
            previousText.setTypeface(typedArray.getFont(R.styleable.EditStepHistoryView_previousFontFamily));

        newText.setText(typedArray.getString(R.styleable.EditStepHistoryView_newText));
        newText.setTextColor(typedArray.getColor(R.styleable.EditStepHistoryView_newTextColor, Color.WHITE));
        if (typedArray.getFont(R.styleable.EditStepHistoryView_newFontFamily) != null)
            newText.setTypeface(typedArray.getFont(R.styleable.EditStepHistoryView_newFontFamily));

        typedArray.recycle();
    }

    private void initView(View view) {
        firstLine = view.findViewById(R.id.firstLine);
        secondLine = view.findViewById(R.id.secondLine);
        previousText = view.findViewById(R.id.previousText);
        newText = view.findViewById(R.id.newText);
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
     * Sets previous text.
     *
     * @param text the text
     */
    public void setPreviousText(String text) {
        previousText.setText(text);
    }

    /**
     * Sets new text.
     *
     * @param text the text
     */
    public void setNewText(String text) {
        newText.setText(text);
    }

    /**
     * Sets first line text color.
     *
     * @param colorInt the color int
     */
    public void setFirstLineTextColor(@ColorInt int colorInt) {
        firstLine.setTextColor(colorInt);
    }

    /**
     * Sets second line text color.
     *
     * @param colorRes the color res
     */
    public void setSecondLineTextColor(@ColorInt int colorRes) {
        secondLine.setTextColor(colorRes);
    }

    /**
     * Sets previos text color.
     *
     * @param colorInt the color int
     */
    public void setPreviosTextColor(@ColorInt int colorInt) {
        previousText.setTextColor(colorInt);
    }

    /**
     * Sets new text color.
     *
     * @param colorRes the color res
     */
    public void setNewTextColor(@ColorInt int colorRes) {
        newText.setTextColor(colorRes);
    }

    /**
     * Sets first line type face.
     *
     * @param font the font
     */
    public void setFirstLineTypeFace(@FontRes int font) {
        firstLine.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets second line type face.
     *
     * @param font the font
     */
    public void setSecondLineTypeFace(@FontRes int font) {
        firstLine.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets previous type face.
     *
     * @param font the font
     */
    public void setPreviousTypeFace(@FontRes int font) {
        firstLine.setTypeface(getResources().getFont(font));
    }

    /**
     * Sets new type face.
     *
     * @param font the font
     */
    public void setNewTypeFace(@FontRes int font) {
        firstLine.setTypeface(getResources().getFont(font));
    }

}
