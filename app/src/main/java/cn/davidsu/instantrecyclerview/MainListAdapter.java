package cn.davidsu.instantrecyclerview;

import cn.davidsu.instantrecyclerview.itemdelegate.Type1Delegate;
import cn.davidsu.instantrecyclerview.itemdelegate.Type2Delegate;
import cn.davidsu.library.SuperBaseAdapter;

/**
 * Created by cxzheng on 2018/3/29.
 */

public class MainListAdapter extends SuperBaseAdapter {

    public static int VIEW_TYPE_1 = 1 << 5;
    public static int VIEW_TYPE_2 = 1 << 6;

    public MainListAdapter() {
        addItemType(VIEW_TYPE_1, new Type1Delegate());
        addItemType(VIEW_TYPE_2, new Type2Delegate());
    }
}
