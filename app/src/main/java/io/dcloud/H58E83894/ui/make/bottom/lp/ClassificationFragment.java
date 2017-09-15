package io.dcloud.H58E83894.ui.make.bottom.lp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseFragment;

/**
 * Created by fire on 2017/7/25  11:54.
 */

public class ClassificationFragment extends BaseFragment {
    @Override
    protected View onCreateViewInit(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_class_ification_layout, container, false);
    }

    @OnClick({R.id.str_classifcation_course_container, R.id.str_classification_school_life_container,
            R.id.str_classification_natural_science_container, R.id.str_classification_social_science_container,
            R.id.str_classification_life_science_container, R.id.str_classification_cultural_science_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.str_classifcation_course_container:
                startAct("16",getString(R.string.str_classification_course_study));
                break;
            case R.id.str_classification_school_life_container:
                startAct("27",getString(R.string.str_classification_school_life));
                break;
            case R.id.str_classification_natural_science_container:
                startAct("18",getString(R.string.str_classification_natural_science));
                break;
            case R.id.str_classification_social_science_container:
                startAct("19",getString(R.string.str_classification_social_science));
                break;
            case R.id.str_classification_life_science_container:
                startAct("36",getString(R.string.str_classification_life_science));
                break;
            case R.id.str_classification_cultural_science_container:
                startAct("37",getString(R.string.str_classification_cultural_science));
                break;
            default:
                break;
        }
    }

    private void startAct(String id,String title) {
        ListenSecListActivity.startListenSecAct(getActivity(), id, title);
    }
}
