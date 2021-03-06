package io.dcloud.H58E83894.ui.make.easyResource.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.easyResource.adapter.EaPastAllAdapter;
import io.dcloud.H58E83894.ui.make.lexicalResource.adapter.PastAllAdapter;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class EaSeeAllFragment extends BaseFragment {

    public static EaSeeAllFragment getInstance(Context context, String id, int type) {
        EaSeeAllFragment speakQuestionFragment = new EaSeeAllFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SQ_ID", id);
        bundle.putInt("SQ_TYPE", type);
        speakQuestionFragment.setArguments(bundle);
        return speakQuestionFragment;
    }

    private EaPastAllAdapter adapter;
    protected Context mContext;
    private List<SpeakQuestionData> mSpeakDatas;
    private int currentPage = 0;//默认等于零
    private String id;
    private int type;

    @BindView(R.id.all_answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_tv_tip)
    TextView emptyTipTxt;
    @BindView(R.id.answer_idea_tv)
    TextView ideaTipTxt;

    @Override
    protected void getArgs() {

        Bundle arguments = getArguments();
        if (arguments == null) return;
        id = arguments.getString("SQ_ID");
        type = arguments.getInt("SQ_TYPE");
    }




    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_make_past, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asyncUiInfo();
    }

    protected void asyncUiInfo() {
        showLoadDialog();

        asyncGlodData();

    }

    private void asyncGlodData() {
        addToCompositeDis(HttpUtil
                .getEaPastAllList().subscribe(new Consumer<AllTaskData>() {
                    @Override
                    public void accept(@NonNull AllTaskData data) throws Exception {
                        dismissLoadDialog();
                        List<AllTaskData.DataBean> todayList= data.getData();
                        showAllAnswer(todayList);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }
    private void showAllAnswer(List<AllTaskData.DataBean> share) {
        if (share == null) return;
        if (share == null || share.isEmpty()) {
            Utils.setVisible(emptyTipTxt);
            Utils.setGone(mRecyclerView, ideaTipTxt);
        } else {
            initRecyclerData(share);
            Utils.setVisible(mRecyclerView);
            Utils.setGone(emptyTipTxt, ideaTipTxt);
        }
    }


    private void initRecyclerData(List<AllTaskData.DataBean> share) {
        if (adapter == null) {
            initRecycler(mRecyclerView, new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            adapter = new EaPastAllAdapter(share, getContext(), mRxPermissions);
            mRecyclerView.setAdapter(adapter);
        } else {
//            adapterDispose();
//            adapter.update(share);
        }
    }

//    private void adapterDispose() {
//        if (adapter == null) return;
//        List<SpeakShare> share = adapter.getDate();
//        if (share == null || share.isEmpty()) return;
//        for (SpeakShare ss : share) {
//            Disposable disposable = ss.mDisposable;
//            if (disposable != null && !disposable.isDisposed()) {
//                disposable.dispose();
//            }
//        }
//    }





}
