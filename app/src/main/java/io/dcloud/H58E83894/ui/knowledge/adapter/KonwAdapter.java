package io.dcloud.H58E83894.ui.knowledge.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.utils.HtmlUtil;

import static io.dcloud.H58E83894.weiget.DateUtils.timeslashData;


public class KonwAdapter extends BaseRecyclerViewAdapter<DownloadData> {

    private List<DownloadData> datas;
//    List<DownloadData> dataList = new ArrayList<>();
    public KonwAdapter(Context context, List<DownloadData> data, RecyclerView.LayoutManager mLayoutManager, int index) {
        super(context, data, mLayoutManager);
//        Log.d("ooo", "bindItemViewData1: "+index+indexs);
    }


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }


    private OnItemClickLitener mOnItemClickLitener;

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.item_konw;
    }

    @Override
    public int getEveryItemViewType(int position) {

            return 0;
    }

//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, DownloadData itemData) {


//           Log.d("ooo", "bindItemViewData: "+indexs);
//           if ( indexs == 0){//考试
               holder.getTextView(R.id.konw_detail).setText(itemData.getTitle());
//           }else if ( indexs == 1){//高分
//
//           }else if ( indexs == 2){//资料下载
//
//           }else if ( indexs== 3){//鸡精
//
//           }

    }
}
