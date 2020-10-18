package app.developer.uiview.progress_bar;

import android.content.Context;

/**
 * The type Unit utils.
 */
class UnitUtils {
    /**
     * Dip 2 px int.
     *
     * @param context the context
     * @param dpValue the dp value
     * @return the int
     */
    static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
