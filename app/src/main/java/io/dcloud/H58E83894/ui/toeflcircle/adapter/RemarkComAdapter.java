package io.dcloud.H58E83894.ui.toeflcircle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.ToeflApplication;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewHolder;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.http.RetrofitProvider;
import io.dcloud.H58E83894.utils.GlideUtil;
import io.dcloud.H58E83894.utils.TimeUtils;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.commentview.span.SpannableClickable;
import io.dcloud.H58E83894.weiget.commentview.utils.UrlUtils;

public class RemarkComAdapter extends BaseRecyclerViewAdapter<RemarkData.ReplyBean> {

    public RemarkComAdapter(Context context, List<RemarkData.ReplyBean> data) {
        super(context, data);
    }

    @Override
    public int bindItemViewLayout(int viewType) {
        return R.layout.remark_detail_item_layout;
    }

    @Override
    public int getEveryItemViewType(int position) {
        return 0;
    }

    @Override
    public void bindItemViewData(BaseRecyclerViewHolder holder, int position, RemarkData.ReplyBean itemData) {
        String icon = Utils.split(itemData.getUserImage());
        String headUrl = RetrofitProvider.BASEURL + icon;
        GlideUtil.loadDefault(headUrl, holder.getImageView(R.id.remark_detail_item_head_img), false, DecodeFormat.PREFER_ARGB_8888, DiskCacheStrategy.RESULT);
        holder.getTextView(R.id.remark_detail_item_time).setText(TimeUtils.longToString(Long.parseLong(itemData.getCreateTime()) * 1000, "yyyy.MM.dd HH:mm:ss"));

//        int itemSelectorColor = mContext.getResources().getColor(R.color.praise_item_selector_default);
//        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);
        final RemarkData.ReplyBean bean = itemData;
        String name = bean.getUName();
        String id = bean.getUid();
        String toReplyName = "";
        if (!TextUtils.isEmpty(bean.getReplyUserName())) {
            toReplyName = bean.getReplyUserName();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setClickableSpan(name, id));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" 回复 ");
            builder.append(setClickableSpan(toReplyName, bean.getReplyUser()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        holder.getTextView(R.id.remark_detail_item_user_name).setText(builder);

//        holder.getTextView(R.id.remark_detail_item_user_name).setMovementMethod(circleMovementMethod);
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String id) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(mContext.getResources().getColor(R.color.color_orange)) {
                                    @Override
                                    public void onClick(View widget) {
                                        Toast.makeText(ToeflApplication.getInstance(), textStr + " &id = " + id, Toast.LENGTH_SHORT).show();
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }
}
