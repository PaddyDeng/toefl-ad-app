package io.dcloud.H58E83894.ui.make.everyDayListen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.instestlisten.AllInstListenData;
import io.dcloud.H58E83894.data.record.ReadRecordData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.center.record.adapter.ReadRecordAdapter;
import io.dcloud.H58E83894.ui.make.bottom.rp.ReadQuestionActivity;
import io.dcloud.H58E83894.ui.make.bottom.rp.ReadResultActivity;
import io.dcloud.H58E83894.ui.make.everyDayListen.adapter.MyAllEveListenAdapter;
import io.dcloud.H58E83894.ui.make.everyDayListen.adapter.MyEveListenAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.DownloadAdapter;
import io.dcloud.H58E83894.ui.toeflcircle.CommunitysDetailActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/7  09:19.
 */

public abstract class BaseColletionRecordFragment extends BaseRefreshFragment<AllInstListenData.DataBean> {

   private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());

    protected void asyncData(final boolean refresh,  int pages) {
        addToCompositeDis(HttpUtil.getInstAll( pages)
                .subscribe(new Consumer<AllInstListenData>() {
                    @Override
                    public void accept(@NonNull AllInstListenData bean) throws Exception {
                        List<AllInstListenData.DataBean> data = bean.getData();
                        if (data != null && !data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            asyncFail(refresh, "加载失败", true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        asyncFail(refresh, errorTip(throwable), false);
                    }
                }));
    }

    private void asyncFail(boolean refresh, String msg, boolean loadBoo) {
        if (refresh) {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_REFRESH_SUCCESS : InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, loadBoo ? InitDataType.TYPE_LOAD_MORE_SUCCESS : InitDataType.TYPE_LOAD_MORE_FAIL);
        }
        if (!loadBoo) {
            asyncFail();
        }
    }

    protected abstract void asyncFail();

    @Override
    public BaseRecyclerViewAdapter<AllInstListenData.DataBean> getAdapter() {
        return new MyAllEveListenAdapter(getActivity(), null, mManager);
    }

    @Override
    public void setListener(List<AllInstListenData.DataBean> data, int position) {
        super.setListener(data, position);
//        DownloadData downloadData = data.get(position);
//        if (downloadData != null) {
//            CommunitysDetailActivity.startCommunity(getActivity(), downloadData.getId());
//        }
    }

    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_height_divider));
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

//    public abstract int getSelectId();
//
//    public abstract int getIndex();
}
