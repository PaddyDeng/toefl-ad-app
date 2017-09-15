package io.dcloud.H58E83894.utils.media;

import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

/**
 * Created by fire on 2017/7/20  11:46.
 */

public class MediaUtil {

    public static long getMediaTime(String path) {
        long dur = 0;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path); //在获取前，设置文件路径（应该只能是本地路径）
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release(); //释放
        if (!TextUtils.isEmpty(duration)) {
            dur = Long.parseLong(duration);
        }
        return dur;
    }
}
