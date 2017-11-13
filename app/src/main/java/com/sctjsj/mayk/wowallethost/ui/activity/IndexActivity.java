package com.sctjsj.mayk.wowallethost.ui.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.MainLooper;
import com.sctjsj.mayk.wowallethost.Application.MyApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.event.DataEvent;
import com.sctjsj.mayk.wowallethost.event.MessageEvent;
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.sctjsj.mayk.wowallethost.presenter.IIndexPresenter;
import com.sctjsj.mayk.wowallethost.presenter.impl.IndexPresenterImpl;
import com.sctjsj.mayk.wowallethost.util.UpdateUtil;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
//主页
@Route(path = "/main/act/index")
public class IndexActivity extends BaseAppcompatActivity {

    @BindView(R.id.act_index_rb_home)
    RadioButton actIndexRbHome;
    @BindView(R.id.act_index_rb_order)
    RadioButton actIndexRbOrder;
    @BindView(R.id.act_index_rb_shop)
    RadioButton actIndexRbShop;
    @BindView(R.id.act_index_rb_own)
    RadioButton actIndexRbOwn;
    private DataEvent dataEvent;

    private IIndexPresenter indexPresenter;
    /**退出相关**/
    private boolean isExited = false;//标志是否已经退出
    private Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isExited = false;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        if(UserAuthUtil.isUserLogin()){
            /**初始化 indexPresenter**/
            if(indexPresenter==null){
                indexPresenter=new IndexPresenterImpl(this,getSupportFragmentManager());
            }

            /**初始化布局**/
            initView();
        }else {
            ARouter.getInstance().build("/main/act/login").navigation();
            finish();
        }


    }

    @Override
    public int initLayout() {
        return R.layout.activity_index;
    }

    @Override
    public void reloadData() {

    }

    /**
     * 为 RadioButton 设置 selector
     * @param mRadioButton
     * @param id
     */
    private void setDrawable(RadioButton mRadioButton, int id) {
        Drawable mDrawable=getResources().getDrawable(id);
        //设置图标大小
        mDrawable.setBounds(0,0,50,50);
        mRadioButton.setCompoundDrawables(null,mDrawable,null,null);
    }

    private void initView(){
        //登陆成功自动检查版本
//        UpdateUtil.getInstance(this).checkVersion();
//        Beta.checkUpgrade();

        setDrawable(actIndexRbHome,R.drawable.x_act_index_nav_home_selector);
        setDrawable(actIndexRbOrder,R.drawable.x_act_index_nav_order_selector);
        setDrawable(actIndexRbShop,R.drawable.x_act_index_nav_shops_selector);
        setDrawable(actIndexRbOwn,R.drawable.x_act_index_nav_own_selector);
    }

    /**点击 RB 切换 Frg**/
    @OnCheckedChanged({R.id.act_index_rb_home,R.id.act_index_rb_shop,
            R.id.act_index_rb_order,R.id.act_index_rb_own})
    public void indexNavRBCheckedChanged(RadioButton radioButton,boolean checked){
        switch (radioButton.getId()){
            //首页
            case R.id.act_index_rb_home:
                if(checked){
                    indexPresenter.replaceFragment("HOME",0);
                }
                break;
            //店铺
            case R.id.act_index_rb_order:
                if(checked){
                    indexPresenter.replaceFragment("ORDER",0);
                }
                break;
            //朋友
            case R.id.act_index_rb_shop:
                if(checked){
                    indexPresenter.replaceFragment("STORE",0);
                }
                break;
            //我的
            case R.id.act_index_rb_own:
                if(checked){
                    indexPresenter.replaceFragment("OWN",0);
                }
                break;
        }
    }

    //获取透传消息
    @Subscribe
    public void onEventMainThread(MessageEvent event) {
        LogUtil.e("event","onEventMainThread");
        MainLooper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //震动  获得 一个震动的服务
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if(((MyApplication)getApplicationContext()).getToggleButtonStatus()){
                    vibrator.vibrate(500);
                }

                //提示音
                SoundPool soundPool= new SoundPool(10, AudioManager.STREAM_SYSTEM,5);
                final int load = soundPool.load(IndexActivity.this, R.raw.pixiedust, 1);
                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        soundPool.play(load,1, 1, 0, 0, 1);
                    }
                });

            }
        });
    }



    @Subscribe
    public void onEventMainThread(DataEvent event) {
         if(event!=null){
             indexPresenter.replaceFragment(event.getDetaBean().getContent(),event.getDetaBean().getType());
             actIndexRbOrder.setChecked(true);
         }

     }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //物理返回键值
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //连续点击两次返回键退出程序
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出程序
     */
    private void exit() {
        if (!isExited) {
            isExited = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessageDelayed(msg, 2000);
        } else {
            finish();

//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
