package io.dcloud.H58E83894.ui.center.leidou.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.BaseActivity;
import io.dcloud.H58E83894.ui.center.leidou.MyLeidouActivity;

/*
* 充值完成
* */
public class RechargeCompleteActivity extends BaseActivity {



    @BindView(R.id.complete_bt)
    TextView completeBt;//完成按钮
    @BindView(R.id.complete_time)
    TextView completeTime;//完成时间
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leidou_recharege_complete);

        completeTime.setText(getString(R.string.str_counter_downs, 5));

         Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                forword(MyLeidouActivity.class);
            }
        }, 5000);


        completeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forword(MyLeidouActivity.class);
            }
        });

    }

    }

