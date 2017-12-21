package io.dcloud.H58E83894;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.ui.center.CenterFragment;
import io.dcloud.H58E83894.ui.common.update.SimpleUpdateApk;
import io.dcloud.H58E83894.ui.helper.MainNavHelper;
import io.dcloud.H58E83894.ui.make.MakeFragment;
import io.dcloud.H58E83894.ui.prelesson.PreLessonFragment;
import io.dcloud.H58E83894.ui.toeflcircle.ToeflCircleFragment;

public class MainActivity extends BaseActivity {

    private MainNavHelper mHelper;
    @BindView(R.id.nav_containre)
    LinearLayout navContainer;
    private SimpleUpdateApk mSimpleUpdateApk;

    public void setNavContainerVisibility(int visibility) {
        navContainer.setVisibility(visibility);
    }


    public void showOrHideEt(int visibility, RemarkData remarkData, int position) {
        showOrHideEt(visibility, remarkData, position, true);
    }

    /**
     * @param reply 回复评论
     */
    public void showOrHideEt(int visibility, RemarkData remarkData, int position, boolean reply) {
        ToeflCircleFragment fragment = (ToeflCircleFragment) mHelper.getNaviMapFragment().get(R.id.main_nav_circle);
        fragment.showOrHideEt(visibility, remarkData, position, reply);
    }

    public boolean getEtContainerStatus() {
        ToeflCircleFragment fragment = (ToeflCircleFragment) mHelper.getNaviMapFragment().get(R.id.main_nav_circle);
        return fragment.getEtContainerStatus();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new MainNavHelper(this, R.id.main_contaier);
        setContentView(R.layout.activity_main);
//        login(null);
        mSimpleUpdateApk = new SimpleUpdateApk(MainActivity.this);//检查版本更新
        mSimpleUpdateApk.checkVersionUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSimpleUpdateApk != null) {
            mSimpleUpdateApk.onDestory();
            mSimpleUpdateApk = null;
        }
    }

    @Override
    protected void initData() {
        mHelper.addFragment(new PreLessonFragment(), R.id.main_nav_pre_lesson);
        mHelper.addFragment(new MakeFragment(), R.id.main_nav_make);
        mHelper.addFragment(new ToeflCircleFragment(), R.id.main_nav_circle);
        mHelper.addFragment(new CenterFragment(), R.id.main_nav_center);

        mHelper.replaceFragment(getSupportFragmentManager(), R.id.main_nav_pre_lesson);
    }

    @OnClick({R.id.main_nav_pre_lesson, R.id.main_nav_make, R.id.main_nav_circle, R.id.main_nav_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_nav_pre_lesson:
            case R.id.main_nav_make:
            case R.id.main_nav_circle:
            case R.id.main_nav_center:
                mHelper.switchFragment(getSupportFragmentManager(), view);
                break;
            default:
                break;
        }
    }
}
