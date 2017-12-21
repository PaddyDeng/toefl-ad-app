/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package io.dcloud.H58E83894.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class FileUtil {

    public final static String BASE_PATH = "TOEFL";
    public final static String SEC_PATH = "APK";
    public final static String DOWNLAOD = "DOWNLAOD";
    public final static String RECORD = "RECORD";

    public static File getSaveFile(Context context) {
        File file = new File(context.getFilesDir(), "pic.jpg");
        return file;
    }

    public static String getRecordPath(Context context) {
        String path = getFilePathName(externalFilesDir(context), RECORD);

        File p = new File(path);
        if (!p.exists()) {
            p.mkdirs();
        }
        if (!p.exists()) {
            return null;
        }
        return p.getPath();
    }

    public static String getDownloadPath(Context context) {
        String path = getFilePathName(externalFilesDir(context), DOWNLAOD);

        File p = new File(path);
        if (!p.exists()) {
            p.mkdirs();
        }
        if (!p.exists()) {
            return null;
        }
        return p.getPath();
    }

    private static String externalFilesDir(Context context) {
        if (externalExist()) {
            return getFilePathName(Environment.getExternalStorageDirectory().getAbsolutePath(), BASE_PATH);
        }
        return null;
    }

    public static boolean externalExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    //创建文件夹
    public static File createFile(Context context, String fileName) {

        String path = getFilePathName(externalFilesDir(context), SEC_PATH);

        File p = new File(path);
        if (!p.exists()) {
            p.mkdirs();
        }
        if (!p.exists()) {
            return null;
        }
        File f = new File(path, fileName);
        if (!f.exists()) {
            try {
                if (!f.createNewFile()) {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return f;
    }

    public static String getFilePathName(String... args) {
        String[] param = args;
        String Str = args[0];
        int i = 1;

        for (int len = args.length; i < len; ++i) {
            if (Str == null) {
                return null;
            }

            if (Str.endsWith(File.separator) && param[i].startsWith(File.separator)) {
                Str = Str.substring(0, Str.length() - 1) + param[i];
            } else if (!Str.endsWith(File.separator) && !param[i].startsWith(File.separator)) {
                Str = Str + File.separator + param[i];
            } else {
                Str = Str + param[i];
            }
        }

        return Str;
    }

}
