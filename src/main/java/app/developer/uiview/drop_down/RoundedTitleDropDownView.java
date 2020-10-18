package app.developer.uiview.drop_down;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.FontRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import app.developer.uiview.R;

public class RoundedTitleDropDownView extends ConstraintLayout {

    private State state = State.expand_down;
    private AppCompatTextView title;
    private AppCompatTextView headerTitleText;
    private AppCompatImageView rightImageView;
    private LinearLayout llContainer;
    private View rightDivider;
    private ArrowClickListener arrowClickListener;

    public RoundedTitleDropDownView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundedTitleDropDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundedTitleDropDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet attrs) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_rounded_title_dropdown, this, true);
        title = view.findViewById(R.id.title);
        headerTitleText = view.findViewById(R.id.headerTitleText);
        rightImageView = view.findViewById(R.id.rightImageView);
        llContainer = view.findViewById(R.id.llContainer);
        rightDivider = view.findViewById(R.id.rightDivider);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedTitleDropdownView, 0, 0);
        if (typedArray.getDimensionPixelSize(R.styleable.RoundedTitleDropdownView_containerWidth, 0) != 0) {
            LayoutParams layoutParams = (LayoutParams) llContainer.getLayoutParams();
            layoutParams.width = typedArray.getDimensionPixelSize(R.styleable.RoundedTitleDropdownView_containerWidth, 0);
            view.setLayoutParams(layoutParams);
        }

        title.setPadding(typedArray.getDimensionPixelSize(R.styleable.RoundedTitleDropdownView_headerPaddingText, 55), 0, 0, 0);

        title.setText(typedArray.getString(R.styleable.RoundedTitleDropdownView_text));
        title.setTextSize(typedArray.getDimensionPixelSize(R.styleable.RoundedTitleDropdownView_textSize, 24));
        title.setTextColor(typedArray.getColor(R.styleable.RoundedTitleDropdownView_textColor, ContextCompat.getColor(context, R.color.colorWhite)));
        if (typedArray.getFont(R.styleable.RoundedTitleDropdownView_fontFamily) != null) {
            title.setTypeface(typedArray.getFont(R.styleable.RoundedTitleDropdownView_fontFamily));
        }
        if (typedArray.getString(R.styleable.RoundedTitleDropdownView_headerTitleText) != null) {
            headerTitleText.setText(typedArray.getString(R.styleable.RoundedTitleDropdownView_headerTitleText));
        } else {
            headerTitleText.setVisibility(GONE);
        }
        if (typedArray.getInt(R.styleable.RoundedTitleDropdownView_android_gravity, 0) != 0) {
            title.setPadding(0, 0, 0, 0);
            title.setGravity(typedArray.getInt(R.styleable.RoundedTitleDropdownView_android_gravity, 0));
        }
        if (typedArray.getResourceId(R.styleable.RoundedTitleDropdownView_background, R.drawable.bg_rounded_title_dropdown_view) != 0) {
            setBackground(typedArray.getResourceId(R.styleable.RoundedTitleDropdownView_background, R.drawable.bg_rounded_title_dropdown_view));
        }

        state = State.values()[typedArray.getInt(R.styleable.RoundedTitleDropdownView_viewState, 0)];
        setState(state);
        typedArray.recycle();
    }

    public void setText(String text) {
        title.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        title.setTextColor(color);
    }

    public void setFontFamily(@FontRes int font) {
        title.setTypeface(getResources().getFont(font));
    }

    public void setState(State state) {
        switch (state) {
            case expand_down: {
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down));
                break;
            }
            case expand_up: {
                rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down_up));
                break;
            }
            case none: {
                rightDivider.setVisibility(GONE);
                rightImageView.setVisibility(GONE);
                break;
            }
        }
    }

    public void performArrowSelection() {
        if (arrowClickListener != null && rightImageView != null) {
            rightImageView.performClick();
        }
    }

    public void setArrowClickListener(ArrowClickListener arrowClickListener) {
        this.arrowClickListener = arrowClickListener;

        rightImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == State.expand_down) {
                    state = State.expand_up;
                    rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down_up));
                    arrowClickListener.onClick(true);
                } else if (state == State.expand_up) {
                    state = State.expand_down;
                    rightImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_drop_down));
                    arrowClickListener.onClick(false);
                }
            }
        });
    }

    public void setBackground(int backgroundDrawable) {
        llContainer.setBackground(ContextCompat.getDrawable(getContext(), backgroundDrawable));
    }

    public interface ArrowClickListener {
        void onClick(Boolean isOpened);
    }

}
