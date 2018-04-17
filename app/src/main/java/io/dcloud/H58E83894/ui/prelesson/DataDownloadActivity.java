package io.dcloud.H58E83894.ui.prelesson;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.prelesson.LessonData;
import io.dcloud.H58E83894.data.prelesson.LessonDetailBean;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.common.DealActivity;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.weiget.GeneralView;
import io.dcloud.H58E83894.weiget.ObservableScrollView;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 资料下载的activity
*/
public class DataDownloadActivity extends BaseActivity {

    public static void startToeflDetail(Context c, LessonData data, int type) {
        Intent intent = new Intent(c, DataDownloadActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, data);
        intent.putExtra(Intent.EXTRA_INDEX, type);
        c.startActivity(intent);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gmat_detail);

    }
}
