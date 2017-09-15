package io.dcloud.H58E83894.ui.toeflcircle.load;

import android.widget.ImageView;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.caimuhao.rxpicker.utils.RxPickerImageLoader;

import io.dcloud.H58E83894.utils.GlideUtil;

public class GlideImageLoader implements RxPickerImageLoader {

    @Override
    public void display(ImageView imageView, String path, int width, int height) {

        GlideUtil.loadDefaultOverrideNoAnim(path, imageView, width, height, false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);

    }
}
