package io.dcloud.H58E83894.ui.toeflcircle;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListPullActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.receiver.RefreshMakeRecordReceiver;
import io.dcloud.H58E83894.ui.prelesson.adapter.DownloadAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


/**
 * 机经下专区
 * */
public class MaryListFragment extends BaseListPullActivity <DownloadData> {

    public static void startMary(Context c,  String titleName, String catId, String index) {
        Intent intent = new Intent(c, MaryListFragment.class);
        intent.putExtra(Intent.EXTRA_TITLE, titleName);
        intent.putExtra(Intent.EXTRA_TEXT, catId);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        c.startActivity(intent);
    }

    private int page  = 1;
    private int catId;
    private int index;
    private String catsId;

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);


    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null) {
            catsId = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i("ooo", catsId);
            catId = Integer.parseInt(catsId);
            titleTxt.setText(intent.getStringExtra(Intent.EXTRA_TITLE));
            index = Integer.parseInt(getIntent().getStringExtra(Intent.EXTRA_INDEX));
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, mReceiver.getIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    private RefreshMakeRecordReceiver mReceiver = new RefreshMakeRecordReceiver() {
        @Override
        protected void performAction(Intent intent) {
            asyncRequest();
        }
    };


    @Override
    protected void initData() {
        super.initData();

        if (index == 0) {//机经
            sendPosts.setImageResource(R.drawable.ic_mery);

        } else if (index == 1) {
            sendPosts.setImageResource(R.drawable.ic_mery);
        } else if (index == 2) {
            sendPosts.setImageResource(R.drawable.ic_mery);
        } else if (index == 5) {
            sendPosts.setImageResource(R.drawable.ic_essay_update_01);
        }
    }

    @Override
    public BaseRecyclerViewAdapter<DownloadData> getAdapter() {
        return new DownloadAdapter(mContext, null, mManager, index);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }


    @Override
    public void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                MainActivity mainActivity = new MainActivity();
                if (mainActivity.getEtContainerStatus()) {
                    mainActivity.showOrHideEt(View.GONE, null, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void asyncLoadMore() {

        ++page;
        asyncData(page, false);

    }


    private void asyncData(int pages,final boolean isRefresh) {

        addToCompositeDis(HttpUtil
                .MaryList(catId, pages)
                .subscribe(new Consumer<ResultBean<List<DownloadData>>>() {
                    @Override
                    public void accept(@NonNull ResultBean<List<DownloadData>> bean) throws Exception {

                        List<DownloadData> data = bean.getData();
                        if (data != null && !data.isEmpty()) {
                            if (isRefresh) {
                                updateRecycleView(data, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            }else {
                                updateRecycleView(data, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
                            }
                        } else {
                            updateRecycleView(data, "", InitDataType.TYPE_REFRESH_FAIL);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                        throwable.printStackTrace();
                        updateRecycleView(null, "", InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

    @Override
    public void asyncRequest() {

        page = 1;
        asyncData(page, true);

    }

    @Override
    protected void asyncUiInfo() {
        page = 1;
        asyncData(page, true);
    }

}
