package io.dcloud.H58E83894.data.make;

/**
 * Created by fire on 2017/8/8  11:15.
 */

public class ListenQuestionData {


    @Override
    public String toString() {
        return "ListenQuestionData{" +
                "currentData=" + currentData +
                '}';
    }

    private ListenTpoContentData currentData;

    public ListenTpoContentData getCurrentData() {
        return currentData;
    }

    public void setCurrentData(ListenTpoContentData currentData) {
        this.currentData = currentData;
    }
}
