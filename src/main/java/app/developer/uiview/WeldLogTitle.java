package app.developer.uiview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

public class WeldLogTitle extends AppCompatTextView implements View.OnClickListener {
    private int clickCounter = 0;
    private OnWeldLogTitleClickListener listener;

    public WeldLogTitle(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public WeldLogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    public WeldLogTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickCounter++;
        listener.onItemClicked(v, clickCounter % 2 == 0);
    }

    public void setOnWeldLogTitleClickListener(OnWeldLogTitleClickListener listener) {
        this.listener = listener;
    }

    public interface OnWeldLogTitleClickListener {
        void onItemClicked(View v, boolean isReverseOrder);
    }
}
