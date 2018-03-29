package cn.davidsu.library;

import android.support.annotation.Nullable;
import android.util.SparseArray;

/**
 * Created by cxzheng on 2018/2/3.
 * 多ItemType的ItemViewDelegate的调度管理
 */

public class DelegateScheduler {

    @Nullable
    private SparseArray<ItemViewDelegate> itemViewDelegates;

    protected void addItemViewDelegate(int itemType, @Nullable ItemViewDelegate itemViewDelegate) {
        if (itemViewDelegates == null) {
            itemViewDelegates = new SparseArray<>();
        }
        itemViewDelegates.put(itemType, itemViewDelegate);
    }

    @Nullable
    protected ItemViewDelegate getItemViewDelegateByItemType(int itemType) {
        if (itemViewDelegates == null) {
            return null;
        }
        if (!isMultiItemType()) {//列表只有一种类型，则把唯一的delegate返回
            return itemViewDelegates.valueAt(0);
        }

        return itemViewDelegates.get(itemType);
    }

    /**
     * 是否是多itemType的列表
     * <p>不包括基础的几种itemType如header、footer、statusview</p>
     *
     * @return
     */
    protected boolean isMultiItemType() {
        return itemViewDelegates != null && itemViewDelegates.size() > 1;
    }
}
