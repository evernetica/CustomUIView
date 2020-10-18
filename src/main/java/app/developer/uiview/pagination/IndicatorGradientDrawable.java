package app.developer.uiview.pagination;

import android.graphics.drawable.GradientDrawable;

public class IndicatorGradientDrawable extends GradientDrawable {

    private int currentColor;

    @Override
    public void setColor(int argb) {
        super.setColor(argb);

        currentColor = argb;
    }

    public int getCurrentColor() {
        return currentColor;
    }
}