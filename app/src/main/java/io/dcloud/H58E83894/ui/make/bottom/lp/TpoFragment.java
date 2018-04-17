package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.RecycleViewGridDivider;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.ListenTpoData;
import io.dcloud.H58E83894.ui.make.adapter.TpoAdapter;

/**
 * Created by fire on 2017/7/25  11:53.
 */

public class TpoFragment extends BaseFragment {

    @BindView(R.id.tpo_recycler)
    RecyclerView mRecyclerView;
    private TpoAdapter adapter;
    private List<ListenTpoData> mDatas;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_tpo_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler(mRecyclerView, new GridLayoutManager(getActivity(), 5));
        mRecyclerView.addItemDecoration(new RecycleViewGridDivider(getActivity(), R.drawable.gray_one_divider));
        adapter = new TpoAdapter(initTpoData());
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mDatas == null) return;
                ListenTpoData tpoData = mDatas.get(position);
                ListenSecListActivity.startListenSecAct(getActivity(), String.valueOf(tpoData.getCatId()));
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    private List<ListenTpoData> initTpoData() {
        mDatas = new ArrayList<>();
        for (int i = 1; i < 51; i++) {
            ListenTpoData tpoData = new ListenTpoData();
            int cacl = cacl(i);
            if (cacl == i) {
                continue;
            }
            tpoData.setTopNumber(i);
            tpoData.setCatId(cacl);
            mDatas.add(tpoData);
        }
        return mDatas;
    }

    private int cacl(int tpoNumber) {
        if (tpoNumber < 4) {
            tpoNumber += 38;
        } else if (tpoNumber < 35) {
            tpoNumber += 46;
        } else if (tpoNumber < 40) {

        } else if (tpoNumber == 40) {
            tpoNumber += 211;
        } else if (tpoNumber < 43) {
            tpoNumber += 216;
        } else if (tpoNumber < 49) {
            tpoNumber += 229;
        } else if (tpoNumber < 50) {
            tpoNumber += 262;
        }else if (tpoNumber < 51) {
            tpoNumber += 326;
        }
        return tpoNumber;
    }

}
