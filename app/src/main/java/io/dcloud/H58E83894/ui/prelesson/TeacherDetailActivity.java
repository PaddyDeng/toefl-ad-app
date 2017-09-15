package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.prelesson.TeacherItemBean;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.prelesson.adapter.TeacherSpeakAdapter;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CircleImageView;

public class TeacherDetailActivity extends BaseActivity {

    public static void startTeacherDetail(Context c, TeacherItemBean data) {
        Intent intent = new Intent(c, TeacherDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        c.startActivity(intent);
    }

    @BindView(R.id.teacher_detail_title)
    TextView titleTv;
    @BindView(R.id.teacher_detail_title_tv)
    TextView contTitle;
    @BindView(R.id.teacher_detail_des_tv)
    TextView detailDes;
    @BindView(R.id.teacher_detail_item_img)
    CircleImageView headIv;
    @BindView(R.id.teacher_detail_info)
    TextView detailInfo;
    @BindView(R.id.teacher_detail_recycler)
    RecyclerView mRecyclerView;

    private TeacherItemBean mTeacherData;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent != null) {
            mTeacherData = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
    }

    @Override
    protected void initView() {
        if (mTeacherData == null) return;
        titleTv.setText(mTeacherData.getName());
        contTitle.setText(mTeacherData.getName());
        detailDes.setText(mTeacherData.getPerformance());
        Utils.controlTvFocus(headIv);
        GlideUtil.load(RetrofitProvider.TOEFLURL + mTeacherData.getImage(), headIv);
        detailInfo.setText(HtmlUtil.fromHtml(mTeacherData.getDescription()));
        initRecycler(mRecyclerView, new LinearLayoutManager(this));
        List<String> list = Arrays.asList(Utils.splitOption(mTeacherData.getSpeak()));
        mRecyclerView.setAdapter(new TeacherSpeakAdapter(list));
    }

    @OnClick({R.id.teacher_course_reservation})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teacher_course_reservation:
                if (mTeacherData != null)
                    PreProGramLessonActivity.startPre(mContext, mTeacherData.getId(), mTeacherData.getTitle());
                break;
            default:
                break;
        }
    }

}
