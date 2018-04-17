package io.dcloud.H58E83894.ui.knowledge;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.RemarkBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.data.make.ListenSecTpoData;
import io.dcloud.H58E83894.data.make.ListenTpoContentData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.knowledge.adapter.FreeZoneAdapter;
import io.dcloud.H58E83894.ui.toeflcircle.adapter.RemarkAdapter;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 免费专区
 * */
public class KnowFreeFragment extends BaseRefreshFragment<KnowZoneData.FreeBean> {

	private LinearLayoutManager manager = new LinearLayoutManager(getActivity());
	public static KnowFreeFragment newInstance(int id) {

		Bundle args = new Bundle();
		args.putInt("sId", id);
		KnowFreeFragment fragment = new KnowFreeFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private int id;
	private int page;

	@Override
	protected void asyncLoadMore() {
		page++;
		netRequest( false);
	}

	@Override
	protected BaseRecyclerViewAdapter<KnowZoneData.FreeBean> getAdapter() {

	        return new FreeZoneAdapter(getActivity(), null, manager);
	}

	@Override
	protected RecyclerView.LayoutManager getLayoutManager() {
		return manager;
	}

	@Override
	protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
		mRecyclerView.addItemDecoration(new RecycleViewLinearDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.whitle_fifteen_divider));
	}

	@Override
	protected void asyncRequest() {
		page = 1;
		netRequest( true);

	}

	public void netRequest( final boolean isRefresh) {

		Bundle bundle = getArguments();
		if (bundle != null) {
			id = bundle.getInt("sId");
			addToCompositeDis(HttpUtil
					.getKnowLists(id, page)
					.subscribe(new Consumer<KnowZoneData>() {
						@Override
						public void accept(KnowZoneData bean) throws Exception {
							asyncSuccess(bean, isRefresh);
						}
					}, new Consumer<Throwable>() {
						@Override
						public void accept(Throwable throwable) throws Exception {
							throwableDeal(throwable, isRefresh);
						}
					}));
		}
	}

	private void asyncSuccess(@NonNull KnowZoneData data, boolean isRefresh) {
		List<KnowZoneData.FreeBean> contentData = data.getFree();
		if (contentData == null || contentData.isEmpty()) {
			if (isRefresh) {
				updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_REFRESH_FAIL);
			} else {
				updateRecycleView(contentData, getString(R.string.str_nothing_tip), InitDataType.TYPE_LOAD_MORE_SUCCESS);
			}
		} else {

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
	protected String throwMsg(Throwable throwable) {
		return Utils.onError(throwable);
	}
}
