package com.mshy.VInterestSpeed.uikit.common.util.media;

import com.mshy.VInterestSpeed.uikit.common.util.sys.TimeUtil;
import com.mshy.VInterestSpeed.uikit.common.media.model.GLImage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 */
public class MediaUtil {
    public static Map<String, List<GLImage>> divideMedias(List<GLImage> images) {
        Map<String, List<GLImage>> imageItemMap = new LinkedHashMap<>();
        for (int i = 0; i < images.size(); i++) {
            GLImage GLImage = images.get(i);
            String date = TimeUtil.getDateString(GLImage.getAddTime());
            if (imageItemMap.get(date) != null) {
                List<GLImage> GLImageList = imageItemMap.get(date);
                GLImageList.add(GLImage);
                imageItemMap.put(date, GLImageList);
            } else {
                List<GLImage> GLImageList = new ArrayList<>(1);
                GLImageList.add(GLImage);
                imageItemMap.put(date, GLImageList);
            }
        }

        return imageItemMap;
    }
}