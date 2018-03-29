package cn.davidsu.library.itemdecoration;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cxzheng on 2017/5/19.
 * 这里只提供最基础的添加上下左右space的分割线
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private int mTopSpace;
    private int mBottomSpace;
    private int mLeftSpace;
    private int mRightSpace;

    public SpaceItemDecoration(int topSpace, int leftSpace, int rightSpace, int bottomSpace) {
        this.mTopSpace = topSpace;
        this.mLeftSpace = leftSpace;
        this.mRightSpace = rightSpace;
        this.mBottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = mTopSpace;
        outRect.left = mLeftSpace;
        outRect.right = mRightSpace;
        outRect.bottom = mBottomSpace;
    }
}
