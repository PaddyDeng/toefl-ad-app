package io.dcloud.H58E83894.data.make;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fire on 2017/8/3  16:12.
 */

public class ReadAnswerTypeData {
    private String time;
    @SerializedName("true")
    private int corrertNum;
    private int sum;
    private int accuracy;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCorrertNum() {
        return corrertNum;
    }

    public void setCorrertNum(int corrertNum) {
        this.corrertNum = corrertNum;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
}
