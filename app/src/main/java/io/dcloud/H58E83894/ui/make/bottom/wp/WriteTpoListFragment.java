package io.dcloud.H58E83894.ui.make.bottom.wp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseRefreshFragment;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.WriteTpoData;
import io.dcloud.H58E83894.data.make.type.TpoDes;
import io.dcloud.H58E83894.data.make.type.TypeTpo;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.WriteTpoAdapter;
import io.dcloud.H58E83894.utils.C;
import io.dcloud.H58E83894.utils.MeasureUtil;
import io.dcloud.H58E83894.utils.RxBus;
import io.dcloud.H58E83894.utils.Utils;
import io.dcloud.H58E83894.weiget.sticky.GroupListener;
import io.dcloud.H58E83894.weiget.sticky.StickyDecoration;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/26  16:11.
 */

public class WriteTpoListFragment extends BaseRefreshFragment<TypeTpo> {

    public static WriteTpoListFragment getInstance(int startTpoNum, int size) {
        WriteTpoListFragment writeTpoListFragment = new WriteTpoListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TOPSTARTNUM", startTpoNum);
        bundle.putInt("SIZE", size);
        writeTpoListFragment.setArguments(bundle);
        return writeTpoListFragment;
    }

    private LinearLayoutManager mManager = new LinearLayoutManager(getActivity());
    private int startTopNum;
    private int size;
    private String catName;
    private Observable<String> asynObs;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getArg();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getArg() {
        Bundle arguments = getArguments();
        if (arguments == null) return;
        startTopNum = arguments.getInt("TOPSTARTNUM");
        size = arguments.getInt("SIZE");
    }

    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected BaseRecyclerViewAdapter<TypeTpo> getAdapter() {
        return new WriteTpoAdapter(getActivity(), null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        addToCompositeDis(HttpUtil
                .writeTpo(caclCate())
                .subscribe(new Consumer<List<WriteTpoData>>() {
                    @Override
                    public void accept(@NonNull List<WriteTpoData> list) throws Exception {
                        formatData(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, errorTip(throwable), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asynObs = RxBus.get().register(C.REFRESH_WRITE_TPO_LIST, String.class);
        asynObs.subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                if (TextUtils.equals(s, catName)) {
                    asyncRequest();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (asynObs != null)
            RxBus.get().unregister(C.REFRESH_WRITE_TPO_LIST, asynObs);
    }

    @Override
    protected void setListener(List<TypeTpo> data, int position) {
        super.setListener(data, position);
        TypeTpo typeTpo = data.get(position);
        if (typeTpo.getType() != C.TYPE_DES) return;
        TpoDes des = (TpoDes) typeTpo;
        PracticeData question = des.getQuestion();
        if (question == null) return;
        catName = des.getCatName();
        if (question.getFinish() == 0) {
            WriteTpoQuestionActivity.startWriteTpoQuestionAct(getActivity(), des.getCatName(), question.getId());
        } else {
//            String s = catName.replaceAll("\\D", "");
//            int canRefresh = Integer.parseInt(s);
            WriteResultActivity.startResult(getActivity(), Utils.splitInt(catName), question.getId(), C.BELONG_TPO);
        }
    }

    @Override
    protected void initRecyclerViewItemDecoration(RecyclerView mRecyclerView) {
        super.initRecyclerViewItemDecoration(mRecyclerView);
        StickyDecoration decoration = StickyDecoration.Builder
                .init(new GroupListener() {
                    @Override
                    public String getGroupName(int position) {
                        if (adapter == null || adapter.getAdapterData() == null) {
                            return null;
                        }
                        //组名回调
                        List<TypeTpo> adapterData = adapter.getAdapterData();
                        if (adapterData.size() > position) {
                            return ((TpoDes) adapterData.get(position)).getCatName();
                        }
                        return null;
                    }
                })
                .setGroupBackground(Color.parseColor("#EFEFF4"))    //背景色
                .setGroupHeight(MeasureUtil.dip2px(getActivity(), 45))       //高度
//                .setDivideColor(Color.parseColor("#ff0000"))        //分割线颜色
//                .setDivideHeight(MeasureUtil.dip2px(getActivity(), 5))       //分割线高度 (默认没有分割线)
                .setGroupTextColor(Color.parseColor("#333333"))                     //字体颜色
                .setGroupTextSize(MeasureUtil.sp2px(getActivity(), 16))      //字体大小
                .setTextSideMargin(MeasureUtil.dip2px(getActivity(), 15))    // 边距   靠左时为左边距  靠右时为右边距
//                .isAlignLeft(true)                                 //靠右显示  （默认靠左）
                .build();
        mRecyclerView.addItemDecoration(decoration);
    }

    private void formatData(List<WriteTpoData> list) {
        if (list == null || list.isEmpty()) {
            updateRecycleView(null, getString(R.string.str_empty_tip), InitDataType.TYPE_REFRESH_FAIL);
            return;
        }

        List<TypeTpo> typeTpos = new ArrayList<>();
        for (WriteTpoData data : list) {
            //先设置title
//            TpoTitle title = new TpoTitle();
//            title.setCatName(data.getCatName());
//            typeTpos.add(title);
            List<PracticeData> question = data.getQuestion();
            int size = question.size();
            for (int i = 0; i < size; i++) {
                PracticeData pd = question.get(i);
                TpoDes des = new TpoDes();
                des.setCatName(data.getCatName());
                des.setQuestion(pd);
                if (i == size - 1) {
                    des.setLastDesItem(true);
                }
                typeTpos.add(des);
            }
        }
        updateRecycleView(typeTpos, "", InitDataType.TYPE_REFRESH_SUCCESS);
    }

    private String caclCate() {
        return caclOther(startTopNum, size);
    }

    private String caclOther(int startTopNum, int size) {
        StringBuffer sb = new StringBuffer();
        int end = startTopNum + size;
        while (startTopNum < end) {
            sb.append(caclTpoNum(startTopNum));
            startTopNum++;
            if (startTopNum < end) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private int caclTpoNum(int tpoNumber) {
        if (tpoNumber < 35) {
            tpoNumber += 150;
        } else if (tpoNumber < 40) {

        } else if (tpoNumber < 45) {
            tpoNumber += 212;
        } else if (tpoNumber < 49) {
            tpoNumber += 214;
        } else if (tpoNumber < 50) {
            tpoNumber += 264;
        }else if (tpoNumber <  51) {
            tpoNumber += 331;
        }
        return tpoNumber;
    }
}
