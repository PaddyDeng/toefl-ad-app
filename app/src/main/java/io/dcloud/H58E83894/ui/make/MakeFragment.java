package io.dcloud.H58E83894.ui.make;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.make.TaskItemData;
import io.dcloud.H58E83894.data.make.TodayData;
import io.dcloud.H58E83894.data.make.TodayTaskData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.make.bottom.lp.BottomListenPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.rp.BottomReadPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.sp.BottomSpeakPracticeActivity;
import io.dcloud.H58E83894.ui.make.bottom.wp.BottomWritePracticeActivity;
import io.dcloud.H58E83894.ui.make.core.CoreTestActivity;
import io.dcloud.H58E83894.ui.make.grammar.GrammarGuideActivity;
import io.dcloud.H58E83894.ui.make.grammar.GrammarTestActivity;
import io.dcloud.H58E83894.ui.make.listen.ListenActivity;
import io.dcloud.H58E83894.ui.make.practice.ListenPracticeActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/12.
 */

public class MakeFragment extends BaseFragment {

    @BindView(R.id.practice_complete_duty_num_tv)
    TextView completeNumTv;
    @BindView(R.id.practice_learning_day_num_tv)
    TextView learningNumTv;
    @BindView(R.id.grammar_des)
    TextView grammarStatusTv;
    @BindView(R.id.practice_head_iv)
    ImageView headIv;
    @BindView(R.id.grammar_today_end_img)
    ImageView grammarTodayEndImg;
    @BindView(R.id.today_schedule_tv)
    TextView scheduleTv;
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

    private boolean grammarEnd = false;
    private boolean coreEnd = false;
    private boolean listenEnd = false;
    private boolean fineListen = false;
    private int endNum = 0;
    private boolean isFirstInit = true;

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
            R.id.practice_listen_container, R.id.practice_read_container, R.id.practice_speak_container, R.id.practice_write_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.practice_write_container:
                forword(BottomWritePracticeActivity.class);
                break;
            case R.id.practice_speak_container:
                forword(BottomSpeakPracticeActivity.class);
                break;
            case R.id.practice_read_container:
                forword(BottomReadPracticeActivity.class);
                break;
            case R.id.practice_listen_container:
                forword(BottomListenPracticeActivity.class);
                break;
            case R.id.grammer_container:
                if (needLogin()) return;
                if (grammarEnd) {
                    GrammarTestActivity.startGrammarTestActivity(getActivity());
                } else {
                    forword(GrammarGuideActivity.class);
                }
                break;
            case R.id.listen_container://精听
                if (needLogin() || listenEnd) return;
                forword(ListenActivity.class);
                break;
            case R.id.core_container://核心词汇
                if (needLogin()) return;
                if (coreEnd) {
                    CoreTestActivity.startcoreTestActivity(getActivity());
                } else {
                    forword(CoreTestActivity.class);
                }
                break;
            case R.id.listen_practice_container://泛听
                if (needLogin() || fineListen) return;
                forword(ListenPracticeActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirstInit) {
            isFirstInit = false;
            completeNumTv.setText(format(getString(R.string.str_des_num, 0)));
            learningNumTv.setText(format(getString(R.string.str_des_day_num, 0)));
            setSchedule();
        }
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            headIv.setImageResource(R.drawable.icon_default);
        } else {
            GlideUtil.load(RetrofitProvider.TOEFLURL + GlobalUser.getInstance().getUserData().getImage(), headIv);
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

    private void setSchedule() {
        scheduleTv.setText(formatSchedule(getString(R.string.str_schedule, endNum)));
    }

    private void listenEnd() {
        addSchedule();
        listenEnd = true;
        Utils.setVisible(listenEndImg);
        Utils.setGone(listenStatusTv);
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
                coreStatusTv.setText(getString(R.string.str_go_on));
            } else {
                coreStart();
            }
        }
        setSchedule();
    }

    private void grammarStart() {
        grammarEnd = false;
        grammarStatusTv.setText(R.string.str_start);
        Utils.setGone(grammarTodayEndImg);
        Utils.setVisible(grammarStatusTv);
    }

    private void coreStart() {
        coreEnd = false;
        coreStatusTv.setText(getString(R.string.str_start));
        Utils.setGone(coreEndImg);
        Utils.setVisible(coreStatusTv);
    }

    private void fineListenStart() {
        fineListen = false;
        fineListenTv.setText(getString(R.string.str_start));
        Utils.setGone(finsListenImg);
        Utils.setVisible(fineListenTv);
    }

    private void listenStart() {
        listenEnd = false;
        Utils.setVisible(listenStatusTv);
        Utils.setGone(listenEndImg);
        listenStatusTv.setText(getString(R.string.str_start));
    }

    private void coreEnd() {
        addSchedule();
        coreEnd = true;
        Utils.setVisible(coreEndImg);
        Utils.setGone(coreStatusTv);
    }

    private void grammarEnd() {
        addSchedule();
        grammarEnd = true;
        Utils.setGone(grammarStatusTv);
        Utils.setVisible(grammarTodayEndImg);
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
