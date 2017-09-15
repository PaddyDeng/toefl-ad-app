package io.dcloud.H58E83894.ui.center.lesson;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MyLessonActivity extends BaseListActivity<Object> {
    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);
    private int page = 1;

    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_center_my_courses);
        Utils.setVisible(mBaseTitleLine);
    }

    @Override
    protected BaseRecyclerViewAdapter<Object> getAdapter() {
        return new LessonCenterAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        addToCompositeDis(HttpUtil.lessonList(page)
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        updateRecycleView(null, getString(R.string.str_pc_see), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, getString(R.string.str_pc_see), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }
}
