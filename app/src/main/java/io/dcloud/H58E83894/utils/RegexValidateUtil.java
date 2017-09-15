package io.dcloud.H58E83894.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidateUtil {

    public static boolean checkEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        boolean flag;
        try {
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkPhoneNumber(String pn) {
        if (TextUtils.isEmpty(pn) || !pn.startsWith("1") || pn.length() != 11) {
            return false;
        }
        return true;
    }

}
