package io.dcloud.H58E83894.ui.make.bottom.rp;

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
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.ReadData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.ReadTpoAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/8/2  15:16.
 */

public class ReadTpoFragment extends BaseRefreshFragment<PracticeData> {

    public static ReadTpoFragment getInstance(int id, int recycleHeight) {
        ReadTpoFragment readTpoFragment = new ReadTpoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        bundle.putInt("HEIGHT", recycleHeight);
        readTpoFragment.setArguments(bundle);
        return readTpoFragment;
    }

    private int id;
    private int recycleHeight;
    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private String clickId;
    private Observable<String> mObservable;

    public void getArg() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            id = arguments.getInt("ID");
            recycleHeight = arguments.getInt("HEIGHT");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArg();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mObservable = RxBus.get().register(C.REFRESH_READ_LIST, String.class);
        mObservable.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(s, clickId)) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mObservable != null) {
            RxBus.get().unregister(C.REFRESH_READ_LIST, mObservable);
        }
    }

    //不能加载更多
    @Override
    protected boolean canLoadMore() {
        return false;
    }

    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected BaseRecyclerViewAdapter<PracticeData> getAdapter() {
        return new ReadTpoAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_fifteen_divider));
    }

    @Override
    protected void setListener(List<PracticeData> data, int position) {
        super.setListener(data, position);
        if (needLogin()) {
            return;
        }
        PracticeData practiceData = data.get(position);
        clickId = practiceData.getId();
        if (practiceData.getFinish() == 0 || practiceData.getFinish() == 1) {// 0 开始做题  1 继续做题
            ReadQuestionActivity.startReadQuestionAct(getActivity(), practiceData.getId(), C.READ_BELONG_TPO);
        } else {//做题结束
            ReadResultActivity.startReadResultAct(getActivity(), practiceData.getId(), C.READ_BELONG_TPO);
        }
    }

    @Override
    protected void asyncRequest() {
        addToCompositeDis(HttpUtil
                .readTpo(String.valueOf(id))
                .subscribe(new Consumer<ReadData>() {
                    @Override
                    public void accept(@NonNull ReadData bean) throws Exception {
                        List<PracticeData> content = bean.getContent();
                        if (content == null || content.isEmpty()) {
                            updateRecycleView(null, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        } else {
                            ((ReadTpoAdapter) adapter).setItemHeight((int) ((recycleHeight - getResources().getDimension(R.dimen.read_divider) * 3) / 3));
                            updateRecycleView(content, "", InitDataType.TYPE_REFRESH_SUCCESS);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

}
