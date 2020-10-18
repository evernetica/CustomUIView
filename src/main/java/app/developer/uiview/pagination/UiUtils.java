package app.developer.uiview.pagination;

import android.content.Context;
import android.util.TypedValue;

import app.developer.uiview.R;

public class UiUtils {
    public static int getThemePrimaryColor(final Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }
}
