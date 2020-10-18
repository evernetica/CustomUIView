package app.developer.uiview.pagination;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.developer.uiview.R;

/**
 * The type Pagination indicator.
 */
public class PaginationIndicator extends LinearLayout {
    private static final int DEFAULT_POINT_COLOR = Color.CYAN;
    public static final float DEFAULT_WIDTH_FACTOR = 2.5f;

    private List<ImageView> indicators;
    private ViewPager viewPager;
    private float indicatorsSize;
    private float indicatorsCornerRadius;
    private float indicatorsSpacing;
    private float indicatorsWidthFactor;
    private int indicatorsColor;
    private int selectedIndicatorColor;
    private boolean progressMode;

    private boolean indicatorsClickable;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private OnPageChangeListenerHelper onPageChangeListenerHelper;

    /**
     * Instantiates a new Pagination indicator.
     *
     * @param context the context
     */
    public PaginationIndicator(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new Pagination indicator.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public PaginationIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new Pagination indicator.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public PaginationIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        indicators = new ArrayList<>();
        setOrientation(HORIZONTAL);

        indicatorsSize = dpToPx(16); // 16dp
        indicatorsSpacing = dpToPx(4); // 4dp
        indicatorsCornerRadius = indicatorsSize / 2;

        indicatorsWidthFactor = DEFAULT_WIDTH_FACTOR;
        indicatorsColor = DEFAULT_POINT_COLOR;
        indicatorsClickable = true;

        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PaginationIndicator);

            indicatorsColor = typedArray.getColor(R.styleable.PaginationIndicator_indicatorsColor, DEFAULT_POINT_COLOR);
            selectedIndicatorColor = typedArray.getColor(R.styleable.PaginationIndicator_selectedIndicatorColor, DEFAULT_POINT_COLOR);

            indicatorsWidthFactor = typedArray.getFloat(R.styleable.PaginationIndicator_indicatorsWidthFactor, 2.5f);
            if (indicatorsWidthFactor < 1) {
                indicatorsWidthFactor = 2.5f;
            }

            indicatorsSize = typedArray.getDimension(R.styleable.PaginationIndicator_indicatorSize, indicatorsSize);
            indicatorsCornerRadius = (int) typedArray.getDimension(R.styleable.PaginationIndicator_indicatorsCornerRadius, indicatorsSize / 2);
            indicatorsSpacing = typedArray.getDimension(R.styleable.PaginationIndicator_indicatorsSpacing, indicatorsSpacing);

            progressMode = typedArray.getBoolean(R.styleable.PaginationIndicator_progressMode, false);

