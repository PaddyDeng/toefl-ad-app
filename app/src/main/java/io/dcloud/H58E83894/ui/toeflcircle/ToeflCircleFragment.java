package io.dcloud.H58E83894.ui.toeflcircle;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TabLayout;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.base.adapter.TabPagerAdapter;
import io.dcloud.H58E83894.data.ResultBean;
import io.dcloud.H58E83894.data.circle.RemarkData;
import io.dcloud.H58E83894.data.user.GlobalUser;
import io.dcloud.H58E83894.data.user.UserData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.MeasureUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.commentview.CommentListView;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

<<<<<<< HEAD
//托福圈
public class ToeflCircleFragment extends BaseFragment {
    //顶部tablayout
    @BindView(R.id.new_reamrk_tablayout)
    TabLayout mTableLayout;
    //viewpager
    @BindView(R.id.new_remark_viewpager)
    ViewPager mViewPager;
    //title
    @BindView(R.id.remark_new_title_bar)
    RelativeLayout newRemarkTitleBar;
    //输入框
    @BindView(R.id.editTextBodyLl)
    LinearLayout etContainer;
    //输入框et
    @BindView(R.id.circleEt)
    EditText remarkEt;
    //输入框发送
    @BindView(R.id.sendIv)
    ImageView sendPost;

    //总容器
    @BindView(R.id.remark_new_container)
    RelativeLayout mContainer;

    private int screenHeight;//屏幕高度
    private int editTextBodyHeight;//输入框高度
    private int currentKeyboardH;//当前键盘高度
    private int selectCircleItemH;//选中的子项的高度？
    private int selectCommentItemOffset;//选中的子项的偏移？

    private List<Fragment> list;
    public int height;//顶部title的高度
    private RemarkData mRemarkData;//评论数据？
    private int replyFlooIndex;//回复数？
    private int currentPage;//当前页？
    private boolean isReplyUser;//是不是回复人？
=======
public class ToeflCircleFragment extends BaseFragment {
    @BindView(R.id.new_reamrk_tablayout)
    TabLayout mTableLayout;
    @BindView(R.id.new_remark_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.remark_new_title_bar)
    RelativeLayout newRemarkTitleBar;

    @BindView(R.id.editTextBodyLl)
    LinearLayout etContainer;
    @BindView(R.id.circleEt)
    EditText remarkEt;
    @BindView(R.id.sendIv)
    ImageView sendPost;
    @BindView(R.id.remark_new_container)
    RelativeLayout mContainer;

    private int screenHeight;
    private int editTextBodyHeight;
    private int currentKeyboardH;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private List<Fragment> list;
    public int height;
    private RemarkData mRemarkData;
    private int replyFlooIndex;
    private int currentPage;
    private boolean isReplyUser;
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb


    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_toefl_circle_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

<<<<<<< HEAD
    //获取输入框隐藏状态
=======

>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    public int getEtStatus() {
        if (etContainer == null) {
            return View.GONE;
        }
        return etContainer.getVisibility();
    }

<<<<<<< HEAD
    //发表帖子点击事件
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    @OnClick({R.id.toefl_circle_write})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toefl_circle_write:
                if (currentPage == 0) {
                    //发布八卦
                    forword(PostRemarkActivity.class);
                } else if (currentPage == 1) {
                    //社区发帖
                    PostRemarkActivity.startNewPostRemark(getActivity(), true);
                }
                break;
        }
    }

<<<<<<< HEAD

    //当底部view空？
    @Override
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
        super.initWhenRootViewNull(savedInstanceState);
        //获取title视图宽高
=======
    @Override
    protected void initWhenRootViewNull(Bundle savedInstanceState) {
        super.initWhenRootViewNull(savedInstanceState);
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
        newRemarkTitleBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
<<<<<<< HEAD
                Utils.removeOnGlobleListener(newRemarkTitleBar,this);//取消监听？
                height = newRemarkTitleBar.getHeight();//获取title的高度
            }
        });
        mViewPager.setAdapter(getPagerAdapter());
        setViewPagerListener();//设置viewpager监听
        mTableLayout.setupWithViewPager(mViewPager);//tablayout+viewpager
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
        setViewTreeObserver();//监测键盘等view的隐藏显示
