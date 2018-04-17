package io.dcloud.H58E83894.ui.make.easyResource;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.caimuhao.rxpicker.bean.ImageItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.ItemModelData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.data.RandomData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.SpeakQuestionData;
import io.dcloud.H58E83894.db.RecordManager;
import io.dcloud.H58E83894.excep.CustomException;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.receiver.AlarmReceiver;
import io.dcloud.H58E83894.ui.center.setting.ClipImageActivity;
import io.dcloud.H58E83894.ui.common.CorretNoDialog;
import io.dcloud.H58E83894.ui.common.CorretSureDialog;
import io.dcloud.H58E83894.ui.common.RecordProxy;
import io.dcloud.H58E83894.ui.make.easyResource.adapter.EaSpeakCorretAdapter;
import io.dcloud.H58E83894.ui.make.lexicalResource.PricePayCorretActivity;
import io.dcloud.H58E83894.ui.make.lexicalResource.adapter.SpeakCorretAdapter;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.ImageUtil;
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

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class EaTodayCorretFragment extends BaseFragment {

    public static EaTodayCorretFragment getInstance(Context context, String id, int type) {
        EaTodayCorretFragment speakQuestionFragment = new EaTodayCorretFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SQ_ID", id);
        bundle.putInt("SQ_TYPE", type);
        speakQuestionFragment.setArguments(bundle);
        return speakQuestionFragment;
    }

    private EaSpeakCorretAdapter adapter;
    protected Context mContext;
    private List<SpeakQuestionData> mSpeakDatas;
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
    @BindView(R.id.today_free)
    TextView dataTitle;//时间标题 record_answer
    @BindView(R.id.record_answer)
            TextView tvReco;
    Calendar mCalendar;
    private boolean isTure = true;
    private List<ImageItem> upImage;
    //请求相机
    private static final int REQUEST_CAPTURE = 200;
    //请求相册
    private static final int REQUEST_PICK = 201;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 202;

    //调用照相机返回图片临时文件
    private File tempFile;

    private  int contentId;
    private  int token;
    protected RxPermissions mRxPermissions;
    private String isDan;
    RandomData randomData;
    private String dataEaTx;
    private Boolean IsSpTx = false;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asyncUiInfo();
        mRxPermissions = new RxPermissions(getActivity());
        createCameraTempFile(savedInstanceState);
        tvReco.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_easy_corret));
        dataTitle.setText(HtmlUtil.fromHtml(this.getString(R.string.str_corretss_date_num, "今日")));
        EventBus.getDefault().register(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    protected void asyncUiInfo() {

        if(!(ToeflApplication.app.getData("isEasyData").trim().equals("null")) && !TextUtils.isEmpty(ToeflApplication.app.getData("isEasyData"))){
            isDan = ToeflApplication.app.getData("isEasyData").trim();
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

        int randonss = randomData.getTimes()-24500;
        todaySetTime.setText("每天7点准时开枪，"+randonss+"人已设置提醒");
        if(!(String.valueOf(SharedPref.getBoolean("EaTx", getActivity())).equals("null")) && !TextUtils.isEmpty(String.valueOf(SharedPref.getBoolean("EaTx", getActivity())))){
            if(SharedPref.getBoolean("EaTx", getActivity()) == true){
                IsSpTx = true;
                todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_diss_remind));
                int randons = randomData.getTimes()-24500+1;
                todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                startRemind();
            }
        }else {
            int randons = randomData.getTimes()-24500;
            todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");

        }

        showLoadDialog();

        asyncGlodData();

        initTime();//处理点击按钮

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
            if(!(ToeflApplication.app.getData("isEasyData").trim().equals("null")) && !TextUtils.isEmpty(ToeflApplication.app.getData("isEasyData"))){
                isDan = ToeflApplication.app.getData("isEasyData").trim();
            }
            if (!(dataString.equals(isDan))) {
                ToeflApplication.app.setData("isEasyData", null);
                ToeflApplication.app.setData("isEasyData", dataString);
                final Dialog dialog = new Dialog(getContext());
                View view = View.inflate(getContext(), R.layout.corret_dialog, null);
                dialog.setContentView(view);
//                                                                       dialog.setCancelable(true);
                TextView yv = (TextView) view.findViewById(R.id.update_tv);
                dialog.show();
                yv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addToCompositeDis(HttpUtil
                                .getEaPlacess(0)
                                .subscribe(new Consumer<PayDatas>() {
                                    @Override
                                    public void accept(PayDatas bean) throws Exception {
                                        WaitDialog.getInstance(getContext()).dismissWaitDialog();
                                        if (bean.getCode() == 1) {
                                            toastShort(bean.getMessage());
                                            CorretSureDialog corretNoDialog = CorretSureDialog.getInstance("419");
                                            corretNoDialog.showDialog(getFragmentManager());
                                            dialog.dismiss();
                                        } else {
                                            toastShort(bean.getMessage());
                                            CorretNoDialog corretNoDialog = CorretNoDialog.getInstance("419");
                                            corretNoDialog.showDialog(getFragmentManager());
                                            dialog.dismiss();
                                        }

                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
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
                .getEaTodayList().subscribe(new Consumer<TodayListData>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void accept(@NonNull TodayListData data) throws Exception {
                        dismissLoadDialog();
                        if (data == null) return;
                        todayTitle.setText(data.getQuestion());
                        if(Integer.parseInt(data.getIsComplete().trim()) == 0){
                            todayFreeStatus.setText(getResources().getText(R.string.str_corret_spe_al_comple));
                            todayFreeStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PricePayCorretActivity.startPre(getContext(), "419");
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

                                todayFreeStatus.setText(getResources().getText(R.string.str_correts_spe_al_no));
                                todayFreeStatus.setBackground(getResources().getDrawable(R.color.color_text_green));
                                todayFreeStatus.setTextColor(getResources().getColor(R.color.color_white));
//                                showLoadDialog();
                                todayFreeStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final Dialog dialog = new Dialog(getContext());
                                        View view = View.inflate(getContext(), R.layout.corret_dialog, null);
                                        dialog.setContentView(view);
                                        TextView yv = (TextView) view.findViewById(R.id.update_tv);
                                        dialog.show();
                                        yv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                addToCompositeDis(HttpUtil
                                                        .getEaPlacess(0)
                                                        .subscribe(new Consumer<PayDatas>() {
                                                            @Override
                                                            public void accept(PayDatas bean) throws Exception {
//                                                                WaitDialog.getInstance(getContext()).dismissWaitDialog();
                                                                if (bean.getCode() == 1) {
//                                                                    dismissLoadDialog();
                                                                    toastShort(bean.getMessage());
                                                                    CorretSureDialog corretNoDialog = CorretSureDialog.getInstance("419");
                                                                    corretNoDialog.showDialog(getFragmentManager());
                                                                    dialog.dismiss();
                                                                } else {
//                                                                    dismissLoadDialog();
                                                                    toastShort(bean.getMessage());
                                                                    CorretNoDialog corretNoDialog = CorretNoDialog.getInstance("419");
                                                                    corretNoDialog.showDialog(getFragmentManager());
                                                                    dialog.dismiss();
                                                                }

                                                            }
                                                        }, new Consumer<Throwable>() {
                                                            @Override
                                                            public void accept(Throwable throwable) throws Exception {
//                                                                dismissLoadDialog(); dialog.dismiss();
                                                                WaitDialog.getInstance(getContext()).dismissWaitDialog();
                                                                dialog.dismiss();
                                                            }
                                                        }));

                                            }
                                        });
                                    }
                                });
                            }else {
                                todayFreeStatus.setText("七点以后抢免费作文批改名额");
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
            initRecycler(mRecyclerView, new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            adapter = new EaSpeakCorretAdapter(share, getActivity(), mRxPermissions);
            mRecyclerView.setAdapter(adapter);
        } else {
            adapterDispose();
            adapter.update(share);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ItemModelData resultBean) {

        contentId = resultBean.getContentId();
        token =resultBean.getToken();
        headerChoose();
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

    @OnClick({ R.id.record_answer, R.id.today_setting_iv})
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.record_answer:
                if (needLogin()) return;

                showLoadDialog();
                addToCompositeDis(HttpUtil
                        .getPlaces(419)
                        .subscribe(new Consumer<CorretData>() {
                            @Override
                            public void accept(CorretData bean) throws Exception {
                                dismissLoadDialog();
                                if(bean.getCode() == 1){
                                    toastShort("使用作文点评名额成功");
                                    contentId = Integer.parseInt(bean.getContentId().trim());
                                    token = bean.getToken();
                                    headerChoose();

                                }else {
                                    toastShort("今日免费名额已抢完");
                                    PricePayCorretActivity.startPre(getContext(), "200");
                                    dismissLoadDialog();
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dismissLoadDialog();
//                                showLoadDialog();
                            }
                        }));
                break;

            case R.id.today_setting_iv:
//                long nowTime = 1520585547211/100;

                if(IsSpTx == false){
                    toastShort("设置提醒成功");
                    SharedPref.setBoolean("EaTx", true , getActivity());
                    Log.i("appdata1", SharedPref.getBoolean("EaTx", getActivity())+"哈哈哈哈");
                    IsSpTx = true;
                    todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_diss_remind));
                    int randons = randomData.getTimes()-24500+1;
                    todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                    startRemind();

                }else {
                    toastShort("设置取消提醒成功");
                    SharedPref.setBoolean("EaTx", false , getActivity());
                    Log.i("appdata", SharedPref.getBoolean("EaTx", getActivity())+"哈哈哈哈");
                    IsSpTx = false;
                    todaySetIv.setImageDrawable(getResources().getDrawable(R.drawable.icon_sure_remind));
                    int randons = randomData.getTimes()-24500;
                    todaySetTime.setText("每天7点准时开枪，"+randons+"人已设置提醒");
                    stopRemind();
                }

                break;
            default:
                break;
        }
    }

    public void headerChoose() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
         popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    gotoCarema();
                                } else {
                                    toastShort(R.string.str_camera_no_permisson);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    gotoPhoto();
                                } else {
                                    toastShort(R.string.str_read_external_no_permisson);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {

                            }
                        });
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    /**
       * 创建调用系统照相机待存储的临时文件
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.str_please_choose_pic)), REQUEST_PICK);
    }

    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT  >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri imageUri = FileProvider.getUriForFile(getActivity(), "io.dcloud.H58E83894.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        }else {
            Uri  imageUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
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
        mCalendar.set(Calendar.HOUR_OF_DAY, 7);
        //设置在几分提醒 设置的为0分
        mCalendar.set(Calendar.MINUTE, 0);
        //下面这两个看字面意思也知道
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
        //上面设置的就是13点25分的时间点
        //获取上面设置的13点25分的毫秒值
        long selectTime = mCalendar.getTimeInMillis();

        PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0,  getMsgIntent(), 0);
        //得到AlarmManager实例
        AlarmManager am = (AlarmManager)getContext().getSystemService(ALARM_SERVICE);
        //**********注意！！下面的两个根据实际需求任选其一即可*********
        /**
         * 单次提醒
         * mCalendar.getTimeInMillis() 上面设置的13点25分的时间点毫秒值
         */
        am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pi);

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
        intent.setAction(AlarmReceiver.Ea_Action);
        intent.putExtra("Ea", "2");
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
//                if (resultCode == RESULT_OK) {
//                    gotoClipActivity(Uri.fromFile(tempFile));
//                }

                if (resultCode == RESULT_OK) {
                    Uri uri = Uri.fromFile(tempFile);
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getContext(), uri);
                    uploadHeader(cropImagePath);
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回

