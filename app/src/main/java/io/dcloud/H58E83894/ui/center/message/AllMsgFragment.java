package io.dcloud.H58E83894.ui.center.message;

/**
 * Created by fire on 2017/8/11  13:43.
 */

public class AllMsgFragment extends BaseMsgFragment {

    private int page;

    @Override
    protected String getType() {
        return "0";
    }

    @Override
    protected void asyncLoadMore() {
        asyncData(String.valueOf(++page), false);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(String.valueOf(page), true);
    }
}
