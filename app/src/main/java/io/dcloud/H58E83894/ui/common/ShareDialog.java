package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;




import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.MeasureUtil;
import io.dcloud.H58E83894.utils.ShareProxy;


public class ShareDialog extends BaseDialog {
    @BindView(R.id.wechar_friends_container)
    LinearLayout wechartContainer;
    @BindView(R.id.wechart_circle_container)
    LinearLayout circleContainer;
    @BindView(R.id.qq_container)
    LinearLayout qqContainer;
    @BindView(R.id.qzone_container)
    LinearLayout qzoneContainer;
    @BindView(R.id.sina_weibo_container)
    LinearLayout sinaContainer;
    @BindView(R.id.load_down_container)
    LinearLayout loadContainer;
    @BindView(R.id.diss_share)
    ImageView dissShare;
    private String mImagePath;

    @Override
    protected int getContentViewLayId() {
        return R.layout.dialog_new_share;
    }

    @Override
    protected int[] getWH() {
        return new int[]{MeasureUtil.getScreenSize(getActivity()).x, getDialog().getWindow().getAttributes().height};
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getDialog().getWindow())
            getDialog().getWindow().setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImagePath = getActivity().getFilesDir().getPath() + "share.png";
//        mImagePath = "sssss";
        wechartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//微信好友分享
                ShareProxy.getInstance().shareToWechat();
                dismiss();
            }
        });
        circleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//微信朋友圈分享
                ShareProxy.getInstance().shareWechatMoments();
                dismiss();
            }
        });
        qqContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//qq分享
                ShareProxy.getInstance().shareToQQ();
                dismiss();
            }
        });
        qzoneContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//qq空间分享
                ShareProxy.getInstance().shareToQzone(mImagePath);
                dismiss();
            }
        });
        sinaContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新浪分享
                Log.i("ll", mImagePath);
                Log.i("ll", ShareProxy.getInstance().toString());
                ShareProxy.getInstance().shareToSina(mImagePath);
                dismiss();
            }
        });
        loadContainer.setOnClickListener(new View.OnClickListener() {//本地
            @Override
            public void onClick(View v) {

            }
        });
        dissShare.setOnClickListener(new View.OnClickListener() {//关闭
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
