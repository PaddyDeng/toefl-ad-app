package io.dcloud.H58E83894.ui.make.core;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.make.core.CoreQuestionData;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.media.MusicPlayer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;

public class CoreDetailActivity extends BaseCoreActivity {

    public static void startCoreDetail(Activity context, CoreQuestionData data, int titleIndex) {
        Intent intent = new Intent(context, CoreDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_INDEX, titleIndex);
        context.startActivityForResult(intent, C.CORE_REQUEST_CODE);
    }

    @BindView(R.id.detail_word_tv)
    TextView detailWordTv;
    @BindView(R.id.core_detail_phonetic_tv)
    TextView corePhoneticTv;
    @BindView(R.id.core_detail_type_tv)
    TextView typeTv;
    @BindView(R.id.core_type_des)
    TextView desTv;
    @BindView(R.id.core_simple_detail_tv)
    TextView simpleDetailTv;
    @BindView(R.id.core_detail_centertxt)
    TextView titleTxt;

    private CoreQuestionData mQuestionData;
    private String url;
    private AnimationDrawable mAnimationDrawable;
    private RxDownload rxDownload;
    private MusicPlayer mPlayer;
    private int titleIndex;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        mQuestionData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        titleIndex = intent.getIntExtra(Intent.EXTRA_INDEX, 1);
    }

    @Override
    protected void initData() {
        titleTxt.setText(getString(R.string.str_core_vocabulary_test_title , titleIndex));
    }

    @Override
    protected void initView() {
        if (mQuestionData == null) return;
        detailWordTv.setText(mQuestionData.getName());
        corePhoneticTv.setText(getString(R.string.str_core_phonetic, mQuestionData.getAnswer()));
        typeTv.setText(mQuestionData.getCnName().trim());
        desTv.setText(mQuestionData.getNumbering().trim());
        simpleDetailTv.setText(TextUtils.concat(mQuestionData.getDuration(), mQuestionData.getProblemComplement()));
        url = RetrofitProvider.TOEFLURL + mQuestionData.getAlternatives();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_detail);
        rxDownload = RxDownload.getInstance(mContext);
    }

    @OnClick({R.id.next_question, R.id.core_detail_audio_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_question:
                setResult(RESULT_OK);
                finishWithAnim();
                break;
            case R.id.core_detail_audio_img:
                if (TextUtils.isEmpty(url)) return;
                if (mAnimationDrawable == null) {
                    ((ImageView) view).setImageResource(R.drawable.audio_animlist);
                    mAnimationDrawable = (AnimationDrawable) ((ImageView) view).getDrawable();
                }
                if (mPlayer == null)
                    mPlayer = new MusicPlayer();
                playAnim(url, mAnimationDrawable, mPlayer, rxDownload);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
