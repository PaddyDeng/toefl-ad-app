package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ListenSecStartMakeActivity extends BaseActivity {

    public static void startListenSecAct(Context c, String id, String title, boolean makeEnd) {
        Intent intent = new Intent(c, ListenSecStartMakeActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.putExtra(Intent.EXTRA_TEXT, makeEnd);
        c.startActivity(intent);
    }

    @BindView(R.id.down_img)
    ImageView downImg;
    @BindView(R.id.fine_listen_tv)
    TextView topTv;
    @BindView(R.id.make_listen_tv)
    TextView bottomTv;
    @BindView(R.id.listen_start_title)
    TextView titleTv;
    private String id;
    private String title;
    private boolean makeEnd;//这套题已经做完了
    private Observable<String> busObs;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_INDEX);
        title = intent.getStringExtra(Intent.EXTRA_TITLE);
        makeEnd = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_sec_start_make);
    }

    private void switchStatus(boolean checkTop) {
        topTv.setSelected(checkTop);
        bottomTv.setSelected(!checkTop);
    }

    @OnClick({R.id.fine_listen_tv, R.id.make_listen_tv, R.id.listen_start_practice_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fine_listen_tv:
                switchStatus(true);
                break;
            case R.id.make_listen_tv:
                switchStatus(false);
                break;
            case R.id.listen_start_practice_tv:
                if (bottomTv.isSelected()) {
                    if (needLogin()) return;
                    if (makeEnd) {
                        ListenMakeResultActivity.startSecTestAct(mContext, id);
                    } else {
                        ListenSecQuestionActivity.startListenSecQuestionAct(mContext, id, title);
                    }
                } else if (topTv.isSelected()) {
                    FineListenQuestionActivity.startFineQuestionAct(mContext, id, title);
                }
                break;
            default:
                break;
        }

    }


    @Override
    protected void initView() {
        Animation topIn = AnimationUtils.loadAnimation(mContext, R.anim.listen_up_in);
        downImg.startAnimation(topIn);
        switchStatus(true);
        busObs = RxBus.get().register(C.REFRESH_LISTEN_LIST, String.class);
        busObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(s, id)) {
                    makeEnd = !makeEnd;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (busObs != null) {
            RxBus.get().unregister(C.REFRESH_LISTEN_LIST, busObs);
        }
    }
}
