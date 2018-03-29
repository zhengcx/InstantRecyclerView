package cn.davidsu.library.util;

import java.util.List;

/**
 * Created by cxzheng on 2018/2/3.
 */

public class ListUtil {
    public ListUtil() {
    }

    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

}
