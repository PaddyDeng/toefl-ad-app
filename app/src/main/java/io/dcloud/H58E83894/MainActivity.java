package io.dcloud.H58E83894;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.ui.center.CenterFragment;
import io.dcloud.H58E83894.ui.common.update.SimpleUpdateApk;
import io.dcloud.H58E83894.ui.helper.MainNavHelper;
import io.dcloud.H58E83894.ui.knowledge.KnowledgeFragment;
import io.dcloud.H58E83894.ui.make.MakeFragment;
import io.dcloud.H58E83894.ui.prelesson.PreLessonFragment;
import io.dcloud.H58E83894.ui.toeflcircle.ToeflCircleFragment;
import io.dcloud.H58E83894.utils.ActivityCollector;

public class MainActivity extends BaseActivity {

    private MainNavHelper mHelper;
    @BindView(R.id.nav_containre)
    LinearLayout navContainer;
    private SimpleUpdateApk mSimpleUpdateApk;
    private long exitTime = 0;

    /**
     * @deil 沉浸式
     */
    public void setNavContainerVisibility(int visibility) {
        navContainer.setVisibility(visibility);
    }


    public void showOrHideEt(int visibility, RemarkData remarkData, int position) {
        showOrHideEt(visibility, remarkData, position, true);
    }

    /**
     * @param reply 回复评论.
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
        mHelper.addFragment(new KnowledgeFragment(), R.id.main_nav_knowledge);//新增的知识库模块
        mHelper.addFragment(new ToeflCircleFragment(), R.id.main_nav_circle);
        mHelper.addFragment(new CenterFragment(), R.id.main_nav_center);

        mHelper.replaceFragment(getSupportFragmentManager(), R.id.main_nav_pre_lesson);
    }

    // 待补充
    @OnClick({R.id.main_nav_pre_lesson, R.id.main_nav_make, R.id.main_nav_knowledge, R.id.main_nav_circle, R.id.main_nav_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_nav_pre_lesson:
            case R.id.main_nav_make:
            case R.id.main_nav_knowledge:
            case R.id.main_nav_circle:
            case R.id.main_nav_center:
                mHelper.switchFragment(getSupportFragmentManager(), view);
                break;
            default:
                break;
        }
    }


    /**
     * 捕捉返回事件按钮
     * <p>
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                this.exitApp();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出程序
     */
    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            toastShort("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
        }
    }
}
