package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.WriteQuestionData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.ui.common.DetailDialog;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.dcloud.H58E83894.weiget.PlayAudioView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class WriteTpoQuestionActivity extends BaseActivity {

    public static void startWriteTpoQuestionAct(Activity act, String title, String id) {
        Intent intent = new Intent(act, WriteTpoQuestionActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, title);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        act.startActivity(intent);
    }

    @BindView(R.id.write_tpo_title_tv)
    TextView mTitleTv;
    @BindView(R.id.show_has_listen_conainer)
    RelativeLayout mContainer;
    @BindView(R.id.tpo_only_has_question_container)
    LinearLayout tpoOnlyHasQuestionContainer;
    @BindView(R.id.write_tpo_question_txt)
    TextView onlyHasQuestionTxt;
    @BindView(R.id.write_tpo_question_des_info)
    TextView questionTxt;
    @BindView(R.id.write_tpo_general_view)
    GeneralView mGeneralView;
    @BindView(R.id.play_audio_view)
    PlayAudioView mPlayView;

    private String title;
    private String id;
    private DetailDialog mListenDialog;
    private DetailDialog mReadFullDialog;
    private String url;
    private RxDownload mRxDownload;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        title = intent.getStringExtra(Intent.EXTRA_INDEX);
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
        mTitleTv.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRxDownload = RxDownload.getInstance(mContext);
        setDownloadDefalutPath(mRxDownload);
        setContentView(R.layout.activity_write_tpo_question);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == C.REQUEST_COMMIT_ANSWER) {
            RxBus.get().post(C.REFRESH_WRITE_TPO_LIST, title);
            finish();
        }
    }

    @OnClick({R.id.write_tpo_question_listen_tv, R.id.write_tpo_question_read_tv, R.id.write_tpo_start_practice_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_tpo_start_practice_tv:
                WriteAnswerActivity.startWriteAnswer(WriteTpoQuestionActivity.this, id, C.BELONG_TPO);
                break;
            case R.id.write_tpo_question_listen_tv:
                if (mListenDialog != null)
                    mListenDialog.showDialog(getSupportFragmentManager());
                break;
            case R.id.write_tpo_question_read_tv:
                if (mReadFullDialog != null)
                    mReadFullDialog.showDialog(getSupportFragmentManager());
                break;
            default:
                break;
        }
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .independenceDetail(id)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .subscribe(new Consumer<WriteQuestionData>() {
                    @Override
                    public void accept(@NonNull WriteQuestionData bean) throws Exception {
                        dismissLoadDialog();
                        refreshUi(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(WriteQuestionData bean) {
        if (bean == null) return;
        PracticeData data = bean.getData();
        if (data == null) return;
        String halfUrl = data.getFile();
        String readdata = data.getReaddata();
        if (TextUtils.isEmpty(readdata) || TextUtils.isEmpty(halfUrl)) {//只显示问题栏
            Utils.setGone(mContainer);
            Utils.setVisible(tpoOnlyHasQuestionContainer);
            onlyHasQuestionTxt.setText(data.getQuestion());
        } else {//显示三个栏目
            Utils.setVisible(mContainer);
            Utils.setGone(tpoOnlyHasQuestionContainer);
            refreshDiffUi(data);
        }
    }

    private void refreshDiffUi(PracticeData data) {
        questionTxt.setText(data.getQuestion());
        mGeneralView.setSimpleTxt(data.getReaddata());
        mListenDialog = DetailDialog.getInstance(data.getFilemsg(), title);
        mReadFullDialog = DetailDialog.getInstance(data.getReaddata(), title);
        url = RetrofitProvider.TOEFLURL + data.getFile();
        DownloadUtil.download(url, mRxDownload, mRxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus status) {
                if (status.getPercentNumber() == 100) {
                    File[] record = mRxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        mPlayView.setInit(true, (int) time, url, mRxPermissions, mRxDownload);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayView.onDetach();
    }
}
