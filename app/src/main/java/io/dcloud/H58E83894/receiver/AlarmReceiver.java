package io.dcloud.H58E83894.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.List;

import io.dcloud.H58E83894.MainActivity;
import io.dcloud.H58E83894.R;
import io.dcloud.H58E83894.base.waitdialog.WaitDialog;
import io.dcloud.H58E83894.data.CorretData;
import io.dcloud.H58E83894.data.PayDatas;
import io.dcloud.H58E83894.data.commit.TodayListData;
import io.dcloud.H58E83894.http.HttpUtil;
import io.dcloud.H58E83894.permission.RxPermissions;
import io.dcloud.H58E83894.ui.make.easyResource.EaLexxicalResourceActivity;
import io.dcloud.H58E83894.ui.make.lexicalResource.LexxicalResourceActivity;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_FLAG = 1;

    public static final String SP_Action = "com.SP_Action";
    public static final String Ea_Action = "com.EA_Action";

    private static  AlarmReceiver alarmReceiver = new AlarmReceiver();

    public static AlarmReceiver getInstance(){ return  alarmReceiver;}

  protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

  protected void addToCompositeDis(Disposable disposable) {
    mCompositeDisposable.add(disposable);
  }

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public void onReceive(final Context context, Intent intent) {
    //当系统到我们设定的时间点的时候会发送广播，执行这里


      Log.i("ninini", "hahah");
      if (intent.getAction().equals(SP_Action)) {
          PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                  new Intent(context, LexxicalResourceActivity.class), 0);
          // 通过Notification.Builder来创建通知，注意API Level
          // API16之后才支持
          Notification notify = new Notification.Builder(context)
                  .setSmallIcon(R.drawable.icon_lanuer)
                  .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                  .setContentTitle("雷哥托福：口语批改免费名额快来抢！")
                  .setContentText("每天5个免费的口语批改名额等你来抢")
                  .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
          // level16及之后增加的，API11可以使用getNotificatin()来替代
          notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
          // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
          NotificationManager manager = (NotificationManager) context
                  .getSystemService(Context.NOTIFICATION_SERVICE);
          manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示
      }else if (intent.getAction().equals(Ea_Action)){

          PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                  new Intent(context, EaLexxicalResourceActivity.class), 0);
          // 通过Notification.Builder来创建通知，注意API Level
          // API16之后才支持
          Notification notify = new Notification.Builder(context)
                  .setSmallIcon(R.drawable.icon_lanuer)
                  .setTicker("TickerText:" + "您有新短消息，请注意查收！")
                  .setContentTitle("雷哥托福：作文批改免费名额快来抢！")
                  .setContentText("每天3个免费的作文批改名额等你来抢")
                  .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
          // level16及之后增加的，API11可以使用getNotificatin()来替代
          notify.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
          // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
          NotificationManager manager = (NotificationManager) context
                  .getSystemService(Context.NOTIFICATION_SERVICE);
          manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示


      }

  }
}