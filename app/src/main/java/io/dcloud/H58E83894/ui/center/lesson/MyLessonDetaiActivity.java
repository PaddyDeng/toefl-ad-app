package io.dcloud.H58E83894.ui.center.lesson;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gensee.utils.GenseeLog;

import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.ui.center.lesson.voddemo.VodActivity;


public class MyLessonDetaiActivity extends BaseActivity {

    public static void startMyLessonDetai(Context mContext, MyLessonData.DataBean data, String titleName) {
        Intent intent = new Intent(mContext, MyLessonDetaiActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_TITLE, titleName);
        mContext.startActivity(intent);
    }

    @BindView(R.id.recycler_expert)
    RecyclerView teacherRecycler;
    @BindView(R.id.deal_title)
    TextView tvTitle;

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);
    private MyLessonData.DataBean data;
    private boolean asyncTeacher;
    private String titleName;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        titleName = intent.getStringExtra(Intent.EXTRA_TITLE);
        data = intent.getParcelableExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_lecuter);
        initTitle();
        initTeacherData(data);


    }

    private void initTitle() {
        tvTitle.setText(titleName);
    }

    private void initTeacherData(final MyLessonData.DataBean data) {//
        if (data == null) return;
        final List<MyLessonData.DataBean.DatailsBean> been = data.getDatails();
        if (been == null || been.isEmpty()) return;
        teacherRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        LessonDetilAdapter teacherAdapter = new LessonDetilAdapter(this,  been, mManager);
        teacherAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                GenseeLog.i("PlayActivity", "po =" + " dara"+"hahahah");
//                forword(VodActivity.class);
                VodActivity.startVod(MyLessonDetaiActivity.this, data.getDatails().get(position));
//                TeacherDetailActivity.startTeacherDetail(ExpertLecuterActivity.this, been.get(position));
            }
        });
        teacherRecycler.setAdapter(teacherAdapter);
    }
}
