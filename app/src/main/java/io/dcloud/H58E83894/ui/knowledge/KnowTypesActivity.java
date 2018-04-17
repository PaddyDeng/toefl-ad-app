package io.dcloud.H58E83894.ui.knowledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.KnowMaxListData;
import io.dcloud.H58E83894.data.know.KnowTypeData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 知识库类型阶段
 * */
public class KnowTypesActivity extends BaseActivity {



    public static void startKnow(Context c,  int index) {
        Intent intent = new Intent(c, KnowTypesActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, mKnow);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        c.startActivity(intent);
    }

    private int index;
    @BindView(R.id.know_type_title)
    TextView titleName;
    @BindView(R.id.know_type_02)
    TextView knowType02;
    @BindView(R.id.know_type_03)
    TextView knowType03;
    @BindView(R.id.know_type_04)
    TextView knowType04;
    @BindView(R.id.know_type_01)
    TextView knowType01;
    private int knowOne, knowTwo, knowThr, knowFour;
    private List<KnowTypeData.DataBean> data;

    @Override
    protected void getArgs() {
        super.getArgs();
        index = getIntent().getIntExtra(Intent.EXTRA_INDEX, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_type_grade);
        initData();
//        initHttp();

    }

    private void initHttp(int id) {
        addToCompositeDis(HttpUtil.getKnowType(id).subscribe(new Consumer<KnowTypeData>() {
            @Override
            public void accept(@NonNull KnowTypeData bean) throws Exception {
                if (!getHttpResSuc(bean.getCode())) {
                    return;
                }
                if (!bean.getData().isEmpty()) {
                    data = bean.getData();
                    knowType01.setText(data.get(0).getName());//基础
                    knowType02.setText(data.get(1).getName());
                    knowType03.setText(data.get(2).getName());
                    knowType04.setText(data.get(3).getName());

                    knowOne = Integer.parseInt(data.get(0).getId());//传给下一面的id
                    knowTwo = Integer.parseInt(data.get(1).getId());
                    knowThr = Integer.parseInt(data.get(2).getId());
                    knowFour = Integer.parseInt(data.get(3).getId());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
               return;
            }
        }));
    }

    @Override
    protected void initData() {
        super.initData();
        if (index == 0) {
            titleName.setText(R.string.str_practice_listen);
            initHttp(386);
        } else if (index == 1) {
            titleName.setText(R.string.str_practice_voice);
            initHttp(387);
        } else if (index == 2) {
            titleName.setText(R.string.str_practice_write);
            initHttp(388);
        } else if (index == 3) {
            titleName.setText(R.string.str_practice_read);
            initHttp(389);
        }
    }

    @OnClick({R.id.know_type_01, R.id.know_type_02, R.id.know_type_03, R.id.know_type_04})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.know_type_01://备考
                if(!TextUtils.isEmpty(String.valueOf(knowOne))){
                    if (needLogin()) return;
                    KnowPrefectureActivity.KnowPrefecture(this, knowOne, data.get(0).getName());
                }

                break;
            case R.id.know_type_02://基础
                if(!TextUtils.isEmpty(String.valueOf(knowTwo))){
                    if (needLogin()) return;
                    KnowPrefectureActivity.KnowPrefecture(this, knowTwo, data.get(1).getName());
                }
                break;
            case R.id.know_type_03://技巧
                if(!TextUtils.isEmpty(String.valueOf(knowThr))){
                    if (needLogin()) return;
                    KnowPrefectureActivity.KnowPrefecture(this, knowThr, data.get(2).getName());
                }
                break;
            case R.id.know_type_04://tishen
                if(!TextUtils.isEmpty(String.valueOf(knowFour))){
                    if (needLogin()) return;
                    KnowPrefectureActivity.KnowPrefecture(this, knowFour, data.get(3).getName());
                }
                break;
            default:
                break;
        }
    }

}
