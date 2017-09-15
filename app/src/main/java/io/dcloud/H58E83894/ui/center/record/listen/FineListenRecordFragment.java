package io.dcloud.H58E83894.ui.center.record.listen;

import java.util.List;

import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.data.FineListenRecordData;
import io.dcloud.H58E83894.data.ListenRecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.FineListenRecordAdapter;
import io.dcloud.H58E83894.ui.make.bottom.lp.FineListenQuestionActivity;
import io.reactivex.Observable;

/**
 * Created by fire on 2017/8/11  17:18.
 */

public class FineListenRecordFragment extends BaseListenRecordFragment<FineListenRecordData, ListenRecordData> {
    private int page = 1;

    @Override
    protected void requestSuccess(ListenRecordData ts, boolean refresh) {
        refreshUi(ts.getData(), refresh);
    }

    @Override
    protected Observable<ListenRecordData> getObservable() {
        return HttpUtil.fineListenRecord(String.valueOf(page));
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected void setListener(List<FineListenRecordData> data, int position) {
        super.setListener(data, position);
        FineListenRecordData listenRecordData = data.get(position);
        FineListenQuestionActivity.startFineQuestionAct(getActivity(), listenRecordData.getTestId(), listenRecordData.getCatName());
    }

    @Override
    protected BaseRecyclerViewAdapter<FineListenRecordData> getAdapter() {
        return new FineListenRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }
}
