package io.dcloud.H58E83894.utils;

import android.content.Context;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.dcloud.H58E83894.ToeflApplication;

public class ShareProxy {

    private static ShareProxy shareProxy;
    private static Context mContext;

    public static ShareProxy getInstance() {
        if (shareProxy == null) {
            synchronized (ShareProxy.class) {
                if (shareProxy == null) {
                    shareProxy = new ShareProxy(ToeflApplication.getInstance());
                }
            }
        }
        return shareProxy;
    }

    private ShareProxy(Context context) {
        mContext = context;
    }

    private static PlatformActionListener platformActionListener = new PlatformActionListener() {
        public void onError(Platform arg0, int arg1, Throwable arg2) {
            //失败的回调，arg:平台对象，arg1:表示当前的动作，arg2:异常信息
            Utils.logh("onError", arg2.getMessage());
        }

        public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
            Utils.toastShort(mContext, "分享成功");
        }

        public void onCancel(Platform arg0, int arg1) {
            Utils.toastShort(mContext, "取消分享");
        }
    };

    private void share(Platform.ShareParams params, String platName) {
        Platform weibo = ShareSDK.getPlatform(platName);
        weibo.setPlatformActionListener(platformActionListener);
        weibo.share(params);
    }

    public void shareWechatMoments() {

        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setTitle("答题结果");
        sp.setText("我在雷哥 GMAT 的答题结果");
//        sp.setImagePath(imagePath);
        sp.setTitleUrl("http://www.gmatonline.cn"); // 标题的超链接
        sp.setSite("http://www.gmatonline.cn");//发布分享的网站名称
        sp.setSiteUrl("http://www.gmatonline.cn");//发布分享网站的地址

        Platform qq = ShareSDK.getPlatform(WechatMoments.NAME);
        sp.setText("我在雷哥 GMAT 的答题结果");
        sp.setShareType(Platform.SHARE_TEXT);

        qq.share(sp);
//
//        Platform wechatMoment = ShareSDK.getPlatform(WechatMoments.NAME);
//        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
////        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setTitle("答题结果");
//        sp.setText("我在雷哥 GMAT 的答题结果");
////        sp.setImagePath(imagePath);
////        sp.setImageData(bitmap);
//        wechatMoment.setPlatformActionListener(platformActionListener);
//        wechatMoment.share(sp);
    }

    public void shareToWechat() {

        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setTitle("答题结果");
        sp.setText("我在雷哥 GMAT 的答题结果");
//        sp.setImagePath(imagePath);
        sp.setTitleUrl("http://www.gmatonline.cn"); // 标题的超链接
        sp.setSite("http://www.gmatonline.cn");//发布分享的网站名称
        sp.setSiteUrl("http://www.gmatonline.cn");//发布分享网站的地址

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        sp.setText("我在雷哥 GMAT 的答题结果");
        sp.setShareType(Platform.SHARE_TEXT);

        wechat.share(sp);


//        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
//        Wechat.ShareParams sp = new Wechat.ShareParams();
//        sp.setShareType(Platform.SHARE_IMAGE);
//        sp.setTitle("答题结果");
//        sp.setText("我在雷哥 GMAT 的答题结果");
//        sp.setImagePath(imagePath);
//        wechat.setPlatformActionListener(platformActionListener);
//        wechat.share(sp);
    }

    public void shareToSina(String imgPath) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
//        sp.setTitle("答题结果");
        sp.setText("我在雷哥 GMAT 的答题结果");
        sp.setImagePath(imgPath);
        share(sp, SinaWeibo.NAME);
    }

    public void shareToQzone(String imagePath) {
        Platform qqZone = ShareSDK.getPlatform(QZone.NAME);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setTitle("答题结果");
        sp.setText("我在雷哥 GMAT 的答题结果");
        sp.setImagePath(imagePath);
        sp.setTitleUrl("http://www.gmatonline.cn"); // 标题的超链接
        sp.setSite("http://www.gmatonline.cn");//发布分享的网站名称
        sp.setSiteUrl("http://www.gmatonline.cn");//发布分享网站的地址
        qqZone.setPlatformActionListener(platformActionListener);
        qqZone.share(sp);
    }

    public void shareToQQ() {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle("答题结果");
        sp.setText("我在雷哥 GMAT 的答题结果");
//        sp.setImagePath(imagePath);
        sp.setTitleUrl("http://www.gmatonline.cn"); // 标题的超链接
        sp.setSite("http://www.gmatonline.cn");//发布分享的网站名称
        sp.setSiteUrl("http://www.gmatonline.cn");//发布分享网站的地址

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        sp.setText("我在雷哥 GMAT 的答题结果");
        sp.setShareType(Platform.SHARE_TEXT);

        qq.share(sp);

    }
}
