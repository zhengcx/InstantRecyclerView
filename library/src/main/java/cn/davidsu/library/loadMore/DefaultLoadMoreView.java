package cn.davidsu.library.loadMore;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import cn.davidsu.library.R;


/**
 * Created by cxzheng on 2018/2/6.
 * 默认的底部loadMore View
 */

public class DefaultLoadMoreView extends AbsLoadingMoreView {


    public DefaultLoadMoreView(@NonNull ViewGroup parent) {
        super(parent);
    }

    @LayoutRes
    @Override
    public int getRootViewLayout() {
        return R.layout.view_default_load_more;
    }

    @IdRes
    @Override
    public int getLoadingViewId() {
        return R.id.ll_loading;
    }

    @IdRes
    @Override
    public int getLoadEndId() {
        return R.id.tv_no_more_result;
    }

    @IdRes
    @Override
    public int getDefaultViewId() {
        return R.id.tv_drag_to_show_more;
    }
}
