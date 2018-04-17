package io.dcloud.H58E83894.ui.information;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.http.ResultObserver;
import io.dcloud.H58E83894.ui.user.OnUserInfoListener;
import io.dcloud.H58E83894.weiget.LeanTextView;

public class LoadToeflActivity extends BaseActivity{

//    public static void startGradeActivity(Context mContext, String inputAccount, String inputPwd, String grade, String testTime, String targetScore, String no) {
//        Intent intent = new Intent(mContext, LoadToeflActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
//        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
//        intent.putExtra("001", grade);
//        intent.putExtra("002", testTime);
//        intent.putExtra("003", targetScore);
//        intent.putExtra("004", no);
//        mContext.startActivity(intent);
//    }

//    public static void startGradeActivity(Context mContext, String inputAccount, String inputPwd, String grade, String testTime, String targetScore, String no, String score) {
//        Intent intent = new Intent(mContext, LoadToeflActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
//        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
//        intent.putExtra("001", grade);
//        intent.putExtra("002", testTime);
//        intent.putExtra("003", targetScore);
//        intent.putExtra("004", no);
//        intent.putExtra("005", score);
//        mContext.startActivity(intent);
//    }


      @BindView(R.id.time)
      LeanTextView time;
      @BindView(R.id.taegetInt)
      TextView taegetInt;
        private String inputAccount;
        private String inputPwd;
        private String grade;
        private String testTime;
        private int targetScore;
        private int yes;
        private int score;
        private OnUserInfoListener mOnUserInfoListener;

        @Override
        protected void getArgs() {
            Intent intent = getIntent();
            if (intent == null) return;
            Bundle bundle = intent.getExtras();
            inputAccount = bundle.getString(Intent.EXTRA_TITLE);
            inputPwd = bundle.getString(Intent.EXTRA_TEXT);
            grade = bundle.getString("001");
            testTime = bundle.getString("002");
            targetScore = bundle.getInt("targetScore");
            yes = bundle.getInt("yes");
            score = bundle.getInt("aScore");
            Log.i("kkk2", testTime+"haha"+yes+"haha"+targetScore+"haha"+grade+"haha"+score);
        }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_load);
        time.setmDegrees(7);
        time.setText(testTime);
        taegetInt.setText(targetScore+"");
    }

    @OnClick({R.id.infor_test_yes})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infor_test_yes:
                if ((score) == 0){
                    Log.i("kkk", testTime+"haha"+yes+"haha"+targetScore+"haha"+grade+"haha"+score);
                    HttpUtil.registerInfor(testTime, yes, targetScore, grade, score).subscribe();
                } else {
                    HttpUtil.registerInfor(testTime, yes, targetScore, grade, score).subscribe();
                }

                forword(MainActivity.class);
//                login(mOnUserInfoListener, inputAccount, inputPwd);
                break;

        }
    }

}
