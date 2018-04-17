package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.Utils;

/**
 * Created by fire on 2017/8/4  15:15.
 */

public class ReadAnswerAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private final int DEFALUT_VIEW_TYPE = 0;
    private final int MULT_CHOOSE_VIEW_TYPE = DEFALUT_VIEW_TYPE + 1;

    private List<AnswerData> mDataList;
    List<Integer> useList;

    private OnItemClickListener mListener;

    private int index = 1;//多选序号
    private int viewType = 0;
    private int multTypeLen;//type为七，记录选项答案
    private String[] options = new String[]{"A", "B", "C", "D", "E"};
    private boolean showAnswer;
//    private

    public ReadAnswerAdapter(List<AnswerData> list, boolean b, int length) {
        this(list, b);
        multTypeLen = length;
    }


    private void setDefalutViewType() {
        this.viewType = DEFALUT_VIEW_TYPE;
    }

    private void setMultChooseViewType() {
        this.viewType = MULT_CHOOSE_VIEW_TYPE;
    }

    //ABC
    public void setMultChoose(String correctAnswer, String userAnser) {
        setDefalutViewType();
        Utils.logh("correctAnswer  ", correctAnswer);
        Utils.logh("userAnswer  ", userAnser);

        int size = correctAnswer.length();
        List<String> correct = new ArrayList<>();
        List<String> userAS = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            correct.add(correctAnswer.substring(i, i + 1));
        }

        int userAnswerSize = userAnser.length();
        for (int i = 0; i < userAnswerSize; i++) {
            userAS.add(userAnser.substring(i, i + 1));
        }

        for (AnswerData data : mDataList) {
            String option = data.getOption();
            if (correctAnswer.contains(option)) {
                data.setChooseSer(correct.indexOf(option) + 1);
                data.setControl(C.CORRECT);
            } else if (userAnser.contains(option)) {
                data.setChooseSer(userAS.indexOf(option) + 1);
                data.setControl(C.ERROR);
            }
        }
        notifyDataSetChanged();
    }

    //type=7的只显示正确答案
    public void setAnswer(String currectAnswer) {
        showAnswer = true;
        setMultChooseViewType();
        String[] ca = null;
        if (currectAnswer.contains("\\r\\n")) {
            ca = Utils.splitOption(currectAnswer);
        } else {
            ca = Utils.splitOptionThroughN(currectAnswer);
        }

        for (AnswerData data : mDataList) {
            if (currectAnswer.contains(data.getOption())) {
                data.setControl(C.CORRECT);
                for (int i = 0, size = ca.length; i < size; i++) {
                    String c = ca[i];
                    if (c.contains(data.getOption())) {
                        data.setReadMultCorrectOption(options[i]);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setAnswer(String correctAnswer, String userAnswer) {
        setDefalutViewType();
        boolean chooseCorrect = false;
        if (TextUtils.equals(correctAnswer, userAnswer)) {
            chooseCorrect = true;
        }
        for (AnswerData data : mDataList) {
            String option = data.getOption();
            if (TextUtils.equals(option, correctAnswer)) {
                data.setControl(C.CORRECT);
            } else if (!chooseCorrect && TextUtils.equals(option, userAnswer)) {
                data.setControl(C.ERROR);
            }
        }
        notifyDataSetChanged();
    }

    public void setListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public ReadAnswerAdapter(List<AnswerData> dataList, boolean isDefalutViewType) {
        mDataList = dataList;
        index = 1;
        if (isDefalutViewType)
            setDefalutViewType();
        else
            setMultChooseViewType();
    }

    public ReadAnswerAdapter(List<AnswerData> dataList) {
        this(dataList, true);
    }

    public ReadAnswerAdapter(List<AnswerData> dataList, List<Integer> userList) {
        this(dataList, true);
        useList = userList;
    }

    public void upMultData(List<AnswerData> mdatas, int length) {
        if (mdatas == null) return;
        setMultChooseViewType();
        multTypeLen = length;
        mDataList = mdatas;
        notifyDataSetChanged();
    }

    public void upDate(List<AnswerData> mdatas) {
        if (mdatas == null) return;
        setDefalutViewType();
        index = 1;
        mDataList = mdatas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType == DEFALUT_VIEW_TYPE ? DEFALUT_VIEW_TYPE : MULT_CHOOSE_VIEW_TYPE;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (DEFALUT_VIEW_TYPE == viewType)
            return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.answer_item_layout, null));
        else
            return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.listen_answer_form_item_layout, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        final AnswerData data = mDataList.get(position);
        if (getItemViewType(position) == DEFALUT_VIEW_TYPE)
            ordinaryChoose(holder, position, context, data);
        else
            multChoose(holder, context, data);
    }

    private void multChoose(BaseRecyclerViewHolder holder, final Context context, final AnswerData data) {
        TextView contentTv = holder.getTextView(R.id.listen_form_answer_content);
        contentTv.setText(data.getContent());

        LinearLayout mContainer = holder.getView(R.id.listen_form_answer_container);
        mContainer.removeAllViews();

        for (int i = 0; i < multTypeLen; i++) {
            final TextView tv = new TextView(context);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tv.setPadding(0, 8, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.leftMargin = 50;
            tv.setLayoutParams(params);
            final String option = options[i];
            String correctOption = data.getReadMultCorrectOption();
            if (TextUtils.equals(option, correctOption)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_dark_gray));
            }
            tv.setText(option);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (showAnswer) return;
                        String answer = data.getReadMultAnswer();
                        String optionAnswer = ((TextView) v).getText().toString();
                        Log.i("kkkkk", optionAnswer);
                        if (TextUtils.isEmpty(answer)) {
                            data.setReadMultAnswer(optionAnswer);
                            ((TextView) v).setTextColor(ContextCompat.getColor(context, R.color.color_sec_orange));
                        } else if (TextUtils.equals(answer, optionAnswer)) {
                            data.setReadMultAnswer("");
                            ((TextView) v).setTextColor(ContextCompat.getColor(context, R.color.color_dark_gray));
                        } else {
                            recordAnswer(v, data, context);
                        }
                    }
                });
            mContainer.addView(tv);
        }
    }

    private void recordAnswer(View v, AnswerData data, Context context) {
        LinearLayout parent = (LinearLayout) v.getParent();
        int count = parent.getChildCount();
        if (count > 0) {
            int index = 0;
            while (index < count) {
                TextView at = (TextView) parent.getChildAt(index);
                if (at == v) {
                    data.setReadMultAnswer(at.getText().toString());
                    at.setTextColor(ContextCompat.getColor(context, R.color.color_sec_orange));
                } else {
                    at.setTextColor(ContextCompat.getColor(context, R.color.color_dark_gray));
                }
                index++;
            }
        }
    }

    private void ordinaryChoose(BaseRecyclerViewHolder holder, final int position, Context context, final AnswerData data) {
        final TextView optionTv = holder.getTextView(R.id.answer_option_tv);
        TextView contentTv = holder.getTextView(R.id.answer_content_tv);
        if (Utils.isMultChosse(data.getType()) && data.getChooseSer() != 0) {
            optionTv.setText(String.valueOf(data.getChooseSer()));
        } else {
            optionTv.setText(data.getOption());
        }
        contentTv.setText(data.getContent());
        optionTv.setBackgroundResource(R.drawable.option_defalut_bg);
        contentTv.setTextColor(ContextCompat.getColorStateList(context, R.color.color_answer_selector));

        final int control = data.getControl();
        if (control == C.CORRECT) {
            optionTv.setBackgroundResource(R.drawable.single_sup_circle_green_shape);
            contentTv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
        } else if (control == C.ERROR) {
            optionTv.setBackgroundResource(R.drawable.single_sup_circle_red_shape);
            contentTv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_red));
        } else {
            optionTv.setSelected(data.isSelected());
            contentTv.setSelected(data.isSelected());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isMultChosse(data.getType())) {//多选
                    if (data.getControl() != C.DEFALUT) {
                        return;
                    }
                    if (data.isSelected()) {
                        index--;
                        data.setChooseSer(0);//双击取消选择
                    } else {
                        data.setChooseSer(index++);
                    }
                    data.setSelected(!data.isSelected());

                } else {//单选
                    if (singleChoose(control, data)) return;
                }
                if (mListener != null) {
                    mListener.onClick(v, position);
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 单选
     */
    private boolean singleChoose(int control, AnswerData data) {
        if (control != C.DEFALUT) {
            return true;
        }
        data.setSelected(true);
        for (AnswerData ansData : mDataList) {
            if (ansData == data && ansData.isSelected()) {
                continue;
            } else {
                ansData.setSelected(false);
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
