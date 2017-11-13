package com.sctjsj.mayk.wowallethost.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.MainLooper;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.event.PushBean;
import com.sctjsj.mayk.wowallethost.Application.MyApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.event.MessageEvent;
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.sctjsj.mayk.wowallethost.ui.activity.IndexActivity;
import com.sctjsj.mayk.wowallethost.ui.activity.MessageListActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(final Context context, GTTransmitMessage msg) {
        LogUtil.e("接收到了推送_clientid",msg.getClientId()+"");
        if (msg == null) {
            return;
        }

        if (!PushManager.getInstance().getClientid(this).equals(msg.getClientId())) {
            return;
        }else {

            byte[] b = msg.getPayload();

            if (b.length <= 0) {
                return;
            }
            final String data = new String(msg.getPayload());
            LogUtil.e("接收到了推送", data.toString());

            if (!StringUtil.isBlank(data)) {
                final PushBean pb = new Gson().fromJson(data, PushBean.class);
                if (pb.getType() == 2 && pb.getState() == 1) {
                    String content = pb.getContent();
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mBuilder.
                            setAutoCancel(true).
                            setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.host_logo)).
                            setContentTitle("新的订单").
                            setContentText(content).
                            setSmallIcon(R.mipmap.icon_small_message_white).
                            setDefaults(Notification.DEFAULT_SOUND).
                            setVibrate(new long[]{0l});

                    //点击的意图ACTION是跳转到Intent
                    Intent resultIntent = new Intent(this, MessageListActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pendingIntent);
                    mNotificationManager.notify(1, mBuilder.build());

                    EventBus.getDefault().post(new MessageEvent(new MessageBean()));
                }

                if(pb.getType()==2&&pb.getState()==4){
                    String content = pb.getContent();
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    mBuilder.
                            setAutoCancel(true).
                            setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.host_logo)).
                            setContentTitle("平台推送").
                            setContentText(content).
                            setSmallIcon(R.mipmap.icon_small_message_white).
                            setDefaults(Notification.DEFAULT_ALL).
                            setVibrate(new long[]{0l});

                    //点击的意图ACTION是跳转到Intent
                    Intent resultIntent = new Intent(this, MessageListActivity.class);
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pendingIntent);
                    mNotificationManager.notify(1, mBuilder.build());
//                    EventBus.getDefault().post(new MessageEvent(new MessageBean()));
                }
            }


        }

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}