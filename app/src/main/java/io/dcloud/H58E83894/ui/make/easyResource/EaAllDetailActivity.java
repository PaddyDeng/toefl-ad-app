package io.dcloud.H58E83894.ui.make.easyResource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.ui.make.easyResource.adapter.EaAllCorretDeAdapter;
import io.dcloud.H58E83894.ui.make.lexicalResource.adapter.AllCorretDeAdapter;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.Utils;

public class EaAllDetailActivity extends BaseActivity {


    public static void startEaAllDetailActivity(Context c, AllTaskData.DataBean data) {
        Intent intent = new Intent(c, EaAllDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, (Serializable) data);
//        intent.putParcelableArrayListExtra(Intent.EXTRA_PACKAGE_NAME, (ArrayList<AllTaskData.DataBean.DetailsBean >) (Parcelable)list);
        c.startActivity(intent);
    }

    private AllTaskData.DataBean mReadQuestionData;
    @BindView(R.id.today_speak_title)
    TextView todayTitle; //作文标题today_speak_free_status
    @BindView(R.id.today_free)
    TextView dataTitle;//时间标题
    Calendar mCalendar;
    private EaAllCorretDeAdapter adapter;
    private List<AllTaskData.DataBean.DetailsBean> list;

    @BindView(R.id.all_answer_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_tv_tip)
    TextView emptyTipTxt;
    @BindView(R.id.answer_idea_tv)
    TextView ideaTipTxt;
    @BindView(R.id.diff_detail_title_tv)
    TextView tvTitle;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        mReadQuestionData = (AllTaskData.DataBean) intent.getSerializableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma_all_detail);

    }

    @Override
    protected void initView() {
        tvTitle.setText("作文批改详情");
        dataTitle.setText(HtmlUtil.fromHtml(this.getString(R.string.str_corret_date_num, mReadQuestionData.getTime())));
        todayTitle.setText(mReadQuestionData.getQuestion());
        showAllAnswer(mReadQuestionData.getDetails());
    }

    private void showAllAnswer(List<AllTaskData.DataBean.DetailsBean> todayList) {
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


    private void initRecyclerData(List<AllTaskData.DataBean.DetailsBean> share) {
        if (adapter == null) {
            initRecycler(mRecyclerView, new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(this, LinearLayoutManager.VERTICAL, R.drawable.gray_one_divider));
            adapter = new EaAllCorretDeAdapter(share, this, mRxPermissions);
            mRecyclerView.setAdapter(adapter);
        } else {
//            adapterDispose();
            adapter.update(share);
        }
    }


}
