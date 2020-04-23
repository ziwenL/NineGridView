package com.ziwenl.ninegridview.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * PackageName : com.ziwenl.ninegridview.utils
 * Author : Ziwen Lan
 * Date : 2020/4/13
 * Time : 11:40
 * Introduction :
 *
 * 网络图片链接库 未接接口时测试用
 */
public class NetImageUtil {

    private static String[] gridPicArray = new String[]{
            "http://p1.music.126.net/jFb8PqTntpyecI2KX60BLQ==/109951163808953237.jpg",
            "http://p1.music.126.net/TZD99vqXBTOvy_sC8y_n3w==/109951163811355512.jpg",
            "http://p1.music.126.net/KSs9T-FypFuLxu59AOudAA==/109951163808924079.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2172785892,3775136615&fm=26&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3402703251,3634507809&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=4257346747,3809132198&fm=26&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=930068019,2103426518&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1624240531,2195794812&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3493362120,3153261029&fm=26&gp=0.jpg"
    };

    public static String getPicUrl(int posistion) {
        return gridPicArray[posistion % gridPicArray.length];
    }

    public static List<String> getUrls(int count) {
        List<String> gridPicList = new ArrayList<>();
        for (int position = 0; position < count; position++) {
            gridPicList.add(gridPicArray[position % gridPicArray.length]);
        }
        return gridPicList;
    }
}
