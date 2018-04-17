package io.dcloud.H58E83894.ui.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.weiget.LeanTextView;

import static io.dcloud.H58E83894.R.drawable.ic_infor_target_02;

public class IfToeflActivity extends BaseActivity {


    @BindView(R.id.infor_test_yes)
    TextView inforYes;
    @BindView(R.id.infor_test_no)
    TextView inforNO;
    @BindView(R.id.time)
    LeanTextView time;
    @BindView(R.id.taegetInt)
    LeanTextView taegetInt;
    private String inputAccount;
    private String inputPwd;
    private String grade;
    private String testTime;
    private int targetScores;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;

        Bundle bundle = intent.getExtras();
        inputAccount = bundle.getString(Intent.EXTRA_TITLE);
        inputPwd = bundle.getString(Intent.EXTRA_TEXT);
        grade = bundle.getString("001");
        testTime = bundle.getString("002");
        targetScores = bundle.getInt("targetScore");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_if_toefl);
        time.setmDegrees(7);
        time.setText(testTime);
        taegetInt.setmDegrees(-7);
        taegetInt.setText(targetScores+"");
    }

    @OnClick({R.id.infor_test_yes, R.id.infor_test_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infor_test_yes:// 1代表考过
//                Log.i("kkk3", testTime+"haha"+"haha"+Integer.getInteger(targetScore)+"haha"+grade+"haha");
                Bundle bundle = new Bundle();
                Intent intent = new Intent(this, YesToeflActivity.class);
                bundle.putString(Intent.EXTRA_TEXT, inputAccount);
                bundle.putString(Intent.EXTRA_TITLE, inputPwd);
                bundle.putString("001", grade);
                bundle.putString("002", testTime);
                bundle.putInt("targetScore", targetScores);
                bundle.putInt("yes", 1);
                intent.putExtras( bundle);
                startActivity(intent);
                break;

            case R.id.infor_test_no:

                Bundle bundles = new Bundle();
                Intent intents = new Intent(this, LoadToeflActivity.class);
                bundles.putString(Intent.EXTRA_TEXT, inputAccount);
                bundles.putString(Intent.EXTRA_TITLE, inputPwd);
                bundles.putString("001", grade);
                bundles.putString("002", testTime);
                bundles.putInt("targetScore", targetScores);
                bundles.putInt("yes", 2);
                bundles.putInt("aScore", 0);//2代表没考过
                intents.putExtras( bundles);
                startActivity(intents);
//                Log.i("kkk4", testTime+"haha"+"haha"+Integer.getInteger(targetScore)+"haha"+grade+"haha");
//                LoadToeflActivity.startGradeActivity(this,  inputAccount, inputPwd, grade, testTime, targetScore, "2", "0");
                break;

        }
    }

}