            typedArray.recycle();
        }

        if (isInEditMode()) {
            addIndicator(5);
        }

        refreshIndicators();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshIndicators();
    }

    private void refreshIndicators() {
        if (viewPager != null && viewPager.getAdapter() != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    refreshIndicatorCount();
                    refreshIndicatorColors();
                    refreshIndicatorSize();
                    refreshOnPageChangedListener();
                }
            });
        } else {
            Log.e(PaginationIndicator.class.getSimpleName(), "You have to set an adapter to the view pager before !");
        }
    }

    private void refreshIndicatorCount() {
        if (indicators.size() < Objects.requireNonNull(viewPager.getAdapter()).getCount()) {
            addIndicator(viewPager.getAdapter().getCount() - indicators.size());
        } else if (indicators.size() > viewPager.getAdapter().getCount()) {
            removeIndicators(indicators.size() - viewPager.getAdapter().getCount());
        }
    }

    private void addIndicator(int count) {
        for (int i = 0; i < count; i++) {
            View indicator = LayoutInflater.from(getContext()).inflate(R.layout.indicators_layout, this, false);
            ImageView imageView = indicator.findViewById(R.id.indicator);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = params.height = (int) indicatorsSize;
            params.setMargins((int) indicatorsSpacing, 0, (int) indicatorsSpacing, 0);
            IndicatorGradientDrawable background = new IndicatorGradientDrawable();
            background.setCornerRadius(indicatorsCornerRadius);
            if (isInEditMode()) {
                background.setColor(0 == i ? selectedIndicatorColor : indicatorsColor);
            } else {
                background.setColor(viewPager.getCurrentItem() == i ? selectedIndicatorColor : indicatorsColor);
            }
            imageView.setBackground(background);

            final int finalI = i;
            indicator.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (indicatorsClickable && viewPager != null && viewPager.getAdapter() != null && finalI < viewPager.getAdapter()
                            .getCount()) {
                        viewPager.setCurrentItem(finalI, true);
                    }
                }
            });

            indicators.add(imageView);
            addView(indicator);
        }
    }

    private void removeIndicators(int count) {
        for (int i = 0; i < count; i++) {
            removeViewAt(getChildCount() - 1);
            indicators.remove(indicators.size() - 1);
        }
    }

    private void refreshOnPageChangedListener() {
        if (viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0) {
            viewPager.removeOnPageChangeListener(onPageChangeListenerHelper);
            onPageChangeListenerHelper = buildOnPageChangedListener();
            viewPager.addOnPageChangeListener(onPageChangeListenerHelper);
            onPageChangeListenerHelper.onPageScrolled(viewPager.getCurrentItem(), -1, 0f);
        }
    }

    private OnPageChangeListenerHelper buildOnPageChangedListener() {
        return new OnPageChangeListenerHelper() {
            @Override
            void onPageScrolled(int selectedPosition, int nextPosition, float positionOffset) {
                if (selectedPosition == -1) {
                    return;
                }

                ImageView selectedIndicator = indicators.get(selectedPosition);

                int selectedIndicatorWidth = (int) (indicatorsSize + (indicatorsSize * (indicatorsWidthFactor - 1) * (1 - positionOffset)));
                setIndicatorWidth(selectedIndicator, selectedIndicatorWidth);

                if (nextPosition == -1) {
                    return;
                }

                ImageView nextIndicator = indicators.get(nextPosition);
                if (nextIndicator != null) {
                    int nextIndicatorWidth = (int) (indicatorsSize + (indicatorsSize * (indicatorsWidthFactor - 1) * (positionOffset)));
                    setIndicatorWidth(nextIndicator, nextIndicatorWidth);

                    IndicatorGradientDrawable selectedIndicatorBackground = (IndicatorGradientDrawable) selectedIndicator.getBackground();
                    IndicatorGradientDrawable nextIndicatorBackground = (IndicatorGradientDrawable) nextIndicator.getBackground();

                    if (selectedIndicatorColor != indicatorsColor) {
                        int selectedColor = (int) argbEvaluator.evaluate(positionOffset, selectedIndicatorColor, indicatorsColor);
                        int nextColor = (int) argbEvaluator.evaluate(positionOffset, indicatorsColor, selectedIndicatorColor);

                        nextIndicatorBackground.setColor(nextColor);

                        if (progressMode && selectedPosition <= viewPager.getCurrentItem()) {
                            selectedIndicatorBackground.setColor(selectedIndicatorColor);
                        } else {
                            selectedIndicatorBackground.setColor(selectedColor);
                        }
                    }
                }

                invalidate();
            }

            @Override
            void resetPosition(int position) {
                setIndicatorWidth(indicators.get(position), (int) indicatorsSize);
            }

            @Override
            int getPageCount() {
                return indicators.size();
            }
        };
    }

    private void setIndicatorWidth(ImageView indicator, int indicatorWidth) {
        ViewGroup.LayoutParams indicatorParams = indicator.getLayoutParams();
        indicatorParams.width = indicatorWidth;
        indicator.setLayoutParams(indicatorParams);
    }

    private void refreshIndicatorColors() {
        if (indicators == null) {
            return;
        }
        for (int i = 0; i < indicators.size(); i++) {
            ImageView elevationItem = indicators.get(i);
            IndicatorGradientDrawable background = (IndicatorGradientDrawable) elevationItem.getBackground();

            if (i == viewPager.getCurrentItem() || (progressMode && i < viewPager.getCurrentItem())) {
                background.setColor(selectedIndicatorColor);
            } else {
                background.setColor(indicatorsColor);
            }

            elevationItem.setBackground(background);
            elevationItem.invalidate();
        }
    }

    private void refreshIndicatorSize() {
        if (indicators == null) {
            return;
        }
        for (int i = 0; i < viewPager.getCurrentItem(); i++) {
            setIndicatorWidth(indicators.get(i), (int) indicatorsSize);
        }
    }

    private void setUpViewPager() {
        if (viewPager.getAdapter() != null) {
            viewPager.getAdapter().registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    refreshIndicators();
                }
            });
        }
    }

    private int dpToPx(int dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp);
    }

    /**
     * Sets points color.
     *
     * @param color the color
     */
    public void setPointsColor(int color) {
        indicatorsColor = color;
        refreshIndicatorColors();
    }

    /**
     * Sets selected point color.
     *
     * @param color the color
     */
    public void setSelectedPointColor(int color) {
        selectedIndicatorColor = color;
        refreshIndicatorColors();
    }

    /**
     * Sets indicators clickable.
     *
     * @param indicatorsClickable the indicators clickable
     */
    public void setIndicatorClickable(boolean indicatorsClickable) {
        this.indicatorsClickable = indicatorsClickable;
    }

    /**
     * Sets view pager.
     *
     * @param viewPager the view pager
     */
    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        setUpViewPager();
        refreshIndicators();
    }
}
