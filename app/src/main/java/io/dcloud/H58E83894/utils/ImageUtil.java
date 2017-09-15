package io.dcloud.H58E83894.utils;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtil {

    public static String compressImage(Context c, String imgPath) {
        if (TextUtils.isEmpty(imgPath)) {
            return null;
        }
        Bitmap zoomedBitmap = BitmapFactory.decodeFile(imgPath);
        if (zoomedBitmap == null) {
            return null;
        }
        Uri mSaveUri = Uri.fromFile(new File(c.getCacheDir(), "remark_" + System.currentTimeMillis() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = c.getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return getRealFilePathFromUri(c, mSaveUri);
    }

    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String compressPath = null;
        if (scheme == null)
            compressPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            compressPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        compressPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return compressPath;
    }
}
