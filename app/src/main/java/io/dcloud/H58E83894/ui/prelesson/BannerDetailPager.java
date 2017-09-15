package io.dcloud.H58E83894.ui.prelesson;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.GlideUtil;

public class BannerDetailPager extends Fragment {
    public static final String URL = "url";
    private ImageView imageView;
    private String mUrl;

    public static BannerDetailPager getInstance(String url) {
        BannerDetailPager imageDetails = new BannerDetailPager();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        imageDetails.setArguments(bundle);
        return imageDetails;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        imageView = new ImageView(getActivity());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUrl = savedInstanceState == null ? getArguments().getString(URL) : savedInstanceState.getString(URL);
        if (TextUtils.isEmpty(mUrl)) {
            imageView.setBackgroundResource(R.drawable.icon_default);
        } else {
            GlideUtil.load(mUrl, imageView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(URL, mUrl);
    }
}
