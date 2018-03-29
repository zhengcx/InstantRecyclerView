package cn.davidsu.library.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import cn.davidsu.library.BaseItemType;


/**
 * Created by cxzheng on 2018/2/3.
 */

public class RVUtil {

    /**
     * 判断itemType是否是来自mDataList中自定义的type
     *
     * @param viewType
     * @return
     */
    public static boolean isViewTypeFromData(int viewType) {
        return viewType != BaseItemType.VIEW_TYPE_HEADER && viewType != BaseItemType.VIEW_TYPE_FOOTER
                && viewType != BaseItemType.VIEW_TYPE_STATUS && viewType != BaseItemType.VIEW_TYPE_LOAD_MORE;
    }


    /**
     * 判断是否开始加载更多
     *
     * @param preLoadMoreNum
     * @param recyclerView
     * @param newState
     * @return
     */
    public static boolean needLoadingMore(int preLoadMoreNum, @NonNull RecyclerView recyclerView, int newState) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            //获取总的item数
            int totalItemCount = linearManager.getItemCount();
            //获取最后一个可见view的位置
            int lastItemPosition = linearManager.findLastVisibleItemPosition();

            if (lastItemPosition >= totalItemCount - preLoadMoreNum && (newState == RecyclerView.SCROLL_STATE_IDLE)) {
                return true;
            }
        }

        return false;
    }


}
