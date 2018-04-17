package io.dcloud.H58E83894.ui.make;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.TaskItemData;
import io.dcloud.H58E83894.data.make.TodayData;
import io.dcloud.H58E83894.data.make.TodayTaskData;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.make.dub.AllDubCourseActivity;
import io.dcloud.H58E83894.ui.make.dub.adapter.DubAdapter;
import io.dcloud.H58E83894.ui.make.easyResource.EaLexxicalResourceActivity;
import io.dcloud.H58E83894.ui.make.everyDayListen.EveryListenPracticeActivity;
import io.dcloud.H58E83894.ui.make.everyDayListen.MyEveListenLessonActivity;
import io.dcloud.H58E83894.ui.make.lexicalResource.LexxicalResourceActivity;
import io.dcloud.H58E83894.ui.make.bottom.lp.BottomListenPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.rp.BottomReadPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.sp.BottomSpeakPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.wp.BottomWritePracticeActivity;
import io.dcloud.H58E83894.ui.make.core.CoreTestActivity;
import io.dcloud.H58E83894.ui.make.grammar.GrammarGuideActivity;
import io.dcloud.H58E83894.ui.make.grammar.GrammarTestActivity;
import io.dcloud.H58E83894.ui.make.listen.ListenActivity;
import io.dcloud.H58E83894.ui.make.listen.ListenTestActivity;
import io.dcloud.H58E83894.ui.make.practice.ListenPracticeActivity;
import io.dcloud.H58E83894.ui.prelesson.HighScoreStoryActivity;
import io.dcloud.H58E83894.ui.prelesson.MaryListActivity;
import io.dcloud.H58E83894.ui.prelesson.ToeflDetailActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/12.做题
 */

public class MakeFragment extends BaseFragment {

    @BindView(R.id.practice_complete_duty_num_tv)
    TextView completeNumTv;
    @BindView(R.id.practice_learning_day_num_tv)
    TextView learningNumTv;
    @BindView(R.id.today_schedule_tvs)
    TextView scheduleTv;

    @BindView(R.id.grammar_des)
    TextView grammarStatusTv;
    @BindView(R.id.grammar_today_end_img)
    ImageView grammarTodayEndImg;
    @BindView(R.id.practice_head_iv)
    CircleImageView headIv;
    @BindView(R.id.practice_core_end)
    ImageView coreEndImg;
    @BindView(R.id.core_status_tv)
    TextView coreStatusTv;
    @BindView(R.id.practice_listen_end)
    ImageView listenEndImg;
    @BindView(R.id.practice_listen_status_tv)
    TextView listenStatusTv;
    @BindView(R.id.fine_listen_img)
    ImageView finsListenImg;
    @BindView(R.id.fine_listen_start_tv)
    TextView fineListenTv;
    @BindView(R.id.hot_recycler)
    RecyclerView hotRecycler;//配音推荐

    private boolean asynData;
    private boolean grammarEnd = false;
    private boolean coreEnd = false;
    private boolean listenEnd = false;
    private boolean fineListen = false;
    private int endNum = 0;
    private boolean isFirstInit = true;
    private PreLessonData mPreLessonData;

    private Observable<Integer> refreshUiObs;
    private Observable<Integer> refreshListenUiObs;
    private Observable<Integer> refreshFineListenObs;
    private Observable<Integer> refreshCoreUiObs;
    private Observable<Boolean> loginObs;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_make_layout, container, false);
    }

    @OnClick({R.id.grammer_container, R.id.core_container, R.id.listen_container, R.id.listen_practice_container,
            R.id.practice_listen_container, R.id.practice_read_container, R.id.practice_speak_container,
            R.id.practice_write_container, R.id.make_speak_commit, R.id.make_eassay_update, R.id.make_down_load,
            R.id.record_answer, R.id.make_all_dub})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.practice_write_container://写作
                forword(BottomWritePracticeActivity.class);
                break;
            case R.id.practice_speak_container://口语
                forword(BottomSpeakPracticeActivity.class);
                break;
            case R.id.practice_read_container://阅读
                forword(BottomReadPracticeActivity.class);
                break;
            case R.id.practice_listen_container://听力
                forword(BottomListenPracticeActivity.class);
                break;
            case R.id.grammer_container://语法
                if (needLogin()) return;
