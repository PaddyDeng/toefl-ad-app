package io.dcloud.H58E83894.ui.center.record.read;

/**
 * Created by fire on 2017/8/7  09:20.
 */

public class ReadAllRecordFragment extends BaseReadRecordFragment {
    @Override
    protected String getReadType() {
        return "1";
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