=======
                Utils.removeOnGlobleListener(newRemarkTitleBar,this);
                height = newRemarkTitleBar.getHeight();
            }
        });
        mViewPager.setAdapter(getPagerAdapter());
        setViewPagerListener();
        mTableLayout.setupWithViewPager(mViewPager);
        mTableLayout.setTabMode(TabLayout.MODE_FIXED);
        mTableLayout.setTabGravity(TabLayout.GRAVITY_FILL);//不设置gravity没有效果
        setViewTreeObserver();
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRemarkData == null) return;
                if (isReplyUser) {
                    postFloor();//回复楼层
                } else {
                    //评论
                    replyRemark();
                }

            }
        });
    }

<<<<<<< HEAD
    //评论
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    private void replyRemark() {
        String content = remarkEt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (GlobalUser.getInstance().isAccountDataInvalid()) {
            toastShort(R.string.str_no_login_tip);
            return;
        }
        UserData data = GlobalUser.getInstance().getUserData();
<<<<<<< HEAD
        String uName = data.getNickname();//用户名
=======
        String uName = data.getNickname();
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb

//        HttpUtil.reply(content, mRemarkData.getId(), mRemarkData.getUid(), uName, data.getImage())
//                .flatMap(new Function<ResultBean, ObservableSource<Integer>>() {
//                    @Override
//                    public ObservableSource<Integer> apply(@NonNull ResultBean bean) throws Exception {
//                        return loginExpired(bean);
//                    }
//                })
//                .compose(new SchedulerTransformer<Integer>())
//                .subscribe(new ResultObserver<Integer>(new RequestCallback<Integer>() {
//                    @Override
//                    public void beforeRequest(Disposable d) {
//                        showLoadDialog();
//                        addToCompositeDis(d);
//                    }
//
//                    @Override
//                    public void requestFail(String msg) {
//                        dismissLoadDialog();
//                        toastShort(msg);
//                    }
//
//                    @Override
//                    public void requestComplete() {
//                    }
//
//                    @Override
//                    public void requestSuccess(Integer integer) {
//                        if (integer == C.AUTO_LOGIN_SUCCESS) {
//                            replyRemark();
//                        } else if (integer == C.RESPONSE_SUCCESS) {
//                            RemarkFragment fragment = (RemarkFragment) list.get(0);
//                            fragment.netRequest(1, true);
//                            remarkEt.setText("");
//                            dismissLoadDialog();
//                        }
//                    }
//                }));

        addToCompositeDis(HttpUtil.reply(content, mRemarkData.getId(), mRemarkData.getUid(), uName, data.getImage())
<<<<<<< HEAD
                .doOnSubscribe(new Consumer<Disposable>() {//开始网络请求？
=======
                .doOnSubscribe(new Consumer<Disposable>() {
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
<<<<<<< HEAD
                        throwable.printStackTrace();
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {//接收数据
=======
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        RemarkFragment fragment = (RemarkFragment) list.get(0);
                        fragment.netRequest(1, true);
                        remarkEt.setText("");
                        dismissLoadDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
<<<<<<< HEAD
=======

>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
                    }
                }));
    }

<<<<<<< HEAD
    //回复
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    private void postFloor() {
        String content = getEditText(remarkEt);
        if (TextUtils.isEmpty(content)) {
            return;
        }
<<<<<<< HEAD
        List<RemarkData.ReplyBean> reply = mRemarkData.getReply();//获取评论数据集合
=======
        List<RemarkData.ReplyBean> reply = mRemarkData.getReply();
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
        if (reply == null || reply.isEmpty()) return;
        UserData data = GlobalUser.getInstance().getUserData();
        String name = data.getNickname();
        RemarkData.ReplyBean bean = reply.get(replyFlooIndex);
        addToCompositeDis(HttpUtil.replyFloor(content, mRemarkData.getId(), mRemarkData.getUid(), name, data.getImage(), bean.getUid(), bean.getUName())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoadDialog();
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        dismissLoadDialog();
                    }
                })
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(@NonNull ResultBean bean) throws Exception {
                        dismissLoadDialog();
                        if (getHttpResSuc(bean.getCode())) {
                            RemarkFragment fragment = (RemarkFragment) list.get(0);
                            fragment.netRequest(1, true);
                            remarkEt.setText("");
                        } else {
                            toastShort(bean.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                    }
                }));
    }

<<<<<<< HEAD
    //viewpager监听
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    private void setViewPagerListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

<<<<<<< HEAD
    //显示或隐藏输入框
=======
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    public void showOrHideEt(int visibility, RemarkData remarkData) {
        showOrHideEt(visibility, remarkData, 0, isReplyUser);
    }

