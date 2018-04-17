package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.callback.ICallBackImp;
import io.dcloud.H58E83894.data.MediaData;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.data.make.ListenChildData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.make.adapter.ListenSecAnswerAdapter;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/8/9  16:14.
 */

public class ListenResultFragment extends BaseFragment {

    public static ListenResultFragment getInstance(ListenChildData childData) {
        ListenResultFragment fragment = new ListenResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("CHILDDATA", childData);
        Log.i("catId1", childData.toString());
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.result_listen_sec_question_tv)
    TextView questionTv;
    @BindView(R.id.result_listen_sec_test_img)
    ImageView voiceImg;
    @BindView(R.id.result_answer_recycler)
    RecyclerView mRecyclerView;
    private ListenChildData childData;
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F"};
    private List<AnswerData> mDataList;
    private ListenSecAnswerAdapter mAdapter;
    private String url;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private MusicPlayer player;
    private boolean isMultChoose;
    private int answerNum;

    @Override
    protected void getArgs() {
        super.getArgs();
        Bundle bundle = getArguments();
        if (bundle == null) return;
        childData = bundle.getParcelable("CHILDDATA");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_listen_make_layout, container, false);
    }

    @OnClick({R.id.result_listen_sec_test_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.result_listen_sec_test_img:
                if (TextUtils.isEmpty(url)) return;
                if (player == null)
                    player = new MusicPlayer();
                AnimationDrawable mAnimationDrawable = (AnimationDrawable) ((ImageView) view).getDrawable();
                play(url, mAnimationDrawable);
                break;
            default:
                break;
        }
    }

    private void play(final String url, final AnimationDrawable mAnimationDrawable) {

        player.play(mRxPermissions, mRxDownload, url,
                null, new ICallBackImp() {
                    @Override
                    public void onSuccess(Object o) {
                        mAnimationDrawable.start();
                    }
                }, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mAnimationDrawable.stop();
                        mAnimationDrawable.selectDrawable(0);
                    }
                });

//        mRxPermissions.request(READ_EXTERNAL_STORAGE)
//                .doOnNext(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(@NonNull Boolean aBoolean) throws Exception {
//                        if (!aBoolean) {
//                            throw new RuntimeException("no permission");
//                        }
//                    }
//                }).compose(new ObservableTransformer<Boolean, DownloadRecord>() {
//            @Override
//            public ObservableSource<DownloadRecord> apply(Observable<Boolean> upstream) {
//                return upstream.flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
//                    @Override
//                    public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
//                        return mRxDownload.getDownloadRecord(url);
//                    }
//                });
//            }
//        }).compose(new SchedulerTransformer<DownloadRecord>())
//                .subscribe(new Consumer<DownloadRecord>() {
//                    @Override
//                    public void accept(@NonNull DownloadRecord record) throws Exception {
//                        List<MediaData> list = new ArrayList<MediaData>();
//                        MediaData data = new MediaData();
//                        data.setName(record.getSaveName());
//                        data.setPath(TextUtils.concat(record.getSavePath(), File.separator, record.getSaveName()).toString());
//                        list.add(data);
//                        player.setQueue(list, list.size() - 1);
//                        player.setCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                mAnimationDrawable.stop();
//                                mAnimationDrawable.selectDrawable(0);
//                            }
//                        });
//                        mAnimationDrawable.start();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRxDownload = RxDownload.getInstance(getActivity());
        mRxPermissions = new RxPermissions(getActivity());
        if (TextUtils.isEmpty(childData.getFileAdd())) {
            Utils.setGone(voiceImg);
        } else {
            //后台自动下载
            url = RetrofitProvider.TOEFLURL + childData.getFileAdd();
            DownloadUtil.downloadService(url, mRxDownload, mRxPermissions);
            Utils.setVisible(voiceImg);
        }
        questionTv.setText(childData.getName());
        initRecycler();
    }

    private void initRecycler() {
        initRecycler(mRecyclerView, new LinearLayoutManager(getActivity()));
        String optionStr = childData.getQuestSelect();

        String questType = childData.getQuestType();

        int type = 0;
        if (!TextUtils.isEmpty(questType)) {
            type = Integer.parseInt(questType);
        }

        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.clear();

        String[] split = null;
        if (optionStr.contains("\\r\\n")) {
            split = Utils.splitOption(optionStr);
        } else {
            split = Utils.splitOptionThroughN(optionStr);
        }

        if (type == 1) {
            formAdapter(split);
            return;
        }

        defalutAdapter(split, type == 2);
    }

    private void defalutAdapter(String[] split, boolean sort) {
        for (int i = 0, size = split.length; i < size; i++) {
            AnswerData ans = new AnswerData();
            ans.setOption(options[i]);
            ans.setContent(split[i]);
            mDataList.add(ans);
        }
        String answer = childData.getAnswer().trim();
        answer = Utils.removerepeatedchar(answer);
        answerNum = answer.length();
        isMultChoose = answerNum != 1;
        if (mAdapter == null) {
            mAdapter = new ListenSecAnswerAdapter(mDataList);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList);
        }
//        mAdapter.setMultChooseAndSort(isMultChoose, Integer.parseInt(childData.getQuestType()) == 2);
        mAdapter.setMultChooseAndSort(childData.getAnswer().length() > 1, sort, childData.getAnswer(), childData.getUserChooseAnswer());
    }

    private void formAdapter(String[] split) {
        if (split == null || split.length == 0) return;
        String formOption = split[0];
        for (int i = 1, size = split.length; i < size; i++) {
            AnswerData ans = new AnswerData();
            ans.setFormOption(formOption);
            ans.setContent(split[i]);
            mDataList.add(ans);
        }
        if (mAdapter == null) {
            mAdapter = new ListenSecAnswerAdapter(mDataList, false);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.upDate(mDataList, false);
        }
        mAdapter.setFormAnswer(childData.getAnswer(), childData.getUserChooseAnswer());
    }
}
