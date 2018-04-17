package io.dcloud.H58E83894.ui.toeflcircle.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.circle.PhotoInfo;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.ui.toeflcircle.ImagePagerActivity;
import io.dcloud.H58E83894.ui.toeflcircle.RemarkNewMsgActivity;
import io.dcloud.H58E83894.ui.toeflcircle.listener.PriaseListener;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.commentview.CommentListView;
import io.dcloud.H58E83894.weiget.commentview.MultiImageView;

public class RemarkAdapter extends BaseRecyclerViewAdapter<RemarkData> {

    public RemarkAdapter(Context context, List<RemarkData> data) {
        super(context, data);
    }

    public RemarkAdapter(Context context, List<RemarkData> data, RecyclerView.LayoutManager mLayoutManager) {
        super(context, data, mLayoutManager);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        if (viewType == 100) {
            return R.layout.remark_item_layout;
        } else if (viewType == 200) {
            return R.layout.remark_replay_item_layout;//返回另一个布局
        }
        return 0;
    }

    @Override
    public int getEveryItemViewType(int position) {
        if (position == 0 && !TextUtils.isEmpty(mData.get(position).getRemarkNum())) {
            return 200;
        }
        return 100;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, final RemarkData itemData) {
        itemData.setRecyclePosition(position);
        final Context context = holder.itemView.getContext();
        int type = getEveryItemViewType(position);
        if (type == 200) {
            holder.getTextView(R.id.remakr_list_item_reply).setText(context.getString(R.string.str_remark_reply, itemData.getRemarkNum()));
            holder.getView(R.id.remark_new_msg_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去新消息页
                    context.startActivity(new Intent(context, RemarkNewMsgActivity.class));
                }
            });
        } else if (type == 100) {
            TextView remarkTitle = holder.getTextView(R.id.remark_title);
            TextView remarkContent = holder.getTextView(R.id.remark_content);
            //加载头像
            String icon = Utils.split(itemData.getIcon());
            String headUrl = RetrofitProvider.TOEFLURL + icon;
            GlideUtil.load(headUrl, holder.getImageView(R.id.remark_head_img));
            holder.getTextView(R.id.remark_user_name).setText(itemData.getPublisher());
            holder.getTextView(R.id.remark_time).setText(TimeUtils.longToString(Long.parseLong(itemData.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));
            if (TextUtils.isEmpty(itemData.getTitle())) {
                Utils.setGone(remarkTitle);
            } else {
                remarkTitle.setText(itemData.getTitle());
                remarkTitle.setMaxLines(1);
                remarkTitle.setEllipsize(TextUtils.TruncateAt.END);
                Utils.setVisible(remarkTitle);
            }
            if (TextUtils.isEmpty(itemData.getContent())) {
                Utils.setGone(remarkContent);
            } else {
                remarkContent.setText(itemData.getContent().replace("&nbsp;", " "));
                remarkContent.setMaxLines(3);
                remarkContent.setEllipsize(TextUtils.TruncateAt.END);
                Utils.setVisible(remarkContent);
            }
            holder.getTextView(R.id.remark_comm_write).setText(String.valueOf(itemData.getReply().size()));
            holder.getTextView(R.id.remark_comm_write).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalUser.getInstance().isAccountDataInvalid()) {
                        Utils.toastShort(mContext, R.string.str_no_login_tip);
                        return;
                    }
                    ((MainActivity) mContext).showOrHideEt(View.VISIBLE, itemData, 0, false);
                }
            });

            holder.getTextView(R.id.remark_comm_praise).setText(itemData.getLikeNum());

            holder.getTextView(R.id.remark_comm_praise).setSelected(itemData.isLikeId());

            holder.getTextView(R.id.remark_comm_praise).setOnClickListener(new PriaseListener(mContext, itemData));
//        List<String> images = mRemarkData.getImage();
            Object obj = itemData.getImage();
            List<String> images = new ArrayList<>();
            if (obj != null) {
                if (obj instanceof String) {
                    images.add((String) obj);
                } else if (obj instanceof List) {
                    images.addAll((List<String>) obj);
                }
            }
            if (images != null && !images.isEmpty()) {
                Utils.setVisible(holder.getMultiImageView(R.id.remark_multiImagView));
                final List<PhotoInfo> photos = new ArrayList<>();
                for (String url : images) {
                    PhotoInfo pInfo = new PhotoInfo();
                    pInfo.url = RetrofitProvider.GOSSIPURL + Utils.split(url);
                    photos.add(pInfo);
                }
                holder.getMultiImageView(R.id.remark_multiImagView).setList(photos);
                holder.getMultiImageView(R.id.remark_multiImagView).setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ImagePagerActivity.ImageSize imageSize = new ImagePagerActivity.ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());

                        List<String> photoUrls = new ArrayList<String>();
                        for (PhotoInfo photoInfo : photos) {
                            photoUrls.add(photoInfo.url);
                        }
                        ImagePagerActivity.startImagePagerActivity(mContext, photoUrls, position, imageSize);
                    }
                });
            } else {
                Utils.setGone(holder.getMultiImageView(R.id.remark_multiImagView));
            }
            if (itemData.getReply() == null || itemData.getReply().isEmpty()) {
                Utils.setGone(holder.getCommLV(R.id.remark_comment_list), holder.getView(R.id.remark_conn_line));
            } else {
                Utils.setVisible(holder.getCommLV(R.id.remark_comment_list), holder.getView(R.id.remark_conn_line));
            }
            holder.getCommLV(R.id.remark_comment_list).setDatas(itemData.getReply());
            holder.getCommLV(R.id.remark_comment_list).setOnItemClickListener(new CommentListView.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    if (GlobalUser.getInstance().isAccountDataInvalid()) {
                        Utils.toastShort(mContext, R.string.str_no_login_tip);
                        return;
                    }
                    UserData userData = GlobalUser.getInstance().getUserData();
                    List<RemarkData.ReplyBean> reply = itemData.getReply();
                    if (reply != null && !reply.isEmpty()) {
                        if (TextUtils.equals(String.valueOf(userData.getUid()), reply.get(position).getUid())) {
                            //不能自己回复自己
                            Utils.toastShort(mContext, R.string.str_no_replay_yourself);
                        } else {
                            ((MainActivity) mContext).showOrHideEt(View.VISIBLE, itemData, position);
                        }
                    }

                }
            });
        }

    }

}
