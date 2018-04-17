package io.dcloud.H58E83894.ui.make.bottom.sp;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.ViewPagerFragmentAdapter;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.make.SpeakData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.common.RecordProxy;
import io.dcloud.H58E83894.ui.make.adapter.SpeakAnswerAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.dcloud.H58E83894.weiget.ObservableScrollView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SpeakQuestionActivity extends BaseActivity {

    public static void startSpeakQuestionAct(Context context, String id, int type) {
        Intent intent = new Intent(context, SpeakQuestionActivity.class);
        intent.putExtra("SQ_ID", id);
        intent.putExtra("SQ_TYPE", type);
        context.startActivity(intent);
    }

    @BindView(R.id.speak_question_all_answer_conaainer)
    RelativeLayout allAnswerContainer;
    @BindView(R.id.speak_question_answer_idea_conaainer)
    RelativeLayout ideaContainer;
    @BindView(R.id.top_container)
    RelativeLayout topContainer;
    @BindView(R.id.bottom_container)
    RelativeLayout bottomContainer;
    @BindView(R.id.speak_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.all_answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_tv_tip)
    TextView emptyTipTxt;
    @BindView(R.id.answer_idea_tv)
    TextView ideaTipTxt;
    @BindView(R.id.container_answer)
    RelativeLayout answerCircleContainer;
    @BindView(R.id.speak_scroll_view)
    ObservableScrollView mScrollView;

    private String id;
    private int type;
    private int topOriginalHeigth;
    private int bottomOriginalHeigth;
    private List<SpeakQuestionData> mSpeakDatas;
    private int currentPage = 0;//默认等于零
    private boolean clickIdea = false;
    private SpeakAnswerAdapter adapter;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra("SQ_ID");
        type = intent.getIntExtra("SQ_TYPE", 0);
    }

    @Override
    protected void initView() {
        mScrollView.setOnScrollListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScroll(int oldy, int dy, boolean isUp) {
                if (isUp) {
                    Utils.setVisible(answerCircleContainer);
                } else {
                    Utils.setGone(answerCircleContainer);
                }
            }
        });
    }

    @Override
    protected void asyncUiInfo() {
        showLoadDialog();
        if (type == C.TYPE_SPEAK_GLOD) {
            asyncGlodData(id, false);
        } else if (type == C.TYPE_SPEAK_TPO) {
            asyncTpoData();
        }
    }

    private void asyncTpoData() {
        String[] split = id.split(",");
        List<Observable<SpeakQuestionData>> list = new ArrayList<>();
        for (int i = 0, size = split.length; i < size; i++) {
            if (!TextUtils.isEmpty(split[i]))
                list.add(HttpUtil.zipSpeakDetail(split[i]));
        }
        addToCompositeDis(Observable
                .zipIterable(list, new Function<Object[], List<SpeakQuestionData>>() {
                    @Override
                    public List<SpeakQuestionData> apply(@NonNull Object[] objects) throws Exception {
                        List<SpeakQuestionData> sqLists = new ArrayList<>();
                        for (Object obj : objects) {
                            SpeakQuestionData sqd = (SpeakQuestionData) obj;
                            sqLists.add(sqd);
                        }
                        return sqLists;
                    }
                }, false, list.size())
                .compose(new SchedulerTransformer<List<SpeakQuestionData>>())
                .subscribe(new Consumer<List<SpeakQuestionData>>() {
                    @Override
                    public void accept(@NonNull List<SpeakQuestionData> datas) throws Exception {
                        dismissLoadDialog();
                        refreshUi(datas);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(List<SpeakQuestionData> datas) {
        if (datas == null || datas.isEmpty()) return;
        mSpeakDatas = datas;
        List<Fragment> mTopList = new ArrayList<>();
        for (int i = 0, size = datas.size(); i < size; i++) {
            SpeakData data = datas.get(i).getData();
            data.setAllPageSize(size);
            data.setCurrentPage(i + 1);
            mTopList.add(SpeakQuestionFragment.getInstance(data));
        }
        try {
            ViewPagerFragmentAdapter mAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), mTopList);
            mViewPager.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                switchContainer(clickIdea);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        switchContainer(false);
    }

    private void asyncGlodData(String id, final boolean commitSpoken) {
        addToCompositeDis(HttpUtil
                .speakDetail(id)
                .subscribe(new Consumer<SpeakQuestionData>() {
                    @Override
                    public void accept(@NonNull SpeakQuestionData data) throws Exception {
                        dismissLoadDialog();
                        if (commitSpoken) {
                            if (data == null) return;
                            if (mSpeakDatas == null) return;
                            mSpeakDatas.set(currentPage, data);
                            switchContainer(false);
                        } else {
                            List<SpeakQuestionData> lists = new ArrayList<>();
                            lists.add(data);
                            refreshUi(lists);
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

    private void switchContainer(boolean selectedIdea) {
        clickIdea = selectedIdea;
        if (selectedIdea) {
            ideaContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_speak_select));
            allAnswerContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_speak_no_select));
            showIdea();
        } else {
            allAnswerContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_speak_select));
            ideaContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_speak_no_select));
            showAllAnswer();
        }
    }

    private void showAllAnswer() {
        if (mSpeakDatas == null) return;
        SpeakQuestionData data = mSpeakDatas.get(currentPage);
        List<SpeakShare> share = data.getShare();
        if (share == null || share.isEmpty()) {
            Utils.setVisible(emptyTipTxt);
            Utils.setGone(mRecyclerView, ideaTipTxt);
        } else {
            initRecyclerData(share);
            Utils.setVisible(mRecyclerView);
            Utils.setGone(emptyTipTxt, ideaTipTxt);
        }
    }

    private void initRecyclerData(List<SpeakShare> share) {
        if (adapter == null) {
            initRecycler(mRecyclerView, new LinearLayoutManager(mContext));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(mContext, LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            adapter = new SpeakAnswerAdapter(share, mContext, mRxPermissions);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapterDispose();
            adapter.update(share);
        }
    }

    private void adapterDispose() {
        if (adapter == null) return;
        List<SpeakShare> share = adapter.getDate();
        if (share == null || share.isEmpty()) return;
        for (SpeakShare ss : share) {
            Disposable disposable = ss.mDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapterDispose();
        if (VoiceManager.getInstance(mContext) != null) {
            VoiceManager.getInstance(mContext).stopRecordAndPlay();
        }
    }

    private void showIdea() {
        if (mSpeakDatas == null) return;
        SpeakQuestionData data = mSpeakDatas.get(currentPage);
        SpeakData speakData = data.getData();
        if (speakData == null) return;
        String description = speakData.getDescription().trim();
        if (TextUtils.isEmpty(description)) {
            Utils.setVisible(emptyTipTxt);
            Utils.setGone(mRecyclerView, ideaTipTxt);
        } else {
            ideaTipTxt.setText(description);
            Utils.setVisible(ideaTipTxt);
            Utils.setGone(emptyTipTxt, mRecyclerView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak_question);
        bottomContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(bottomContainer,this);
                bottomOriginalHeigth = bottomContainer.getMeasuredHeight();
            }
        });
        topContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(topContainer,this);
                topOriginalHeigth = topContainer.getMeasuredHeight();
            }
        });
    }

    public void switchContainer() {
        if (bottomContainer.getMeasuredHeight() < topOriginalHeigth) {
            heightIncerement(bottomContainer, bottomOriginalHeigth, topOriginalHeigth);
            heightIncerement(topContainer, topOriginalHeigth, bottomOriginalHeigth);
        } else {
            heightIncerement(topContainer, bottomOriginalHeigth, topOriginalHeigth);
            heightIncerement(bottomContainer, topOriginalHeigth, bottomOriginalHeigth);
        }
    }

    @OnClick({R.id.speak_question_all_answer_conaainer, R.id.speak_question_answer_idea_conaainer, R.id.record_answer})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.speak_question_answer_idea_conaainer:
                switchContainer(true);
                break;
            case R.id.speak_question_all_answer_conaainer:
                switchContainer(false);
                break;
            case R.id.record_answer:
                if (needLogin()) return;
                recAudio();
                break;
            default:
                break;
        }
    }

    private void recAudio() {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            RecordProxy.showRecordDialog(mContext, VoiceManager.getInstance(mContext), new ICallBack<String>() {
                                @Override
                                public void onSuccess(String absFilePath) {
                                    commitRecordAudio(absFilePath);
                                }

                                @Override
                                public void onFail() {

                                }
                            });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }

    private void commitRecordAudio(String path) {
        if (TextUtils.isEmpty(path)) return;
        final File file = new File(path);
        if (!file.exists()) return;
        final RequestBody rb = RequestBody.create(MediaType.parse("audio/mpeg"), file);
        final MultipartBody.Part mp = MultipartBody.Part.createFormData("upload_file", file.getName(), rb);
        if (mSpeakDatas == null) return;
        SpeakQuestionData data = mSpeakDatas.get(currentPage);
        if (data == null) return;
        final String id = data.getData().getId();
        if (TextUtils.isEmpty(id)) return;
        showLoadDialog();
        addToCompositeDis(HttpUtil
                .spokenUp(id)
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean>>() {
                    @Override
                    public ObservableSource<ResultBean> apply(@NonNull ResultBean bean) throws Exception {
                        Log.i("getData00", bean.getToken()+"url ="+mp);
                        return HttpUtil.spokenUpToken(bean.getToken(), mp);
                    }
                })
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean>>() {
                    @Override
                    public ObservableSource<ResultBean> apply(@NonNull ResultBean bean) throws Exception {
                        if (getHttpResSuc(bean.getCode())) {
                            Log.i("getData01", id+"url ="+bean.getFile());

                            return HttpUtil.spokenSave(id, bean.getFile());
                        }
                        throw new CustomException(bean.getMessage());
                    }
                })
                .compose(new SchedulerTransformer<ResultBean>())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        asyncGlodData(id, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void heightIncerement(final View container, int from, int to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);//属性动画
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    container.getLayoutParams().height = (int) valueAnimator.getAnimatedValue();
                    container.requestLayout();
                }
            });
            valueAnimator.start();
        }
    }
}
