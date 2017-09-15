package io.dcloud.H58E83894.data.make;

import java.util.List;

/**
 * Created by fire on 2017/8/3  16:11.
 */

public class ReadResultData {

    private List<ReadResultDetailData> data;
    private ReadAnswerTypeData answerType;

    public List<ReadResultDetailData> getData() {
        return data;
    }

    public void setData(List<ReadResultDetailData> data) {
        this.data = data;
    }

    public ReadAnswerTypeData getAnswerType() {
        return answerType;
    }

    public void setAnswerType(ReadAnswerTypeData answerType) {
        this.answerType = answerType;
    }
}
