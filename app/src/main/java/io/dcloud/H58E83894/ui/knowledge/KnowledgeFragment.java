package io.dcloud.H58E83894.ui.knowledge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.KnowMaxListData;
import io.dcloud.H58E83894.data.know.KnowData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/12/25.知识库
 */

public class KnowledgeFragment extends BaseFragment {


//    @BindView(R.id.know_swipe)
//    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.grammar_sc_control_tv)
    ImageView grammarScTv;
    @BindView(R.id.frag_know_container)
    RelativeLayout mContainer;
    @BindView(R.id.know_content_container)
    LinearLayout mContentContainer;
    @BindView(R.id.logic_des)
    TextView logicDesTxt;
    @BindView(R.id.grammar_des)
    TextView grammarDesTxt;
    @BindView(R.id.read_des)
    TextView readDesTxt;
    @BindView(R.id.math_des)
    TextView mathDesTxt;
    private  List<KnowMaxListData.DataBean>    knowDatas;
    private KnowData knowData1;

    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_knowledge, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Utils.removeOnGlobleListener(mContainer, this);
                int singleHeight = mContainer.getMeasuredHeight() / 4;
                int count = mContentContainer.getChildCount();
                for (int i = 0; i < count; i++) {
                    RelativeLayout child = (RelativeLayout) mContentContainer.getChildAt(i);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params.height = singleHeight;
                    child.requestLayout();
                }
            }
        });
//        控制字体大小
//        int fontSize = SharedPref.getFontSize(getActivity());
//        logicCrTv.setFontSize(fontSize);
//        grammarScTv.setFontSize(fontSize);
//        readRcTv.setFontSize(fontSize);
//        mathQTv.setFontSize(fontSize);
        initHttp();
    }

    private void initHttp() {
        addToCompositeDis(HttpUtil.getKnowBase().subscribe(new Consumer<KnowMaxListData>() {
            @Override
            public void accept(@NonNull KnowMaxListData bean) throws Exception {
                if (!getHttpResSuc(bean.getCode())) {
                    initHttp();
                    return;
                }
                if (!bean.getData().isEmpty()) {
                    knowDatas = bean.getData();
                    refreshUi();
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                initHttp();
            }
        }));
    }

    @Override
    protected void refreshUi() {
        super.refreshUi();
        if (knowDatas == null || knowDatas.isEmpty()) return;
        for (int i = 0; i < knowDatas.size(); i++) {
            KnowMaxListData.DataBean  data = knowDatas.get(i);
            if (i == 0) {
                setText(logicDesTxt,data);
            } else if (i == 1) {
                setText(grammarDesTxt, data);
            } else if (i == 2) {
                setText(readDesTxt, data);
            } else if (i == 3) {
                setText(mathDesTxt, data);
            }
        }

    }

    private void setText(TextView tv, KnowMaxListData.DataBean data) {
        tv.setText(HtmlUtil.fromHtml(getString(R.string.str_know_people_num_des, data.getCount(), data.getNum())));
    }

    @OnClick({R.id.know_login_container, R.id.know_math_container, R.id.know_grammer_container, R.id.know_read_container})
    public void onClick(View v) {
        if (knowDatas == null || knowDatas.isEmpty()) {
            return;
        }
        switch (v.getId()) {
            case R.id.know_login_container://听力
                    KnowTypesActivity.startKnow(getActivity(),   0);
//                KnowTypeActivity.startKnow(getActivity(), this.getResources().getString(R.string.str_practice_listen));
                break;
            case R.id.know_grammer_container://口语
                if (knowDatas.size() >= 2)
                    KnowTypesActivity.startKnow(getActivity(),  1);
//                     KnowTypeActivity.startKnow(getActivity(), this.getResources().getString(R.string.str_practice_voice));
                break;
            case R.id.know_read_container://写作
                if (knowDatas.size() >= 3)
                    KnowTypesActivity.startKnow(getActivity(),  2);
//                    KnowTypeActivity.startKnow(getActivity(), this.getResources().getString(R.string.str_practice_read));
                break;
            case R.id.know_math_container://阅读
                if (knowDatas.size() >= 4)
                    KnowTypesActivity.startKnow(getActivity(),  3);
//                    KnowTypeActivity.startKnow(getActivity(), this.getResources().getString(R.string.str_practice_write));
                break;
            default:
                break;
        }
    }

}
