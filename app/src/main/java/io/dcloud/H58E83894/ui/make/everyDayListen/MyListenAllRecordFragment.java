package io.dcloud.H58E83894.ui.make.everyDayListen;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.instestlisten.AllInstListenData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.read.BaseReadRecordFragment;
import io.dcloud.H58E83894.ui.make.everyDayListen.adapter.MyAllEveListenAdapter;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/7  09:20.
 */

public class MyListenAllRecordFragment extends BaseRefreshFragment<AllInstListenData.DataBean> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());

    private int page;

    @Override
    public void onResume() {
        super.onResume();
        asyncRequest();
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        netRequest(false);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    @Override
    protected BaseRecyclerViewAdapter<AllInstListenData.DataBean> getAdapter() {
        return new MyAllEveListenAdapter(getActivity(), null, manager);
    }


    @Override
    protected void asyncRequest() {

        page = 1;
        netRequest(true);

    }

    public void netRequest(final boolean isRefresh) {


        addToCompositeDis(HttpUtil.getInstAll( page)
                .subscribe(new Consumer<AllInstListenData>() {
                    @Override
                    public void accept(@NonNull AllInstListenData bean) throws Exception {

                        asyncSuccess(bean, isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwableDeal(throwable, isRefresh);
                    }
                }));
    }

    private void asyncSuccess(@NonNull AllInstListenData data, boolean isRefresh) {
        List<AllInstListenData.DataBean> contentData = data.getData();
        if (contentData == null || contentData.isEmpty()) {
            if (isRefresh) {
                updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
            } else {
                updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        } else {

            if (isRefresh) {
                updateRecycleView(contentData, "", InitDataType.TYPE_REFRESH_SUCCESS);
            } else {
                updateRecycleView(contentData, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        }
    }

    private void throwableDeal(@NonNull Throwable throwable, boolean isRefresh) {
        throwable.printStackTrace();
        if (isRefresh) {
            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
            page--;
        }
    }

    protected String throwMsg(Throwable throwable) {
        return Utils.onError(throwable);
    }




//    private int page;
//
//    @Override
//    protected void asyncFail() {
//        page--;
//        page = page < 1 ? 1 : page;
//    }
//
////    @Override
////    public int getSelectId() {
////        return 0;
////    }
////
////    @Override
////    public int getIndex() {
////        return 0;
////    }
//
//    @Override
//    protected void asyncLoadMore() {
//        ++page;
//        asyncData(false, page);
//
//    }
//
//    @Override
//    protected void asyncRequest() {
//
//        page = 1;
//        asyncData(true, page);
//    }
}
