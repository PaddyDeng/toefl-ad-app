package io.dcloud.H58E83894.ui.guide;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.GlideUtil;

public class GuideDetailPager extends Fragment {

    public static final String URL = "url";
    private ImageView imageView;
    private int drawableRes;

    public static GuideDetailPager getInstance(@DrawableRes int dres) {
        GuideDetailPager imageDetails = new GuideDetailPager();
        Bundle bundle = new Bundle();
        bundle.putInt(URL, dres);
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
        drawableRes = savedInstanceState == null ? getArguments().getInt(URL) : savedInstanceState.getInt(URL);
        if (drawableRes != 0)
            GlideUtil.loadNoDefalut(drawableRes, imageView);
//            Glide.with(getActivity()).load(drawableRes).fitCenter().into(imageView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(URL, drawableRes);
    }
}
