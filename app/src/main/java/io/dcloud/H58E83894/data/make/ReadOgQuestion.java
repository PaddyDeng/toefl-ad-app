package io.dcloud.H58E83894.data.make;

import java.util.List;

/**
 * Created by fire on 2017/8/3  09:48.
 */

public class ReadOgQuestion {
    private String catName;
    private String catId;
    private List<PracticeData> question;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public List<PracticeData> getQuestion() {
        return question;
    }

    public void setQuestion(List<PracticeData> question) {
        this.question = question;
    }
}
