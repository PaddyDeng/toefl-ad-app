package io.dcloud.H58E83894.ui.center.know;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.ListsData;
import io.dcloud.H58E83894.data.know.KnowZoneData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.knowledge.adapter.LeiZoneAdapter;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * wode zhishi专区
 * */
public class MyKnowLeiZoneFragment extends BaseRefreshFragment<ListsData.ListBean> {

	private LinearLayoutManager manager = new LinearLayoutManager(getActivity());


	private int id;
	private int page;

	@Override
	protected void asyncLoadMore() {
		page++;
		netRequest(false);
	}

	@Override
	protected BaseRecyclerViewAdapter<ListsData.ListBean>  getAdapter() {

		return new MyLeiZoneAdapter(getActivity(), null, manager);
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
		netRequest(true);

	}

	public void netRequest(final boolean isRefresh) {

			addToCompositeDis(HttpUtil
					.getMyKnowLists(page)
					.subscribe(new Consumer<ListsData>() {
						@Override
						public void accept(ListsData bean) throws Exception {
							asyncSuccess(bean, isRefresh);
						}
					}, new Consumer<Throwable>() {
						@Override
						public void accept(Throwable throwable) throws Exception {
							throwableDeal(throwable, isRefresh);
						}
					}));

	}

	private void asyncSuccess(@NonNull ListsData data, boolean isRefresh) {
		List<ListsData.ListBean> contentData = data.getList();
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
