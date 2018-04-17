package io.dcloud.H58E83894.ui.information;

import android.app.DatePickerDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.Buffer;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;

public class TestTimeActivity extends BaseActivity {


    public static void startGradeActivity(Context mContext, String inputAccount, String inputPwd, String grade) {
        Intent intent = new Intent(mContext, TestTimeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, inputAccount);
        intent.putExtra(Intent.EXTRA_TITLE, inputPwd);
        intent.putExtra(Intent.EXTRA_TEMPLATE, grade);
        mContext.startActivity(intent);
    }

    private int mYear;
    private int mMonth ;
    private int mDay;
    @BindView(R.id.infor_time)
    TextView btn_day;
    private String inputAccount;
    private String inputPwd;
    private String grade;

    @Override
    protected void getArgs() {
        Intent intent = getIntent();
        if (intent == null) return;

        inputAccount = intent.getStringExtra(Intent.EXTRA_TITLE);
        inputPwd = intent.getStringExtra(Intent.EXTRA_TEXT);
        grade = intent.getStringExtra(Intent.EXTRA_TEMPLATE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_test_time);


        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
//
    }

    @OnClick({R.id.infor_sure_time, R.id.infor_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.infor_sure_time:
                if(TextUtils.isEmpty(btn_day.getText())){
                    toastShort(R.string.str_please_choose_data);
                    return;
                }
                TargetScoreActivity.startGradeActivity(this,  inputAccount, inputPwd, grade, btn_day.getText().toString());
                break;
            case R.id.infor_time:
                btn_day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 调用时间选择器
                        Calendar minCalendar = Calendar.getInstance();
                        minCalendar.set(Calendar.MILLISECOND, minCalendar.MILLISECOND - 1000);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(TestTimeActivity.this, onDateSetListener, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(minCalendar.getTimeInMillis());//设置当前时间为最小时间
                        datePickerDialog.show();
                    }
                });
                break;
        }
    }

    /**
     * 日期选择器对话框监听
     */
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }

            }
            btn_day.setText(days);
        }
    };

}
