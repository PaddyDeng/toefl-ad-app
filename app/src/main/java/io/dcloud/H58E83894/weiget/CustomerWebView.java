package io.dcloud.H58E83894.weiget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.utils.Utils;

import static io.dcloud.H58E83894.utils.HtmlUtil.fromHtml;
import static io.dcloud.H58E83894.utils.HtmlUtil.getHtml;

public class CustomerWebView extends WebView {

    public CustomerWebView(Context context) {
        this(context, null);
    }

    public CustomerWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        MOVE_THRESHOLD_DP = 20.0F * context.getResources().getDisplayMetrics().density;
        initWebView();
    }

    private void initWebView() {
        WebSettings mWebSettings = getSettings();
        mWebSettings.setJavaScriptEnabled(true);
//        mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        requestFocus();
        setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }


//    </p>\r\n\r\n<p><br />\r\n  <br />\r\n<br />\r\n  </p>\r\n\r\n<p><br />\r\n

    private String replace(String content) {
        if (content.contains("&amp;")) {
            content = content.replace("&amp;", "&");
        }
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", " ");
        }
        if (content.contains("\\r\\n")) {
            content = content.replace("\\r\\n", "<br />").trim();
        }
        content = content.replaceAll("(<br />)+", "<br />");
//        if (content.contains("<p><br />")) {
//            content = content.replace("<p><br />", "<p>").trim();
//        }
        content = rep(content, regs, rep);
        if (content.contains("</p><br /><p><br />")) {
            content = content.replace("</p><br /><p><br />", "</p><p>").trim();
        }

        log(content);

        return content.trim();
    }

    private String[] regs = new String[]{"<(p|P)>(\\s*)<br/>", "</(p|P)>(\\s*)<br/>", "<br/>(\\s*)<(p|P)>", "<br/>(\\s*)</(p|P)>", "<(p|P)>(\\s*)</(p|P)>"};
    private String[] rep = new String[]{"<p>", "</p>", "<p>", "</p>", ""};
    private String[] options = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};

    private String rep(String html, String[] reg, String[] rep) {
        for (int i = 0; i < reg.length; i++) {
            html = html.replaceAll(reg[i], rep[i]);
        }
        return html;
    }

    private String addBgColor(String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("<span style=\"background-color:#FFFF00;\">");
        sb.append(content);
        sb.append("</span>");
        return sb.toString();
    }

    private String insertBorder(String content, String option, String questionType) {
        StringBuffer sb = new StringBuffer();
        if (TextUtils.equals(questionType, "6")) {
            sb.append(content);
            sb.append("[▊");
            sb.append(option);
            sb.append(" ]");
        } else {
            sb.append("[▊");
            sb.append(option);
            sb.append(" ]");
            sb.append(content);
        }
        return sb.toString();
    }

    /**
     * 单选: questType 不等于 4 也不等于 6
     * 插入(相当于单选一个句子插入阅读文章使语句通顺): questionType = 6
     * <p>
     * 多选而且排序(打个比方, 提供 A - F几个选项, 但是需要选择 3 个, 然后 3 个的顺序还必须正确,比如 BCE 才是正确答案, CBE ECB 都是错误的)  questionType = 4
     */
    public void setText(String html, String positionD, String positionW, String questionType, String[] split) {
        String newPositionD = positionD.replace("\u00a0", " ");
        String newPositionW = positionW.replace("\u00a0", " ");

        if (TextUtils.equals("6", questionType) || TextUtils.equals("9", questionType)) {
            //插入
            for (int i = 0, size = split.length; i < size; i++) {
                String oldStr = split[i];
                if (TextUtils.isEmpty(oldStr)) {
                    continue;
                }
                if (TextUtils.isEmpty(oldStr.trim())) {
                    continue;
                }
                oldStr = synbolSwitchHtml(oldStr);
                log(oldStr);

                html = html.replace(oldStr.trim(), insertBorder(oldStr.trim(), options[i], questionType));
                log(html);
            }
        } else if (TextUtils.equals("2", questionType) || TextUtils.equals("5", questionType)) {
            // type等于2或者等于5表示【标记段落】：
            // 标记当前段落（可有多段）
            positionD = positionD.replace("\u2666", "").trim();
            html = addStar(html, positionD);
        } else if (TextUtils.equals("1", questionType)) {
            html = morePara(html, newPositionD, newPositionW);
        } else if (TextUtils.equals("3", questionType)) {
            //    type等于3表示【标记段落或者单词】：
            //   （1）如果有单词，就高亮当前段落的单词（可有多段）
            //   （2）如果没有单词，就标记当前段落（可有多段）
            if (newPositionW.isEmpty())//没有单词
                addStar(html, newPositionD);
            else //有单词,高亮段落单词，有多段和多个单词的情况
                html = morePara(html, newPositionD, newPositionW);

        }
        setLoadWebView(getHtml(html));
    }

    private String synbolSwitchHtml(String oldStr) {
        oldStr = oldStr.replace("\u00a0", "");
        oldStr = oldStr.replace("'", "&#39;");
        oldStr = oldStr.replace("\"", "&quot;");
        return oldStr;
    }

    /**
     * type等于1表示【标记单词】：
     * （1）如果没有段落就让单词加粗高亮
     * （2）如果有段落，就高亮当前段落的单词（可有多段）
     */
    private String morePara(String html, String newPositionD, String newPositionW) {

        if (newPositionD.isEmpty())//没有段落，高亮单词
            html = setBgColor(html, newPositionD, newPositionW);
        else {
            String[] d = null;
            if (newPositionD.contains("\r\n")) {
                d = Utils.splitOption(newPositionD);
            } else if (newPositionD.contains("\n")) {
                d = Utils.splitOptionThroughN(newPositionD);
            }
            String[] w = null;
            if (newPositionW.contains("\r\n")) {
                w = Utils.splitOption(newPositionW);
            } else if (newPositionW.contains("\n")) {
                w = Utils.splitOptionThroughN(newPositionW);
            }
            if (d == null) {
                if (w == null) {
                    html = setBgColor(html, newPositionD, newPositionW);
                } else {
                    for (String sw : w) {
                        html = setBgColor(html, newPositionD, sw);
                    }
                }
            } else if (w == null) {
                for (String sd : d) {
                    html = setBgColor(html, sd, newPositionW);
                }
            } else {
                int dSize = d.length;
                int wSize = w.length;
                int size = dSize >= wSize ? wSize : dSize;
                for (int i = 0; i < size; i++) {
                    html = setBgColor(html, d[i], w[i]);
                }
            }
        }
        return html;
    }

    private String addStar(String html, String positionD) {
        String[] pd = null;
        if (positionD.contains("\\r\\n")) {
            pd = Utils.splitOption(positionD);
        } else {
            pd = Utils.splitOptionThroughN(positionD);
        }
        for (String p : pd) {
            if (TextUtils.isEmpty(p)) {
                continue;
            }
            if (TextUtils.isEmpty(p.trim())) {
                continue;
            }
            if (p.contains("\u00a0"))
                p = p.replace("\u00a0", " ");
            p = fromHtml(p).toString();
            if (p.contains("\""))
                p = p.split("\"")[0];
            if (p.contains("\'"))
                p = p.split("\'")[0];
            log(TextUtils.concat("♦", p).toString());
            html = html.replace(p.trim(), TextUtils.concat("♦", p));
            log(html);
        }
        return html;
    }

    /**
     * 高亮单词，或某一段中的单词
     */
    private String setBgColor(String html, String positionD, String positionW) {
        if (TextUtils.isEmpty(positionD)) {
            if (!TextUtils.isEmpty(positionW)) {
                //将positionW变成高亮
                positionW = synbolSwitchHtml(positionW);
                html = html.replace(positionW.trim(), addBgColor(positionW));
            }
        } else {
            positionD = positionD.trim();
            positionD = synbolSwitchHtml(positionD);
            if (TextUtils.isEmpty(positionW)) {
                html = html.replace(positionD, addBgColor(positionD));
            } else {
                positionW = positionW.trim();
                positionW = synbolSwitchHtml(positionW);
                String newPositionD = "";
                //将positionD中的positionW变成高亮
                if (TextUtils.equals(positionD, positionW))
                    newPositionD = addBgColor(positionW);
                else
                    newPositionD = positionD.replace(positionW, addBgColor(positionW));

                log(newPositionD);
                if (html.contains(positionD)) {
                    html = html.replace(positionD, newPositionD);
                } else {
                    html = html.replace(positionW, addBgColor(positionW));
                }
                log(html);
            }
        }
        return html;
    }

    public void setText(String html) {
        setLoadWebView(getHtml(html));
    }

    private void setLoadWebView(String html) {
        loadDataWithBaseURL("file:///android_asset/", html, "text/html", "utf-8", null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (intercept) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        return super.dispatchTouchEvent(ev);
    }

    //    private boolean
    private boolean intercept = true;

    public void setIntercept(boolean intercept) {
        this.intercept = intercept;
    }


    private float mDownPosX;
    private float mDownPosY;
    private float mUpPosX;
    private float mUpPosY;

    private float MOVE_THRESHOLD_DP;
    private OnItemClickListener clickListener;

    private final int CLICK_ON_WEBVIEW = 1;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case CLICK_ON_WEBVIEW:
                    if (clickListener != null) {
                        clickListener.onClick(CustomerWebView.this, 0);
                    }
                    break;
            }
            return false;
        }
    });

    public void setOnCustomeClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    private void log(String msg) {
        Utils.logh("========", msg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.mDownPosX = event.getX();
                this.mDownPosY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
                this.mUpPosX = event.getX();
                this.mUpPosY = event.getY();
                if ((Math.abs(mUpPosX - this.mDownPosX) < MOVE_THRESHOLD_DP) && (Math.abs(mUpPosY - this.mDownPosY) < MOVE_THRESHOLD_DP)) {
                    if (!mHandler.hasMessages(CLICK_ON_WEBVIEW)) {
                        mHandler.sendEmptyMessage(CLICK_ON_WEBVIEW);
                    }
                }
                break;

//            case MotionEvent.ACTION_CANCEL://被拦截了执行的语句
//                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
