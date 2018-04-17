package io.dcloud.H58E83894.ui.toeflcircle;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import io.dcloud.H58E83894.ui.toeflcircle.BaseMaterialFragment;
/**
 * 考试资讯
 * */
public class ExamInfoFragment extends BaseMaterialFragment {

	public static ExamInfoFragment newInstance(String id, String Index) {

		Bundle args = new Bundle();
		args.putString("sId", id);
		args.putString("sIndex", Index);
		ExamInfoFragment fragment = new ExamInfoFragment();
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
			return 138;
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
			return 0;
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
