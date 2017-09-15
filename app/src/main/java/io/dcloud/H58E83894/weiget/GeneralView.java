package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;

import static io.dcloud.H58E83894.utils.HtmlUtil.repairContent;
import static io.dcloud.H58E83894.utils.HtmlUtil.getHtml;
import static io.dcloud.H58E83894.utils.HtmlUtil.fromHtml;

public class GeneralView extends RelativeLayout {
    private LayoutInflater layoutInflater;
    private WebView webView;
    private Context mContext;
    private List<String> titles;//下载所需
    private List<String> links;//下载所需
    private int isReply;//1是可以下载的。

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

    public void setHtmlText(String html, String reHttp) {
        html = html.replace("text-indent: 2em;", "");
        html = html.replace("<br/>", "");
        html = repairContent(html, reHttp);
        setLoadWebView(getHtml(html));
    }

    public void setSimpleTxt(String html) {
        setLoadWebView(getHtml(html));
    }

    public void setText(String html) {
//        html = replaceSpace(replace(fromHtml(html).toString()));
//        html = repairContent(html, RetrofitProvider.TOEFLURL);
        setLoadWebView(getHtml(html));
    }

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
        result = rep(result.replace("&nbsp;", " "), regs, rep);
//        log(result);
        setLoadWebView(getHtml(result));
    }

    //        <p><span style="font-size: 12px; font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;"><br/></span></p>
//        <p><br/></p><p></p><p></p>
    String[] regs = new String[]{"<p><span ([^>]*)>\\s*<br/>\\s*</span></p>", "(<p><br/></p>)+",
            "(<p></p>)+", "<p><span ([^>]*)>\\s*</span>\\s*<br/>\\s*</p>",
            "<p><span ([^>]*)>\\s*</span></p>", "<p><span ([^>]*)></span>\\s*</p>"};
    String[] rep = new String[]{"", "", "", "", "", ""};

    private String rep(String html, String[] reg, String[] rep) {
        for (int i = 0; i < reg.length; i++) {
            html = html.replaceAll(reg[i], rep[i]);
        }
        return html;
    }


    public void setText(String replaceHost, String html) {
        String result = repairContent(html, replaceHost);
        result = repairHref(replaceHost, result);
        setLoadWebView(getHtml(result));
    }

    public void setTitle(int isReply) {
        this.isReply = isReply;
    }

    public void setKnowDetailText(String html, int fontSize) {
        setText(RetrofitProvider.TOEFLURL, html, fontSize);
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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                addImageClickListner();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                DealActivity.startDealActivity(mContext, "", url);
                return true;
//                view.loadUrl(url);
//                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        webView.addJavascriptInterface(new JavascriptInterface(mContext), "imagelistner");
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
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
        }
    }

}
