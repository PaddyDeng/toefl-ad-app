package io.dcloud.H58E83894.utils;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    public static Spanned fromHtml(String body) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(body);
        } else {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        }
    }

    public static String replaceSpace(String content) {
        if (content.contains("&amp;")) {
            content = content.replace("&amp;", "&");
        }
        if (content.contains("&nbsp;")) {
            content = content.replace("&nbsp;", " ");
        }
        if (content.contains("<p><br/></p>")) {
            content = content.replace("<p><br/></p>", " ");
        }
        if (content.contains("\\r<br/>")) {
            content = content.replace("\\r<br/>", "");
        }

//        if (content.contains("&amp;nbsp;")) {
//            content = content.replace("&amp;nbsp;", " ");
//        }
//        if (content.contains("&rsquo;")) {
//            content = content.replace("&rsquo;", "'");
//        }
//        if (content.contains("&amp;rsquo;")) {
//            content = content.replace("&amp;rsquo;", "'");
//        }
//        if (content.contains("&ndash;")) {
//            content = content.replace("&ndash;", "–");
//        }
//        if (content.contains("&amp;ndash;")) {
//            content = content.replace("&amp;ndash;", "–");
//        }
//        if (content.contains("&amp;euml;")) {
//            content = content.replace("&amp;euml;", "ë");
//        }
//        if (content.contains("&euml;")) {
//            content = content.replace("&euml;", "ë");
//        }
        content = replaceRN(content);
        return content.trim();
    }

    public static String replaceRN(String content) {
        content = content.trim();
        if (content.contains("\\r\\n\\r\\n")) {
            content = content.replace("\\r\\n\\r\\n", "").trim();
        }
//        if (content.contains("\\r")) {
//            content = content.replace("\\r", "").trim();
//        }
        return content;
    }

    /**
     * 将img标签中的src进行二次包装
     *
     * @param content     内容
     * @param replaceHttp 需要在src中加入的域名
     * @return
     */
    public static String repairContent(String content, String replaceHttp) {
//        <img src="http://www.gmatonline.cn//files/attach/images/20151109/1447062370368626.png" title="1447062370368626.png" alt="1.png"/>
        String patternStr = "<img\\s*([^>]*)\\s*src=\\\"(.*?)\\\"\\s*([^>]*)>";
        Pattern pattern = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        String result = content;
        while (matcher.find()) {
            String src = matcher.group(2);
            String replaceSrc = "";
//            if (src.lastIndexOf(".") > 0) {
//                replaceSrc = src.substring(0, src.lastIndexOf(".")) + src.substring(src.lastIndexOf("."));
//            }
            if (!src.startsWith("http://") && !src.startsWith("https://") && !src.contains("data:image/png;base64")) {
                replaceSrc = replaceHttp + src;//+"\" " +"onerror=\"this.src=\"";
            } else {
                replaceSrc = src;
            }
            result = result.replaceAll(src, replaceSrc);
        }
        return result;
    }

    public static String getHtml(String content) {

        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<style>");
//        sb.append("@font-face{");
//        sb.append("font-family: 'customerFonts';");
//        sb.append("src: url('Helvetica Condensed.ttf') format('truetype');");
//        sb.append("src: url('fonts/ChaparralPro-Regular.otf');");
//        sb.append("font-weight: bold;");
//        sb.append("}");
        sb.append("body{");
        sb.append("word-wrap: break-word;");
//        sb.append("font-family: customerFonts;");
        sb.append("font-size: 14px;");
        sb.append("}");
        sb.append("img{");
        sb.append("max-width:95%;");
        sb.append("height : auto;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }


    public static String getHtml(String content, int fontSize) {
        String font = "16px";
        switch (fontSize) {
            case 0:
                font = "14px";
                break;
            case 1:
                font = "15px";
                break;
            case 2:
                font = "16px";
                break;
            case 3:
                font = "18px";
                break;
            case 4:
                font = "20px";
                break;
        }

        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<meta charset=\"UTF-8\">");
        sb.append("<style>");
//        sb.append("@font-face{");
//        sb.append("font-family: 'customerFonts';");
//        sb.append("src: url('Helvetica Condensed.ttf') format('truetype');");
//        sb.append("src: url('fonts/ChaparralPro-Regular.otf');");
//        sb.append("font-weight: bold;");
//        sb.append("}");
        sb.append("body{");
        sb.append("word-wrap: break-word;");
//        sb.append("font-family: customerFonts;");
        sb.append("font-size: " + font + ";");
        sb.append("}");
        sb.append("img{");
        sb.append("max-width:95%;");
        sb.append("height : auto;");
        sb.append("}");
        sb.append("</style>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(content);
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }


}
