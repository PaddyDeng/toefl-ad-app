package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.LessonDetailBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.dcloud.H58E83894.weiget.ObservableScrollView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ToeflDetailActivity extends BaseActivity {

    public static void startToeflDetail(Context c, LessonData data, int type) {
        Intent intent = new Intent(c, ToeflDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_INDEX, type);
        c.startActivity(intent);
    }

    @BindView(R.id.gmat_detail_title_container)
    RelativeLayout mTitleContainer;
    @BindView(R.id.gmat_detail_img_header)
    ImageView headImg;
    @BindView(R.id.gmat_detail_content_title_tv)
    TextView contentTitleTv;
    @BindView(R.id.gamt_detai_general_view)
    GeneralView mGeneralView;
    @BindView(R.id.scrollView)
    ObservableScrollView scrollView;
    @BindView(R.id.header)
    ImageView headerImg;
    @BindView(R.id.rl_header_container)
    RelativeLayout headerContainer;
    @BindView(R.id.gmat_detail_num_tv)
    TextView detailNumTv;

    private LessonData mLessonData;
    private float mActionBarSize;
    private float titleBarSize;
    private int type;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null) {
            mLessonData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            type = intent.getIntExtra(Intent.EXTRA_INDEX, 0);
        }
    }

    @Override
    protected void initData() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        mActionBarSize = styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        titleBarSize = getResources().getDimension(R.dimen.header_height);

        scrollView.setOnScrollListener(new ObservableScrollView.ScrollViewListener() {

            @Override
            public void onScroll(int oldy, int dy, boolean isUp) {
                if (dy < 0) {
                    dy = 0;
                }
                float move_distance = titleBarSize - mActionBarSize;
                if (!isUp && dy <= move_distance) {
                    //标题栏逐渐从透明变成不透明
                    mTitleContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_black));
                    TitleAlphaChange(dy, move_distance);//标题栏渐变
                    HeaderTranslate(dy);//图片视差平移

                } else if (!isUp && dy > move_distance) {//手指往上滑,距离超过200dp
                    TitleAlphaChange(1, 1);//设置不透明百分比为100%，防止因滑动速度过快，导致距离超过200dp,而标题栏透明度却还没变成完全不透的情况。

                    HeaderTranslate(titleBarSize);//这里也设置平移，是因为不设置的话，如果滑动速度过快，会导致图片没有完全隐藏。

                } else if (isUp && dy > move_distance) {//返回顶部，但距离头部位置大于200dp
                    //不做处理

                } else if (isUp && dy <= move_distance) {//返回顶部，但距离头部位置小于200dp
                    //标题栏逐渐从不透明变成透明
                    TitleAlphaChange(dy, move_distance);//标题栏渐变
                    HeaderTranslate(dy);//图片视差平移
                }
            }
        });

    }

    private void HeaderTranslate(float distance) {
        headerContainer.setTranslationY(-distance);
        headerImg.setTranslationY(distance / 2);
    }

    private void TitleAlphaChange(int dy, float mHeaderHeight_px) {
        float percent = (float) Math.abs(dy) / Math.abs(mHeaderHeight_px);
        int alpha = (int) (percent * 255);
        mTitleContainer.getBackground().setAlpha(alpha);
    }


    @Override
    protected void initView() {
        if (mLessonData == null) return;
        contentTitleTv.setText(mLessonData.getName());
        detailNumTv.setText(getString(R.string.str_gmat_detail_number, mLessonData.getViewCount()));
        if (type == C.TYPE_PUBLIC_LESSON) {
            GlideUtil.load(RetrofitProvider.SMARTAPPLYURL + mLessonData.getImage(), headImg);
            mGeneralView.setHtmlText(mLessonData.getSentenceNumber(), RetrofitProvider.SMARTAPPLYURL);
        } else {
            GlideUtil.load(RetrofitProvider.TOEFLURL + mLessonData.getImage(), headImg);
            async();
        }
    }

    private void async() {
        addToCompositeDis(HttpUtil
                .lessonDetail(mLessonData.getId())
                .subscribe(new Consumer<ResultBean<LessonDetailBean>>() {
                    @Override
                    public void accept(@NonNull ResultBean<LessonDetailBean> bean) throws Exception {
                        LessonDetailBean data = bean.getData();
                        if (data == null) return;
                        mGeneralView.setHtmlText(data.getAnswer(), RetrofitProvider.TOEFLURL);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    @OnClick({R.id.go_advisory, R.id.gmat_detail_trial_course_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.go_advisory:
                DealActivity.startDealActivity(mContext, "", "http://p.qiao.baidu.com/im/index?siteid=7905926&ucid=18329536&cp=&cr=&cw=", C.DEAL_ADD_HEADER);
                break;
            case R.id.gmat_detail_trial_course_btn:
                forword(TrialCourseTypeActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gmat_detail);
        mTitleContainer.getBackground().setAlpha(0);
    }
}
