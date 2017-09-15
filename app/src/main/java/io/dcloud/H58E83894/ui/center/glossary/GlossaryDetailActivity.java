package io.dcloud.H58E83894.ui.center.glossary;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.DividerGridItemDecoration;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.GlossaryWordData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class GlossaryDetailActivity extends BaseListActivity<GlossaryWordData> {

    public static void startGlossaryAct(Context c, String startTime) {
        Intent intent = new Intent(c, GlossaryDetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, startTime);
        c.startActivity(intent);
    }

    private String startTime;
    private int page;

    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent == null) return;
        startTime = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void initView() {
        super.initView();
        baseTitleTxt.setText(R.string.str_glossary_detail_title);
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(mContext, R.drawable.trans_divider));
    }

    private StaggeredGridLayoutManager mManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    protected void asyncData(final boolean isRefresh) {
        addToCompositeDis(HttpUtil
                .getGlossaryWords(startTime, String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<GlossaryWordData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<GlossaryWordData>> bean) throws Exception {
                        List<GlossaryWordData> data = bean.getData();
                        if (data == null || data.isEmpty()) {
                            if (isRefresh) {
                                updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
                            } else {
                                updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            if (isRefresh) {
                                updateRecycleView(data, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (isRefresh) {
                            updateRecycleView(null, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                        }
                    }
                }));
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected BaseRecyclerViewAdapter<GlossaryWordData> getAdapter() {
        return new GlossaryWordAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        asyncData(true);
    }
}
