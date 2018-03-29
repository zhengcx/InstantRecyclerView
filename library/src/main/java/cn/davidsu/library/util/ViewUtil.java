package cn.davidsu.library.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by cxzheng on 2018/2/3.
 */

public class ViewUtil {

    private ViewUtil() {
    }

    public static void setViewHidden(View view, boolean hidden) {
        if (view != null) {
            view.setVisibility(hidden ? View.GONE : View.VISIBLE);
        }
    }

    public static int dp2px(Context context, float dpValue) {
        return (int)getRawSize(context, 1, dpValue);
    }

    public static float getRawSize(Context context, int unit, float size) {
        return TypedValue.applyDimension(unit, size, context.getResources().getDisplayMetrics());
    }
}
