package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.WriteAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/26  09:42.
 */

public class WriteListFragment extends BaseRefreshFragment<PracticeData> {

    public static WriteListFragment getInstance(String page, int startNum) {
        WriteListFragment writeListFragment = new WriteListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("PAGE", page);
        bundle.putInt("STARTNUMBER", startNum);
        writeListFragment.setArguments(bundle);
        return writeListFragment;
    }

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private String page;
    private int startNum;
    private int canRefresh;
    private Observable<Integer> refreshObs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArg();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshObs != null) {
            RxBus.get().unregister(C.REFRESH_WRITE_LIST, refreshObs);
        }
    }

    private void getArg() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            page = arguments.getString("PAGE");
            startNum = arguments.getInt("STARTNUMBER");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshObs = RxBus.get().register(C.REFRESH_WRITE_LIST, Integer.class);
        refreshObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == canRefresh) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    protected void setListener(List<PracticeData> data, int position) {
        super.setListener(data, position);
        canRefresh = startNum + position;
        PracticeData practiceData = data.get(position);
        if (practiceData.getFinish() == 0) {//未做题目
            WriteQuestionActivity.startWriteQuestionAct(getActivity(), canRefresh, practiceData.getId());
        } else {//做过题目了
            WriteResultActivity.startResult(getActivity(), canRefresh, practiceData.getId(), C.BELONG_INDENPDENT);
        }
    }

    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected BaseRecyclerViewAdapter<PracticeData> getAdapter() {
        return new WriteAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        if (TextUtils.isEmpty(page)) return;
        addToCompositeDis(HttpUtil
                .independence(page)
                .subscribe(new Consumer<List<PracticeData>>() {
                    @Override
                    public void accept(@NonNull List<PracticeData> dataList) throws Exception {
                        ((WriteAdapter) adapter).setStartNum(startNum);
                        if (dataList == null || dataList.isEmpty()) {
                            updateRecycleView(null, getString(R.string.str_empty_tip), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(dataList, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }
}
