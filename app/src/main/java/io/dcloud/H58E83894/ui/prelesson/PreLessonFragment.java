package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.better.banner.BBanner;
import com.better.banner.ItemAdapter;
import com.better.banner.OnClickItemListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.RandomData;
import io.dcloud.H58E83894.data.prelesson.BannerData;
import io.dcloud.H58E83894.data.prelesson.ChineseData;
import io.dcloud.H58E83894.data.prelesson.FreeCursorData;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.prelesson.TeacherData;
import io.dcloud.H58E83894.data.prelesson.TeacherItemBean;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.prelesson.adapter.CardAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.HotAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.LessonAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.TeacherAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.AutoPollRecyclerView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by fire on 2017/7/12.抢现课堂
 */

public class PreLessonFragment extends BaseFragment {

    @BindView(R.id.toefl_banner)
     BBanner bBanner;
    @BindView(R.id.card_recycler)
    RecyclerView cardRecycler;
    @BindView(R.id.teacher_recycler)
    AutoPollRecyclerView teacherRecycler;
    @BindView(R.id.hot_recycler)
    RecyclerView hotRecycler;
    @BindView(R.id.lesson_recycler)
    RecyclerView lessonRecycler;
//    @BindView(R.id.gmat_free_one_random)
//    TextView freeLabelOneRandom;
//    @BindView(R.id.gmat_free_two_random)
//    TextView freeLabelTwoRandom;
//    @BindView(R.id.gmat_free_thr_random)
//    TextView freeLabelThrRandom;
//    @BindView(R.id.gmat_free_four_random)
//    TextView freeLabelFourRandom;
//    @BindView(R.id.free_one_cursor_time_tv)
//    TextView oneTimeTv;
//    @BindView(R.id.toefl_speak_free)
//    TextView twoTimeTv;
//    @BindView(R.id.toefl_read_free)
//    TextView thrTimeTv;
//    @BindView(R.id.four_write_cursor)
//    TextView fourTimeTv;

//    @BindView(R.id.toefl_free_one_title)
//    TextView oneTitleTv;
//    @BindView(R.id.toefl_free_two_title)
//    TextView twoTitleTv;
//    @BindView(R.id.toefl_free_thr_title)
//    TextView thrTitleTv;
//    @BindView(R.id.toefl_free_four_title)
//    TextView fourTitleTv;


    private boolean asynData;
    private boolean asyncTeacher;
    private boolean asyncFreeData;
    private Context context;
    private PreLessonData mPreLessonData;
    private List<BannerData> bannerDatas;
    private String TRIAL_SC;
    private String TRIAL_RC;
    private String TRIAL_CR;
    private String TRIAL_MATH;

    private void queryRandromNumber(final String courseId, final int type, final ICallBack<Integer> iCallBack) {
        addToCompositeDis(Observable.just(1).flatMap(new Function<Integer, ObservableSource<RandomData>>() {
            @Override
            public ObservableSource<RandomData> apply(@NonNull Integer integer) throws Exception {
                return Observable.create(new ObservableOnSubscribe<RandomData>() {
                    @Override
                    public void subscribe(ObservableEmitter<RandomData> e) throws Exception {
                        RandomData randomData = RecordManager.getInstance().queryNumber(courseId, type);
                        e.onNext(randomData);
                        e.onComplete();
                    }
                });
            }
        }).compose(new SchedulerTransformer<RandomData>())
                .subscribe(new Consumer<RandomData>() {
                    @Override
                    public void accept(@NonNull RandomData data) throws Exception {
                        iCallBack.onSuccess(data.getTimes());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        iCallBack.onFail();
                    }
                }));
    }

    public void startDeal(String titleName, String url) {
        if (TextUtils.isEmpty(url))
            return;
        DealActivity.startDealActivity(getActivity(), titleName, url);
    }

