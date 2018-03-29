package cn.davidsu.library;

import android.support.annotation.NonNull;
import android.view.ViewGroup;

/**
 * Created by cxzheng on 2018/2/5.
 */

public interface ItemViewDelegate<T, VH extends BaseViewHolder> {

    @NonNull
    VH createViewHolder(ViewGroup parent);

    void bindViewHolder(VH holder, T entity);
}
