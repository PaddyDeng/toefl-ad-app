package io.dcloud.H58E83894.ui.toeflcircle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.RemarkBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.RemarkAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/14  11:26.
 */

public class RemarkFragment extends BaseRefreshFragment<RemarkData> {

    private LinearLayoutManager manager = new LinearLayoutManager(getActivity());
    protected int page = 1;
    private boolean pushComeOn;//推送来了。或者发表了八卦。或者在评论详情页评论。,需要刷新
    private boolean downRefresh;
    private Observable<Boolean> refreshObs;

    @Override
    protected void asyncLoadMore() {
        page++;
        netRequest(page, false);
    }

    /**
     * @param page      页码
     * @param isRefresh true 是下拉刷新
     */
    public void netRequest(int page, final boolean isRefresh) {
        addToCompositeDis(HttpUtil.getRemarkList(String.valueOf(page)).subscribe(new Consumer<ResultBean<RemarkBean>>() {
            @Override
            public void accept(@NonNull ResultBean<RemarkBean> bean) throws Exception {
                List<RemarkData> data = bean.getData().getData();
                if (bean.getData() != null && !data.isEmpty()) {
                    if (isRefresh && !TextUtils.isEmpty(bean.getNum()) && !TextUtils.equals(bean.getNum(), "0")) {
                        RemarkData remarkData = new RemarkData();
                        remarkData.setRemarkNum(bean.getNum());
                        data.add(0, remarkData);
                    }
                    if (isRefresh) {
                        updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                    } else {
                        updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                    }
                } else {
                    refreshRecycleFail(isRefresh, bean.getMessage());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                refreshRecycleFail(isRefresh, errorTip(throwable));
            }
        }));
    }

    private void refreshRecycleFail(boolean isRefresh, String msg) {
        if (isRefresh) {
            updateRecycleView(null, msg, InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, msg, InitDataType.TYPE_LOAD_MORE_FAIL);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshObs = RxBus.get().register(C.TOEFL_CIRCLE_POST_REMARK, Boolean.class);
        refreshObs.subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                refresh();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (refreshObs != null)
            RxBus.get().unregister(C.TOEFL_CIRCLE_POST_REMARK, refreshObs);
    }

    @Override
    protected BaseRecyclerViewAdapter<RemarkData> getAdapter() {
        return new RemarkAdapter(getActivity(), null, manager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return manager;
    }

    @Override
    protected void asyncRequest() {
        if (pushComeOn || downRefresh || adapter == null || adapter.getAdapterData().isEmpty()) {
            pushComeOn = false;
            downRefresh = false;
            page = 1;
            netRequest(page, true);
        }
    }

    @Override
    protected void setListener(List<RemarkData> data, int position) {
        super.setListener(data, position);
        RemarkData bean = data.get(position);
        if (TextUtils.isEmpty(bean.getRemarkNum())) {//不是新消息
            RemarkDetailActivity.startRemarkDetail(getActivity(), bean.getId(), C.COM_REQUEST_CODE);
        }
    }

    public int getListviewOffset(RemarkData commentConfig, int screenHeight, int selectCircleItemH, int currentKeyboardH, int editTextBodyHeight, int selectCommentItemOffset) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight;//- titleBar.getHeight();
//        if(commentConfig.getRecyclePosition() == CommentConfig.Type.REPLY){
        //回复评论的情况
        listviewOffset = listviewOffset + selectCommentItemOffset;
//        }
        return listviewOffset;
    }

    public LinearLayoutManager getManager() {
        return manager;
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity.getEtContainerStatus()) {
                    mainActivity.showOrHideEt(View.GONE, null, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void refresh() {
        downRefresh = true;
        super.refresh();
    }

}
