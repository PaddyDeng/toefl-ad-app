package io.dcloud.H58E83894.ui.center.record.speak;

/**
 * Created by fire on 2017/8/15  11:08.
 */

public class SpeakGlodFragment extends BaseSpeakRecordFragment {
    @Override
    protected int getType() {
        return 6;
    }

    private int page = 1;

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
