package io.dcloud.H58E83894.ui.toeflcircle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;
import io.dcloud.H58E83894.data.prelesson.BannerData;
import io.dcloud.H58E83894.data.prelesson.PreLessonData;
import io.dcloud.H58E83894.utils.HtmlUtil;

public class CommunityNewFragment extends BaseFragment{

    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.hot)
    TextView hotText;
//    @BindView(R.id.iv_01)
//    FrameLayout ivOne;
//    @BindView(R.id.iv_02)
//    FrameLayout ivTwo;
//    @BindView(R.id.iv_03)
//    FrameLayout ivThr;
//    @BindView(R.id.iv_04)
//    FrameLayout ivFour;
//    @BindView(R.id.iv_05)
//    FrameLayout ivFif;
//    @BindView(R.id.iv_06)
//    FrameLayout ivSix;
//    @BindView(R.id.toefl_banner)
//    BBanner bBanner;

    private List<BannerData> bannerDatas;
    private boolean asynData;
    private PreLessonData mPreLessonData;
    private Context context;


    private RecyclerView.LayoutManager mManager = new LinearLayoutManager(getActivity()
    );
    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_new_community, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();


        Fragment myfragment = ExamInfoFragment.newInstance("138","0");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.frame, myfragment);
        hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_01)));
        transaction.commit();
//        initBBaner();
//        asynBannerData();
    }


    @OnClick({R.id.iv_01, R.id.iv_02, R.id.iv_03,
            R.id.iv_04, R.id.iv_05,  R.id.iv_06})
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.iv_01:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_01)));
                Fragment myfragment = ExamInfoFragment.newInstance("138","0");
                transaction.replace(R.id.frame, myfragment);
                transaction.commit();
                break;
            case R.id.iv_02:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_02)));
                Fragment myfragment01 = DownLoadFragment.newInstance("11","1");
                transaction.replace(R.id.frame, myfragment01);
                transaction.commit();
                break;
            case R.id.iv_03:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_03)));
                Fragment myfragment02 = DownLoadFragment.newInstance("9","2");
                transaction.replace(R.id.frame, myfragment02);
                transaction.commit();
                break;
            case R.id.iv_04:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types)));
                Fragment myfragment03 = ExamInfoFragment.newInstance("372","3");
                transaction.replace(R.id.frame, myfragment03);
                transaction.commit();
                break;
            case R.id.iv_05:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_04)));
                Fragment myfragment04 = ExamInfoFragment.newInstance("138","4");
                transaction.replace(R.id.frame, myfragment04);
                transaction.commit();
                break;
            case R.id.iv_06:
                hotText.setText(HtmlUtil.fromHtml(getActivity().getString(R.string.str_item_download_types_05)));
                Fragment myfragment05 = ExamInfoFragment.newInstance("374","5");
                transaction.replace(R.id.frame, myfragment05);
                transaction.commit();
                break;
        }
    }


//    private void initRecycle() {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction transaction01 = fm.beginTransaction();
//        Fragment myfragment= new ExamInfoFragment();
//        transaction01.replace(R.id.frame, myfragment);
//        transaction01.commit();
//
//        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
//        manager.setAutoMeasureEnabled(true);
//        mRecyclerView.setLayoutManager(manager);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setNestedScrollingEnabled(false);
////        bBanner.setVisibility(View.VISIBLE);
//        String[] array = getResources().getStringArray(R.array.test_type_drawable_name);
//        final TestTypeAdapter adapter = new TestTypeAdapter(initTestType(array));
//        adapter.setOnItemClickListener(new SimpleAdapter<TestTypeData>() {
//            @Override
//            public void onClick(View view, int position, TestTypeData data) {
////                bBanner.setVisibility(View.GONE);
//                Log.i("mmm", view+"s"+position+"s"+data.getId());
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction transaction = fm.beginTransaction();
//                switch(position){
//
//                    case 0:
//                        Fragment myfragment= ExamInfoFragment.newInstance("138","0");
//                        transaction.replace(R.id.frame, myfragment);
//                        transaction.commit();
//                        break;
//
//                    case 1:
//                        Fragment myfragment01= DownLoadFragment.newInstance("11","1");
//                        transaction.replace(R.id.frame, myfragment01);
//                        transaction.commit();
//                        break;
//
//                    case 2:
//                        Fragment myfragment02= DownLoadFragment.newInstance("9","2");
//                        transaction.replace(R.id.frame, myfragment02);
//                        transaction.commit();
//                        break;
////                        Fragment myfragment02= new DownLoadsFragment();
//                    case 3:
//                        Fragment myfragment03= ExamInfoFragment.newInstance("372","3");
//                        transaction.replace(R.id.frame, myfragment03);
//                        transaction.commit();
//                        break;
//
//                    case 4:
//                        Fragment myfragment04= ExamInfoFragment.newInstance("138","4");
//                        transaction.replace(R.id.frame, myfragment04);
//                        transaction.commit();
//                        break;
//
//                    case 5:
//                        Fragment myfragment05= ExamInfoFragment.newInstance("374","5");
//                        transaction.replace(R.id.frame, myfragment05);
//                        transaction.commit();
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        });
//        mRecyclerView.setAdapter(adapter);
//    }
//
//    private List<TestTypeData> initTestType(String[] names) {
//        List<TestTypeData> mDatas = new ArrayList<>();
//        for (int i = 0, size = names.length; i < size; i++) {
//            TestTypeData data = new TestTypeData();
//            data.setDrawableName(names[i]);
//            mDatas.add(data);
//        }
//        return mDatas;
//    }



//    private void asynBannerData() {
//        addToCompositeDis(HttpUtil.preLesson()
//                .subscribe(new Consumer<PreLessonData>() {
//                    @Override
//                    public void accept(@NonNull PreLessonData data) throws Exception {
//                        asynData = true;
//                        mPreLessonData = data;
//                        refreshUi();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                    }
//                }));
//    }

//    @Override
//    protected void refreshUi() {
//        if (mPreLessonData == null) return;
//        bannerDatas = mPreLessonData.getImage();
//        bBanner.setData(getChildFragmentManager(), new ItemAdapter() {
//            @Override
//            public Fragment getItem(int p) {
//                return BannerDetailPager.getInstance(RetrofitProvider.TOEFLURL + bannerDatas.get(p).getImage());
//            }
//
//            @Override
//            public int getCount() {
//                return bannerDatas.size();
//            }
//        });
//    }

//    private void initBBaner() {
//
//        bBanner.setOnItemClickListener(new OnClickItemListener() {
//            @Override
//            public void onClick(int i) {
//                if (bannerDatas != null && bannerDatas.get(i) != null && !TextUtils.isEmpty(bannerDatas.get(i).getUrl())) {
//                    DealActivity.startDealActivity(getActivity(), bannerDatas.get(i).getTitle(), bannerDatas.get(i).getUrl());
//                }
//            }
//        });
//
//        bBanner.setPageChangeDuration(1000);
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}



