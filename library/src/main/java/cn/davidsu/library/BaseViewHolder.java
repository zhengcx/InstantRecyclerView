package cn.davidsu.library;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by cxzheng on 2018/2/2.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    @Nullable
    protected Bundle bundle;
    protected View rootView;

    public BaseViewHolder(@NonNull View itemView) {
        this(itemView, null);

    }

    public BaseViewHolder(@NonNull View itemView, @Nullable Bundle bundle) {
        super(itemView);
        this.bundle = bundle;
        this.rootView = itemView;
        if (itemView != null) {
            bindView(itemView);
        }
    }

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        this(parent, layoutId, null);
    }

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId, @Nullable Bundle bundle) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false), bundle);
    }

    public View getRootView() {
        return rootView;
    }

    public abstract void bindView(@NonNull View itemView);

}
