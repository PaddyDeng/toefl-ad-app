package io.dcloud.H58E83894.ui.center.setting;

import android.os.Bundle;


import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.ui.center.font.ControlTextView;
import io.dcloud.H58E83894.ui.center.font.FontControlContainer;
import io.dcloud.H58E83894.utils.SharedPref;

public class FontSizeSettingActivity extends BaseActivity implements FontControlContainer.FontSizeChangeListener {

    @BindView(R.id.change_font_size_container)
    FontControlContainer changeFontSize;
    @BindView(R.id.simple_word)
    ControlTextView mControlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_size_setting);
    }

    @Override
    protected void initView() {
        int index = SharedPref.getFontSize(mContext);
        changeFontSize.setThumbLeftMargin(index);
        mControlTextView.setFontSize(index);
    }

    @Override
    protected void initData() {
        changeFontSize.setFontSizeChangeListener(this);
    }

    @Override
    public void fontSize(int index) {
        SharedPref.saveFontSize(mContext, index);
        mControlTextView.setFontSize(index);
    }
}
