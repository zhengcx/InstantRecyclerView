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
 * peocess type2 item
 */

public class Type2Delegate implements ItemViewDelegate<MultiItemEntity, Type2Delegate.Type2ViewHolder> {


    @NonNull
    @Override
    public Type2ViewHolder createViewHolder(ViewGroup parent) {
        return new Type2ViewHolder(parent, R.layout.view_item_type2);
    }

    @Override
    public void bindViewHolder(Type2ViewHolder holder, MultiItemEntity entity) {
        String text = (String) entity.getItem();
        holder.bindData(text);
    }


    class Type2ViewHolder extends BaseViewHolder {

        private TextView tv;

        public Type2ViewHolder(@NonNull ViewGroup parent, int layoutId) {
            super(parent, layoutId);
        }

        @Override
        public void bindView(@NonNull View itemView) {
            tv = itemView.findViewById(R.id.tv_type_2);
        }

        public void bindData(String s) {
            tv.setText(s);
        }
    }
}
