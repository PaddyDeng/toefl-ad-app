package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.make.bottom.sp.listener.SpeakPraiseListener;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/7/28  17:25.
 */

public class SpeakAnswerAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<SpeakShare> share;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private VoiceManager voiceManager;
    private int lastPosition = -1;

    public SpeakAnswerAdapter(List<SpeakShare> share, Context context, RxPermissions rxPermissions) {
        this.share = share;
        mRxDownload = RxDownload.getInstance(context);
        this.mRxPermissions = rxPermissions;
        voiceManager = VoiceManager.getInstance(context);
    }

    public List<SpeakShare> getDate() {
        return share;
    }

    public void update(List<SpeakShare> lists) {
        if (lists == null || lists.isEmpty()) return;
        share = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.speak_answer_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        final SpeakShare share = this.share.get(position);
        String nickname = share.getNickname();
        if (TextUtils.isEmpty(nickname)) {
            nickname = share.getUserName();
        }
        GlideUtil.load(RetrofitProvider.TOEFLURL + share.getImage(), holder.getCircleImageView(R.id.speak_answer_item_head_img));
        holder.getTextView(R.id.speak_answer_item_name).setText(nickname);

        final TextView timeTv = holder.getTextView(R.id.speak_audio_time_tv);
        //init timeTv
        timeTv.setText("0''");
        timeTv.setTextColor(ContextCompat.getColor(holder.mContext, R.color.color_txt_gray));
        final TextView praise = holder.getTextView(R.id.speak_item_praise_tv);
        praise.setText(holder.mContext.getString(R.string.str_speak_praise_num, share.getLiked()));
        praise.setOnClickListener(new SpeakPraiseListener(share.getId(), holder.mContext));
        final String url = RetrofitProvider.TOEFLURL + share.getAnswer();

        holder.getView(R.id.play_audio_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceManager.isPlaying() && lastPosition == position) {
                    voiceManager.stopPlay();
                } else {
                    voiceManager.stopPlay();
                    voiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
                        @Override
                        public void voiceTotalLength(long time, String strTime) {

                        }

                        @Override
                        public void playDoing(long time, String strTime) {

                        }

                        @Override
                        public void playPause() {

                        }

                        @Override
                        public void playStart() {

                        }

                        @Override
                        public void playFinish() {
                        }
                    });
                    getDownRecord(url).subscribe(new Consumer<DownloadRecord>() {
                        @Override
                        public void accept(@NonNull DownloadRecord record) throws Exception {
                            if (record.getFlag() == DownloadFlag.COMPLETED) {
                                voiceManager.startPlay(record.getSavePath() + File.separator + record.getSaveName());
                            } else {
                                Utils.toastShort(holder.mContext, holder.mContext.getString(R.string.str_file_destory));
                            }
                        }
                    });
                }
                lastPosition = position;
            }
        });

/*
        getDownRecord(url).subscribe(new Consumer<DownloadRecord>() {
            @Override
            public void accept(@NonNull DownloadRecord record) throws Exception {
                if (record.getFlag() != DownloadFlag.COMPLETED) {
                    DownloadUtil.downloadService(url, mRxDownload, mRxPermissions);
                }
            }
        });
*/
        DownloadUtil.downloadService(url, mRxDownload, mRxPermissions);
        share.mDisposable = mRxDownload.receiveDownloadStatus(url)
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(@NonNull DownloadEvent downloadEvent) throws Exception {
                        if (downloadEvent.getFlag() == DownloadFlag.COMPLETED) {
                            //更新ui设置语音时间
                            setTime(url, timeTv, holder);
                        }
                    }
                });
    }

    private Observable<DownloadRecord> getDownRecord(final String url) {
        return mRxPermissions.request(READ_EXTERNAL_STORAGE)
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (!aBoolean) {
                            throw new RuntimeException("no permission");
                        }
                    }
                }).compose(new ObservableTransformer<Boolean, DownloadRecord>() {
                    @Override
                    public ObservableSource<DownloadRecord> apply(Observable<Boolean> upstream) {
                        return upstream.flatMap(new Function<Boolean, ObservableSource<DownloadRecord>>() {
                            @Override
                            public ObservableSource<DownloadRecord> apply(@NonNull Boolean aBoolean) throws Exception {
                                return mRxDownload.getDownloadRecord(url);
                            }
                        });
                    }
                });
    }

    private void setTime(String url, TextView timeTv, BaseRecyclerViewHolder holder) {
        File[] records = mRxDownload.getRealFiles(url);
        if (records[0].exists()) {
            long time = MediaUtil.getMediaTime(records[0].getAbsolutePath());
            long l = time / 1000;
            if (time % 1000 != 0) {
                l += 1;
            }
            if (l != 0) {
                timeTv.setText(holder.mContext.getString(R.string.str_speak_time_des, l));
                timeTv.setTextColor(ContextCompat.getColor(holder.mContext, R.color.color_sec_orange));
            }
        }
    }

    @Override
    public int getItemCount() {
        return share == null ? 0 : share.size();
    }
}
