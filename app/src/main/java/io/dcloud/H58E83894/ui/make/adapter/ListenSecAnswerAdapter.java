package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.utils.C;

/**
 * Created by fire on 2017/8/8  15:44.
 */

public class ListenSecAnswerAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private final int DEFALUT_VIEW_TYPE = 0;
    private final int FORM_VIEW_TYPE = DEFALUT_VIEW_TYPE + 1;

    private List<AnswerData> mDataList;
    private String[] options = new String[]{"A", "B", "C", "D", "E"};

    private OnItemClickListener mListener;
    private boolean multChoose;
    private boolean sort;//默认不排序，为true才排序
    private int index = 1;//多选序号
    private int viewType = 0;
    //显示表单选择结果
    private boolean showFormResult;

    private void setDefalutViewType() {
        this.viewType = DEFALUT_VIEW_TYPE;
    }

    private void setFormViewType() {
        this.viewType = FORM_VIEW_TYPE;
    }

    public ListenSecAnswerAdapter(List<AnswerData> dataList, boolean isDefalutViewType) {
        index = 1;
        mDataList = dataList;
        if (isDefalutViewType)
            setDefalutViewType();
        else
            setFormViewType();
    }

    public ListenSecAnswerAdapter(List<AnswerData> dataList) {
        this(dataList, true);
    }

    public void setMultChooseAndSort(boolean multChoose, boolean sort) {
        this.multChoose = multChoose;
        this.sort = sort;
    }

    public void setFormAnswer(String correctAnswer, String userAnser) {
        showFormResult = true;
        for (int i = 0, size = mDataList.size(); i < size; i++) {
            AnswerData data = mDataList.get(i);
            String c = String.valueOf(correctAnswer.charAt(i));
            String at = String.valueOf(userAnser.charAt(i));
            data.setCorrectOption(c);
            if (TextUtils.equals(c, at)) {
                data.setControl(C.CORRECT);
            } else {
                data.setControl(C.ERROR);
                data.setErrorOption(at);
            }
        }
        notifyDataSetChanged();
    }

    public void setMultChooseAndSort(boolean multChoose, boolean sort, String correctAnswer, String userAnswer) {
        this.sort = sort;
        setMultChoose(multChoose, correctAnswer, userAnswer);
    }

    /**
     * set mult choose
     */
    public void setMultChoose(boolean multChoose, String correctAnswer, String userAnswer) {
        this.multChoose = multChoose;
        if (!multChoose) {
            setAnswer(correctAnswer, userAnswer);
            return;
        }
        //选择错误
        for (AnswerData data : mDataList) {
            String option = data.getOption();
            int indexOf = correctAnswer.indexOf(option);
            if (indexOf == -1) {
                data.setChooseSer(0);
            } else {
                data.setChooseSer(indexOf + 1);
                data.setControl(C.CORRECT);
            }
//            if (correctAnswer.contains(option)) {
//                data.setControl(C.CORRECT);
//            } else if (userAnswer.contains(option)) {
//                data.setControl(C.ERROR);
//            }
        }
        notifyDataSetChanged();
    }

    public void setAnswer(String correctAnswer, String userAnswer) {
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

    public void upDate(List<AnswerData> mdatas, boolean isDefalutViewType) {
        if (mdatas == null) return;
        if (isDefalutViewType)
            setDefalutViewType();
        else
            setFormViewType();
        index = 1;
        mDataList = mdatas;
        notifyDataSetChanged();
    }

    public void upDate(List<AnswerData> mdatas) {
        upDate(mdatas, true);
    }

    @Override
    public int getItemViewType(int position) {
        return viewType == DEFALUT_VIEW_TYPE ? DEFALUT_VIEW_TYPE : FORM_VIEW_TYPE;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DEFALUT_VIEW_TYPE)
            return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.answer_item_layout, null));
        else
            return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.listen_answer_form_item_layout, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {

        final AnswerData data = mDataList.get(position);
        Context context = holder.itemView.getContext();
        if (getItemViewType(position) == FORM_VIEW_TYPE) {
            setFormOption(holder, data, context);
        } else {
            defalutViewType(context, holder, position, data);
        }
    }

    private void setFormOption(BaseRecyclerViewHolder holder, final AnswerData data, final Context context) {
        TextView contentTv = holder.getTextView(R.id.listen_form_answer_content);
        contentTv.setText(data.getContent());
        if (data.getControl() == C.CORRECT) {
            contentTv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
        } else if (data.getControl() == C.ERROR) {
            contentTv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_red));
        } else {
            contentTv.setTextColor(ContextCompat.getColor(context, R.color.color_black));
        }
        LinearLayout mContainer = holder.getView(R.id.listen_form_answer_container);

        String option = data.getFormOption();
        if (TextUtils.isEmpty(option)) return;
        String[] split = option.split(",");
        mContainer.removeAllViews();

        for (int i = 0, size = split.length; i < size; i++) {
            String s = split[i];
            String opt = options[i];
            String correctOption = data.getCorrectOption();
            String errorOption = data.getErrorOption();
            final TextView tv = new TextView(context);
            tv.setText(opt + ":" + s);
            if (TextUtils.equals(correctOption, opt)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_green));
            } else if (TextUtils.equals(errorOption, opt)) {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_sup_red));
            } else {
                tv.setTextColor(ContextCompat.getColor(context, R.color.color_dark_gray));
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tv.setPadding(0, 8, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.leftMargin = 30;
            tv.setLayoutParams(params);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (showFormResult) return;
                    data.setSingleChooseAnswer(((TextView) v).getText().toString().substring(0, 1));
                    LinearLayout parent = (LinearLayout) v.getParent();
                    int count = parent.getChildCount();
                    if (count > 0) {
                        int index = 0;
                        while (index < count) {
                            TextView at = (TextView) parent.getChildAt(index);
                            if (at == v) {
                                at.setTextColor(ContextCompat.getColor(context, R.color.color_black));
                            } else {
                                at.setTextColor(ContextCompat.getColor(context, R.color.color_dark_gray));
                            }
                            index++;
                        }
                    }
                }
            });
            mContainer.addView(tv);
        }
    }

    private void defalutViewType(Context context, BaseRecyclerViewHolder holder, final int position, final AnswerData data) {
        TextView optionTv = holder.getTextView(R.id.answer_option_tv);
        TextView contentTv = holder.getTextView(R.id.answer_content_tv);
        if (multChoose && data.getChooseSer() != 0 && sort) {
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
                if (multChoose) {
                    if (data.isSelected()) {
                        index--;
                        data.setChooseSer(0);//双击取消选择
                    } else {
                        data.setChooseSer(index++);
                    }
                    data.setSelected(!data.isSelected());
                } else {
                    if (singleChoose(control, data)) return;
                }
                if (mListener != null) {
                    mListener.onClick(v, position);
                }
                notifyDataSetChanged();
            }
        });
    }

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