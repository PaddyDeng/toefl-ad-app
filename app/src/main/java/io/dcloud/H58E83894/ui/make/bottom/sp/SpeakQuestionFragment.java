package io.dcloud.H58E83894.ui.make.bottom.sp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.make.SpeakData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.http.callback.RequestImp;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.weiget.PlayAudioView;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

/**
 * Created by fire on 2017/7/27  17:16.
 */

public class SpeakQuestionFragment extends BaseFragment {

    public static SpeakQuestionFragment getInstance(SpeakData speakData) {
        SpeakQuestionFragment speakQuestionFragment = new SpeakQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("SPEAK_QUESTION_KEY", speakData);
        speakQuestionFragment.setArguments(bundle);
        return speakQuestionFragment;
    }

    @BindView(R.id.speak_question_play_view)
    PlayAudioView mPlayAudioView;
    @BindView(R.id.speak_question_tv)
    TextView questionTv;
    @BindView(R.id.page_number_tv)
    TextView pageNum;
    @BindView(R.id.read_and_listen_container)
    LinearLayout mReadListenContainer;
    @BindView(R.id.speak_question_reading_tv)
    TextView readTv;
    private SpeakData mSpeakData;

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        mSpeakData = arguments.getParcelable("SPEAK_QUESTION_KEY");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshUi();
    }

    @Override
    protected void refreshUi() {
        if (mSpeakData == null) return;
        String article = mSpeakData.getArticle();
        String file = mSpeakData.getFile();
        final String url = RetrofitProvider.TOEFLURL + file;
        final RxDownload rxDownload = RxDownload.getInstance(getActivity());
        final RxPermissions rxPermissions = new RxPermissions(getActivity());
        if (TextUtils.isEmpty(article)) {
            Utils.setGone(mReadListenContainer);
        } else {
            if (TextUtils.isEmpty(article.trim())) {
                Utils.setGone(mReadListenContainer);
            } else {
                Utils.setVisible(mReadListenContainer);
                readTv.setText(HtmlUtil.fromHtml(article));
            }
        }
        questionTv.setText(mSpeakData.getQuestion());
        pageNum.setText(getString(R.string.str_page_num, mSpeakData.getCurrentPage(), mSpeakData.getAllPageSize()));
        if (TextUtils.isEmpty(file)) return;
        DownloadUtil.download(url, rxDownload, rxPermissions, new RequestImp<DownloadStatus>() {
            @Override
            public void requestSuccess(DownloadStatus status) {
                if (status.getPercentNumber() == 100) {
                    File[] record = rxDownload.getRealFiles(url);
                    if (record[0].exists()) {
                        long time = MediaUtil.getMediaTime(record[0].getAbsolutePath());
                        mPlayAudioView.setInit(true, (int) time, url, rxPermissions, rxDownload);
                    }
                }
            }
        });
    }

    @Override
    public void onDetach() {
        mPlayAudioView.onDetach();
        super.onDetach();
    }

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_speak_question_layout, container, false);
    }

    @OnClick({R.id.frag_speak_question_content_container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_speak_question_content_container:
                ((SpeakQuestionActivity) getActivity()).switchContainer();
                break;
            default:
                break;
        }
    }

}
