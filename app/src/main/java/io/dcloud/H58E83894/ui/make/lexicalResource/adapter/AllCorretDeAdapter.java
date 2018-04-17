package io.dcloud.H58E83894.ui.make.lexicalResource.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HostType;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
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

public class AllCorretDeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<AllTaskData.DataBean.DetailsBean> shares;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private VoiceManager voiceManager;
    private int lastPosition = -1;
    private Context context;
    private AnimationDrawable mAnimationDrawable;

    public AllCorretDeAdapter(List<AllTaskData.DataBean.DetailsBean> share, Context context, RxPermissions rxPermissions) {
        Log.i("yiyi",share.toString()+"dfff");
        this.shares = share;
        this.context = context;
        mRxDownload = RxDownload.getInstance(context);
        this.mRxPermissions = rxPermissions;
        voiceManager = VoiceManager.getInstance(context);
    }

    public List<AllTaskData.DataBean.DetailsBean> getDate() {
        return shares;
    }

    public void update(List<AllTaskData.DataBean.DetailsBean> lists) {
        if (lists == null || lists.isEmpty()) return;
        shares = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("yiyi1",""+"dfff");
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.speak_corret_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        Log.i("yiyi2",""+"dfff");
        final AllTaskData.DataBean.DetailsBean share = shares.get(position);
        /**
         * 图像等后台数据
         * */
        GlideUtil.load(RetrofitProvider.TOEFLURL + share.getImage(), holder.getCircleImageView(R.id.speak_answer_item_head_img));
        holder.getTextView(R.id.speak_answer_item_name).setText(share.getAuthor());

        final TextView timeTv = holder.getTextView(R.id.speak_audio_time_tv);
        //init timeTv
        timeTv.setText("0''");
        timeTv.setTextColor(ContextCompat.getColor(holder.mContext, R.color.color_txt_gray));
        final TextView praise = holder.getTextView(R.id.speak_item_praise_tv);
        if(Integer.parseInt(share.getStatus().trim()) == 1){
            praise.setText("点评中");
            holder.getView(R.id.liaere).setVisibility(View.GONE);
            holder.getTextView(R.id.corret_teacher).setVisibility(View.GONE);
            holder.getTextView(R.id.txt_content).setVisibility(View.GONE);
        }else if(Integer.parseInt(share.getStatus().trim()) == 2) {
            praise.setText(share.getScore()+"分");
            holder.getView(R.id.liaere).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.corret_teacher).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.txt_content).setVisibility(View.VISIBLE);
            praise.setTextColor(context.getResources().getColor(R.color.color_sup_red));
            holder.getTextView(R.id.corret_teacher).setText(share.getTeacher()+"老师评语：");
            holder.getTextView(R.id.corret_teacher).setTextColor(context.getResources().getColor(R.color.color_text_green));
            holder.getTextView(R.id.txt_content).setText(share.getComment());
        }else {
            holder.getView(R.id.liaere).setVisibility(View.GONE);
        }

        final String url = RetrofitProvider.TOEFLURL + share.getAnswer();
        holder.getView(R.id.play_audio_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceManager.isPlaying() && lastPosition == position) {
                    voiceManager.stopPlay();
                    if ( mAnimationDrawable !=null) {
                        mAnimationDrawable.stop();
                        mAnimationDrawable = null;
                        holder.getImageView(R.id.sp_iv).setImageResource(R.drawable.icon_audio_thr);
                    }
                } else {
                    voiceManager.stopPlay();
                    if ( mAnimationDrawable !=null) {
                        mAnimationDrawable.selectDrawable(0);
                        mAnimationDrawable.stop();
                        mAnimationDrawable = null;
                        holder.getImageView(R.id.sp_iv).setImageResource(R.drawable.icon_audio_thr);
                    }
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
                                holder.getImageView(R.id.sp_iv).setImageResource(R.drawable.icon_audio_thr);
                                if ( mAnimationDrawable !=null) {
                                    holder.getImageView(R.id.sp_iv).setImageResource(R.drawable.icon_audio_thr);
                                    mAnimationDrawable = null;
                                }else if(mAnimationDrawable ==null){
                                    holder.getImageView(R.id.sp_iv).setImageResource(R.drawable.audio_animlist);
                                    mAnimationDrawable = (AnimationDrawable) ( holder.getImageView(R.id.sp_iv) ).getDrawable();
                                    mAnimationDrawable.start();
                                }
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
        return shares == null ? 0 : shares.size();
    }
}