    @OnClick({ R.id.expert_lecturer_iv,  R.id.free_trial_tvs, R.id.free_trial_tvss,
            R.id.lesson_toefl_iv })
    public void onClick(View v) {
        switch (v.getId()) {
//            R.id.free_lesson_one_container, R.id.free_lesson_two_container, R.id.free_lesson_thr_container,
//                    R.id.free_lesson_four_container,
//            case R.id.free_lesson_one_container://免费试听课程模块点击事件是与webView交互
//                startDeal(getString(R.string.str_free_one_type), TRIAL_SC);
//                break;
//            case R.id.free_lesson_two_container:
//                startDeal(getString(R.string.str_free_two_type), TRIAL_RC);
//                break;
//            case R.id.free_lesson_thr_container:
//                startDeal(getString(R.string.str_free_thr_type), TRIAL_CR);
//                break;
//            case R.id.free_lesson_four_container:
//                startDeal(getString(R.string.str_free_four_type), TRIAL_MATH);
//                break;
            case R.id.expert_lecturer_iv://专家讲师页面
                forword(ExpertLecuterActivity.class);
                break;
            case R.id.lesson_toefl_iv://toefl页面
//                DealActivity.startDealActivity(getActivity(), "sss", "http://m.toeflonline.cn/course.html");
                forword(ToeflLessonActivity.class);
//                GradeActivity.startGradeActivity(getContext(),  "deil.x@thinkwithu.cn", "xsz2368663");
//                forword(GradeActivity.class);
            break;
            case R.id.free_trial_tvs://资料
                HighScoreStoryActivity.startMary(getActivity(),  getResources().getString(R.string.str_data_download), "9", "2");
                break;
            case R.id.free_trial_tvss://机经
                MaryListActivity.startMary(getActivity(), getResources().getString(R.string.str_for_zone), "372", "3");
            default:
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        queryRandromNumber(C.FREE_SC, C.TRIAL_COURSE, new ICallBack<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                freeLabelOneRandom.setTag(integer);
//                freeLabelOneRandom.setTextContent(getString(R.string.str_play_times, integer));
//            }
//
//            @Override
//            public void onFail() {
//            }
//        });
//        queryRandromNumber(C.FREE_RC, C.TRIAL_COURSE, new ICallBack<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                freeLabelTwoRandom.setTag(integer);
//                freeLabelTwoRandom.setTextContent(getString(R.string.str_play_times, integer));
//            }
//
//            @Override
//            public void onFail() {
//            }
//        });
//        queryRandromNumber(C.FREE_CR, C.TRIAL_COURSE, new ICallBack<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                freeLabelThrRandom.setTag(integer);
//                freeLabelThrRandom.setTextContent(getString(R.string.str_play_times, integer));
//            }
//
//            @Override
//            public void onFail() {
//            }
//        });
//        queryRandromNumber(C.FREE_MATH, C.TRIAL_COURSE, new ICallBack<Integer>() {
//            @Override
//            public void onSuccess(Integer integer) {
//                freeLabelfourRandom.setTag(integer);
//                freeLabelfourRandom.setTextContent(getString(R.string.str_play_times, integer));
//            }
//
//            @Override
//            public void onFail() {
//            }
//        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!asynData) {
            asynBannerData();
        }
        if (!asyncTeacher) {
            asynTeacher();
        }
        if (!asyncFreeData) {
            asyncFreeData();
        }
        bBanner.setOnItemClickListener(new OnClickItemListener() {
            @Override
            public void onClick(int i) {
                if (bannerDatas != null && bannerDatas.get(i) != null && !TextUtils.isEmpty(bannerDatas.get(i).getUrl())) {
                    DealActivity.startDealActivity(getActivity(), bannerDatas.get(i).getTitle(), bannerDatas.get(i).getUrl());
                }
            }
        });

        bBanner.setPageChangeDuration(1000);
    }

    private void asyncFreeData() {
        addToCompositeDis(HttpUtil
                .getFreeCursor()
                .subscribe(
                        new Consumer<List<FreeCursorData>>() {
                            @Override
                            public void accept(@NonNull List<FreeCursorData> datas) throws Exception {
//                                initFreeData(datas);
                                Log.i("fff", datas.toString());
                                asyncFreeData = true;
                            }
                        }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        }));
    }

//    private void initFreeData(List<FreeCursorData> datas) {
//        if (datas == null || datas.isEmpty()) return;
//        for (int i = 0, size = datas.size(); i < size; i++) {
//            FreeCursorData data = datas.get(i);
//            Log.i("fff1", data.toString());
//            if (i == 0) {
//                setLabel(freeLabelOneRandom, data,  oneTitleTv);
//                TRIAL_SC = data.getUrl();
//            }
//            else if (i == 1) {
//                setLabel(freeLabelTwoRandom, data,  twoTitleTv);
//                TRIAL_RC = data.getUrl();
//            } else if (i == 2) {
//                setLabel(freeLabelThrRandom, data,  thrTitleTv);
//                TRIAL_CR = data.getUrl();
//            } else if (i == 3) {
//                setLabel(freeLabelFourRandom, data,  fourTitleTv);
//                TRIAL_MATH = data.getUrl();
//            }
//        }
//    }
//
//    private void setLabel(TextView countView, FreeCursorData data,  TextView titleTv) {
////        timeTv.setText(data.getDuration());//课时
//        titleTv.setText(data.getName());//标题
//        int count = 0;
//        if (!TextUtils.isEmpty(data.getName())){
//            count = Integer.parseInt(data.getCount());
//            // view.setTag(count);
//            countView.setText(getResources().getString(R.string.str_pub_lesson_nums, new Object[]{count}));//播放次数
//        }
//
////            LabelImageView labelImageView = (LabelImageView) view;
////            labelImageView.setTextContent(getString(R.string.str_play_times,count));
////        } else if (view instanceof LabelLinearLayout) {
////            LabelLinearLayout labelLinearLayout = (LabelLinearLayout) view;
////            labelLinearLayout.setTextContent(getString(R.string.str_play_times, count));
////        }
//    }

