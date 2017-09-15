package io.dcloud.H58E83894.ui.center.record.listen;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.data.ListenRecordData;
import io.dcloud.H58E83894.data.RecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.ListenRecordAdapter;
import io.dcloud.H58E83894.ui.make.bottom.lp.ListenMakeResultActivity;
import io.dcloud.H58E83894.ui.make.bottom.lp.ListenSecQuestionActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/11  17:18.
 */

public class MakeListenRecordFragment extends BaseListenRecordFragment<RecordData, ListenRecordData> {

    private int page = 1;
    private Observable<String> busObs;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        busObs = RxBus.get().register(C.REFRESH_LISTEN_LIST, String.class);
        busObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                asyncRequest();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (busObs != null) {
            RxBus.get().unregister(C.REFRESH_LISTEN_LIST, busObs);
        }
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected void setListener(List<RecordData> data, int position) {
        super.setListener(data, position);
        RecordData recordData = data.get(position);
        int child = recordData.getTotal();
        int num = Integer.parseInt(recordData.getNum());
        boolean makeEnd = child == num ? true : false;
        String id = recordData.getId();
        if (makeEnd) {
            ListenMakeResultActivity.startSecTestAct(getActivity(), id);
        } else {
            ListenSecQuestionActivity.startListenSecQuestionAct(getActivity(), id, recordData.getTitle());
        }
    }

    @Override
    protected BaseRecyclerViewAdapter<RecordData> getAdapter() {
        return new ListenRecordAdapter(getActivity(), null, mManager);
    }

    @Override
    protected void requestSuccess(ListenRecordData ts, boolean refresh) {
        refreshUi(ts.getRecord(), refresh);
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }


    @Override
    protected Observable<ListenRecordData> getObservable() {
        return HttpUtil.makeListenRecord(String.valueOf(page));
    }
}
