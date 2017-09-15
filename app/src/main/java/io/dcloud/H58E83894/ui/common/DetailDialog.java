package io.dcloud.H58E83894.ui.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.data.make.ReadArticle;
import io.dcloud.H58E83894.data.make.ReadQuestion;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.utils.HtmlUtil;
import io.dcloud.H58E83894.utils.MeasureUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.CustomerWebView;
import io.dcloud.H58E83894.weiget.overscroll.FastAndOverScrollScrollView;

/**
 * Created by fire on 2017/7/26  15:22.
 */

public class DetailDialog extends BaseDialogView {
    @BindView(R.id.analyze_tv)
    TextView contentTv;
    @BindView(R.id.dialog_title_txt)
    TextView titleTv;
    @BindView(R.id.detail_close_dialog)
    ImageView closeIv;
    @BindView(R.id.detail_dialog_general_view)
    CustomerWebView mCustomerView;
    @BindView(R.id.detail_scroll_view_container)
    FastAndOverScrollScrollView mContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setWindowAnimations(R.style.analyze_anim_style);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static DetailDialog getInstance(String title, ReadQuestionData readQuestion) {
        DetailDialog detaildialog = new DetailDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("READQUESTION", readQuestion);
        bundle.putString("TITLE", title);
        detaildialog.setArguments(bundle);
        return detaildialog;
    }

    public static DetailDialog getInstance(String content, String title) {
        DetailDialog detaildialog = new DetailDialog();
        Bundle bundle = new Bundle();
        bundle.putString("CONTENT", content);
        bundle.putString("TITLE", title);
        detaildialog.setArguments(bundle);
        return detaildialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void getArgs() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        String content = arguments.getString("CONTENT");
        String title = arguments.getString("TITLE");
        ReadQuestionData readquestion = arguments.getParcelable("READQUESTION");
        if (!TextUtils.isEmpty(content)) {
            contentTv.setText(HtmlUtil.fromHtml(content));
            Utils.setVisible(mContainer);
            Utils.setGone(mCustomerView);
        }
        if (readquestion == null) return;
        ReadArticle article = readquestion.getArticle();
        ReadQuestion question = readquestion.getQuestion();
        if (readquestion != null) {
            Utils.setVisible(mCustomerView);
            Utils.setGone(mContainer);
            String[] split = null;
            String optionStr = question.getAnswerA();
            if (optionStr.contains("\\r\\n")) {
                split = Utils.splitOption(optionStr);
            } else {
                split = Utils.splitOptionThroughN(optionStr);
            }
            mCustomerView.setText(article.getQuestion(), question.getPostionD(), question.getPostionW(), question.getQuestionType(), split);
        }
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
        }
    }

    @Override
    protected int getRootViewId() {
        return R.layout.dialog_detail;
    }


    @Override
    protected int[] getWH() {
        int[] wh = {MeasureUtil.getScreenSize(getActivity()).x, MeasureUtil.getScreenSize(getActivity()).y};
        return wh;
    }

    @Override
    protected boolean isNoTitle() {
        return true;
    }

}
