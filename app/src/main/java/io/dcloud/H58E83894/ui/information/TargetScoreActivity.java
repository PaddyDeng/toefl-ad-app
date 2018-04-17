package io.dcloud.H58E83894.ui.information;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
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

public class TargetScoreActivity extends BaseActivity {


    public static void startGradeActivity(Context mContext, String inputAccount, String inputPwd, String grade, String testTime) {
        Intent intent = new Intent(mContext, TargetScoreActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
        intent.putExtra("001", grade);
        intent.putExtra("002", testTime);
        mContext.startActivity(intent);
    }

    @BindView(R.id.target_et)
    EditText targetEt;
    @BindView(R.id.target_tv)
    TextView targetTv;
    @BindView(R.id.time)
    LeanTextView time;

    private String inputAccount;
    private String inputPwd;
    private String grade;
    private String testTime;


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;

        inputAccount = intent.getStringExtra(Intent.EXTRA_TITLE);
        inputPwd = intent.getStringExtra(Intent.EXTRA_TEXT);
        grade = intent.getStringExtra("001");
        testTime = intent.getStringExtra("002");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_target_score);
        time.setmDegrees(7);
        time.setText(testTime);
//
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.target_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.target_tv:
                if(TextUtils.isEmpty(targetEt.getText())){
                    toastShort(R.string.str_infor_others);
                }else {
                    String targetScore = targetEt.getText().toString().trim();
                    int targetScoreInt = Integer.parseInt(targetScore);
                    if((targetScoreInt > 120) || (targetScoreInt < 1)){
                        toastShort(R.string.str_infor_others);
                        return;
                    }else {
                        targetTv.setBackground(getResources().getDrawable(ic_infor_target_02));
                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(this, IfToeflActivity.class);
                        bundle.putString(Intent.EXTRA_TEXT, inputAccount);
                        bundle.putString(Intent.EXTRA_TITLE, inputPwd);
                        bundle.putString("001", grade);
                        bundle.putString("002", testTime);
                        bundle.putInt("targetScore", Integer.valueOf(targetScore));
                        intent.putExtras(bundle);
                        startActivity(intent);

                        Log.i("kkk6", testTime + "haha" + "" + grade + "haha" + targetEt.getText().toString());
//                    IfToeflActivity.startGradeActivity(this, inputAccount, inputPwd, grade, testTime, targetScore, "22");
                    }
                }

                break;

        }
    }


}
