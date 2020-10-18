package app.developer.uiview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ModeView extends ConstraintLayout {

    private Mode selectedMode = Mode.WELD;
    private Button btnWeld, btnTest, btnEdit;
    private ModeButtonSelectListener modeButtonSelectListener;

    public ModeView(Context context) {
        super(context);
        init(context, null);
    }

    public ModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ModeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@Nullable Context context, @Nullable AttributeSet set) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_mode, this, true);
        btnWeld = view.findViewById(R.id.btnWeld);
        btnTest = view.findViewById(R.id.btnTest);
        btnEdit = view.findViewById(R.id.btnEdit);
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.ModeView, 0, 0);
        setMode();
        initListeners();
        typedArray.recycle();
    }

    private void initListeners() {

        btnTest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeButtonSelectListener != null) {
                    selectedMode = Mode.TEST;
                    setMode();
                    modeButtonSelectListener.onTestButtonSelected();
                }
            }
        });

        btnWeld.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeButtonSelectListener != null) {
                    selectedMode = Mode.WELD;
                    setMode();
                    modeButtonSelectListener.onWeldButtonSelected();
                }
            }
        });
        btnEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modeButtonSelectListener != null) {
                    selectedMode = Mode.EDIT;
                    setMode();
                    modeButtonSelectListener.onEditButtonSelected();
                }
            }
        });
    }

    private void setMode() {
        switch (selectedMode) {
            case WELD: {
                btnEdit.setSelected(false);
                btnTest.setSelected(false);
                btnWeld.setSelected(true);
                break;
            }
            case TEST: {
                btnEdit.setSelected(false);
                btnTest.setSelected(true);
                btnWeld.setSelected(false);
                break;
            }
            case EDIT: {
                btnEdit.setSelected(true);
                btnTest.setSelected(false);
                btnWeld.setSelected(false);
                break;
            }
        }
    }

    public Mode getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(Mode selectedMode) {
        this.selectedMode = selectedMode;
        setMode();
    }

    public void setModeButtonSelectListener(ModeButtonSelectListener modeButtonSelectListener) {
        this.modeButtonSelectListener = modeButtonSelectListener;
    }

    public enum Mode {WELD, TEST, EDIT}

    public interface ModeButtonSelectListener {
        void onWeldButtonSelected();

        void onTestButtonSelected();

        void onEditButtonSelected();
    }
}