//                forword(GrammarGuideActivity.class);
                if (grammarEnd) {
                    GrammarTestActivity.startGrammarTestActivity(getActivity());
                } else {
                    forword(GrammarGuideActivity.class);
                }
                break;
            case R.id.listen_container://精听
                if (needLogin()) return;
                if (listenEnd){
                    ListenTestActivity.startListenTestActivity(getActivity());
                }else {
                    forword(ListenActivity.class);
                }
                break;
            case R.id.core_container://核心词汇
                if (needLogin() ) return;
//                forword(CoreTestActivity.class);
//                if (needLogin()) return;
                if (coreEnd) {
                    CoreTestActivity.startcoreTestActivity(getActivity());
                } else {
                    forword(CoreTestActivity.class);
                }
                break;
            case R.id.listen_practice_container://泛听
                if (needLogin() ) return;
                if(fineListen){
                    forword(ListenPracticeActivity.class);
                }else {
                    forword(ListenPracticeActivity.class);
                }

                break;
            case R.id.make_speak_commit://口语
//                toastShort("敬请期待口语评分模块");
                if (needLogin() ) return;
                forword(LexxicalResourceActivity.class);
//                KnowTypeActivity.startKnow(getActivity(), this.getResources().getString(R.string.str_speak_commit));
                break;
            case R.id.make_eassay_update://作文批改
                if (needLogin() ) return;
                forword(EaLexxicalResourceActivity.class);
//                MaryListActivity.startMary(getActivity(),  getResources().getString(R.string.str_eassay_make), "374", "5");
                break;
            case R.id.make_down_load://资料下载
                HighScoreStoryActivity.startMary(getActivity(),  getResources().getString(R.string.str_data_download), "9", "2");
                break;

            case R.id.record_answer://每日听力
                if (needLogin() ) return;
                if(fineListen){
                    forword(MyEveListenLessonActivity.class);
                }else {
                    forword(MyEveListenLessonActivity.class);
                }
                break;

            case R.id.make_all_dub:
                if (needLogin() ) return;
                    forword(AllDubCourseActivity.class);
                break;

            default:
                break;
        }

    }

    private void asynBannerData() {
        addToCompositeDis(HttpUtil.preLesson()
                .subscribe(new Consumer<PreLessonData>() {
                    @Override
                    public void accept(@NonNull PreLessonData data) throws Exception {
                        asynData = true;
                        mPreLessonData = data;
                        initHotData(mPreLessonData.getHotData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

    private void initHotData(final List<LessonData> data) {//热门模块
        hotRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DubAdapter hotAdapter = new DubAdapter(data);
        hotAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                LessonData hotData = data.get(position);
                ToeflDetailActivity.startToeflDetail(getActivity(), hotData, C.TYPE_HOT_LESSON);
            }
        });
        hotRecycler.setAdapter(hotAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        asynBannerData();
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            headIv.setImageResource(R.drawable.ic_make_tou_03);
        }else {
            UserData data = GlobalUser.getInstance().getUserData();
            if (!TextUtils.isEmpty(data.getImage())) {
                GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), headIv);
            }
//
        }

        if (isFirstInit) {
            isFirstInit = false;
            completeNumTv.setText(format(getString(R.string.str_des_num, 0)));
            learningNumTv.setText(format(getString(R.string.str_des_day_num, 0)));
            setSchedule();
        }


        refreshUiObs = RxBus.get().register(C.MAKE_GRAMMAR, Integer.class);
        refreshCoreUiObs = RxBus.get().register(C.MAKE_CORE_REFRESH, Integer.class);
        refreshListenUiObs = RxBus.get().register(C.MAKE_LISTEN_REFRESH, Integer.class);
        refreshFineListenObs = RxBus.get().register(C.MAKE_FINE_LISTEN_REFRESH, Integer.class);

        refreshCoreUiObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == C.MAKEING) {
                    log("makeing");
                    coreEndImg.setVisibility(View.GONE);
                    coreStatusTv.setVisibility(View.VISIBLE);
                    coreStatusTv.setText(getString(R.string.str_go_on));
                } else if (integer == C.MAKE_END) {
                    log("end");
                    coreEnd();
                } else {
                    coreStart();
                }
            }
        });
        refreshUiObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == C.MAKEING) {
                    grammarTodayEndImg.setVisibility(View.GONE);
                    grammarStatusTv.setVisibility(View.VISIBLE);
                    grammarStatusTv.setText(getString(R.string.str_go_on));
                } else if (integer == C.MAKE_END) {
                    grammarEnd();
                } else {
                    grammarStart();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
        refreshListenUiObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == C.MAKEING) {
                  //  listenStatusTv.setText(getString(R.string.str_go_on));
                    listenEndImg.setVisibility(View.GONE);
                    listenStatusTv.setVisibility(View.VISIBLE);
                    listenStatusTv.setText(getString(R.string.str_go_on));
                } else if (integer == C.MAKE_END) {
                    listenEnd();
                } else {
                    listenStart();
                }
            }
        });
        refreshFineListenObs.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (integer == C.MAKEING) {
                    finsListenImg.setVisibility(View.GONE);
                    fineListenTv.setVisibility(View.VISIBLE);
                    fineListenTv.setText(getString(R.string.str_go_on));
                } else if (integer == C.MAKE_END) {
                    fineListenEnd();
                } else {
                    fineListenStart();
                }
            }
        });
        loginObs = RxBus.get().register(C.LOGIN_INFO, Boolean.class);
        loginObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                updateMakeStatus();
            }
        });
        //更新做题状态
        updateMakeStatus();
    }


