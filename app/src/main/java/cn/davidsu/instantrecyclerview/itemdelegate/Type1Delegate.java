package cn.davidsu.instantrecyclerview.itemdelegate;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.davidsu.instantrecyclerview.R;
import cn.davidsu.library.BaseViewHolder;
import cn.davidsu.library.ItemViewDelegate;
import cn.davidsu.library.MultiItemEntity;

/**
 * Created by cxzheng on 2018/3/29.
 * process type1 item
 */

public class Type1Delegate implements ItemViewDelegate<MultiItemEntity, Type1Delegate.Type1ViewHolder> {


    @NonNull
    @Override
    public Type1ViewHolder createViewHolder(ViewGroup parent) {
        return new Type1ViewHolder(parent, R.layout.view_item_type1);
    }

    @Override
    public void bindViewHolder(Type1ViewHolder holder, MultiItemEntity entity) {
        String text = (String) entity.getItem();
        holder.bindData(text);
    }

    class Type1ViewHolder extends BaseViewHolder {

        private TextView tv;

        public Type1ViewHolder(@NonNull ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        public void bindView(@NonNull View itemView) {
            tv = itemView.findViewById(R.id.tv_type_1);
        }

        public void bindData(String s) {
            tv.setText(s);
        }
    }
}
