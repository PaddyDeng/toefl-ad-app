package io.dcloud.H58E83894.ui.common;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.utils.FileUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.dcloud.H58E83894.weiget.VoiceLineView;

/**
 * Created by fire on 2017/7/31  13:51.
 */

public class RecordProxy {
    public static String filePathandName;

    public static void showRecordDialog(final Context context, final VoiceManager voiceManager, final ICallBack<String> callBack) {
        filePathandName = "";
        final Dialog recordDialog = new Dialog(context, R.style.record_voice_dialog);
        recordDialog.setContentView(R.layout.dialog_record_voice);
//        recordDialog.setCanceledOnTouchOutside(false);
//        recordDialog.setCancelable(false);
        final RelativeLayout auditionContainer = (RelativeLayout) recordDialog.findViewById(R.id.audition_container);
        final RelativeLayout recordContainer = (RelativeLayout) recordDialog.findViewById(R.id.record_continaer);
        final VoiceLineView voicLine = (VoiceLineView) recordDialog.findViewById(R.id.voicLine);
        final TextView mRecordHintTv = (TextView) recordDialog.findViewById(R.id.tv_length);
        final TextView auditionTime = (TextView) recordDialog.findViewById(R.id.record_audition_time);
        mRecordHintTv.setText("00:00:00");
//        final ImageView mIvPauseContinue = (ImageView) recordDialog.findViewById(R.id.iv_continue_or_pause);
//        final ImageView mIvComplete = (ImageView) recordDialog.findViewById(R.id.iv_complete);
        final TextView mTvComplete = (TextView) recordDialog.findViewById(R.id.tv_determine_record_voice);
        final TextView mTvPauseContinue = (TextView) recordDialog.findViewById(R.id.tv_continue_or_pause);
        recordDialog.findViewById(R.id.play_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(voiceManager, recordDialog);
            }
        });
        recordDialog.findViewById(R.id.rec_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(voiceManager, recordDialog);
            }
        });
        recordDialog.show();
        //暂停或继续
        mTvPauseContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (voiceManager != null) {
                    voiceManager.pauseOrStartVoiceRecord();
                }
            }
        });
        //暂停或继续
//        mIvPauseContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (voiceManager != null) {
//                    voiceManager.pauseOrStartVoiceRecord();
//                }
//            }
//        });
        //完成
        mTvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceManager != null) {
                    voiceManager.stopVoiceRecord();
                }
            }
        });
        //完成
//        mIvComplete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (voiceManager != null) {
//                    voiceManager.stopVoiceRecord();
//                }
////                recordDialog.dismiss();
//            }
//        });

        voiceManager.setVoiceRecordListener(new VoiceManager.VoiceRecordCallBack() {
            @Override
            public void recDoing(long time, String strTime) {
                mRecordHintTv.setText(strTime);
            }

            @Override
            public void recVoiceGrade(int grade) {
                voicLine.setVolume(grade);
            }

            @Override
            public void recStart(boolean init) {
//                mIvPauseContinue.setImageResource(R.drawable.icon_audio_pause);
                mTvPauseContinue.setText(context.getString(R.string.str_pause));
                voicLine.setContinue();
            }

            @Override
            public void recPause(String str) {
//                mIvPauseContinue.setImageResource(R.drawable.icon_audio_continue);
                mTvPauseContinue.setText(context.getString(R.string.str_go_on));
                voicLine.setPause();
            }


            @Override
            public void recFinish(long length, String strLength, String path) {
                auditionTime.setText(strLength);
                filePathandName = path;
                Utils.setVisible(auditionContainer);
                Utils.setGone(recordContainer);

            }
        });
        final ImageView auditionView = (ImageView) recordDialog.findViewById(R.id.play_audio_iv);
        auditionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(filePathandName)) return;
                if (voiceManager.isPlaying()) {
                    voiceManager.stopPlay();
                    auditionView.setImageResource(R.drawable.icon_audio_continue);
                } else {
                    voiceManager.startPlay(filePathandName);
                    auditionView.setImageResource(R.drawable.icon_audio_pause);
                }
            }
        });
        voiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
            @Override
            public void voiceTotalLength(long time, String strTime) {

            }

            @Override
            public void playDoing(long time, String strTime) {
                auditionTime.setText(strTime);
            }

            @Override
            public void playPause() {
            }

            @Override
            public void playStart() {
            }

            @Override
            public void playFinish() {
                auditionView.setImageResource(R.drawable.icon_audio_continue);
            }
        });

        recordDialog.findViewById(R.id.commit_record_audio_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.onSuccess(filePathandName);
                }
                recordDialog.dismiss();
            }
        });

        voiceManager.startVoiceRecord(FileUtil.getRecordPath(context));
    }

    private static void dismiss(VoiceManager voiceManager, Dialog recordDialog) {
        if (voiceManager != null) {
            voiceManager.stopRecordAndPlay();
        }
        recordDialog.dismiss();
    }
}
