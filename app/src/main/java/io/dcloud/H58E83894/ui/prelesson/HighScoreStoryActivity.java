package io.dcloud.H58E83894.ui.prelesson;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.ui.toeflcircle.DownLoadFragment;
import io.dcloud.H58E83894.ui.toeflcircle.ExamInfoFragment;


/**
 * 高分下专区
 * */
public class HighScoreStoryActivity extends BaseActivity {

    public static void startMary(Context c,  String titleName, String catId, String index) {
        Intent intent = new Intent(c, HighScoreStoryActivity.class);
        intent.putExtra(Intent.EXTRA_TITLE, titleName);
        intent.putExtra(Intent.EXTRA_TEXT, catId);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        c.startActivity(intent);
    }

    private int page  = 1;
    private int catId;
    private int index;
    private String catsId;
    @BindView(R.id.ss_base_iv)
    ImageView sendPosts;
    @BindView(R.id.title_centertxt)
    TextView titleTxt;

    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);


    @Override
    protected void getArgs() {
        super.getArgs();
        Intent intent = getIntent();
        if (intent != null) {
            catsId = intent.getStringExtra(Intent.EXTRA_TEXT);
            Log.i("ooo", catsId);
            catId = Integer.parseInt(catsId);
            titleTxt.setText(intent.getStringExtra(Intent.EXTRA_TITLE));
            index = Integer.parseInt(getIntent().getStringExtra(Intent.EXTRA_INDEX));
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mist_list);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initData() {
        super.initData();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        if (index == 3) {//机经
            sendPosts.setImageResource(R.drawable.ic_mery);
            Fragment myfragment03= ExamInfoFragment.newInstance("372","3");
            transaction.replace(R.id.frame, myfragment03);
            transaction.commit();
//            Fragment myfragment03= new MachFragment();
//            transaction.replace(R.id.frame, myfragment03);
//            transaction.commit();

        } else if (index == 5) {
            sendPosts.setImageResource(R.drawable.ic_essay_update_01);
            Fragment myfragment05= ExamInfoFragment.newInstance("374","5");
            transaction.replace(R.id.frame, myfragment05);
            transaction.commit();
//            Fragment myfragment05= new EaFragment();
//            transaction.replace(R.id.frame, myfragment05);
//            transaction.commit();
        }else if (index == 2) {
            sendPosts.setImageResource(R.drawable.ic_download_01);
            Fragment myfragment02= DownLoadFragment.newInstance("9","2");
            transaction.replace(R.id.frame, myfragment02);
            transaction.commit();
//            Fragment myfragment02= new DownLoadsFragment();
//            transaction.replace(R.id.frame, myfragment02);
//            transaction.commit();
        }
    }



}
