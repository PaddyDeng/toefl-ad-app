package io.dcloud.H58E83894.data.make;

import java.util.List;

/**
 * Created by fire on 2017/7/27  10:02.
 */

public class WriteTpoData {
    private String catName;
    private String catId;
    private int num;
    private List<PracticeData> question;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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
