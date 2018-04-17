package io.dcloud.H58E83894.ui.center.setting;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.net.URI;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.center.setting.modify.ModifyPhoneOrEmailDialog;
import io.dcloud.H58E83894.ui.center.setting.modify.ModifyPwdDialog;
import io.dcloud.H58E83894.ui.common.update.DownloadApk;
import io.dcloud.H58E83894.ui.common.update.SimpleUpdateApk;
import io.dcloud.H58E83894.ui.user.modify.ModifyNickActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.DataCleanManager;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.ImageUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class SettingActivity extends BaseActivity implements DownloadApk.OnDownloadApkListener {

    //    @BindView(R.id.setting_user_name_tv)
//    TextView userNameTv;
    @BindView(R.id.setting_nick_name_tv)
    TextView nickNameTv;
    @BindView(R.id.setting_phone_tv)
    TextView phoneTv;
    @BindView(R.id.setting_email_tv)
    TextView emailTv;
    @BindView(R.id.user_info_container)
    LinearLayout userInfoContainer;
    @BindView(R.id.exit_login)
    TextView exitTxt;
    @BindView(R.id.center_user_img)
    CircleImageView headIv;
    @BindView(R.id.set_modify_pwd_container)
    RelativeLayout pwdContainer;
    @BindView(R.id.current_version)
    TextView currentVersionTv;
    @BindView(R.id.cache_data_size_tv)
    TextView cacheTv;


    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;

    //调用照相机返回图片临时文件
    private File tempFile;

    //    private RxPermissions rxPermission;
    private SimpleUpdateApk mSimpleUpdateApk;
    private Context context;

    private Observable<Integer> modifyInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
//        rxPermission = new RxPermissions(this);
        mSimpleUpdateApk = new SimpleUpdateApk(SettingActivity.this, true);
        createCameraTempFile(savedInstanceState);
        modifyInfo = RxBus.get().register(C.MODIFY_INFO, Integer.class);
        modifyInfo.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                if (GlobalUser.getInstance().isAccountDataInvalid()) return;
                UserData userData = GlobalUser.getInstance().getUserData();
                if (C.MODIFY_NICKNAME == integer) {
                    if (!TextUtils.isEmpty(userData.getNickname())) {
                        nickNameTv.setText(userData.getNickname());
                    }
                    postRxBus();//这里用来更新昵称
                }
            }
        });
        caclCacheSize();
    }

    private void caclCacheSize() {
        try {
            cacheTv.setText(DataCleanManager.getTotalCacheSize(mContext));
        } catch (Exception e) {
            cacheTv.setText("0");
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }



    private void picTyTakePhoto() {

    }
    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT  >= 24) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri   imageUri = FileProvider.getUriForFile(this, "io.dcloud.H58E83894.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        }else {
            Uri  imageUri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.str_please_choose_pic)), REQUEST_PICK);
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
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
//            case C.SET_NICK_REQUEST_CODE:
//                nickNameTv.setText(GlobalUser.getInstance().getUserData().getNickname());
//                //这里并不是退出登录，只是发送广播通知个人中心界面更新ui
////                LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(C.EXIT_LOGIN_ACTION));
//                break;
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = ImageUtil.getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);

                    headIv.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    uploadHeader(cropImagePath);
                }
                break;
        }
    }

    private void uploadHeader(String cropImagePath) {
        File file = new File(cropImagePath);
        if (!file.exists()) {
            return;
        }
        RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part mp = MultipartBody.Part.createFormData("upload", file.getName(), rb);
        addToCompositeDis(HttpUtil.replaceHeader(mp).subscribe(new Consumer<ResultBean>() {
            @Override
            public void accept(@NonNull ResultBean bean) throws Exception {
                if (getHttpResSuc(bean.getCode())) {
                    savePhoto(bean.getImage());
                } else {
                    toastShort(bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }

    private void savePhoto(String photourl) {
        if (TextUtils.isEmpty(photourl)) return;
        GlobalUser.getInstance().setPhotoUrl(photourl);
        GlobalUser.getInstance().resetUserInfoToSp(mContext);
        postRxBus();//这里用来刷新头像
    }

    private void postRxBus() {
        RxBus.get().post(C.LOGIN_INFO, true);
    }

    @OnClick({R.id.set_head_container, R.id.exit_login, R.id.set_nick_name_container, /*R.id.setting_user_name_container,*/ R.id.set_modify_phone_container,
            R.id.set_modify_email_container, R.id.set_modify_pwd_container, R.id.set_gov_container, R.id.version_check, R.id.setting_font_size,
            R.id.set_wexin_container, R.id.set_tencent_qq_container, R.id.feed_back_container, R.id.cache_data_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cache_data_container:
                DataCleanManager.clearAllCache(mContext);
                caclCacheSize();
                break;
            case R.id.setting_font_size:
                forword(FontSizeSettingActivity.class);
                break;
            case R.id.version_check:
//                checkVersion();
                mSimpleUpdateApk.checkVersionUpdate();
                break;
            case R.id.set_head_container:
                headerChoose();
                break;
            case R.id.exit_login:
                //清空sp中的登录信息，清空内存中的个人对象
                GlobalUser.getInstance().exitLogin(mContext);
                finishWithAnim();
                break;
//            case R.id.setting_user_name_container:
//                toastShort(R.string.str_set_modify_user_name_fail_tip);
//                break;
            case R.id.set_nick_name_container:
                modifyNickName();
                break;
            case R.id.set_modify_phone_container:
                modifyPhoneOrEmail(false);
                break;
            case R.id.set_modify_email_container:
                modifyPhoneOrEmail(true);
                break;
            case R.id.set_modify_pwd_container:
                modifyPwd();
                break;
            case R.id.set_gov_container:
                Utils.copy(getString(R.string.str_set_gmat_net), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.set_wexin_container:
                Utils.copy(getString(R.string.str_set_wx_num_info), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.set_tencent_qq_container:
                Utils.copy(getString(R.string.str_set_gmat_qq_number), mContext);
                toastShort(R.string.str_set_copy_success);
                break;
            case R.id.feed_back_container:
                forword(FeedBackActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSimpleUpdateApk != null) {
            mSimpleUpdateApk.onDestory();
            mSimpleUpdateApk = null;
        }
    }

    private void headerChoose() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                params.alpha = 1.0f;
                getWindow().setAttributes(params);
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

    private void modifyPwd() {
        ModifyPwdDialog.getInstance(new ICallBack<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFail() {
            }
        }).showDialog(getSupportFragmentManager());
    }

    private void modifyPhoneOrEmail(final boolean modifyEmail) {
        ModifyPhoneOrEmailDialog.getInstance(modifyEmail, new ICallBack<String>() {
            @Override
            public void onSuccess(String emailOrPhone) {
                if (modifyEmail) {
                    emailTv.setText(emailOrPhone);
                } else {
                    phoneTv.setText(emailOrPhone);
                }
            }

            @Override
            public void onFail() {
            }
        }).showDialog(getSupportFragmentManager());
    }

    private void modifyNickName() {
        forword(ModifyNickActivity.class);
    }

    @Override
    protected void initData() {
        currentVersionTv.setText(Utils.getCurrentVersion(mContext));
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
            //有效
            UserData userData = GlobalUser.getInstance().getUserData();
//            if (!TextUtils.isEmpty(userData.getUsername())) {
//                userNameTv.setText(userData.getUsername());
//            }
            if (!TextUtils.isEmpty(userData.getNickname())) {
                nickNameTv.setText(userData.getNickname());
            }
            if (!TextUtils.isEmpty(userData.getPhone())) {
                phoneTv.setText(userData.getPhone());
            }
            if (!TextUtils.isEmpty(userData.getEmail())) {
                emailTv.setText(userData.getEmail());
            }
            if (!TextUtils.isEmpty(userData.getImage())) {
//                headIv
                GlideUtil.load(RetrofitProvider.TOEFLURL + userData.getImage(), headIv);
            }
            if (Utils.isEmpty(SharedPref.getPassword(mContext), SharedPref.getAccount(mContext))) {
                Utils.setGone(pwdContainer);
            }
        } else {
            Utils.setGone(userInfoContainer, exitTxt);
        }

    }

    @Override
    public void onDownError() {
        toastShort(R.string.str_update_apk_fail);
    }
}
