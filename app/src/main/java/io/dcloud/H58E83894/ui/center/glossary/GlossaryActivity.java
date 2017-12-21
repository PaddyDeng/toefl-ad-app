package io.dcloud.H58E83894.ui.center.glossary;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.GlossaryData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class GlossaryActivity extends BaseListActivity<GlossaryData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(mContext);
    private int page;

    private void asyncData(final boolean refresh) {
        addToCompositeDis(HttpUtil
                .glossaryList(String.valueOf(page))
                .subscribe(new Consumer<ResultBean<List<GlossaryData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<GlossaryData>> bean) throws Exception {
                        List<GlossaryData> data = bean.getData();
                        if (data == null || data.isEmpty()) {
                            if (refresh) {
                                updateRecycleView(null,  getString(R.string.str_glossary_tip), InitDataType.TYPE_REFRESH_FAIL);
                            } else {
                                updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            if (refresh) {
                                updateRecycleView(data, getString(R.string.str_glossary_tip), InitDataType.TYPE_REFRESH_SUCCESS);
                            } else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (refresh) {
                            updateRecycleView(null, getString(R.string.str_glossary_tip), InitDataType.TYPE_REFRESH_FAIL);
                        } else {
                            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
                        }
                    }
                }));
    }

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_glossary_title);
        Utils.setVisible(mBaseTitleLine);
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected void setListener(List<GlossaryData> data, int position) {
        super.setListener(data, position);
        GlossaryData glossaryData = data.get(position);
        GlossaryDetailActivity.startGlossaryAct(mContext, glossaryData.getStartTime());
    }

    @Override
    protected BaseRecyclerViewAdapter<GlossaryData> getAdapter() {
        return new GlossaryAdapter(mContext, null, mManager);
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
