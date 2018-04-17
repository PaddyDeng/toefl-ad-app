package io.dcloud.H58E83894.ui.toeflcircle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.circle.ReplyData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.RemarkMsgAdapter;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class RemarkNewMsgActivity extends BaseActivity {

    @BindView(R.id.remark_msg_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_new_msg);
    }
//    头像：userimage
//gossipId 点击进入详情页


    @Override
    protected void initView() {
        super.initView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    protected void asyncUiInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(GlobalUser.getInstance().getUserData().getUid()));
        Log.i("lll", String.valueOf(GlobalUser.getInstance().getUserData().getUid()));
        addToCompositeDis(HttpUtil.replyList(GlobalUser.getInstance().getUserData().getUid()).subscribe(new Consumer<List<ReplyData>>() {
            @Override
            public void accept(@NonNull final List<ReplyData> datas) throws Exception {
                RemarkMsgAdapter adapter = new RemarkMsgAdapter(datas);
                adapter.setItemListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ReplyData data = datas.get(position);
                        RemarkDetailActivity.startRemarkDetail(RemarkNewMsgActivity.this, data.getGossipId());
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {

            }
        }));
    }
}
