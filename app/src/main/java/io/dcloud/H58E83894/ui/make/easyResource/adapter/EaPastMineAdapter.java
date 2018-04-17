package io.dcloud.H58E83894.ui.make.easyResource.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.caimuhao.rxpicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.DownloadData;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.data.make.OnlyMineData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.dcloud.H58E83894.weiget.ExpandTextView;
import zlc.season.rxdownload2.RxDownload;


public class EaPastMineAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private int indexs;
//    List<DownloadData> dataList = new ArrayList<>();
    private List<OnlyMineData.DataBean> share;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private VoiceManager voiceManager;
    private int lastPosition = -1;
    private Context context;
    private List<ImageItem> upImage;


    public EaPastMineAdapter(List<OnlyMineData.DataBean> share, Context context, RxPermissions rxPermissions) {
        this.share = share;
        this.context = context;
        mRxDownload = RxDownload.getInstance(context);
        this.mRxPermissions = rxPermissions;
        voiceManager = VoiceManager.getInstance(context);
    }


    public List<OnlyMineData.DataBean> getDate() {
        return share;
    }

    public void update(List<OnlyMineData.DataBean> lists) {
        if (lists == null || lists.isEmpty()) return;
        share = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.eapast_mine_item, parent, false);
        return new BaseRecyclerViewHolder(parent.getContext(), itemView);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, final int position) {

        final OnlyMineData.DataBean itemData = share.get(position);
        if(itemData.toString().equals("null") || TextUtils.isEmpty(itemData.toString())){ return;}
        String times =(itemData.getCreateTime()).substring(0, 10);
        holder.getTextView(R.id.today_free).setText(times+"作文批改");//日期批改
        holder.getTextView(R.id.today_speak_title).setText(itemData.getQuestion());//标题
        UserData data = GlobalUser.getInstance().getUserData();
        if (!TextUtils.isEmpty(itemData.getImage())) {
            GlideUtil.load(RetrofitProvider.TOEFLURL + itemData.getImage(), holder.getCircleImageView(R.id.speak_answer_item_head_img));
        }else {
            GlideUtil.load(RetrofitProvider.TOEFLURL + data.getImage(), holder.getCircleImageView(R.id.speak_answer_item_head_img));
        }

        holder.getTextView(R.id.speak_answer_item_name).setText(itemData.getName());

        if(TextUtils.isEmpty(itemData.getAlternatives()) || itemData.getAlternatives().length() == 0){return;}
        if (Integer.parseInt(itemData.getAlternatives()) == 2){
            holder.getView(R.id.linear).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.corret_teacher).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.txt_content).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.speak_item_praise_tv).setText(itemData.getArticle()+"分");//老师的点评
            holder.getTextView(R.id.txt_contents).setText(itemData.getDescription());//老师的点评
            holder.getTextView(R.id.txt_teacher).setText(itemData.getA());//老师
            holder.getTextView(R.id.speak_item_praise_tv).setTextColor(context.getResources().getColor(R.color.color_sup_red));
        }else if (Integer.parseInt(itemData.getAlternatives()) == 1){
            holder.getTextView(R.id.speak_item_praise_tv).setText("点评中");//老师的点评
            holder.getTextView(R.id.txt_contents).setVisibility(View.GONE);//老师的点评
            holder.getView(R.id.linear).setVisibility(View.GONE);
            holder.getTextView(R.id.txt_teacher).setVisibility(View.GONE);//老师
        }else {
            holder.getView(R.id.linear).setVisibility(View.GONE);
        }

        final String url = RetrofitProvider.TOEFLURL + itemData.getAnswer();
        holder.getTextView(R.id.speaks_answer_item_name).setOnClickListener(new View.OnClickListener() {//查看图片
            @Override
            public void onClick(View v) {
                List<String> photoUrls = new ArrayList<>();
                    photoUrls.add(url);

                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(width, height);
                ImagePagerActivity.startImagePagerActivity(context, photoUrls, position, imageSize);
            }
        });


    }

    @Override
    public int getItemCount() {
        return share == null ? 0 : share.size();
    }

}
