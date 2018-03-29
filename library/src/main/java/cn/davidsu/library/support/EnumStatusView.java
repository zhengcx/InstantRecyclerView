package cn.davidsu.library.support;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cxzheng on 2018/2/6.
 * status view的状态
 */

public class EnumStatusView {
    public static final int STATUS_VIEW_LOADING = 1;
    public static final int STATUS_VIEW_DATA_EMPTY = 2;
    public static final int STATUS_VIEW_LOAD_FAILED = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_VIEW_LOADING, STATUS_VIEW_DATA_EMPTY, STATUS_VIEW_LOAD_FAILED})
    public @interface StatusViewType {
    }

}
