package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.zxing.Result;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.callback.ClickCallBack;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;
import io.dcloud.H58E83894.ui.toeflcircle.load.DownloadListActivity;
import io.dcloud.H58E83894.utils.SharedPref;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.zxing.DecodeImage;

import static io.dcloud.H58E83894.utils.HtmlUtil.fromHtml;
import static io.dcloud.H58E83894.utils.HtmlUtil.getHtml;
import static io.dcloud.H58E83894.utils.HtmlUtil.repairContent;
import static io.dcloud.H58E83894.utils.HtmlUtil.replaceSpace;

public class GeneralView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private WebView webView;
    private Context mContext;
    private  String imgUrlss;
    public LongClickCallBack mCallBack;
    private List<String> titles;//下载所需
    private List<String> links;//下载所需
    private int isReply;//1是可以下载的。
    private boolean isQR;//判断是否为二维码
    private Result result;//二维码解析结果
    private ClickCallBack<String> mClickCallBack;
    private ArrayAdapter<String> adapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                if (isQR) {
                    adapter.add("识别图中二维码");
                }
                adapter.notifyDataSetChanged();
            }
        }

        ;
    };

    public GeneralView(Context context) {
        this(context, null);
    }

    public GeneralView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneralView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.comment_view_layout, null, false);
        webView = (WebView) view.findViewById(R.id.comment_web_view);
        addView(view);
        initWebView();
    }

    public void setSimulation(String html) {
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
    }

    public void setHtmlText(String html, String reHttp) {
        html = html.replace("text-indent: 2em;", "");
        html = html.replace("<br/>", "");
        html = repairContent(html, reHttp);
        setLoadWebView(getHtml(html));
    }

    //    public void setSimulationTestQuestion(String html) {
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
//    }


    public void setSimpleTxt(String html) {
        setLoadWebView(getHtml(html));
    }

    public void setText(String html) {
        html = replaceSpace(replace(fromHtml(html).toString()));
        html = repairContent(html, RetrofitProvider.VIPLGW);
        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
    }

    public void setHighDetalText(String html) {
        html = repairContent(html, RetrofitProvider.BASEURL);
        setLoadWebView(getHtml(rep(html)));
    }

//    public void setTestQuestion(String html) {
//        html = replaceSpace(replace(fromHtml(html).toString()));
//        html = repairContent(html, RetrofitProvider.BASEURL);
//        setLoadWebView(getHtml(html, SharedPref.getFontSize(mContext)));
//    }

    //      <a style="font-size:14px; color:#0066cc;" href="/files/attach/file/20170705/1499241703280757.pdf"title="7.1换库数学鸡精整理（更新至第47题.pdf">7.1换库数学鸡精整理（更新至第47题.pdf</a>===0===
//            log(matcher.group(0)+"===0===");
//      "/files/attach/file/20170705/1499241703280757.pdf"===1===
//            log(matcher.group(1)+"===1===");
//      /files/attach/file/20170705/1499241703280757.pdf===2===
//            log(matcher.group(2)+"===2===");
//      null===3===
//            log(matcher.group(3)+"===3===");
//      null===4===
//            log(matcher.group(4)+"===4===");
//      7.1换库数学鸡精整理（更新至第47题.pdf===5===
//            log(matcher.group(5)+"===5===");
    private String repairHref(String replaceHttp, String html) {
        String patternStr = "<a[^>]*href=(\\\"([^\\\"]*)\\\"|\\'([^\\']*)\\'|([^\\\\s>]*))[^>]*>(.*?)</a>";
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        String result = html;
        titles = new ArrayList<>();
        links = new ArrayList<>();
        while (matcher.find()) {
            titles.add(matcher.group(5));
            String src = matcher.group(2);
            links.add(src);
            String replaceSrc = "";
//            if (src.lastIndexOf(".") > 0) {
//                replaceSrc = src.substring(0, src.lastIndexOf(".")) + src.substring(src.lastIndexOf("."));
//            }
            if (!src.startsWith("http://") && !src.startsWith("https://")) {
                replaceSrc = replaceHttp + src;
            } else {
                replaceSrc = src;
            }
            result = result.replaceAll(src, replaceSrc);
        }
        return result;
    }


    public void setTestInfomationText(String replaceHost, String html) {
        String result = repairContent(fromHtml(html).toString(), replaceHost);
        result = repairHref(replaceHost, result);
        result = rep(result.replace("&nbsp;", " "));
        setLoadWebView(getHtml(result, SharedPref.getFontSize(mContext)));
    }

    //        <p><span style="font-size: 12px; font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;"><br/></span></p>
