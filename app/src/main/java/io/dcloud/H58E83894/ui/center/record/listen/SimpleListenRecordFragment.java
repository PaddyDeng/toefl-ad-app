package io.dcloud.H58E83894.ui.center.record.listen;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ListenRecordData;
import io.dcloud.H58E83894.data.RecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.ListenRecordAdapter;
import io.reactivex.Observable;

/**
 * Created by fire on 2017/8/11  17:18.
 */

public class SimpleListenRecordFragment extends BaseListenRecordFragment<RecordData, ListenRecordData> {

    private int page = 1;

    @Override
    protected void requestSuccess(ListenRecordData ts, boolean refresh) {
        updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
    }

    @Override
    protected Observable<ListenRecordData> getObservable() {
        return HttpUtil.simpleListenRecord(String.valueOf(page));
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected BaseRecyclerViewAdapter<RecordData> getAdapter() {
        return new ListenRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }
}
