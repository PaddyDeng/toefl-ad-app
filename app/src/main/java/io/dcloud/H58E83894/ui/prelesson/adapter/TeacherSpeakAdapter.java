package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;

/**
 * Created by fire on 2017/7/21  17:10.
 */

public class TeacherSpeakAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<String> mList;

    public TeacherSpeakAdapter(List<String> list) {
        mList = list;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_speak_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.getTextView(R.id.teacher_speak_item_tv).setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
