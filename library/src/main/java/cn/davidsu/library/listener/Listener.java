package cn.davidsu.library.listener;

import android.view.View;

/**
 * Created by cxzheng on 2018/2/3.
 */

public class Listener {

    public interface OnItemClickListener<E> {

        void onItemClick(E data, View view, int position, int viewType);
    }

    public interface OnItemLongClickListener<E> {

        boolean onItemLongClick(E data, View view, int position, int viewType);
    }

    public interface LoadMoreListener {
        void loadMore();
    }
}
