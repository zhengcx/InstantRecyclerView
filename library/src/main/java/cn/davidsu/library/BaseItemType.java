package cn.davidsu.library;

/**
 * Created by cxzheng on 2018/2/3.
 * <p>提供的几种基础ItemType</p>
 */

public class BaseItemType {

    //header
    public static final int VIEW_TYPE_HEADER = 1 << 0;
    //footer
    public static final int VIEW_TYPE_FOOTER = 1 << 1;

    //状态View(包括loading、dataEmpty、load failed等状态)
    public static final int VIEW_TYPE_STATUS = 1 << 3;

    //加载更多
    public static final int VIEW_TYPE_LOAD_MORE = 1 << 4;
}
