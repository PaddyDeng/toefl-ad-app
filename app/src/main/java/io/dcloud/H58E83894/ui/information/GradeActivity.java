package io.dcloud.H58E83894.ui.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.data.make.ReadQuestionData;
import io.dcloud.H58E83894.ui.make.bottom.rp.ReadAnswerAnalyzeActivity;
import io.dcloud.H58E83894.ui.user.OnUserInfoListener;

public class GradeActivity extends BaseActivity {

    private String inputAccount;
    private String inputPwd;

    public static void startGradeActivity(Context mContext,  String inputAccount, String inputPwd) {
        Intent intent = new Intent(mContext, GradeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
        mContext.startActivity(intent);
    }

    @BindView(R.id.infor_junior)
    TextView inforJunior;
    @BindView(R.id.infor_senior)
    TextView inforSenior;
    @BindView(R.id.infor_collage)
    TextView inforCollage;
    @BindView(R.id.infor_graduate)
    TextView inforGradute;
    @BindView(R.id.infor_other)
    TextView inforOther;


    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;
        inputAccount = intent.getStringExtra(Intent.EXTRA_TITLE);
        inputPwd = intent.getStringExtra(Intent.EXTRA_TEXT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_grade);
    }


    @OnClick({R.id.infor_junior, R.id.infor_senior, R.id.infor_collage, R.id.infor_graduate, R.id.infor_other})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infor_junior:

                String grede = inforJunior.getText().toString();
                Log.i("kkk", inforJunior.getText().toString()+""+inputAccount+""+inputPwd+""+grede);
                TestTimeActivity.startGradeActivity(this,  inputAccount, inputPwd, grede);
                break;

            case R.id.infor_senior:
                String grede_01 =  inforSenior.getText().toString();
                TestTimeActivity.startGradeActivity(this,  inputAccount, inputPwd, grede_01);
                break;

            case R.id.infor_collage:
                String grede_02 =  inforCollage.getText().toString();
                TestTimeActivity.startGradeActivity(this,  inputAccount, inputPwd, grede_02);
                break;

            case R.id.infor_graduate:
                String grede_03 =  inforGradute.getText().toString();
                TestTimeActivity.startGradeActivity(this,  inputAccount, inputPwd, grede_03);
                break;

            case R.id.infor_other:
                String grede_04 =  inforOther.getText().toString();
                TestTimeActivity.startGradeActivity(this,  inputAccount, inputPwd, grede_04);
                break;

        }
    }


}
