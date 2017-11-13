package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//修改用户信息
@Route(path = "/main/act/UserInfoActivity")
public class UserInfoActivity extends BaseAppcompatActivity {

    @BindView(R.id.user_info_realName)
    TextView userInfoRealName;
    @BindView(R.id.user_info_alipayNumber)
    TextView userInfoAlipayNumber;
    private HttpServiceImpl service;
    private UserBean bean;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    UserBean bean = (UserBean) msg.obj;
                    if (!StringUtil.isBlank(bean.getRealName())) {
                        userInfoRealName.setText(bean.getRealName());
                    } else {
                        userInfoRealName.setText("暂未设置");
                    }
                    if (!StringUtil.isBlank(bean.getAlipayNumber())) {
                        userInfoAlipayNumber.setText(bean.getAlipayNumber());
                    } else {
                        userInfoAlipayNumber.setText("暂未设置");
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.user_info_realNameLayout, R.id.user_info_alipayLayout, R.id.setting_back,R.id.user_info_changeLoginPwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_info_realNameLayout:
                ARouter.getInstance().build("/main/act/ChangeRealNameActivity").navigation();
                break;
            case R.id.user_info_alipayLayout:
                ARouter.getInstance().build("/main/act/ChangeUserAlipayActivity").navigation();
                break;
            case R.id.setting_back:
                finish();
                break;
            //修改登陆密码
            case R.id.user_info_changeLoginPwd:
                ARouter.getInstance().build("/main/act/user/change_login_pwd").navigation();
                break;
        }
    }


    //获取用户信息
    private void getUserData() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", UserAuthUtil.getUserId() + "");
        body.put("jf", "photo");
        service.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUserData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        UserBean bean = new UserBean();
//                        bean.setUsername(data.getString("username"));
//                        bean.setSex(data.getInt("sex"));
//                        bean.setEmail(data.getString("email"));
//                        bean.setPhone(data.getString("phone"));
                        bean.setRealName(data.getString("realName"));
                        bean.setAlipayNumber(data.getString("alipayNumber"));
//                        bean.setUrl(data.getJSONObject("photo").getString("url"));
                        Message message = new Message();
                        message.what = 2;
                        message.obj = bean;
                        handle.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

    @Subscribe
    public void onEventMainThread(String s) {
        if (s.equals("out")) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
