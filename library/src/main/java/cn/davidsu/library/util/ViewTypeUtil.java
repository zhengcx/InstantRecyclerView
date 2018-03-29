package cn.davidsu.library.util;

import android.support.annotation.NonNull;


import java.util.List;

import cn.davidsu.library.MultiItemEntity;

import static cn.davidsu.library.BaseItemType.VIEW_TYPE_FOOTER;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_HEADER;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_LOAD_MORE;
import static cn.davidsu.library.BaseItemType.VIEW_TYPE_STATUS;


/**
 * Created by cxzheng on 2018/2/4.
 * 根据postion处理itemViewType逻辑的工具类
 */

public class ViewTypeUtil {

    /**
     * <p>在dataEmpty的情况根据position位置来获取对应的itemType</p>
     * <p>dataEmpty的情况下就只可能出现三种itemType:header、statusView、footer</p>
     *
     * @param position
     * @return
     */
    public static int getItemTypeIfDataEmpty(int position, boolean hasHeader) {
        switch (position) {
            case 0:
                if (hasHeader) {
                    return VIEW_TYPE_HEADER;
                } else {
                    return VIEW_TYPE_STATUS;
                }
            case 1:
                if (hasHeader) {
                    return VIEW_TYPE_STATUS;
                } else {
                    return VIEW_TYPE_FOOTER;
                }
            case 2:
                return VIEW_TYPE_FOOTER;
            default:
                return VIEW_TYPE_STATUS;
        }
    }


    /**
     * 数据不为空且有header的情况 根据position来获取对应的itemType
     *
     * @param position
     * @param dataList
     * @param <T>
     * @return
     */
    public static <T> int getItemTypeHasHeader(int position, @NonNull List<T> dataList, boolean isMultiType, boolean hasFooter) {
        if (position == 0) {
            return VIEW_TYPE_HEADER;
        } else if (position <= dataList.size()) {
            if (isMultiType) {//多item类型
                if (!(dataList.get(position - 1) instanceof MultiItemEntity)) {
                    throw new IllegalArgumentException("you has multi item type,so you must let your list items extends class MultiItemEntity");
                }
                return ((MultiItemEntity) dataList.get(position - 1)).getViewType();
            } else {
                //如果item只有一种类型，则type随意返回，最终都会拿viewManager中唯一的一个viewHolder来处理的
                return 0;
            }

        } else {
            if (hasFooter && position - dataList.size() == 1) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_LOAD_MORE;
            }

        }
    }


    /**
     * 数据不为空且没有header的情况 根据position来获取对应的itemType
     *
     * @param position
     * @param dataList
     * @param <T>
     * @return
     */
    public static <T> int getItemTypeNoHeader(int position, @NonNull List<T> dataList, boolean isMultiType, boolean hasFooter) {

        if (position < dataList.size()) {
            if (isMultiType) {//多item类型
                if (!(dataList.get(position) instanceof MultiItemEntity)) {
                    throw new IllegalArgumentException("you has multi item type,so you must let your list items extends class MultiItemEntity");
                }
                return ((MultiItemEntity) dataList.get(position)).getViewType();
            } else {
                //如果item只有一种类型，则type随意返回，最终都会拿viewManager中唯一的一个viewHolder来处理的
                return 0;
            }

        } else {
            if (hasFooter && position == dataList.size()) {
                return VIEW_TYPE_FOOTER;
            } else {
                return VIEW_TYPE_LOAD_MORE;
            }
        }
    }


}
