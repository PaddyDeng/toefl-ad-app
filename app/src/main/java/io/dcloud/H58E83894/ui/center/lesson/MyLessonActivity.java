package io.dcloud.H58E83894.ui.center.lesson;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.MyLessonData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.utils.Utils;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MyLessonActivity extends BaseListActivity<MyLessonData.DataBean> {
    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(mContext);
    private int page = 1;
    private int countPage;
    Throwable throwables;

    @Override
    protected void asyncLoadMore() {
//        if(page < countPage){
            page ++;
            netRequest( false);
//        }

    }

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_center_my_courses);
//        Utils.setVisible(mBaseTitleLine);
    }

    @Override
    protected BaseRecyclerViewAdapter<MyLessonData.DataBean> getAdapter() {
        return new LessonCenterAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void asyncRequest() {
        page = 1;
        netRequest( true);
    }

    @Override
    protected boolean canLoadMore() {
         if(page <= countPage){
             return true;
        }else {
             return false;
         }
//        return true;

    }

    public void netRequest(final boolean isRefresh) {

        addToCompositeDis(HttpUtil.lessonList(page)
                .subscribe(new Consumer<MyLessonData>() {
                    @Override
                    public void accept(@NonNull MyLessonData bean) throws Exception {
                        asyncSuccess(bean, isRefresh);

                       countPage =  bean.getCountPage();
//                        updateRecycleView(null, getString(R.string.str_pc_see), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwableDeal(throwable, isRefresh);
//                        updateRecycleView(null, getString(R.string.str_pc_see), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
        }


    private void asyncSuccess(@NonNull MyLessonData data, boolean isRefresh) {
        List<MyLessonData.DataBean> contentDatas = data.getData();
        List<MyLessonData.DataBean> contentData = new ArrayList<>();//没过期的
        contentData.clear();
        long currentTime = System.currentTimeMillis() / 1000;
       int  currentTimes = Integer.parseInt(String.valueOf(currentTime).trim());
       int  ExpireTime;



       for (int i =0; i<contentDatas.size(); i++){
           ExpireTime = Integer.parseInt(contentDatas.get(i).getExpireTime().trim());
           if ( (ExpireTime - currentTimes) >= 0){
               contentData.add(contentDatas.get(i));
           }
       }

       if(contentData.equals(contentDatas)){
           throwableDeal(throwables, isRefresh);
       }

        Log.i("eeet", "ddddd3"+contentData.toString()+"结果 = ");

        if (contentDatas == null || contentDatas.isEmpty()) {
            Log.i("eeet", "ddddd4"+contentData.toString()+"结果 = ");
            if (isRefresh) {
                updateRecycleView(contentDatas, "您暂时没有已购买的有效课程", InitDataType.TYPE_REFRESH_FAIL);
            } else {
                updateRecycleView(contentDatas, "您暂时没有已购买的有效课程", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        } else {

            if (isRefresh) {
                Log.i("eeet", "ddddd5"+contentData.toString()+"结果 = ");
                updateRecycleView(contentDatas, "", InitDataType.TYPE_REFRESH_SUCCESS);
            } else {
                updateRecycleView(contentDatas, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
            }
        }
    }

    private void throwableDeal(@NonNull Throwable throwable, boolean isRefresh) {
        throwable.printStackTrace();
        Log.i("eeet", "ddddd4"+"结果 = ");
        if (isRefresh) {
            updateRecycleView(null,"您暂时没有已购买的有效课程", InitDataType.TYPE_REFRESH_FAIL);
        } else {
            updateRecycleView(null, "您暂时没有已购买的有效课程", InitDataType.TYPE_LOAD_MORE_FAIL);
            page--;
        }
    }
    protected String throwMsg(Throwable throwable) {
        return Utils.onError(throwable);
    }
}
