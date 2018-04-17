package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.ViewPagerFragmentAdapter;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.ListenChildData;
import io.dcloud.H58E83894.data.make.ListenQuestionData;
import io.dcloud.H58E83894.data.make.ListenSecRecordData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.data.make.OptionInfoData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.make.adapter.NewSerialAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.weiget.PlayAudioView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class ListenMakeResultActivity extends BaseActivity {

    public static void startSecTestAct(Context c, String id) {
        Intent intent = new Intent(c, ListenMakeResultActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        c.startActivity(intent);
    }

    @BindView(R.id.analyze_des_tv)
    TextView analyzeTv;
    @BindView(R.id.listen_make_result_tv)
    TextView resutlDes;
    @BindView(R.id.question_serial_num)
    RecyclerView mRecyclerView;
    @BindView(R.id.question_and_answer_viewpage)
    ViewPager mViewPager;
    @BindView(R.id.listen_make_result_play_audio_view)
    PlayAudioView mPlayView;
    private String id;
    private List<OptionInfoData> optInfoList;
    private List<Fragment> fragList;
    private NewSerialAdapter adapter;
    private int recyclerWidth;
    private RxDownload rxDownload;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mRecyclerView,this);
                recyclerWidth = mRecyclerView.getMeasuredWidth();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_make_result);
    }

    @Override
    protected void asyncUiInfo() {

        if (TextUtils.isEmpty(id)) return;
        Log.i("catId4", id);
        addToCompositeDis(HttpUtil
                .listenTopicRequest(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ListenQuestionData>() {
                    @Override
                    public void accept(@NonNull ListenQuestionData data) throws Exception {
                        dismissLoadDialog();
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    @OnClick({R.id.new_start_make})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_start_make://重新做题
                newMake();
                break;
            default:
        }
    }

    private void newMake() {
        addToCompositeDis(HttpUtil
                .deleteListen(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.REFRESH_LISTEN_LIST, id);
                            finish();
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(ListenQuestionData data) {
        if (data == null) return;
        ListenTpoContentData currentData = data.getCurrentData();
        Log.i("catId", currentData.toString());
        analyzeTv.setText(currentData.getName());
        if (currentData == null) return;
        List<ListenChildData> child = currentData.getChild();
        if (child == null || child.isEmpty()) return;
        List<ListenSecRecordData> record = currentData.getRecord();
        if (record == null || record.isEmpty()) return;
        init(child, record);
        final String url = RetrofitProvider.TOEFLURL + currentData.getFile();
        if (rxDownload == null)
            rxDownload = RxDownload.getInstance(mContext);
        DownloadUtil.download(url, rxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus status) {
                if (status.getPercentNumber() == 100) {
                    File[] record = rxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        mPlayView.setInit(true, (int) time, url, mRxPermissions, rxDownload);
                    }
                }
            }
        });
    }

    private void init(List<ListenChildData> child, List<ListenSecRecordData> record) {
        if (optInfoList == null) optInfoList = new ArrayList<>();
        if (fragList == null) fragList = new ArrayList<>();
        int index = 0;
        int correctNum = 0;
        optInfoList.clear();
        for (ListenSecRecordData rd : record) {
            OptionInfoData oif = new OptionInfoData();
            if (index == 0) {
                oif.setSelected(true);
            }
            ListenChildData childData = child.get(index);

            childData.setUserChooseAnswer(rd.getAnswer());
            oif.setTopicNum(++index);
            boolean equals = TextUtils.equals(rd.getAnswer(), rd.getTrueAnswer());
            if (equals) correctNum++;
            oif.setCorrect(equals);
            optInfoList.add(oif);
            fragList.add(ListenResultFragment.getInstance(childData));
        }
        initRecycler(optInfoList);
        initViewPager(fragList);
        resutlDes.setText(getString(R.string.str_make_result_num, correctNum, record.size()));
    }

    private void initViewPager(List<Fragment> list) {
        mViewPager.setAdapter(new ViewPagerFragmentAdapter(getSupportFragmentManager(), list));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetRecycler(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void resetRecycler(int position) {
        for (int i = 0, size = optInfoList.size(); i < size; i++) {
            OptionInfoData optionInfoData = optInfoList.get(i);
            if (i == position) {
                optionInfoData.setSelected(true);
            } else {
                optionInfoData.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void initRecycler(final List<OptionInfoData> list) {
        adapter = new NewSerialAdapter(mContext, list);
        adapter.setItemWidth(recyclerWidth / list.size());
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                resetRecycler(position);
                mViewPager.setCurrentItem(position, false);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayView.onDetach();
    }
}