//        <p><br/></p><p></p><p></p>
    String[] regs = new String[]{
            "<p><span ([^>]*)>\\s*<br/>\\s*</span></p>",
            "<p><span ([^>]*)>\\s*</span>\\s*<br/>\\s*</p>",
            "<p ([^>]*)><span ([^>]*)>\\s*<br/>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<br ([^>]*)>\\s*<span ([^>]*)>\\s*</span>\\s*</p>",
            "(<p><br/></p>)+",
            "(<p></p>)+",
            "<p><span ([^>]*)>\\s*</span></p>",
            "<p><span ([^>]*)></span>\\s*</p>",
            "(<p ([^>]*)>\\s*<br/>\\s*</p>)+",
            "<p ([^>]*)>\\s*<br ([^>]*)>\\s*</p>",
            "(<p>\\s*<br ([^>]*)>\\s*</p>)+",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong>\\s*<br ([^>]*)>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong>\\s*<br/>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong ([^>]*)>\\s*<br/>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong ([^>]*)><span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</strong>\\s*</span>\\s*</p>",
            "<p ([^>]*)>\\s*<span ([^>]*)>\\s*<strong><span ([^>]*)>\\s*<br ([^>]*)>\\s*</span>\\s*</strong>\\s*</span>\\s*</p>"
    };
