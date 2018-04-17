package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.prelesson.TeacherItemBean;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.prelesson.PreProGramLessonActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;


public class TeachersAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private List<TeacherItemBean> data;


    private OnItemClickListener mListener;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public TeachersAdapter(List<TeacherItemBean> data) {
        this.data = data;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_item_layout, parent, false);
        mCardAdapterHelper.onTeacherCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        final Context context = holder.itemView.getContext();
//        if(position >= 2){
//            holder.itemView.setVisibility(View.GONE);
//        }else
            {
            final TeacherItemBean teacherData = data.get(position);
            GlideUtil.load(RetrofitProvider.TOEFLURL + teacherData.getImage(), holder.getImageView(R.id.teacher_item_img));
            holder.getTextView(R.id.teacher_title_tv).setText(teacherData.getName());
            holder.getTextView(R.id.teacher_item_number).setText(context.getString(R.string.str_teacher_lesson_number, teacherData.getViewCount()));
            holder.getTextView(R.id.teacher_des_tv).setText(HtmlUtil.fromHtml(teacherData.getDescription()).toString());
            holder.getTextView(R.id.teacher_item_course).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (teacherData != null && !TextUtils.isEmpty(teacherData.getId())) {
                        PreProGramLessonActivity.startPre(context, teacherData.getId(), teacherData.getTitle());
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v, position);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
