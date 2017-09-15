package io.dcloud.H58E83894.ui.toeflcircle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caimuhao.rxpicker.RxPicker;
import com.caimuhao.rxpicker.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.RecycleViewGridCommissionDivider;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.callback.ICallBack;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.SchedulerTransformer;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.PostRemarkAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.ImageUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by fire on 2017/7/17  10:40.
 */

public class PostRemarkActivity extends BaseActivity {

    /**
     * @param from 来自社区的发布帖子
     */
    public static void startNewPostRemark(Context c, boolean from) {
        Intent intent = new Intent(c, PostRemarkActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, from);
        c.startActivity(intent);
    }

    @BindView(R.id.post_btn)
    TextView sendBtn;
    @BindView(R.id.remark_post_enter_title_et)
    EditText titleEt;
    @BindView(R.id.remark_post_enter_content_et)
    EditText contentEt;
    @BindView(R.id.post_img_recycler)
    RecyclerView imgRecycler;
    @BindView(R.id.new_remark_post_btn)
    TextView newPostBtn;
    @BindView(R.id.post_pic_container)
    LinearLayout oldPostContainer;
    private List<ImageItem> upImage;
    private PostRemarkAdapter imgAdapter;
    private boolean fromNewRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_remark);
    }

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        fromNewRemark = intent.getBooleanExtra(Intent.EXTRA_TEXT, false);
    }

    @Override
    protected void initData() {
        super.initData();
        sendBtn.setSelected(false);
        sendBtn.setClickable(false);
        titleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changePostStatus();
            }
        });
        contentEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changePostStatus();
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        if (fromNewRemark) {
            Utils.setVisible(newPostBtn);
            Utils.setGone(oldPostContainer);
        } else {
            Utils.setGone(newPostBtn);
            Utils.setVisible(oldPostContainer);
        }
        imgRecycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        imgRecycler.addItemDecoration(new RecycleViewGridCommissionDivider(mContext, R.drawable.post_divider));
        imgRecycler.setHasFixedSize(true);
        imgRecycler.setNestedScrollingEnabled(false);
        upImage = new ArrayList<>();
        upImage.add(new ImageItem());
        imgAdapter = new PostRemarkAdapter(upImage);
        imgRecycler.setAdapter(imgAdapter);
        List<String> photoUrls = new ArrayList<String>();
