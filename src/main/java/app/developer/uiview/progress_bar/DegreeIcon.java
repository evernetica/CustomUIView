package app.developer.uiview.progress_bar;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

public class DegreeIcon {
    private int degree;

    @DrawableRes
    private int drawable;

    @Nullable
    private String axis;

    private Boolean isLast = false;
    /**
     * Instantiates a new Degree icon.
     *
     * @param degree   the degree
     * @param drawable the drawable
     * @param axis     the axis
     */

    public DegreeIcon(int degree, int drawable, @Nullable String axis) {
        this.degree = degree;
        this.drawable = drawable;
        this.axis = axis;
    }

    public DegreeIcon(int degree, int drawable, @Nullable String axis, @Nullable Boolean isLast) {
        this.degree = degree;
        this.drawable = drawable;
        this.axis = axis;
        this.isLast = isLast;
    }

    /**
     * Gets drawable.
     *
     * @return the drawable
     */
    public int getDrawable() {
        return drawable;
    }

    /**
     * Gets degree.
     *
     * @return the degree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Gets axis.
     *
     * @return the axis
     */
    @Nullable
    public String getAxis() {
        return axis;
    }

    public Boolean getLast() {
        return isLast;
    }
}
