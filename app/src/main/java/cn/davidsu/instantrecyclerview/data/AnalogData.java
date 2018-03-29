package cn.davidsu.instantrecyclerview.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.davidsu.library.MultiItemEntity;

import static cn.davidsu.instantrecyclerview.MainListAdapter.VIEW_TYPE_1;
import static cn.davidsu.instantrecyclerview.MainListAdapter.VIEW_TYPE_2;

/**
 * Created by cxzheng on 2018/3/29.
 * analog data
 */

public class AnalogData {

    public static List<MultiItemEntity> getPageData(int dataSize) {
        List<MultiItemEntity> datas = new ArrayList<>();
        for (int i = 0; i < dataSize; i++) {
            if (rand() == 0) {
                datas.add(new MultiItemEntity(VIEW_TYPE_1, "我是Type1"));
            } else {
                datas.add(new MultiItemEntity(VIEW_TYPE_2, "Type2"));
            }
        }
        return datas;
    }


    public static int rand() {
        Random rd = new Random();
        return rd.nextInt(2);
    }
}