//    private void getUserDetailInfo() {
//        addToCompositeDis(HttpUtil.getUserDetailInfo()
//                .compose(new SchedulerTransformer<ResultBean<UserData>>())
//                .subscribe(new Consumer<ResultBean<UserData>>() {
//                    @Override
//                    public void accept(@NonNull ResultBean<UserData> bean) throws Exception {
//                        UserData data = bean.getData();
//                        if (data != null) {
//                            String userJson = JsonUtil.toJson(data);
//                            SharedPref.saveLoginInfo(getActivity(), userJson);
//                            GlobalUser.getInstance().resetUserDataBySharedPref(userJson);
//                            GlobalUser.getInstance().setUserData(data);
//                        }
//                        setUi();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                }));
//    }

//    private void setUi() {
//        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//            nameTv.setText(getString(R.string.str_no_login));
//            headImg.setImageResource(R.drawable.icon_default);
//        } else {
//            UserData data = GlobalUser.getInstance().getUserData();
//            nameTv.setText(data.getNickname());
//            if (!TextUtils.isEmpty(data.getImage())) {
//                GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), headImg);
//            }
//        }
//    }
    private void setSchedule() {
        scheduleTv.setText(formatSchedule(getString(R.string.str_schedule, endNum)));
    }

    private void listenEnd() {
        addSchedule();
        listenEnd = true;
        Utils.setVisible(listenEndImg);
        Utils.setGone(listenStatusTv);
        listenEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_today_end));
    }

    private void addSchedule() {
        endNum++;
        setSchedule();
    }

    private void fineListenEnd() {
        addSchedule();
        fineListen = true;
        Utils.setGone(fineListenTv);
        Utils.setVisible(finsListenImg);
        finsListenImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_today_end));
    }

    private void updateMakeStatus() {
        addToCompositeDis(HttpUtil
                .todayTask()
                .subscribe(new Consumer<TodayData>() {
                    @Override
                    public void accept(@NonNull TodayData data) throws Exception {
                        if (getHttpResSuc(data.getCode())) {
                            refreshUi(data);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void refreshUi(TodayData data) {
        completeNumTv.setText(format(getString(R.string.str_des_num, data.getTaskNumber())));
        learningNumTv.setText(format(getString(R.string.str_des_day_num, data.getTaskDays())));
        TodayTaskData task = data.getTodayTask();
        if (task == null) return;
        endNum = 0;
        TaskItemData listening = task.getIntensiveListening();
        if (listening != null) {
            if (listening.getStatus() == 1) {
                listenEnd();
            } else if (listening.getNum() != 1) {
                listenEndImg.setVisibility(View.GONE);
                listenStatusTv.setVisibility(View.VISIBLE);
                listenStatusTv.setText(getString(R.string.str_go_on));
            } else {
                listenStart();
            }
        }
        TaskItemData fineListen = task.getPanListensPractice();
        if (fineListen != null) {
            if (fineListen.getStatus() == 1) {
                fineListenEnd();
            } else if (fineListen.getNum() != 1) {
                finsListenImg.setVisibility(View.GONE);
                fineListenTv.setVisibility(View.VISIBLE);
                fineListenTv.setText(getString(R.string.str_go_on));
            } else {
                fineListenStart();
            }
        }
        TaskItemData learning = task.getGrammarLearning();
        if (learning != null) {
            if (learning.getStatus() == 1) {
                grammarEnd();
            } else if (learning.getNum() != 1) {
                grammarTodayEndImg.setVisibility(View.GONE);
                grammarStatusTv.setVisibility(View.VISIBLE);
                grammarStatusTv.setText(getString(R.string.str_go_on));
            } else {
                grammarStart();
            }
        }
        TaskItemData words = task.getKeyWords();
        if (words != null) {
            if (words.getStatus() == 1) {
                coreEnd();
            } else if (words.getNum() != 1) {
                coreEndImg.setVisibility(View.GONE);
                coreStatusTv.setVisibility(View.VISIBLE);
                coreStatusTv.setText(getString(R.string.str_go_on));
            } else {
                coreStart();
            }
        }
        setSchedule();
    }

    private void grammarStart() {
        grammarEnd = false;
        Utils.setGone(grammarStatusTv);
        Utils.setVisible(grammarTodayEndImg);
        grammarTodayEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_go_03));
    }

    private void coreStart() {
        coreEnd = false;
        Utils.setGone(coreStatusTv);
        Utils.setVisible(coreEndImg);
        coreEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_go_03));
    }

    private void fineListenStart() {
        fineListen = false;
        Utils.setGone(fineListenTv);
        Utils.setVisible(finsListenImg);
        finsListenImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_go_03));
    }

    private void listenStart() {
        listenEnd = false;
        Utils.setVisible(listenEndImg);
        Utils.setGone(listenStatusTv);
        listenEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_go_03));
    }

    private void coreEnd() {
        addSchedule();
        coreEnd = true;
        Utils.setGone(coreStatusTv);
        Utils.setVisible(coreEndImg);
        coreEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_today_end));
    }

    private void grammarEnd() {
        addSchedule();
        grammarEnd = true;
        Utils.setGone(grammarStatusTv);
        Utils.setVisible(grammarTodayEndImg);
        grammarTodayEndImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_today_end));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshUiObs != null) {
            RxBus.get().unregister(C.MAKE_GRAMMAR, refreshUiObs);
        }
        if (refreshListenUiObs != null) {
            RxBus.get().unregister(C.MAKE_LISTEN_REFRESH, refreshListenUiObs);
        }
        if (refreshCoreUiObs != null) {
            RxBus.get().unregister(C.MAKE_CORE_REFRESH, refreshCoreUiObs);
        }
        if (refreshFineListenObs != null) {
            RxBus.get().unregister(C.MAKE_FINE_LISTEN_REFRESH, refreshFineListenObs);
        }
        if (loginObs != null) {
            RxBus.get().unregister(C.LOGIN_INFO, loginObs);
        }
    }

    private Spanned format(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(0.6f), text.length() - 1, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private Spanned formatSchedule(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getActivity(), R.color.color_sec_orange)), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