    private void asynTeacher() {
        addToCompositeDis(HttpUtil.teacher()
                .subscribe(new Consumer<TeacherData>() {
                    @Override
                    public void accept(@NonNull TeacherData data) throws Exception {
                        asyncTeacher = true;
                        initTeacherData(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void initTeacherData(TeacherData data) {//专家讲师模块
        if (data == null) return;
        final List<TeacherItemBean> been = data.getData();
        if (been == null || been.isEmpty()) return;
        teacherRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        TeacherAdapter teacherAdapter = new TeacherAdapter(been);
        teacherAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TeacherDetailActivity.startTeacherDetail(getActivity(), been.get(position));
            }
        });
//        teacherRecycler.setItemAnimator(RecyclerViewIte);
        teacherRecycler.setAdapter(teacherAdapter);
        if (true) {//保证itemCount的总个数宽度超过屏幕宽度->自己处理
            teacherRecycler.start();
    }
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.setVisible(bBanner, cardRecycler);
    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.setInvisible(bBanner, cardRecycler);
    }


    private void asynBannerData() {
        addToCompositeDis(HttpUtil.preLesson()
                .subscribe(new Consumer<PreLessonData>() {
                    @Override
                    public void accept(@NonNull PreLessonData data) throws Exception {
                        asynData = true;
                        mPreLessonData = data;
                        refreshUi();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    @Override
    protected void refreshUi() {
        if (mPreLessonData == null) return;
        bannerDatas = mPreLessonData.getImage();
        bBanner.setData(getChildFragmentManager(), new ItemAdapter() {
            @Override
            public Fragment getItem(int p) {
                return BannerDetailPager.getInstance(RetrofitProvider.TOEFLURL + bannerDatas.get(p).getImage());
            }

            @Override
            public int getCount() {
                return bannerDatas.size();
            }
        });
        initCardData(mPreLessonData.getPubClass());
        initHotData(mPreLessonData.getHotData());
        initOnlineData(mPreLessonData.getData());
    }

    private void initOnlineData(ChineseData data) {//托福模块
        lessonRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        final List<LessonData> allData = new ArrayList<>();
        allData.addAll(data.get托福名师私人订制课程());
        allData.addAll(data.get全科强化精品视频课程());
        allData.addAll(data.getVIP10人强化直播课程());
        allData.addAll(data.get托福95分私人订制课程());
        allData.addAll(data.get托福100分私人订制课程());
        allData.addAll(data.get托福110分私人订制课程());
        allData.addAll(data.get在线50人听说读写冲分班());
        LessonAdapter lessonAdapter = new LessonAdapter(allData);
        lessonAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData lessonData = allData.get(position);
                ToeflDetailActivity.startToeflDetail(getActivity(), lessonData, C.TYPE_ONLINE_LESSON);
            }
        });
        lessonRecycler.setAdapter(lessonAdapter);
    }

    private void initHotData(final List<LessonData> data) {//热门模块
        hotRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        HotAdapter hotAdapter = new HotAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                ToeflDetailActivity.startToeflDetail(getActivity(), hotData, C.TYPE_HOT_LESSON);
            }
        });
        hotRecycler.setAdapter(hotAdapter);
    }

    private void initCardData(final List<LessonData> aClass) {//卡片模块
        cardRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        CardAdapter cardAdapter = new CardAdapter(aClass);
        cardRecycler.setAdapter(cardAdapter);
        cardAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData lessonData = aClass.get(position);
                ToeflDetailActivity.startToeflDetail(getActivity(), lessonData, C.TYPE_PUBLIC_LESSON);
            }
        });
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_pre_lesson_layout, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Util.isOnMainThread()&&!getActivity().isFinishing())
        {
            Glide.with(this).pauseRequests();
        }
    }
}
