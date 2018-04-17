package io.dcloud.H58E83894.ui.toeflcircle;


import android.os.Bundle;
import android.text.TextUtils;

/**
 * 高分故事
 * */
public class DownLoadFragment extends BaseMaterialsFragment {


	public static DownLoadFragment newInstance(String id, String Index) {
		Bundle args = new Bundle();
		args.putString("sId", id);
		args.putString("sIndex", Index);
		DownLoadFragment fragment = new DownLoadFragment();
		fragment.setArguments(args);
		return fragment;
	}

	private int page;
	private int index;
	private String sIndex;

	@Override
	protected void asyncFail() {
		page--;
		page = page < 1 ? 1 : page;
	}

	@Override
	public int getSelectId() {

		Bundle bundle = getArguments();
		if (bundle != null) {
			String sId = bundle.get("sId").toString().trim();
			int seletcId = Integer.parseInt(sId);
			return seletcId;
		}else {
			return 11;
		}
	}

	@Override
	public int getIndex() {

		Bundle bundle = getArguments();
		if (bundle != null) {
			sIndex = bundle.get("sIndex").toString().trim();
			index = Integer.parseInt(sIndex);
			return index;
		}else {
			return 1;
		}
	}


	@Override
	public void asyncLoadMore() {
		++page;
		asyncData(false, page);
	}

	@Override
	public void asyncRequest() {
		page = 1;
		asyncData(true, page);
	}
}
