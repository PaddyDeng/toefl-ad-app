package io.dcloud.H58E83894.ui.make.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.make.WriteTpoData;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.VerticalScrollImageView;

/**
 * Created by fire on 2017/7/28  09:45.
 */

public class SpeakTpoAdapter extends BaseRecyclerViewAdapter<WriteTpoData> {

    private Random mRandom;
    public SpeakTpoAdapter(Context context, List<WriteTpoData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.write_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, WriteTpoData itemData) {
        Context context = holder.itemView.getContext();
        String name = itemData.getCatName();
//        holder.getTextView(R.id.speak_tpo_item_title_tv).setText(name);
//        holder.getTextView(R.id.speak_tpo_item_des_tv).setText(context.getString(R.string.str_write_independent_practiec, itemData.getNum()));
//        VerticalScrollImageView vimg = holder.getView(R.id.vertical_scroll_img);


        int positins = position+1;
        if(positins<10){
            holder.getTextView(R.id.write_item_serial_num).setText("0"+positins);
        }else {
            holder.getTextView(R.id.write_item_serial_num).setText(""+positins);
        }

        holder.getTextView(R.id.write_item_title).setText(name);
//        int num = mRandom.nextInt(150) + 50 + Integer.valueOf(itemData.getNum());
        holder.getTextView(R.id.write_item_view_count).setText(context.getString(R.string.str_write_independent_practiec, itemData.getNum()));


    }
/*
    public void setImgView(VerticalScrollImageView view,Context context,int resId){
        Glide.with(context).load(resId).asBitmap().fitCenter().into(view);
    }
    public void setView(VerticalScrollImageView view,Context context, int position) {
        position = position % 45;
        switch (position) {
            case 0:setImgView(view,context,R.drawable.icon_speak_1);break;
            case 1:setImgView(view,context,R.drawable.icon_speak_2);break;
            case 2:setImgView(view,context,R.drawable.icon_speak_3);break;
            case 3:setImgView(view,context,R.drawable.icon_speak_4);break;
            case 4:setImgView(view,context,R.drawable.icon_speak_5);break;
            case 5:setImgView(view,context,R.drawable.icon_speak_6);break;
            case 6:setImgView(view,context,R.drawable.icon_speak_7);break;
            case 7:setImgView(view,context,R.drawable.icon_speak_8);break;
            case 8:setImgView(view,context,R.drawable.icon_speak_9);break;
            case 9:setImgView(view,context,R.drawable.icon_speak_10);break;
            case 10:setImgView(view,context,R.drawable.icon_speak_11);break;
            case 11:setImgView(view,context,R.drawable.icon_speak_12);break;
            case 12:setImgView(view,context,R.drawable.icon_speak_13);break;
            case 13:setImgView(view,context,R.drawable.icon_speak_14);break;
            case 14:setImgView(view,context,R.drawable.icon_speak_15);break;
            case 15:setImgView(view,context,R.drawable.icon_speak_16);break;
            case 16:setImgView(view,context,R.drawable.icon_speak_17);break;
            case 17:setImgView(view,context,R.drawable.icon_speak_18);break;
            case 18:setImgView(view,context,R.drawable.icon_speak_19);break;
            case 19:setImgView(view,context,R.drawable.icon_speak_20);break;
            case 20:setImgView(view,context,R.drawable.icon_speak_21);break;
            case 21:setImgView(view,context,R.drawable.icon_speak_22);break;
            case 22:setImgView(view,context,R.drawable.icon_speak_23);break;
            case 23:setImgView(view,context,R.drawable.icon_speak_24);break;
            case 24:setImgView(view,context,R.drawable.icon_speak_25);break;
            case 25:setImgView(view,context,R.drawable.icon_speak_26);break;
            case 26:setImgView(view,context,R.drawable.icon_speak_27);break;
            case 27:setImgView(view,context,R.drawable.icon_speak_28);break;
            case 28:setImgView(view,context,R.drawable.icon_speak_29);break;
            case 29:setImgView(view,context,R.drawable.icon_speak_30);break;
            case 30:setImgView(view,context,R.drawable.icon_speak_31);break;
            case 31:setImgView(view,context,R.drawable.icon_speak_32);break;
            case 32:setImgView(view,context,R.drawable.icon_speak_33);break;
            case 33:setImgView(view,context,R.drawable.icon_speak_34);break;
            case 34:setImgView(view,context,R.drawable.icon_speak_35);break;
            case 35:setImgView(view,context,R.drawable.icon_speak_36);break;
            case 36:setImgView(view,context,R.drawable.icon_speak_37);break;
            case 37:setImgView(view,context,R.drawable.icon_speak_38);break;
            case 38:setImgView(view,context,R.drawable.icon_speak_39);break;
            case 39:setImgView(view,context,R.drawable.icon_speak_40);break;
            case 40:setImgView(view,context,R.drawable.icon_speak_41);break;
            case 41:setImgView(view,context,R.drawable.icon_speak_42);break;
            case 42:setImgView(view,context,R.drawable.icon_speak_43);break;
            case 43:setImgView(view,context,R.drawable.icon_speak_44);break;
            default:break;
        }
    }
*/
}
