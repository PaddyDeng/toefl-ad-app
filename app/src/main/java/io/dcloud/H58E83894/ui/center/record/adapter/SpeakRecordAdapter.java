package io.dcloud.H58E83894.ui.center.record.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.SeekBar;

import java.io.File;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.make.bottom.sp.SpeakQuestionActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DownloadUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadFlag;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/8/15  11:10.
 */

public class SpeakRecordAdapter extends BaseRecyclerViewAdapter<SpeakShare> {

    private VoiceManager mVoiceManager;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private int lastPosition = -1;

    public SpeakRecordAdapter(Context context, List<SpeakShare> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
        mVoiceManager = VoiceManager.getInstance(context);
        mRxDownload = RxDownload.getInstance(context);
        mRxPermissions = new RxPermissions((FragmentActivity) context);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.speak_record_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(final BaseRecyclerViewHolder holder, final int position, final SpeakShare itemData) {
        holder.getTextView(R.id.speak_record_title_tv).setText(itemData.getCatName());
        String time = itemData.getCreateTime();
        time = time.replace("-", "/");
        time = time.substring(0, time.lastIndexOf(":"));
        holder.getTextView(R.id.speak_record_item_des).setText(mContext.getString(R.string.str_time_des, time));
        final String url = RetrofitProvider.TOEFLURL + itemData.getAnswer();
        DownloadUtil.downloadService(url, mRxDownload, mRxPermissions);
        final SeekBar seekBar = holder.getView(R.id.speak_record_item_seek_bar);
        View view = holder.getView(R.id.speak_item_status_iv);
        view.setSelected(itemData.isCheck());
        if (!itemData.isCheck()) {
            seekBar.setProgress(0);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setSelected(!v.isSelected());
                if (lastPosition == position) {
                    mVoiceManager.continueOrPausePlay();
                } else {
                    for (SpeakShare ss : getAdapterData()) {
                        if (ss.isCheck()) {
                            ss.setCheck(false);
                        }
                    }
                    itemData.setCheck(true);
                    mVoiceManager.stopPlay();
                    mVoiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
                        @Override
                        public void voiceTotalLength(long time, String strTime) {
                            seekBar.setMax((int) time);
                        }

                        @Override
                        public void playDoing(long time, String strTime) {
                            seekBar.setProgress((int) time);
                        }

                        @Override
                        public void playPause() {
                            v.setSelected(false);
                        }

                        @Override
                        public void playStart() {
                            v.setSelected(true);
                        }

                        @Override
                        public void playFinish() {
                            v.setSelected(false);
                        }
                    });
                    getDownRecord(url).subscribe(new Consumer<DownloadRecord>() {
                        @Override
                        public void accept(@NonNull DownloadRecord record) throws Exception {
                            if (record.getFlag() == DownloadFlag.COMPLETED) {
                                mVoiceManager.startPlay(record.getSavePath() + File.separator + record.getSaveName());
                            } else {
                                Utils.toastShort(mContext, mContext.getString(R.string.str_file_destory));
                                v.setSelected(false);
                            }
                        }
                    });
                    lastPosition = position;
                    notifyDataSetChanged();
                }
            }
        });
        holder.getTextView(R.id.speak_item_record_tv).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //这里的type，只是使下一个页面请求数据时，区分请求参数id个数
                        SpeakQuestionActivity.startSpeakQuestionAct(mContext, itemData.getContentId(), C.TYPE_SPEAK_GLOD);
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
}
