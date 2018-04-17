package io.dcloud.H58E83894.ui.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.JsonRootBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.weiget.GeneralKnowView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;



public class KnowDetailActivity extends BaseActivity {

    public static void startKnowDetail(Context c, String id) {
        Intent intent = new Intent(c, KnowDetailActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, id);
        c.startActivity(intent);
    }

    private String postId;
//    @BindView(R.id.conn_detail_title)
//    TextView detailTitle;
//    @BindView(R.id.comm_detail_view_count)
//    TextView detailViewCount;
//    @BindView(R.id.comm_reply_count)
//    TextView replyCount;
//    @BindView(R.id.comm_detail_post_time)
//    TextView postTime;
    @BindView(R.id.tv)
    GeneralKnowView mGeneralView;
    @BindView(R.id.know_type_title)
    TextView titleName;
    private int fontSize;
//    @BindView(R.id.comm_detail_recycler)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.tv)
//    TextView tv;
//    @BindView(R.id.remark_new_detail_me_head)
//    CircleImageView mHeadView;
//    @BindView(R.id.remark_new_me_content_et)
//    EditText newRemarkEt;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra(Intent.EXTRA_INDEX);
        }
    }

//    @Override
//    protected void initView() {
//        LinearLayoutManager manager = new LinearLayoutManager(mContext);
//        manager.setAutoMeasureEnabled(true);
//        mRecyclerView.addItemDecoration(new RecycleViewGridCommissionDivider(mContext, R.drawable.gray_divider));
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        if (!GlobalUser.getInstance().isAccountDataInvalid()) {
//            GlideUtil.loadDefault(RetrofitProvider.BASEURL + GlobalUser.getInstance().getUserData().getImage(), mHeadView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
//        }
//        newRemarkEt.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
//                    //评论
//                    asyncReplay();
//                }
//                return false;
//            }
//        });
//    }

//    private void asyncReplay() {
//        String text = getEditText(newRemarkEt);
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
//        if (GlobalUser.getInstance().isAccountDataInvalid()) {
//            toastShort(R.string.str_no_login_tip);
//            return;
//        }
//        addToCompositeDis(HttpUtil.postReply(postId, text)
//                .doOnSubscribe(new Consumer<Disposable>() {
//                    @Override
//                    public void accept(@NonNull Disposable disposable) throws Exception {
//                        showLoadDialog();
//                    }
//                })
//                .doOnError(new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        dismissLoadDialog();
//                    }
//                })
//                .subscribe(new Consumer<ResultBean>() {
//                    @Override
//                    public void accept(@NonNull ResultBean bean) throws Exception {
//                        dismissLoadDialog();
//                        if (getHttpResSuc(bean.getCode())) {
//                            newRemarkEt.setText("");
//                            asyncUiInfo();
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//
//                    }
//                }));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_detail);
    }

    @Override
    protected void onStart() {
        addWaterMarkView();
        asyncUiInfo();
        super.onStart();
    }

    private void addWaterMarkView() {
        View waterMarkView = LayoutInflater.from(this)
                .inflate(R.layout.layout_watermark,null);
        getRootView().addView(waterMarkView,1);
    }

    private ViewGroup getRootView() {
        ViewGroup rootView = (ViewGroup)findViewById(android.R.id.content);
        return rootView;
    }


//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return false;
//    }

    @Override
    protected void asyncUiInfo() {
        fontSize = SharedPref.getFontSize(mContext);
        int postIds = Integer.parseInt(postId);
        addToCompositeDis(HttpUtil.getKnowInfo(postIds)
                .subscribe(new Consumer<JsonRootBean>() {
                    @Override
                    public void accept(@NonNull JsonRootBean data1) throws Exception {
                        titleName.setText(data1.getData().getName());
                        mGeneralView.setHtmlText(data1.getData().getArticle(), RetrofitProvider.TOEFLURL );
//                        mGeneralView.setText(Html.fromHtml(data1.getData().getArticle()));
                        Log.i("tmds", data1.getData().getArticle().toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

//    private void refreshUi(CommunityData data) {
//////        detailTitle.setText(data.getTitle());
//////        detailViewCount.setText(data.getViewCount());
//////        List<ReplyData> reply = data.getReply();
//////        int size = 0;
//////        if (reply != null && !reply.isEmpty())
//////            size = reply.size();
//////        replyCount.setText(String.valueOf(size));
//////        postTime.setText(getString(R.string.str_community_post, data.getDateTime()));
////        tv.setText(data.getArticle().toString()+data.getCreateTime().toString());
//        mGeneralView.setText(RetrofitProvider.TOEFLURL , data.getArticle());
//////        mRecyclerView.setAdapter(new CommDetailAdapter(data.getReply()));
//////        mRecyclerView.setVisibility(View.GONE);
//////        tv.setVisibility(View.VISIBLE);
//////        tv.setText(data.getDescription());
//    }
}
