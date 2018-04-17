package io.dcloud.H58E83894.ui.make.easyResource.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.data.make.AllTaskData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.media.MediaUtil;
import io.dcloud.H58E83894.utils.media.VoiceManager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadRecord;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

/**
 * Created by fire on 2017/7/28  17:25.
 */

public class EaAllCorretDeAdapter extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<AllTaskData.DataBean.DetailsBean> shares;
    private RxDownload mRxDownload;
    private RxPermissions mRxPermissions;
    private VoiceManager voiceManager;
    private int lastPosition = -1;
    private Context context;

    public EaAllCorretDeAdapter(List<AllTaskData.DataBean.DetailsBean> share, Context context, RxPermissions rxPermissions) {
        this.shares = share;
        this.context = context;
        mRxDownload = RxDownload.getInstance(context);
        this.mRxPermissions = rxPermissions;
        voiceManager = VoiceManager.getInstance(context);
    }

    public List<AllTaskData.DataBean.DetailsBean> getDate() {
        return shares;
    }

    public void update(List<AllTaskData.DataBean.DetailsBean> lists) {
        if (lists == null || lists.isEmpty()) return;
        shares = lists;
        notifyDataSetChanged();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext()).inflate(R.layout.easpeak_corret_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        final AllTaskData.DataBean.DetailsBean share = shares.get(position);
        String nickname = share.getAuthor();
        UserData data = GlobalUser.getInstance().getUserData();
        if (TextUtils.isEmpty(nickname)) {

            nickname = data.getUserName();
        }
        /**
         * 图像等后台数据
         * */
        GlideUtil.load(RetrofitProvider.TOEFLURL + share.getImage(), holder.getCircleImageView(R.id.speak_answer_item_head_img));
        holder.getTextView(R.id.speak_answer_item_name).setText(nickname);

        final TextView praise = holder.getTextView(R.id.speak_item_praise_tv);
        if(Integer.parseInt(share.getStatus().trim()) == 1){
            praise.setText("点评中");
            holder.getTextView(R.id.corret_teacher).setVisibility(View.GONE);
            holder.getTextView(R.id.txt_content).setVisibility(View.GONE);
            holder.getView(R.id.linear).setVisibility(View.GONE);
        }else if(Integer.parseInt(share.getStatus().trim()) == 2) {
            praise.setText(share.getScore()+"分");
            holder.getView(R.id.linear).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.corret_teacher).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.txt_content).setVisibility(View.VISIBLE);
            praise.setTextColor(context.getResources().getColor(R.color.color_sup_red));
            holder.getTextView(R.id.corret_teacher).setText(share.getTeacher()+"老师评语：");
            holder.getTextView(R.id.corret_teacher).setTextColor(context.getResources().getColor(R.color.color_text_green));
            holder.getTextView(R.id.txt_content).setText(share.getComment());
        }else {
            holder.getView(R.id.linear).setVisibility(View.GONE);
        }


        final String url = RetrofitProvider.TOEFLURL + share.getAnswer();

        holder.getTextView(R.id.speaks_answer_item_name).setOnClickListener(new View.OnClickListener() {//查看图片
            @Override
            public void onClick(View v) {
                List<String> photoUrls = new ArrayList<>();
//                for (int i = 0, size = (upImage.size() - 1); i < size; i++) {
//                    ImageItem img = upImage.get(i);
                photoUrls.add(url);
//                }
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
        return shares == null ? 0 : shares.size();
    }
}
