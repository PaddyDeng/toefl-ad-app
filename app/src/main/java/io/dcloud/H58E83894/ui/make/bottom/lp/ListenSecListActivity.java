package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.ListenChildData;
import io.dcloud.H58E83894.data.make.ListenSecRecordData;
import io.dcloud.H58E83894.data.make.ListenSecTpoData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.ListenSecAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.RxBus;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class ListenSecListActivity extends BaseListActivity<ListenTpoContentData> {

    public static void startListenSecAct(Context c, String id) {
//        Intent intent = new Intent(c, ListenSecListActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, id);
//        c.startActivity(intent);
        startListenSecAct(c, id, "");
    }

    public static void startListenSecAct(Context c, String id, String title) {
        Intent intent = new Intent(c, ListenSecListActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        intent.putExtra(Intent.EXTRA_INDEX, title);
        c.startActivity(intent);
    }

    private String id;
    private int page;
    private boolean fromClassificatoin;
    private String title;
    private LinearLayoutManager mManager = new LinearLayoutManager(mContext);
    private Observable<String> busObs;
    private String clickId;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        id = intent.getStringExtra(Intent.EXTRA_TEXT);
        title = intent.getStringExtra(Intent.EXTRA_INDEX);
        if (!TextUtils.isEmpty(title)) {
            baseTitleTxt.setText(title);
            fromClassificatoin = true;
        }
    }

    @Override
    protected void setListener(List<ListenTpoContentData> data, int position) {
        super.setListener(data, position);
        ListenTpoContentData contentData = data.get(position);
        List<ListenChildData> child = contentData.getChild();
        List<ListenSecRecordData> record = contentData.getRecord();
        boolean makeEnd = false;
        if (record == null || record.isEmpty()) {
            makeEnd = false;
        } else {
            if (child != null && !child.isEmpty()) {
                if (record.size() == child.size()) {
                    makeEnd = true;
                }
            }
        }
        clickId = contentData.getId();
        ListenSecStartMakeActivity.startListenSecAct(mContext, contentData.getId(), title, makeEnd);
    }

    @Override
    protected void initView() {
        super.initView();
        mBaseContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
        busObs = RxBus.get().register(C.REFRESH_LISTEN_LIST, String.class);
        busObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(s, clickId)) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    protected void asyncLoadMore() {
        page++;
        asyncData(false);
    }

    @Override
    protected boolean canLoadMore() {
        if (fromClassificatoin) return true;
        return false;
    }

    @Override
    protected BaseRecyclerViewAdapter<ListenTpoContentData> getAdapter() {
        ListenSecAdapter adapter = new ListenSecAdapter(mContext, null, mManager);
        adapter.setRxPermissions(mRxPermissions);
        return adapter;
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(mContext, LinearLayoutManager.VERTICAL, R.drawable.whitle_fifteen_divider));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispose();
        if (busObs != null) {
            RxBus.get().unregister(C.REFRESH_LISTEN_LIST, busObs);
        }
    }

    private void dispose() {
        List<ListenTpoContentData> data = adapter.getAdapterData();
        if (data != null && !data.isEmpty()) {
            for (ListenTpoContentData lcd : data) {
                if (lcd.mDisposable == null) continue;
                if (!lcd.mDisposable.isDisposed()) {
                    lcd.mDisposable.dispose();
                }
            }
        }
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    private void asyncData(final boolean isRefresh) {
        if (fromClassificatoin) {//来自分类
            asyncClassificationData(isRefresh);
            return;
        }
        addToCompositeDis(HttpUtil
                .listenTpo(id, String.valueOf(page))
                .subscribe(new Consumer<ListenSecTpoData>() {
                    @Override
                    public void accept(@NonNull ListenSecTpoData data) throws Exception {
                        asyncSuccess(data, isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwableDeal(throwable, isRefresh);
                    }
                }));
    }

    private void asyncSuccess(@NonNull ListenSecTpoData data, boolean isRefresh) {
        List<ListenTpoContentData> contentData = data.getContentData();
        if (contentData == null || contentData.isEmpty()) {
            if (isRefresh) {
                updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
            } else {
                updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        } else {
            if (TextUtils.isEmpty(title)) {
                ListenTpoContentData tpoContentData = contentData.get(0);
                title = tpoContentData.getCatName();
                baseTitleTxt.setText(title);
            }
            if (isRefresh) {
                updateRecycleView(contentData, "", InitDataType.TYPE_REFRESH_SUCCESS);
            } else {
                updateRecycleView(contentData, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        }
    }

    private void throwableDeal(@NonNull Throwable throwable, boolean isRefresh) {
        throwable.printStackTrace();
        if (isRefresh) {
            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_LOAD_MORE_FAIL);
            page--;
        }
    }

    private void asyncClassificationData(final boolean refresh) {
        addToCompositeDis(HttpUtil
                .listenClassification(id, String.valueOf(page))
                .subscribe(new Consumer<ListenSecTpoData>() {
                    @Override
                    public void accept(@NonNull ListenSecTpoData data) throws Exception {
                        asyncSuccess(data, refresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwableDeal(throwable, refresh);
                    }
                }));
    }

    @Override
    protected void asyncRequest() {
        dispose();
        page = 1;
        asyncData(true);
    }
}