//    String[] rep = new String[]{"", "", "", "", "", "", "", ""};

    private String rep(String html) {
        for (int i = 0; i < regs.length; i++) {
            html = html.replaceAll(regs[i], "");
        }
        log(html);
        return html;
    }

    private void log(String msg) {
        Utils.logh("GeneralView", msg);
    }

    public void setText(String replaceHost, String html) {
        String result = repairContent(html, replaceHost);
        result = repairHref(replaceHost, result);
        setLoadWebView(getHtml(result, SharedPref.getFontSize(mContext)));
    }

    public void setTitle(int isReply) {
        this.isReply = isReply;
    }

    public void setKnowDetailText(String html, int fontSize) {
        setText(RetrofitProvider.BASEURL, html, fontSize);
    }

    private void setText(String replaceHost, String html, int fontSize) {
        String result = repairContent(html, replaceHost);
        setLoadWebView(getHtml(result, fontSize));
    }

    public String rS(String content) {
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", " ");
        }
        return content.trim();
    }


    private String replace(String html) {
        if (html.contains("\\n")) {
            return html.replace("\\n", "<br/>");
        }
        return html;
    }


    private void setLoadWebView(String html) {
        webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    private void initWebView() {
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setBlockNetworkImage(false);
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebSettings.setUseWideViewPort(true);
//        mWebSettings.setLoadWithOverviewMode(true);
        webView.requestFocus();




        webView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final WebView.HitTestResult htr = webView.getHitTestResult();//获取所点击的内容
                if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE
                        || htr.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                        || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    //判断被点击的类型为图片
//                    if (mCallBack!=null) {
//                        mCallBack.onLongClickCallBack(htr.getExtra());
//                    }


                    new Thread() {
                        public void run() {
                            try {
                                Log.i("imgurl",imgUrlss);
                                decodeImage(htr.getExtra());
                                handler.sendEmptyMessage(0);
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }

                    }.start();
                    showDialog();
                }
                return false;
            }
        });



        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListner();
                addGeneralViewHeight();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (selfDeal(url)) {
//                    mContext.startActivity(new Intent(mContext, DownloadListActivity.class));
                    if (titles != null && links != null && !links.isEmpty() && !titles.isEmpty()) {
                        int index = -1;
                        for (int i = 0, size = links.size(); i < size; i++) {
                            if (url.contains(links.get(i))) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            if (isReply == 1) {
                                DownloadListActivity.startDownloadAct(mContext, url, titles.get(index));
                            } else {
                                if (mClickCallBack != null) {
                                    mClickCallBack.onClick(url);
                                }
                            }
                        }
                    }
                    return true;
                } else {
                    DealActivity.startDealActivity(mContext, "", url);
                    return true;
                }
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.addJavascriptInterface(new JavascriptInterface(mContext), "imagelistner");
        webView.addJavascriptInterface(new GeneralViewHeight(), "App");
    }

    private void addGeneralViewHeight() {
        webView.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
    }



    // js通信接口
    public class GeneralViewHeight {

        public GeneralViewHeight() {
        }

        @android.webkit.JavascriptInterface
        public void resize(final float height) {
            ((BaseActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此处的 layoutParmas 需要根据父控件类型进行区分，这里为了简单就不这么做了
                    webView.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,
                            (int) (Math.ceil(height * getResources().getDisplayMetrics().density)) + 50));
                }
            });
        }
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
             imgUrlss = "javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()";
        webView.loadUrl(imgUrlss);
    }

    /**
     * @param sUrl
     * @return
     * @throws Throwable
     */
    private boolean decodeImage(String sUrl) throws Throwable {
        result = DecodeImage.handleQRCodeFormBitmap(getBitmap(sUrl));
        if (result == null) {
            isQR = false;
        } else {
            isQR = true;
        }
        return isQR;
    }


    /**
     * 根据地址获取网络图片
     *
     * @param sUrl 图片地址
     * @return
     * @throws
     */
    public static Bitmap getBitmap(String sUrl) {
        try {
            URL url = new URL(sUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showDialog() {

        adapter = new ArrayAdapter<String>(getContext(), R.layout.item_dialog);
        adapter.add("保存到手机");

        CustomDialog mCustomDialog = new CustomDialog(getContext(), R.layout.custom_dialog) {

            @Override
            public void initViews() {
                // 初始CustomDialog化控件
                ListView mListView = (ListView) findViewById(R.id.lv_dialog);
                mListView.setAdapter(adapter);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 点击事件
                        switch (position) {
                            case 0:
                                Utils.toastShort(mContext, "已保存到手机");

//                                Toast.makeText(getContext(), "二维码识别结果: " + result.toString(), Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                intent.setData(Uri.parse(result.toString()));
//                                startActivity(intent);
                                closeDialog();
                                break;

                            case 1:
                                Uri uri = Uri.parse(result.toString());
                                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                                mContext.startActivity(intent);
//                                Toast.makeText(getContext(), "已保存到手机", Toast.LENGTH_LONG).show();
                                closeDialog();
                                break;
                        }

                    }
                });
            }
        };
        mCustomDialog.show();
    }

    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String imgUrl) {
            ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(-2, -2);
            List<String> imgs = new ArrayList<>();
            imgs.add(imgUrl);
            ImagePagerActivity.startImagePagerActivity(context, imgs, 0, imageSize);

            for(int i = 0; i < imgs.size(); i++){

            }
//            Intent intent = new Intent();
//            intent.putExtra("image", imgUrl);
//            intent.setClass(context, ShowWebImageActivity.class);
//            context.startActivity(intent);
        }
    }

    public void setOnClickListener(ClickCallBack<String> clickCallBack) {
        mClickCallBack = clickCallBack;
    }

    private boolean selfDeal(String url) {
        url = url.toLowerCase();
        List<String> res = new ArrayList<>();
        res.add(".doc");
        res.add(".docx");
        res.add(".xls");
        res.add(".xlsx");
        res.add(".pdf");
        res.add(".ppt");
        res.add(".pptx");
        res.add(".mp3");
        res.add(".mp4");
        res.add(".txt");
        for (String r : res) {
            if (url.endsWith(r)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 长按事件回调接口，传递图片地址
     * @author LinZhang
     */

    private interface LongClickCallBack{
        /**用于传递图片地址*/
        void onLongClickCallBack(String imgUrl);
    }

}