//                if (resultCode == RESULT_OK) {
//                    Uri uri = intent.getData();
//                    gotoClipActivity(uri);
//                }

                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    uploadHeader(cropImagePath);
                }
                break;
//            case REQUEST_CROP_PHOTO:  //剪切图片返回
//                if (resultCode == RESULT_OK) {
//                    final Uri uri = intent.getData();
//                    if (uri == null) {
//                        return;
//                    }
//                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getContext(), uri);
//                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
//
////                    headIv.setImageBitmap(bitMap);
//
//                    //此处后面可以将bitMap转为二进制上传后台网络
//                    //......
//                    uploadHeader(cropImagePath);
//                }

//                if (resultCode == RESULT_OK) {
//                    final Uri uri = intent.getData();
//                    if (uri == null) {
//                        return;
//                    }
//                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getContext(), uri);
//                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
//
////                    headIv.setImageBitmap(bitMap);
//
//                    //此处后面可以将bitMap转为二进制上传后台网络
//                    //......
//                    uploadHeader(cropImagePath);
//                }
//                break;
        }
    }

    private void uploadHeader(final String cropImagePath) {
        if (TextUtils.isEmpty(cropImagePath)) return;
        File file = new File(cropImagePath);
        if (!file.exists()) {
            return;
        }
        RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part mp = MultipartBody.Part.createFormData("upload", file.getName(), rb);


        if(TextUtils.isEmpty(String.valueOf(token)) || (String.valueOf(token)).length() == 0){ return;}
        if(TextUtils.isEmpty(String.valueOf(contentId)) || (String.valueOf(contentId)).length() == 0){ return;}
        showLoadDialog();
        Log.i("mmmp3", mp+"= gggg = "+ cropImagePath);
        addToCompositeDis(HttpUtil
                .spokenUpTokenss(mp)
                .flatMap(new Function<ResultBean, ObservableSource<ResultBean>>() {
                    @Override
                    public ObservableSource<ResultBean> apply(@NonNull ResultBean bean) throws Exception {
                        Log.i("mmmp4", mp+"= gggg = "+ cropImagePath+"url ="+bean.getFile()+"url ="+bean.getCode()+bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            Log.i("mmmp01", id+"url ="+bean.getFile());

                            return HttpUtil.spokenSaves(contentId, bean.getFile());
                        }
                        toastShort(bean.getMessage());
                        throw new CustomException(bean.getMessage());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
