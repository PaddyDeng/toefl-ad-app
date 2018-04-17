package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.LessonDetailBean;
import io.dcloud.H58E83894.data.prelesson.TeacherData;
import io.dcloud.H58E83894.data.prelesson.TeacherItemBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.prelesson.adapter.TeacherAdapter;
import io.dcloud.H58E83894.ui.prelesson.adapter.TeachersAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.dcloud.H58E83894.weiget.ObservableScrollView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 *专家讲师
*/
public class ExpertLecuterActivity extends BaseActivity {

    @BindView(R.id.recycler_expert)
    RecyclerView teacherRecycler;
    @BindView(R.id.deal_title)
    TextView tvTitle;

    private boolean asyncTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_lecuter);
        initTitle();
        asynTeacher();
    }

    private void initTitle() {
        tvTitle.setText(getResources().getText(R.string.str_expert_lecturer));
    }

    private void asynTeacher() {
        addToCompositeDis(HttpUtil.teacher()
                .subscribe(new Consumer<TeacherData>() {
                    @Override
                    public void accept(@NonNull TeacherData data) throws Exception {
                        asyncTeacher = true;
                        initTeacherData(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                }));
    }

    private void initTeacherData(TeacherData data) {//专家讲师模块
        if (data == null) return;
        final List<TeacherItemBean> been = data.getData();
        if (been == null || been.isEmpty()) return;
        teacherRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        TeachersAdapter teacherAdapter = new TeachersAdapter(been);
        teacherAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                TeacherDetailActivity.startTeacherDetail(ExpertLecuterActivity.this, been.get(position));
            }
        });
        teacherRecycler.setAdapter(teacherAdapter);
    }
}
