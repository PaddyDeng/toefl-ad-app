package io.dcloud.H58E83894.ui.prelesson.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import io.dcloud.H58E83894.utils.MeasureUtil;

public class CardHotAdapterHelper {
    private int mPagePadding = 8;
    private int mShowLeftCardWidth = 8;
    private int mShowLeftCardLengh = 8;

    public void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = (parent.getWidth() - MeasureUtil.dip2px(itemView.getContext(), (/*mPagePadding + */mShowLeftCardWidth))) * 15 / 32;
        lp.height = (parent.getHeight() - MeasureUtil.dip2px(itemView.getContext(), (/*mPagePadding + */mShowLeftCardLengh))) * 15 / 32;
        itemView.setLayoutParams(lp);
    }

    public void onTeacherCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = (parent.getWidth() - MeasureUtil.dip2px(itemView.getContext(), (mShowLeftCardWidth))) * 7/8;
        lp.height = (parent.getHeight() - MeasureUtil.dip2px(itemView.getContext(), (mShowLeftCardLengh))) * 7/8;
        itemView.setLayoutParams(lp);
    }

    public void onDubCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = (parent.getWidth() - MeasureUtil.dip2px(itemView.getContext(), (mShowLeftCardWidth))) * 20/48;
        lp.height = (parent.getHeight() - MeasureUtil.dip2px(itemView.getContext(), (mShowLeftCardLengh))) * 8/8;
        itemView.setLayoutParams(lp);
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
//        int padding = MeasureUtil.dip2px(itemView.getContext(), mPagePadding);
        int margin = MeasureUtil.dip2px(itemView.getContext(), mShowLeftCardWidth);
        int heigh = MeasureUtil.dip2px(itemView.getContext(), mShowLeftCardLengh);
        int topMarin = heigh;
        int bottomMarin = heigh;
//        itemView.setPadding(padding, 0, padding, 0);
        int leftMarin = margin;//position == 0 ? margin /*+ ScreenUtil.dip2px(itemView.getContext(), mShowLeftCardWidth)*/ : 0;
        int rightMarin = position == itemCount - 1 ? margin/* + ScreenUtil.dip2px(itemView.getContext(), mShowLeftCardWidth)*/ : 0;
        setViewMargin(itemView, leftMarin, topMarin, rightMarin, bottomMarin);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    public void setPagePadding(int pagePadding) {
        mPagePadding = pagePadding;
    }

    public void setShowLeftCardWidth(int showLeftCardWidth) {
        mShowLeftCardWidth = showLeftCardWidth;
    }
}
