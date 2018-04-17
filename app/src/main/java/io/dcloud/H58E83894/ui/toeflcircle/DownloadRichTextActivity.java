package io.dcloud.H58E83894.ui.toeflcircle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.RecycleViewGridCommissionDivider;
import io.dcloud.H58E83894.callback.ClickCallBack;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.CommunityData;
import io.dcloud.H58E83894.data.circle.ReplyData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.CommDetailAdapter;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CircleImageView;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DownloadRichTextActivity extends BaseActivity {

    public static void startCommunity(Context c, String postId) {
        Intent intent = new Intent(c, DownloadRichTextActivity.class);
        intent.putExtra(Intent.EXTRA_INDEX, postId);
        c.startActivity(intent);
    }

    private String postId;
    @BindView(R.id.conn_detail_title)
    TextView detailTitle;
    @BindView(R.id.comm_detail_view_count)
    TextView detailViewCount;
    @BindView(R.id.comm_reply_count)
    TextView replyCount;
    @BindView(R.id.comm_detail_post_time)
    TextView postTime;
    @BindView(R.id.comm_detail_deneral_veiw)
    GeneralView mGeneralView;
    @BindView(R.id.comm_detail_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.remark_new_detail_me_head)
    CircleImageView mHeadView;
    @BindView(R.id.remark_new_me_content_et)
    EditText newRemarkEt;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra(Intent.EXTRA_INDEX);
        }
    }

    @Override
    protected void initView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setAutoMeasureEnabled(true);
        mRecyclerView.addItemDecoration(new RecycleViewGridCommissionDivider(mContext, R.drawable.remark_com_divider));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (!GlobalUser.getInstance().isAccountDataInvalid()) {

            GlideUtil.loadDefault(RetrofitProvider.BASEURL + GlobalUser.getInstance().getUserData().getImage(), mHeadView, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        }
        newRemarkEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    //评论
                    asyncReplay();
                }
                return false;
            }
        });
    }

    private void asyncReplay() {
        String text = getEditText(newRemarkEt);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(R.string.str_no_login_tip);
            return;
        }
        addToCompositeDis(HttpUtil.postReply(postId, text)
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
                        toastShort(bean.getMessage());
                        if (getHttpResSuc(bean.getCode())) {
                            newRemarkEt.setText("");
                            asyncUiInfo();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_rich_text);
    }


    @Override
    protected void asyncUiInfo() {

        int postIds = Integer.parseInt(postId);
        addToCompositeDis(HttpUtil.getPostDeail(postId)
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
                .subscribe(new Consumer<CommunityData>() {
                    @Override
                    public void accept(@NonNull CommunityData data) throws Exception {
                        dismissLoadDialog();
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }


    private void refreshUi(CommunityData data) {
        detailTitle.setText(data.getTitle());
        detailViewCount.setText(data.getViewCount());
        List<ReplyData> reply = data.getReply();
        int size = 0;
        if (reply != null && !reply.isEmpty())
            size = reply.size();
        replyCount.setText(String.valueOf(size));
        postTime.setText(getString(R.string.str_community_post, data.getDateTime()));

        mGeneralView.setText(RetrofitProvider.GOSSIPURL, contHtml(data.getContent(), data.getDatum(), data.getDatumTitle()));
        mGeneralView.setTitle(data.getIsReply());
        mGeneralView.setOnClickListener(new ClickCallBack<String>() {
            @Override
            public void onClick(String s) {
                goDownloadCondi();
            }
        });
        mRecyclerView.setAdapter(new CommDetailAdapter(data.getReply()));
    }

    private void goDownloadCondi() {
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            //登录无效。
            if (needLogin()) return;
        } else {
            //需要评论
            toastShort(R.string.str_post_after_download);
            Utils.keyBordShowFromWindow(mContext, detailTitle);
            newRemarkEt.requestFocus();
        }
    }

    private String contHtml(String html, List<String> links, List<String> titles) {
        StringBuffer sb = new StringBuffer();
        sb.append(html);
        for (int i = 0, size = links.size(); i < size; i++) {
            String link = links.get(i);
            String title = titles.get(i);
            if (!TextUtils.isEmpty(link) && !TextUtils.isEmpty(title)) {
                sb.append(contLinkTitle(link, title));
            }
        }
        return sb.toString();
    }

    private String contLinkTitle(String link, String title) {
        return "<p style=\"line-height: 16px;\">" +
                "<a style=\"font-size:14px; color:#0066cc;\" " +
                "href=\"" + link + "\"" +
                "title=\"" + title + "\"" + ">" + title + "</a></p>";
    }


}
