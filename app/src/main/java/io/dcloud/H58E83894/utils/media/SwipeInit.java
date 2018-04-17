package io.dcloud.H58E83894.utils.media;

import android.support.v4.widget.SwipeRefreshLayout;

public class SwipeInit {

    public static void setRefreshColor(SwipeRefreshLayout mSwipe) {
        mSwipe.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }
}
