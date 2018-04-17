package io.dcloud.H58E83894.ui.information;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.weiget.LeanTextView;

import static io.dcloud.H58E83894.R.drawable.ic_infor_target_02;

public class YesToeflActivity extends BaseActivity {

//    public static void startGradeActivity(Context mContext, String inputAccount, String inputPwd, String grade, String testTime, String targetScore, String yes) {
//        Intent intent = new Intent(mContext, YesToeflActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
//        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
//        intent.putExtra("001", grade);
//        intent.putExtra("002", testTime);
//        intent.putExtra("targetScore", targetScore);
//        intent.putExtra("yes", yes);
//        mContext.startActivity(intent);
//    }

    @BindView(R.id.target_et)
    EditText targetEt;
    @BindView(R.id.target_tv)
    TextView targetTv;
    @BindView(R.id.time)
    LeanTextView time;
    @BindView(R.id.taegetInt)
    LeanTextView taegetInt;
    private String inputAccount;
    private String inputPwd;
    private String grade;
    private String testTime;
    private int targetScoreS;
    private int yes;


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        Bundle bundle = intent.getExtras();
        inputAccount = bundle.getString(Intent.EXTRA_TITLE);
        inputPwd = bundle.getString(Intent.EXTRA_TEXT);
        grade = bundle.getString("001");
        testTime = bundle.getString("002");
        targetScoreS = bundle.getInt("targetScore");
        yes = bundle.getInt("yes");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_target_yes);
        time.setmDegrees(7);
        time.setText(testTime);
        taegetInt.setmDegrees(-7);
        taegetInt.setText(targetScoreS+"");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.target_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.target_tv:

                if(TextUtils.isEmpty(targetEt.getText())){
                    toastShort(R.string.str_infor_others);
                }else {
                    int targetEtInt = Integer.parseInt(targetEt.getText().toString().trim());
                    if((targetEtInt > 120) || (targetEtInt < 1)){
                        toastShort(R.string.str_infor_otherss);
                        return;
//                    LoadToeflActivity.startGradeActivity(this,  inputAccount, inputPwd, grade, testTime, targetScoreS, "1", targetEt.getText().toString());
                    }else {
                        targetTv.setBackground(getResources().getDrawable(ic_infor_target_02));
                        Log.i("kkk10", testTime+"haha"+yes+"haha"+(targetScoreS)+"haha"+grade+"haha");
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(this, LoadToeflActivity.class);
                        bundle.putString(Intent.EXTRA_TEXT, inputAccount);
                        bundle.putString(Intent.EXTRA_TITLE, inputPwd);
                        bundle.putString("001", grade);
                        bundle.putString("002", testTime);
                        bundle.putInt("targetScore", targetScoreS);
                        bundle.putInt("yes", 1);
                        bundle.putInt("aScore", Integer.parseInt(targetEt.getText().toString()));
                        intent.putExtras( bundle);
                        startActivity(intent);

                    }
                }


                break;


        }
    }

}
