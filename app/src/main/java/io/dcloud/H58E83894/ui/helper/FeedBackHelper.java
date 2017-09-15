package io.dcloud.H58E83894.ui.helper;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import io.dcloud.H58E83894.ui.common.QuestionReportDialog;

/**
 * Created by fire on 2017/8/15  17:27.
 */

public class FeedBackHelper {
    private FeedBackHelper() {
    }

    private static String recordId;
    private static QuestionReportDialog dialog;

    public static void showDialog(String id, FragmentManager fm) {
        if (TextUtils.equals(recordId, id)) {
            if (dialog == null) {
                dialog = QuestionReportDialog.getInstance(id);
            }
        } else {
            recordId = id;
            dialog = QuestionReportDialog.getInstance(id);
        }
        dialog.showDialog(fm);
    }

    public static void destory() {
        if (dialog != null) {
            dialog = null;
        }
    }
}
