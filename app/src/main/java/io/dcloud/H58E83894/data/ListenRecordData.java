package io.dcloud.H58E83894.data;

import java.util.List;

/**
 * Created by fire on 2017/8/11  17:35.
 */

public class ListenRecordData {
    private List<RecordData> record;//做题
    private List<FineListenRecordData> data;//精听

    public List<RecordData> getRecord() {
        return record;
    }

    public void setRecord(List<RecordData> record) {
        this.record = record;
    }

    public List<FineListenRecordData> getData() {
        return data;
    }

    public void setData(List<FineListenRecordData> data) {
        this.data = data;
    }
}
