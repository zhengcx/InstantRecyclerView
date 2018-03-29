package cn.davidsu.library.support;

import android.support.annotation.NonNull;
import android.view.View;

import cn.davidsu.library.BaseViewHolder;


/**
 * Created by cxzheng on 2018/2/4.
 */

public class NoOpViewHolder extends BaseViewHolder {

    public NoOpViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bindView(@NonNull View itemView) {

    }
}