//        for (PhotoInfo photoInfo : photos) {
//            photoUrls.add(photoInfo.url);
//        }
//        ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
        imgAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                List<String> photoUrls = new ArrayList<>();
                for (int i = 0, size = (upImage.size() - 1); i < size; i++) {
                    ImageItem img = upImage.get(i);
                    photoUrls.add(img.getPath());
                }
                ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
            }
        });
        imgAdapter.setChoosePicListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                choosePic(0, 9 - position);
            }
        });
    }

    private void changePostStatus() {
        String txt = getEditText(titleEt);
        String content = getEditText(contentEt);
        if (TextUtils.isEmpty(txt) || TextUtils.isEmpty(content)) {
            sendBtn.setSelected(false);
            sendBtn.setClickable(false);
        } else {
            sendBtn.setSelected(true);
            sendBtn.setClickable(true);
        }

    }

    @OnClick({R.id.post_btn, R.id.new_remark_post_btn})
    public void onClick(View view) {
        switch (view.getId()) {
//                choosePic();
            case R.id.post_btn:
//                log(upImage.size() + "==");
                postRemark();
                break;
            case R.id.new_remark_post_btn:
                postNewRemark();
                break;
            default:
                break;
        }
    }

    private void postNewRemark() {
        final String titleText = getEditText(titleEt);
        final String contentText = getEditText(contentEt);
        if (judeCondi(titleText, contentText)) return;
        addToCompositeDis(HttpUtil.addPost(titleText, contentText)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RxBus.get().post(C.TOEFL_CIRCLE_POST_COMMUNITY, true);
                            finishWithAnim();
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


    private void postRemark() {
        final String titleText = getEditText(titleEt);
        final String contentText = getEditText(contentEt);
        if (judeCondi(titleText, contentText)) return;
        showLoadDialog();
        if (upImage == null || upImage.size() == 1/*只有一个加号*/) {
            //只发布了文字
            releaseStatus(titleText, contentText, new ArrayList<String>());
        } else {
            //先上传图片在发状态
            upload(upImage, new ICallBack<List<String>>() {
                @Override
                public void onFail() {
                    dismissLoadDialog();
                }

                @Override
                public void onSuccess(List<String> strings) {
                    //返回的图片数据
                    releaseStatus(titleText, contentText, strings);
                }
            });
        }
    }

    private boolean judeCondi(String titleText, String contentText) {
        if (TextUtils.isEmpty(titleText)) {
            toastShort(getString(R.string.str_post_remark_enter_title));
            return true;
        }
        if (TextUtils.isEmpty(contentText)) {
            toastShort(getString(R.string.str_post_remark_enter_content));
            return true;
        }
        return false;
    }

    private void releaseStatus(String title, String content, List<String> images) {
        if (needLogin()) {
            return;
        }
//        if (GlobalUser.getInstance().isAccountDataInvalid()) {//本地未登录
//            toastShort(R.string.str_no_login_tip);
//            dismissLoadDialog();
//            return;
//        }
        UserData data = GlobalUser.getInstance().getUserData();
        String uName = data.getNickname();
        addToCompositeDis(HttpUtil.releaseStatus(title, content, images, data.getImage(), uName)
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        RxBus.get().post(C.TOEFL_CIRCLE_POST_REMARK, true);
                        finishWithAnim();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    private void upload(List<ImageItem> images, final ICallBack<List<String>> mCallBack) {
        if (images == null || images.isEmpty()) {
            mCallBack.onFail();
            return;
        }
        List<Observable<ResultBean>> uploads = new ArrayList<>();
        for (ImageItem item : images) {
            if (TextUtils.isEmpty(item.getPath())) {
                continue;
            }
            String compressImagePath = ImageUtil.compressImage(mContext, item.getPath());
            if (TextUtils.isEmpty(compressImagePath)) {
                continue;
            }
            File file = new File(compressImagePath);
            if (!file.exists()) {
                continue;
            }
            RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part mp = MultipartBody.Part.createFormData("upload", file.getName(), rb);
            uploads.add(HttpUtil.upload(mp));
        }
        if (uploads.isEmpty()) {
            mCallBack.onFail();
            return;
        }
        addToCompositeDis(Observable.zipIterable(uploads, new Function<Object[], List<String>>() {
            @Override
            public List<String> apply(@NonNull Object[] objects) throws Exception {
                List<String> list = new ArrayList<String>();
                for (int i = 0, size = objects.length; i < size; i++) {
                    ResultBean bean = (ResultBean) objects[i];
                    if (getHttpResSuc(bean.getCode())) {
                        list.add(bean.getImage());
                    }
                }
                return list;
            }
        }, false, images.size()).compose(new SchedulerTransformer<List<String>>())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(@NonNull List<String> strings) throws Exception {
                        //图片上传成功，发表备料八卦
                        mCallBack.onSuccess(strings);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        mCallBack.onFail();
                    }
                }));
    }

    private void choosePic(final int start, final int end) {
        mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@NonNull Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            RxPicker.of()
                                    .single(false)
                                    .camera(true)
                                    .limit(start, end)
                                    .start(PostRemarkActivity.this)
                                    .subscribe(new Consumer<List<ImageItem>>() {
                                        @Override
                                        public void accept(@NonNull List<ImageItem> items) throws Exception {
                                            int size = upImage.size();
                                            upImage.remove(size - 1);
                                            upImage.addAll(items);
                                            changePostStatus();
                                            initRecyclerData(upImage);
                                        }
                                    });
                        } else {
                            //没有权限

                        }
                    }
                });
    }

    private void initRecyclerData(List<ImageItem> upImage) {
        boolean needAddNullImageItem = false;
        if (upImage.size() < 9) {
            upImage.add(new ImageItem());
        } else {
            needAddNullImageItem = true;
        }
        imgAdapter.setDatas(upImage, needAddNullImageItem);
    }
}
