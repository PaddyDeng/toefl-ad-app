package io.dcloud.H58E83894.ui.make.lexicalResource.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.OnlyMineData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.common.RecordSureProxy;
import io.dcloud.H58E83894.ui.make.lexicalResource.adapter.PastMineAdapter;
import io.dcloud.H58E83894.utils.FileUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class OnlyMineFragment extends BaseFragment {

    public static OnlyMineFragment getInstance(Context context, String id, int type) {
        OnlyMineFragment speakQuestionFragment = new OnlyMineFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SQ_ID", id);
        bundle.putInt("SQ_TYPE", type);
        speakQuestionFragment.setArguments(bundle);
        return speakQuestionFragment;
    }

    private PastMineAdapter adapter;
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

    RxPermissions rxPermissions;


    Calendar mCalendar;
    private boolean isTure = true;

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
        rxPermissions = new RxPermissions(getActivity());
        asyncUiInfo();
    }

    protected void asyncUiInfo() {
        showLoadDialog();
        asyncGlodData();
    }

    private void asyncGlodData() {

        addToCompositeDis(HttpUtil
                .getOnlyMineList().subscribe(new Consumer<OnlyMineData>() {
                    @Override
                    public void accept(@NonNull OnlyMineData data) throws Exception {
                        dismissLoadDialog();
                        List<OnlyMineData.DataBean> todayList= data.getData();
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


    private void showAllAnswer(List<OnlyMineData.DataBean> todayList) {
        if (todayList == null) return;
        if (todayList == null || todayList.isEmpty()) {
            Utils.setVisible(emptyTipTxt);
            Utils.setGone(mRecyclerView, ideaTipTxt);
        } else {
            initRecyclerData(todayList);
            Utils.setVisible(mRecyclerView);
            Utils.setGone(emptyTipTxt, ideaTipTxt);
        }
    }


    private void initRecyclerData(List<OnlyMineData.DataBean> share) {
        if (adapter == null) {
            adapter = new PastMineAdapter(share, getActivity(), rxPermissions);
            initRecycler(mRecyclerView, new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            mRecyclerView.setAdapter(adapter);
        } else {
            adapterDispose();
            adapter.update(share);
        }
    }

    private void adapterDispose() {
        if (adapter == null) return;
        List<OnlyMineData.DataBean> share = adapter.getDate();
        if (share == null || share.isEmpty()) return;
        for (OnlyMineData.DataBean ss : share) {
            Disposable disposable = ss.mDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterDispose();
        if (VoiceManager.getInstance(getActivity()) != null) {
            VoiceManager.getInstance(getActivity()).stopRecordAndPlay();
        }
    }
}
