package cn.davidsu.library.loadMore;

import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cn.davidsu.library.util.ViewUtil;

/**
 * Created by cxzheng on 2018/2/7.
 * TODO:状态待整理
 */

public abstract class AbsLoadingMoreView {

    public final static int STATE_IDLE = 0;
    public final static int STATE_LOADING = 1;
    public final static int STATE_EMPTY = 2;
    public final static int STATE_END = 3;

    @Nullable
    private View rootView;
    @Nullable
    private View stateloadingView;
    @Nullable
    private View stateEndView;
    @Nullable
    private View stateDefaultView;

    private int currentState = STATE_IDLE;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_IDLE, STATE_LOADING, STATE_EMPTY, STATE_END})
    public @interface LoadMoreState {
    }


    public AbsLoadingMoreView(@NonNull ViewGroup parent) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(getRootViewLayout(), parent, false);
        initStatusView();
    }


    public void setState(@LoadMoreState int state) {
        currentState = state;
        ViewUtil.setViewHidden(stateDefaultView, true);
        switch (state) {
            case STATE_EMPTY:
                ViewUtil.setViewHidden(stateloadingView, true);
                ViewUtil.setViewHidden(stateEndView, true);
                break;
            case STATE_END:
                ViewUtil.setViewHidden(stateloadingView, true);
                ViewUtil.setViewHidden(stateEndView, false);
                break;
            default:
                ViewUtil.setViewHidden(stateloadingView, false);
                ViewUtil.setViewHidden(stateEndView, true);
                break;
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    private void initStatusView() {
        if (rootView != null) {
            stateloadingView = rootView.findViewById(getLoadingViewId());
            stateEndView = rootView.findViewById(getLoadEndId());
            stateDefaultView = rootView.findViewById(getDefaultViewId());
        }
    }

    /**
     * 重置rootView
     *
     * @param rootView
     */
    public void resetRootView(@Nullable View rootView) {
        if (rootView == null) {
            return;
        }
        this.rootView = rootView;
        initStatusView();
        setState(currentState);
    }

    @Nullable
    public View getRootView() {
        return rootView;
    }

    /**
     * loadMore布局文件
     *
     * @return
     */
    public abstract @LayoutRes
    int getRootViewLayout();

    /**
     * loading状态View的id
     *
     * @return
     */
    public abstract @IdRes
    int getLoadingViewId();

    /**
     * 无更多结果状态View Id
     *
     * @return
     */
    public abstract @IdRes
    int getLoadEndId();

    /**
     * 默认显示View Id
     *
     * @return
     */
    public abstract @IdRes
    int getDefaultViewId();


}
