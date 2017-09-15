package io.dcloud.H58E83894.data.prelesson;

import java.util.List;

/**
 * Created by fire on 2017/7/21  11:35.
 */

public class PreLessonData {
    private List<BannerData> image;
    private List<LessonData> pubClass;
    private ChineseData data;
    private List<LessonData> hotData;

    public List<BannerData> getImage() {
        return image;
    }

    public void setImage(List<BannerData> image) {
        this.image = image;
    }

    public List<LessonData> getPubClass() {
        return pubClass;
    }

    public void setPubClass(List<LessonData> pubClass) {
        this.pubClass = pubClass;
    }

    public ChineseData getData() {
        return data;
    }

    public void setData(ChineseData data) {
        this.data = data;
    }

    public List<LessonData> getHotData() {
        return hotData;
    }

    public void setHotData(List<LessonData> hotData) {
        this.hotData = hotData;
    }
}
