package io.dcloud.H58E83894.ui.center.leidou;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.base.adapter.RecycleViewLinearDivider;
import io.dcloud.H58E83894.base.adapter.callback.OnItemClickListener;
import io.dcloud.H58E83894.data.LeidouReData;
import io.dcloud.H58E83894.data.prelesson.TeacherItemBean;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.prelesson.PreProGramLessonActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.HtmlUtil;


public class LeidouReAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

//    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();

    private boolean isHide;//隐藏
    private boolean isOpen;//展开
    private Context context;
    private List<LeidouReData.DetailsBean> luckyCodeHideList;
    private List<LeidouReData.DetailsBean> luckyCodeOpenList;
    private List<LeidouReData.DetailsBean> data;


    private OnItemClickListener mListener;
    private int index;

    public void setItemListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public LeidouReAdapter(Context context, List<LeidouReData.DetailsBean> data, int index) {
        this.data = data;
        this.context = context;
        this.index = index;
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.init_recycler_leidou, parent, false);
//        mCardAdapterHelper.onTeacherCreateViewHolder(parent, itemView);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {


        final Context context = holder.itemView.getContext();
        if(!TextUtils.isEmpty(data.get(position).toString()) && !data.get(position).toString().equals("null")){
            final LeidouReData.DetailsBean teacherData = data.get(position);

            holder.getTextView(R.id.title_name).setText(teacherData.getBehavior());
            holder.getTextView(R.id.title_time).setText(teacherData.getCreateTime());
            if(teacherData.getType().equals("1")){
                holder.getTextView(R.id.title_num).setText("+"+teacherData.getIntegral());
                holder.getTextView(R.id.title_num).setTextColor(context.getResources().getColor(R.color.color_text_green));
            }else {
                holder.getTextView(R.id.title_num).setText("-"+teacherData.getIntegral());
            }
        }


        }

    @Override
    public int getItemCount() {

        if( index == 0){
            if(data.size() <4){
                return data.size();
            }else {
                return 4;
            }

        }else {
            return data == null ? 0 : data.size();
        }
    }


    private HideOrShowCallBack hideOrShowCallBack;

    public void setHideOrShowCallBack(HideOrShowCallBack hideOrShowCallBack) {
        this.hideOrShowCallBack = hideOrShowCallBack;
    }

    public interface HideOrShowCallBack {
        void hide();

        void open();
    }

}