<<<<<<< HEAD
    //设置视图观察器
    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mContainer.getViewTreeObserver();//获取总容器视图观察器
        //获取总容器宽高
=======
    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = mContainer.getViewTreeObserver();
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
<<<<<<< HEAD
                mContainer.getWindowVisibleDisplayFrame(r);//将当前窗口可视区域大小赋值给r
                int statusBarH = MeasureUtil.getStatusBarHeight(getActivity());//状态栏高度
                int screenH = mContainer.getRootView().getHeight();//获取根view的高度
=======
                mContainer.getWindowVisibleDisplayFrame(r);
                int statusBarH = MeasureUtil.getStatusBarHeight(getActivity());//状态栏高度
                int screenH = mContainer.getRootView().getHeight();
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
<<<<<<< HEAD
                int keyboardH = screenH - (r.bottom - r.top);//键盘高度
=======
                int keyboardH = screenH - (r.bottom - r.top);
>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
//                Log.d(TAG, "screenH＝ " + screenH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = etContainer.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    showOrHideEt(View.GONE, null);
                    return;
                }
                //偏移listview
                RemarkFragment remarkFragment = (RemarkFragment) list.get(0);
                LinearLayoutManager manager = remarkFragment.getManager();
                if (manager != null && mRemarkData != null) {
                    manager.scrollToPositionWithOffset(mRemarkData.getRecyclePosition(),
                            remarkFragment.getListviewOffset(mRemarkData, screenHeight, selectCircleItemH, currentKeyboardH, editTextBodyHeight, selectCommentItemOffset) - height);
                }
            }
        });
    }

    public boolean getEtContainerStatus() {
        return etContainer == null ? false : etContainer.getVisibility() == View.VISIBLE;
    }

    private void measureCircleItemHighAndCommentItemOffset(RemarkData commentConfig, int index) {
        if (commentConfig == null)
            return;
        LinearLayoutManager manager = ((RemarkFragment) list.get(0)).getManager();
        int firstPosition = manager.findFirstVisibleItemPosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = manager.getChildAt(commentConfig.getRecyclePosition() - firstPosition);

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

        //回复评论的情况
        CommentListView commentLv = (CommentListView) selectCircleItem.findViewById(R.id.remark_comment_list);
        if (commentLv != null) {
            //找到要回复的评论view,计算出该view距离所属动态底部的距离
            View selectCommentItem = commentLv.getChildAt(index);
            if (selectCommentItem != null) {
                //选择的commentItem距选择的CircleItem底部的距离
                selectCommentItemOffset = 0;
                View parentView = selectCommentItem;
                do {
                    int subItemBottom = parentView.getBottom();
                    parentView = (View) parentView.getParent();
                    if (parentView != null) {
                        selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                    }
                } while (parentView != null && parentView != selectCircleItem);
            }
        }
    }

    public void showOrHideEt(int visibility, RemarkData remarkData, int position, boolean replyUser) {
        isReplyUser = replyUser;
        mRemarkData = remarkData;
        replyFlooIndex = position;
        etContainer.setVisibility(visibility);
        measureCircleItemHighAndCommentItemOffset(remarkData, position);

        MainActivity activity = (MainActivity) getActivity();
        if (visibility == View.VISIBLE) {
            activity.setNavContainerVisibility(View.GONE);
            remarkEt.requestFocus();
            if (isReplyUser) {
                List<RemarkData.ReplyBean> reply = remarkData.getReply();
                if (reply != null && !reply.isEmpty()) {
                    remarkEt.setHint("@" + reply.get(position).getUName());
                }
            } else {
                remarkEt.setHint(getString(R.string.str_remark_detail_et_hint));
            }
            Utils.keyBordShowFromWindow(remarkEt.getContext(), remarkEt);
        } else if (visibility == View.GONE) {
            activity.setNavContainerVisibility(View.VISIBLE);
            Utils.keyBordHideFromWindow(remarkEt.getContext(), remarkEt);
        }
    }

<<<<<<< HEAD
    //设置顶部tab值及fragment
=======

>>>>>>> 9d5a20271315c2e10e02f62b7d2b686b86e92ffb
    public PagerAdapter getPagerAdapter() {
        list = new ArrayList<>();
        list.add(new RemarkFragment());
        list.add(new CommunityFragment());
        return new TabPagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.toefl_circle_tab_arr)) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
        };
    }
}
