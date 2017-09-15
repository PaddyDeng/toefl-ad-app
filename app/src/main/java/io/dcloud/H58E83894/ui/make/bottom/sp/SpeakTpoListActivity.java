package io.dcloud.H58E83894.ui.make.bottom.sp;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseListActivity;
import io.dcloud.H58E83894.base.adapter.BaseRecyclerViewAdapter;
import io.dcloud.H58E83894.base.adapter.type.InitDataType;
import io.dcloud.H58E83894.data.make.PracticeData;
import io.dcloud.H58E83894.data.make.WriteTpoData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.ui.make.adapter.SpeakTpoAdapter;
import io.dcloud.H58E83894.utils.C;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by fire on 2017/7/28  09:29.
 */

public class SpeakTpoListActivity extends BaseListActivity<WriteTpoData> {

    private LinearLayoutManager mManager = new LinearLayoutManager(this);
    private boolean asyncDataSuccess = false;

    @Override
    protected void initView() {
        baseTitleTxt.setText(R.string.str_read_practice_tpo_title);
    }

    @Override
    protected void asyncLoadMore() {
        updateRecycleView(null, "", InitDataType.TYPE_LOAD_MORE_SUCCESS);
    }

    @Override
    protected BaseRecyclerViewAdapter<WriteTpoData> getAdapter() {
        return new SpeakTpoAdapter(mContext, null, mManager);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mManager;
    }

    @Override
    protected void setListener(List<WriteTpoData> data, int position) {
        WriteTpoData tpoData = data.get(position);
        List<PracticeData> question = tpoData.getQuestion();
        SpeakQuestionActivity.startSpeakQuestionAct(mContext, cont(question), C.TYPE_SPEAK_TPO);
    }

    private String cont(List<PracticeData> question) {
        StringBuffer sb = new StringBuffer();
        int size = question.size();
        for (int i = 0; i < size; i++) {
            sb.append(question.get(i).getId());
            if (i < size) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    protected void asyncRequest() {
        if (asyncDataSuccess) {
            mPullRefreshLayout.finishRefresh();
            return;
        }
        addToCompositeDis(HttpUtil
                .speakTpo(cate())
                .subscribe(new Consumer<List<WriteTpoData>>() {
                    @Override
                    public void accept(@NonNull List<WriteTpoData> list) throws Exception {
                        if (list != null && !list.isEmpty()) {
                            for (WriteTpoData data : list) {
                                int num = 0;
                                List<PracticeData> question = data.getQuestion();
                                for (PracticeData pd : question) {
                                    num += Integer.parseInt(pd.getNum());
                                }
                                data.setNum(num);
                            }
                            updateRecycleView(list, "", InitDataType.TYPE_REFRESH_SUCCESS);
                            asyncDataSuccess = true;
                        } else {
                            updateRecycleView(null, getString(R.string.str_empty_tip), InitDataType.TYPE_REFRESH_FAIL);

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        updateRecycleView(null, throwMsg(throwable), InitDataType.TYPE_REFRESH_FAIL);
                    }
                }));
    }

    private String cate() {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < 50; i++) {
            if (i >= 35 && i <= 39) {
                continue;
            }
            sb.append(cacl(i));
            if (i < 49) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private String cacl(int tpoNumber) {
        if (tpoNumber < 35) {
            tpoNumber += 102;
        } else if (tpoNumber < 40) {

        } else if (tpoNumber < 49) {
            tpoNumber += 238;
        } else if (tpoNumber < 50) {
            tpoNumber += 263;
        }
        return String.valueOf(tpoNumber);
    }
}
