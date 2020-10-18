package app.developer.uiview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import java.text.SimpleDateFormat;

import app.developer.uiview.drop_down.TitleDropdownView;

public class NavigationBarView extends LinearLayoutCompat {

    private TitleDropdownView[] clickableTitleDropDownViews;
    private OnNavigationBarItemSelectListener itemSelectListener;
    private TextView tvDate;

    /**
     * Instantiates a new Navigation bar view.
     *
     * @param context the context
     */
    public NavigationBarView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Instantiates a new Navigation bar view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public NavigationBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Instantiates a new Navigation bar view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public NavigationBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint("SetTextI18n")
    private void init(@Nullable Context context, @Nullable AttributeSet set) {
        assert context != null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_navigation_bar, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.NavigationBarView, 0, 0);
        TitleDropdownView tdvOrbitalWeld = view.findViewById(R.id.tdvOrbitalWeld);
        TitleDropdownView tdvProcedure = view.findViewById(R.id.tdvProcedure);
        TitleDropdownView tdvSettings = view.findViewById(R.id.tdvSettings);
        TitleDropdownView tdvLog = view.findViewById(R.id.tdvLog);
        tvDate = view.findViewById(R.id.tvDate);

        clickableTitleDropDownViews = new TitleDropdownView[]{
                tdvOrbitalWeld,
                tdvProcedure,
                tdvSettings,
                tdvLog
        };

        initItemListener(tdvOrbitalWeld);
        initItemListener(tdvProcedure);
        initItemListener(tdvSettings);
        initItemListener(tdvLog);

        typedArray.recycle();
    }

    private void initItemListener(final TitleDropdownView view) {
        view.setItemClickListener((isSelected, id) -> {
            for (TitleDropdownView dropdownView : clickableTitleDropDownViews) {
                if (id == dropdownView.getId()) {
                    itemSelectListener.onItemSelected(id);
                    dropdownView.setActive(true);
                    view.setBackground(R.drawable.bg_selected_tab_green_gradient);
                } else {
                    dropdownView.setActive(false);
                    dropdownView.setBackgroundViewColor(R.color.colorLightBlack);
                }
            }
        });
    }

    public void setDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, yyyy h:mm a");
        tvDate.setText(sdf.format(date));
    }

    /**
     * Register a callback to be invoked when this view is selected.
     *
     * @param itemSelectListener The callback that will run
     */
    public void setItemSelectListener(OnNavigationBarItemSelectListener itemSelectListener) {
        this.itemSelectListener = itemSelectListener;
    }

    /**
     * Call this view's OnNavigationBarItemSelectListener, if it is defined.
     */

    public void performSelection(int viewId) {
        for (TitleDropdownView dropdownView : clickableTitleDropDownViews) {
            if (itemSelectListener != null && dropdownView.getId() == viewId) {
                dropdownView.callOnCLick();
            }
        }
    }

    public interface OnNavigationBarItemSelectListener {
        void onItemSelected(int itemKey);
    }
}
