package io.dcloud.H58E83894.ui.make.lexicalResource;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.ViewPagerFragmentAdapter;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.ItemModelData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.data.RandomData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.SpeakData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.data.make.SpeakShare;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.receiver.AlarmReceiver;
import io.dcloud.H58E83894.ui.center.leidou.recharge.ItemModel;
import io.dcloud.H58E83894.ui.common.CorretNoDialog;
import io.dcloud.H58E83894.ui.common.CorretSureDialog;
import io.dcloud.H58E83894.ui.common.RecordProxy;
import io.dcloud.H58E83894.ui.common.RecordSureProxy;
import io.dcloud.H58E83894.ui.common.SimpleLoginTipDialog;
import io.dcloud.H58E83894.ui.make.lexicalResource.adapter.SpeakCorretAdapter;
import io.dcloud.H58E83894.ui.make.bottom.sp.SpeakQuestionFragment;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class TodayCorretFragment extends BaseFragment {

    public static TodayCorretFragment getInstance(Context context, String id, int type) {
        TodayCorretFragment speakQuestionFragment = new TodayCorretFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SQ_ID", id);
        bundle.putInt("SQ_TYPE", type);
        speakQuestionFragment.setArguments(bundle);
        return speakQuestionFragment;
    }

    private SpeakCorretAdapter adapter;
    protected Context mContext;
    private int currentPage = 0;//默认等于零
    private String id;
    private int type;

    @BindView(R.id.all_answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_tv_tip)
    TextView emptyTipTxt;
    @BindView(R.id.answer_idea_tv)
    TextView ideaTipTxt;

    @BindView(R.id.today_setting_time)
    TextView todaySetTime; //设置提醒人数
    @BindView(R.id.today_setting_iv)
    ImageView todaySetIv; //设置提醒人数
    @BindView(R.id.today_speak_title)
    TextView todayTitle; //作文标题today_speak_free_status
    @BindView(R.id.today_speak_free_status)
    TextView todayFreeStatus; //面额状态
    Calendar mCalendar;
    private Boolean IsSpTx = false;
    RxPermissions rxPermissions;
    RandomData randomData;
    private String isDan ;
    private String dataTx = "false";
    private String isConplete;

    @Override
    protected void getArgs() {

        Bundle arguments = getArguments();
        if (arguments == null) return;
        id = arguments.getString("SQ_ID");
        type = arguments.getInt("SQ_TYPE");
    }




    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_make_today, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rxPermissions = new RxPermissions(getActivity());
        asyncUiInfo();
        EventBus.getDefault().register(this);

    }

    protected void asyncUiInfo() {


        //进页面按钮状态
        if(!(ToeflApplication.app.getData("isDanData").trim().equals("null")) && !TextUtils.isEmpty(ToeflApplication.app.getData("isDanData"))){
                isDan = ToeflApplication.app.getData("isDanData").trim();
        }else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date begainTime = new Date(System.currentTimeMillis()- 1100);
            Calendar date = Calendar.getInstance();
            date.setTime(begainTime);
            date.set(Calendar.DATE, date.get(Calendar.DATE)-1);
            try {
                Date endDate = format.parse(format.format(date.getTime()));
                isDan = format.format(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //进页面提醒状态
        randomData = RecordManager.getInstance().queryNumber("free_rc", 1);
        int randonss = randomData.getTimes()-25000;
        todaySetTime.setText("每天7点准时开枪，"+randonss+"人已设置提醒");
        if(!(String.valueOf(SharedPref.getBoolean("SpTx", getActivity())).equals("null")) && !TextUtils.isEmpty(String.valueOf(SharedPref.getBoolean("SpTx", getActivity())))){
            if(SharedPref.getBoolean("SpTx", getActivity()) == true){
                IsSpTx = true;
                todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_diss_remind));
                int randons = randomData.getTimes()-25000+1;
                todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                startRemind();
            }
        }else {
            int randons = randomData.getTimes()-25000;
            todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");

        }

        Log.i("appdata1tt", SharedPref.getBoolean("SpTx", getActivity())+"哈哈哈哈");
//
        showLoadDialog();

        asyncGlodData();
        initTime();

    }

    private void initTime() {

        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒 设置的为7点
        mCalendar.set(Calendar.HOUR_OF_DAY, 7);
        //设置在几分提醒 设置的为0分
        mCalendar.set(Calendar.MINUTE, 0);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        //上面设置的就是13点25分的时间点
        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();


        // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
        if(systemTime > selectTime) {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            String dataString = format.format(date);
            if(!(ToeflApplication.app.getData("isDanData").trim().equals("null")) && !TextUtils.isEmpty(ToeflApplication.app.getData("isDanData"))){
                isDan = ToeflApplication.app.getData("isDanData").trim();
            }
            if (!(dataString.equals(isDan))) {
                ToeflApplication.app.setData("isDanData", null);
                ToeflApplication.app.setData("isDanData", dataString);
                final Dialog dialog = new Dialog(getContext());
                View view = View.inflate(getContext(), R.layout.corret_dialog, null);
                dialog.setContentView(view);
//                                                                       dialog.setCancelable(true);
                TextView yv = (TextView) view.findViewById(R.id.update_tv);
                dialog.show();
//               showLoadDialog();
                yv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCompositeDis(HttpUtil
                                .getPlacess(0)
                                .subscribe(new Consumer<PayDatas>() {
                                    @Override
                                    public void accept(PayDatas bean) throws Exception {
//                                        dismissLoadDialog();
                                        if (bean.getCode() == 1) {
                                            toastShort(bean.getMessage());
                                            CorretSureDialog corretNoDialog = CorretSureDialog.getInstance("420");
                                            corretNoDialog.showDialog(getFragmentManager());
                                            dialog.dismiss();
                                        } else {
                                            toastShort(bean.getMessage());
                                            CorretNoDialog corretNoDialog = CorretNoDialog.getInstance("420");
                                            corretNoDialog.showDialog(getFragmentManager());
                                            dialog.dismiss();
                                        }

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        dismissLoadDialog();
                                        WaitDialog.getInstance(getContext()).dismissWaitDialog();
//
                                    }
                                }));

                    }
                });

            }
        }else {

        }
    }

    private void asyncGlodData() {

        addToCompositeDis(HttpUtil
                .getTodayList().subscribe(new Consumer<TodayListData>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void accept(@NonNull final TodayListData data) throws Exception {
                        dismissLoadDialog();
                        if (data == null) return;
                        todayTitle.setText(data.getQuestion());
                        isConplete = data.getIsComplete();
                        if(Integer.parseInt(data.getIsComplete().trim()) == 0){
//                            isDan = false;
                            todayFreeStatus.setText(getResources().getText(R.string.str_corret_spe_al_comple));
                            todayFreeStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PricePayCorretActivity.startPre(getContext(), "420");
                                }
                            });
                        }else {
//                            isDan = true;
                            mCalendar = Calendar.getInstance();
                            mCalendar.setTimeInMillis(System.currentTimeMillis());
                            //获取当前毫秒值
                            long systemTime = System.currentTimeMillis();
                            //是设置日历的时间，主要是让日历的年月日和当前同步
                            mCalendar.setTimeInMillis(System.currentTimeMillis());
                            // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
                            mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                            //设置在几点提醒 设置的为7点
                            mCalendar.set(Calendar.HOUR_OF_DAY, 7);
                            //设置在几分提醒 设置的为0分
                            mCalendar.set(Calendar.MINUTE, 0);
                            //下面这两个看字面意思也知道
                            mCalendar.set(Calendar.SECOND, 0);
                            mCalendar.set(Calendar.MILLISECOND, 0);
                            //上面设置的就是13点25分的时间点
                            //获取上面设置的13点25分的毫秒值
                            long selectTime = mCalendar.getTimeInMillis();

                            // 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                            if(systemTime > selectTime) {

                                todayFreeStatus.setText(getResources().getText(R.string.str_corret_spe_al_no));
                                todayFreeStatus.setBackground(getResources().getDrawable(R.color.color_text_green));
                                todayFreeStatus.setTextColor(getResources().getColor(R.color.color_white));
                                todayFreeStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Dialog dialog = new Dialog(getContext());
                                        View view = View.inflate(getContext(), R.layout.corret_dialog, null);
                                        dialog.setContentView(view);
                                        TextView yv = (TextView) view.findViewById(R.id.update_tv);
                                        dialog.show();
//                                        showLoadDialog();
                                        yv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                addToCompositeDis(HttpUtil
                                                        .getPlacess(0)
                                                        .subscribe(new Consumer<PayDatas>() {
                                                            @Override
                                                            public void accept(PayDatas bean) throws Exception {
                                                                WaitDialog.getInstance(getContext()).dismissWaitDialog();
//                                                                dismissLoadDialog();
                                                                if (bean.getCode() == 1) {
                                                                    toastShort(bean.getMessage());
                                                                    CorretSureDialog corretNoDialog = CorretSureDialog.getInstance("420");
                                                                    corretNoDialog.showDialog(getFragmentManager());
                                                                    dialog.dismiss();
                                                                } else {
                                                                    toastShort(bean.getMessage());
                                                                    CorretNoDialog corretNoDialog = CorretNoDialog.getInstance("420");
                                                                    corretNoDialog.showDialog(getFragmentManager());
                                                                    dialog.dismiss();
                                                                }

                                                            }
                                                        }, new Consumer<Throwable>() {
                                                            @Override
                                                            public void accept(Throwable throwable) throws Exception {
//                                                                dismissLoadDialog();
                                                                WaitDialog.getInstance(getContext()).dismissWaitDialog();
                                                                dialog.dismiss();
                                                            }
                                                        }));

                                            }
                                        });
                                    }
                                });
                            }else {
                                todayFreeStatus.setText("七点以后抢免费口语批改名额");
                                todayFreeStatus.setBackground(getResources().getDrawable(R.color.color_dot));
                                todayFreeStatus.setTextColor(getResources().getColor(R.color.color_sup_red));
                            }

                        }
                        List<TodayListData.DataBean> todayList= data.getData();
                        showAllAnswer(todayList);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        Log.i("tttt", throwable+ "ddd");
                        errorTip(throwable);
                    }
                }));
    }

    private void showAllAnswer(List<TodayListData.DataBean> todayList) {
        if (todayList == null) return;
        if (todayList == null || todayList.isEmpty()) {
            Utils.setVisible(emptyTipTxt);
            Utils.setGone(mRecyclerView, ideaTipTxt);
        } else {
            initRecyclerData(todayList);
            Utils.setVisible(mRecyclerView);
            Utils.setGone(emptyTipTxt, ideaTipTxt);
        }
    }



    private void initRecyclerData(List<TodayListData.DataBean> share) {
        if (adapter == null) {
            initRecycler(mRecyclerView, new LinearLayoutManager(getActivity()));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getActivity(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            adapter = new SpeakCorretAdapter(share, getContext(), rxPermissions);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapterDispose();
            adapter.update(share);
        }
    }


    private void adapterDispose() {
        if (adapter == null) return;
        List<TodayListData.DataBean> share = adapter.getDate();
        if (share == null || share.isEmpty()) return;
        for (TodayListData.DataBean ss : share) {
            Disposable disposable = ss.mDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ItemModelData resultBean) {


        int contentId = resultBean.getContentId();
        recAudio(contentId, resultBean.getToken());

//        tv_recharge_money.setText(money+"");
    }

    @OnClick({ R.id.record_answer, R.id.today_setting_iv})
    public void onClick(View view) {

//        if(!TextUtils.isEmpty(ToeflApplication.app.getData("tixing"))){
//            dataTx = ToeflApplication.app.getData("tixing");
//        }else {
//            dataTx = "false";
//        }

        switch (view.getId()) {
            case R.id.record_answer:
                if (needLogin()) return;
                    addToCompositeDis(HttpUtil
                            .getPlaces(420)
                            .subscribe(new Consumer<CorretData>() {
                                @Override
                                public void accept(CorretData bean) throws Exception {
//                                dismissLoadDialog();
                                    if(bean.getCode() == 1){
                                        toastShort("使用口语点评名额成功");
                                        int contentid = Integer.parseInt(bean.getContentId().trim());
                                        recAudio(contentid, bean.getToken());

                                    }else {
                                        toastShort("今日免费名额已抢完");
                                        PricePayCorretActivity.startPre(getContext(), "420");
                                        dismissLoadDialog();
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
//                                showLoadDialog();
                                }
                            }));

                break;

            case R.id.today_setting_iv:

                if(IsSpTx == false){

                    toastShort("设置提醒成功");
                    SharedPref.setBoolean("SpTx", true , getActivity());
                    Log.i("appdata1", SharedPref.getBoolean("SpTx", getActivity())+"哈哈哈哈");
                    IsSpTx = true;
                    todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_diss_remind));
                    int randons = randomData.getTimes()-25000+1;
                    todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                    startRemind();

                }else {
                    toastShort("设置取消提醒成功");
                    SharedPref.setBoolean("SpTx", false , getActivity());
                    Log.i("appdata", SharedPref.getBoolean("SpTx", getActivity())+"哈哈哈哈");
                    IsSpTx = false;
                    todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sure_remind));
                    int randons = randomData.getTimes()-25000;
                    todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                    stopRemind();
                }
//                startRemind();
                break;
            default:
                break;
        }
    }

    private void recAudio(final int contentId,  final  int token) {//语音


        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            RecordSureProxy.showRecordDialog(getActivity(), VoiceManager.getInstance(getActivity()), new ICallBack<String>() {
                                @Override
                                public void onSuccess(String absFilePath) {

                                    commitRecordAudio(absFilePath, contentId, token);
                                }

                                @Override
                                public void onFail() {

                                }
                            });
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                });
    }


    private void commitRecordAudio(String path, final int contentId, int token) {
        if (TextUtils.isEmpty(path)) return;
        final File file = new File(path);
        if (!file.exists()) return;
        final RequestBody rb = RequestBody.create(MediaType.parse("audio/mpeg"), file);
        final MultipartBody.Part mp = MultipartBody.Part.createFormData("upload_file", file.getName(), rb);
        showLoadDialog();
        addToCompositeDis(HttpUtil
                .spokenUpTokens(token, mp)
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean>>() {
                    @Override
                    public ObservableSource<ResultBean> apply(@NonNull ResultBean bean) throws Exception {
                        return HttpUtil.spokenSaves(contentId, bean.getFile());
                    }
                }).compose(new SchedulerTransformer<ResultBean>())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        toastShort("上传成功");
                        asyncGlodData();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                        toastShort("上传失败");
                        errorTip(throwable);
                    }
                }));
    }

    /**
     * 开启提醒
     */
    private void startRemind(){
        //得到日历实例，主要是为了下面的获取时间
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        //获取当前毫秒值
        long systemTime = System.currentTimeMillis();
        //是设置日历的时间，主要是让日历的年月日和当前同步
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下，不然可能个别手机会有8个小时的时间差
        mCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置在几点提醒 设置的为7点
        mCalendar.set(Calendar.HOUR_OF_DAY,7);
        //设置在几分提醒 设置的为0分
        mCalendar.set(Calendar.MINUTE, 0);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        //上面设置的就是13点25分的时间点
        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        //AlarmReceiver.class为广播接受者
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, getMsgIntent(), 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getActivity().getSystemService(getActivity().ALARM_SERVICE);
        //**********注意！！下面的两个根据实际需求任选其一即可*********
        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
            am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);
            Log.i("ninini_4", "hahah");



        /**
         * 重复提醒
         * 第一个参数是警报类型；下面有介绍
         * 第二个参数网上说法不一，很多都是说的是延迟多少毫秒执行这个闹钟，但是我用的刷了MIUI的三星手机的实际效果是与单次提醒的参数一样，即设置的13点25分的时间点毫秒值
         * 第三个参数是重复周期，也就是下次提醒的间隔 毫秒值 我这里是一天后提醒
         */
//        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), (1000 * 60 * 60 * 24), pi);
    }

    private Intent getMsgIntent() {
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.setAction(AlarmReceiver.SP_Action);
        intent.putExtra("Ea", "1");
        return  intent;
    }
    /**
     * 关闭提醒
     */
    private void stopRemind(){
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0,
                intent, 0);
        AlarmManager am = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
//        Toast.makeText(this, "关闭了提醒", Toast.LENGTH_SHORT, 0).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapterDispose();
        if (VoiceManager.getInstance(getActivity()) != null) {
            VoiceManager.getInstance(getActivity()).stopRecordAndPlay();
        }
        EventBus.getDefault().unregister(this);
    }

}
