package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.make.AnswerData;
import io.dcloud.H58E83894.utils.C;

/**
 * Created by fire on 2017/7/18  11:51.
 */

public class AnswerAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private List<AnswerData> mDataList;

    private OnItemClickListener mListener;

    private String correctAnswer;
    private String userAnswer;

    public void setAnswer(String correctAnswer, String userAnswer) {
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
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

    public AnswerAdapter(List<AnswerData> dataList) {
        mDataList = dataList;
    }

    public void upDate(List<AnswerData> mdatas) {
        if (mdatas == null) return;
        correctAnswer = "";
        userAnswer = "";
        mDataList = mdatas;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), View.inflate(parent.getContext(), R.layout.answer_item_layout, null));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        final AnswerData data = mDataList.get(position);
        TextView optionTv = holder.getTextView(R.id.answer_option_tv);
        TextView contentTv = holder.getTextView(R.id.answer_content_tv);
        optionTv.setText(data.getOption());
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
                if (control != C.DEFALUT) {
                    return;
                }
                data.setSelected(true);
                for (AnswerData ansData : mDataList) {
                    if (ansData == data && ansData.isSelected()) {
                        continue;
                    } else {
                        ansData.setSelected(false);
                    }
                }
                if (mListener != null) {
                    mListener.onClick(v, position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
