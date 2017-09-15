package io.dcloud.H58E83894.data.make.type;

import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.utils.C;

/**
 * Created by fire on 2017/7/27  10:07.
 */

public class TpoDes implements TypeTpo {

    private String catName;
    private PracticeData question;
    private boolean isLastDesItem;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public PracticeData getQuestion() {
        return question;
    }

    public void setQuestion(PracticeData question) {
        this.question = question;
    }

    public boolean isLastDesItem() {
        return isLastDesItem;
    }

    public void setLastDesItem(boolean lastDesItem) {
        isLastDesItem = lastDesItem;
    }

    @Override
    public int getType() {
        return C.TYPE_DES;
    }
}
