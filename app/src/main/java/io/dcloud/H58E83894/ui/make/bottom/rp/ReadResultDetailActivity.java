package io.dcloud.H58E83894.ui.make.bottom.rp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.ViewPagerFragmentAdapter;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.OptionInfoData;
import io.dcloud.H58E83894.data.make.ReadQuestion;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.data.make.ReadResultData;
import io.dcloud.H58E83894.data.make.ReadResultDetailData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.SerialAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ReadResultDetailActivity extends BaseActivity {

    public static void startReadResultDetailAct(Context c, String id) {
        Intent intent = new Intent(c, ReadResultDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        c.startActivity(intent);
    }

    @BindView(R.id.topic_serial_num_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.question_vp)
    ViewPager mViewPager;
    private String id;
    private List<OptionInfoData> optInfoList;
    private SerialAdapter adapter;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_result_detail);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void asyncUiInfo() {
        if (TextUtils.isEmpty(id)) return;
        addToCompositeDis(HttpUtil
                .readResult(id)
                .subscribe(new Consumer<ReadResultData>() {
                    @Override
                    public void accept(@NonNull ReadResultData data) throws Exception {
                        refreshUi(data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        errorTip(throwable);
                    }
                }));
    }

    private void refreshUi(ReadResultData data) {
        if (data == null) return;
        List<ReadResultDetailData> datas = data.getData();
        if (datas == null || datas.isEmpty()) return;
        int i = 0;
        int size = datas.size();
        optInfoList = new ArrayList<>();
        List<Fragment> vpList = new ArrayList<>();
        for (ReadResultDetailData resultDetailData : datas) {
            ReadQuestionData readQuestionData = new ReadQuestionData();
            readQuestionData.setCount(String.valueOf(size));
            readQuestionData.setArticle(resultDetailData.getPotion());
            readQuestionData.setQuestion(resultDetailData.getExtend());
            vpList.add(ReadAnswerFragment.getInstance(readQuestionData, resultDetailData.getAnswer()));

            ReadQuestion extend = resultDetailData.getExtend();
            OptionInfoData infoData = new OptionInfoData();
            if (i == 0) {
                infoData.setSelected(true);
            }
            infoData.setTopicNum(++i);
            infoData.setCorrect(TextUtils.equals(resultDetailData.getAnswer(), extend.getAnswer()));
            optInfoList.add(infoData);
        }
        initRecycler(optInfoList);
        initViewPager(vpList);
    }

    private void initViewPager(List<Fragment> list) {
        ViewPagerFragmentAdapter vpAdapter = new ViewPagerFragmentAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(vpAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mRecyclerView.smoothScrollToPosition(position);
                resetSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initRecycler(List<OptionInfoData> list) {
        adapter = new SerialAdapter(mContext, list);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                mViewPager.setCurrentItem(position, false);
                resetSelected(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private void resetSelected(int position) {
        if (adapter == null) return;
        for (int i = 0, size = optInfoList.size(); i < size; i++) {
            OptionInfoData data = optInfoList.get(i);
            if (i == position) {
                data.setSelected(true);
            } else {
                data.setSelected(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
