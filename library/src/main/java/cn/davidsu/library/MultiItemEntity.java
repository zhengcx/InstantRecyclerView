package cn.davidsu.library;

import android.support.annotation.Nullable;

/**
 * Created by cxzheng on 2018/2/4.
 * 当列表时多itemType时，列表项数据内需设置itemType
 */

public class MultiItemEntity<T> {
    private int viewType;
    private T item;

    public MultiItemEntity(int viewType, T item) {
        this.viewType = viewType;
        this.item = item;
    }

    public int getViewType() {
        return viewType;
    }

    @Nullable
    public T getItem() {
        return item;
    }
}
