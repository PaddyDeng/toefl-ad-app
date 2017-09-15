package io.dcloud.H58E83894.ui.common.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.FileUtil;
import io.dcloud.H58E83894.utils.Utils;

/**
 * apk检查更新类,Activity 需实现OnDownloadApkListener
 */
public class DownloadApk {
    private Activity context;
    private String updateFilePath;
    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;
    private static final int DOWN_STEP = 2;
    private String down_url;
    private static final int TIMEOUT = 10 * 1000;
    private Dialog downloadDialog;
    private TextView mApkStep;
    private OnDownloadApkListener onDownloadApkListener;

    public DownloadApk(Activity context) {
        this.context = context;
//        try {
//            onDownloadApkListener = (OnDownloadApkListener) context;
//        } catch (ClassCastException e) {
//			throw new ClassCastException(context.getClass().getName()
//                    + " must implement OnDownloadApkListener");
//        }
    }

    public void setOnDownloadApkListener(OnDownloadApkListener onDownloadApkListener) {
        this.onDownloadApkListener = onDownloadApkListener;
    }

    public void downloadApk(String downUrl) {
        this.down_url = downUrl;
        View view = View.inflate(context, R.layout.loading, null);
        mApkStep = (TextView) view.findViewById(R.id.loading_msg);
        mApkStep.setText(String.format(
                context.getString(R.string.str_dialog_apk_downloding_msg), 0, "%"));
        downloadDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();
        downloadDialog.show();
        updateFilePath = createFile();
        doDownloadApk(updateFilePath);
    }

    private String createFile() {
        File file = FileUtil.createFile(context, "gmat.apk");
        if (file == null || TextUtils.isEmpty(file.getPath())) {
            return null;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file.getAbsolutePath();
    }

    private void doDownloadApk(final String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            Utils.logh("UPDATEAPK", "创建文件失败");
            handler.sendEmptyMessage(DOWN_ERROR);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                try {
                    if (downloadUpdateFile(down_url, filePath) > 0) {
                        handler.sendEmptyMessage(DOWN_OK);
                        error = false;
                    } else {
                        error = true;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    error = true;
                }
                if (error) {
                    File file = new File(filePath);
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                    handler.sendEmptyMessage(DOWN_ERROR);
                }
            }
        }).start();
    }

    private long downloadUpdateFile(String down_url, String file)
            throws IOException {
        URL url;
        try {
            url = new URL(down_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return 0;
        }

        int down_step = 5;
        int totalSize;
        int downloadCount = 0;
        int updateCount = 0;
        InputStream inputStream;
        OutputStream outputStream;

        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // get the total size of file
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            return 0;
        }
        inputStream = httpURLConnection.getInputStream();
        outputStream = new FileOutputStream(file, false);// overlay if file
        // exist
        byte buffer[] = new byte[1024];
        int readsize = 0;
        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;
            if (updateCount == 0 ||
                    (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                final Message message = new Message();
                message.what = DOWN_STEP;
                message.arg1 = updateCount;
                handler.sendMessage(message);
            }

        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;

    }

    private void installFile() {
        File f = new File(updateFilePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Utils.isBelowAndroidVersion(Build.VERSION_CODES.N)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(f), "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(FileProvider.getUriForFile(context, "io.dcloud.H58E83894.fileprovider", f), "application/vnd.android.package-archive");
        }
        context.startActivityForResult(intent, C.REQUST_CODE_UPDATE);
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_STEP:
                    downStep(msg);
                    break;
                case DOWN_OK:
                    downOk();
                    installFile();
                    break;
                case DOWN_ERROR:
                    downError();
                    break;
            }
            return false;
        }

    });

    private void downStep(Message msg) {
        mApkStep.setText(String.format(context.getResources().getString(
                R.string.str_dialog_apk_downloding_msg), msg.arg1, "%"));
    }

    private void downOk() {
        if (null != downloadDialog && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
        }
        downloadDialog = null;
    }

    private void downError() {
        if (null != downloadDialog && downloadDialog.isShowing()) {
            downloadDialog.dismiss();
        }
        downloadDialog = null;
        onDownloadApkListener.onDownError();
    }


    public interface OnDownloadApkListener {
        void onDownError();//下载失败
    }

}
