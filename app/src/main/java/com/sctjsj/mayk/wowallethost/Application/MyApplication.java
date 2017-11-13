package com.sctjsj.mayk.wowallethost.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.app.BaseApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.push.DemoIntentService;
import com.sctjsj.mayk.wowallethost.push.DemoPushService;
import com.sctjsj.mayk.wowallethost.ui.activity.IndexActivity;
import com.sctjsj.mayk.wowallethost.ui.activity.SettingActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

/**
 * Created by lifuy on 2017/5/15.
 */

public class MyApplication extends BaseApplication{
    private SharedPreferences spf;
    @Override
    public void onCreate() {
        super.onCreate();

        // DemoPushService 为第三方自定义推送服务
        PushManager.getInstance().initialize(this.getApplicationContext(),DemoPushService.class);

        // DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);

        //bugly相关
        Beta.autoInit = true;
        Bugly.init(getApplicationContext(), "094435e46b", false);
        Beta.autoCheckUpgrade = true;
        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.defaultBannerId = R.mipmap.host_logo;
    }

    //存储是否接受推送
    public boolean getToggleButtonStatus(){
        spf=getSharedPreferences("SYSTEM",MODE_PRIVATE);
        boolean b=spf.getBoolean("ToggleButton",false);
        return b;
    }
    public void saveToggleButtonStatus(boolean b){
        spf=getSharedPreferences("SYSTEM",MODE_PRIVATE);
        spf.edit().putBoolean("ToggleButton",b).commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
