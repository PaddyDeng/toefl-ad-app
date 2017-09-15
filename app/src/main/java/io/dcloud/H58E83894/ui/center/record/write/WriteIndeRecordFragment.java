package io.dcloud.H58E83894.ui.center.record.write;

/**
 * Created by fire on 2017/8/7  13:42.
 */

public class WriteIndeRecordFragment extends BaseWriteRecordFragment {

    @Override
    protected String getReadType() {
        return "2";
    }

    private int page;

    @Override
    protected void setPage(int page) {
        this.page = --page;
    }

    @Override
    protected void asyncLoadMore() {
        asyncData(++page, false);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(page, true);
    }
}
