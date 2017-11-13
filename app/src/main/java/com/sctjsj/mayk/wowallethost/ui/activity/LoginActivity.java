package com.sctjsj.mayk.wowallethost.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_ERROR;
import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_FAILED;
import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_SUCCESS;
import static com.sctjsj.basemodule.core.config.Tag.TAG_LOGIN_RESULT;

@Route(path = "/main/act/login")
public class LoginActivity extends BaseAppcompatActivity {

    @BindView(R.id.login_edt_user)
    EditText loginEdtUser;
    @BindView(R.id.login_edt_pass)
    EditText loginEdtPass;
    @BindView(R.id.login_bt)
    Button loginBt;
    @BindView(R.id.login_lin_contract)
    LinearLayout loginLinContract;
    @BindView(R.id.act_login_tv_to_forget_pwd)
    TextView actLoginTvToForgetPwd;
  /*  @BindView(R.id.act_login_tv_to_register1)
    TextView actLoginTvToRegister1;*/
    private HttpServiceImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if(UserAuthUtil.isUserLogin()){
            ARouter.getInstance().build("/main/act/index").navigation();
            finish();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.login_bt, R.id.login_lin_contract, R.id.act_login_tv_to_forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //登陆
            case R.id.login_bt:
                if(reNameAndPas()){
                    logIn();
                }
                break;
            case R.id.login_lin_contract:
                break;
            //忘记密码
            case R.id.act_login_tv_to_forget_pwd:
                ARouter.getInstance().build("/main/act/forget_pwd").navigation();
                break;
           /* //注册
            case R.id.act_login_tv_to_register1:
                ARouter.getInstance().build("/main/act/regist1").navigation();
                break;*/
        }
    }


    private boolean reNameAndPas(){
        if(!TextUtils.isEmpty(loginEdtUser.getText().toString())
                &&!TextUtils.isEmpty(loginEdtPass.getText().toString())){
            return true;
        }else {
            return islogin();
        }
    }

    //登陆
    private void  logIn(){
        HashMap<String,String> body=new HashMap<>();
        body.put("username",loginEdtUser.getText().toString());
        body.put("password",loginEdtPass.getText().toString());
        String cid=PushManager.getInstance().getClientid(this);
        if(StringUtil.isBlank(cid)){
            Toast.makeText(this, "clientId 获取失败", Toast.LENGTH_SHORT).show();
            return;
        }
        body.put("cid",cid);
        Log.e("登陆结果", body.toString());
            service.doCommonPost(null, MainUrl.loginUrl, body, new XProgressCallback() {
                @Override
                public void onSuccess(String resultStr) {
                    Log.e("loginonSuccess", resultStr.toString());
                    if (!StringUtil.isBlank(resultStr)) {

                        try {
                            JSONObject obj = new JSONObject(resultStr);
                            String infoStr = "";
                            if (obj.has("info")) {
                                infoStr = obj.getString("info");
                            }
                            String resultMsg = obj.getString("resultMsg");
                            boolean result = obj.getBoolean("result");


                            if (result) {

                                JSONObject info = new JSONObject(infoStr);
                                int userId = info.getInt("id");
                                String username = info.getString("username");
                                int agent=info.getInt("agent");
                                //店铺
                                JSONObject store = info.getJSONObject("store");
                                int invitationCode = info.getInt("invitationCode");
                                int storeId = store.getInt("id");
                                double latitude = store.getDouble("latitude");
                                double longitude = store.getDouble("longitude");
                                String name = store.getString("name");
                                double score = store.getDouble("score");
                                String storeAddress = store.getString("storeAddress");
                                String telephone = store.getString("telephone");
                                String qrCode = store.getString("qrCode");

                                String storeLogoStr = store.getString("storeLogo");
                                String logoUrl = null;
                                int logoId = -1;
                                if (!StringUtil.isBlank(storeLogoStr)) {
                                    logoUrl = store.getJSONObject("storeLogo").getString("url");
                                    logoId = store.getJSONObject("storeLogo").getInt("id");
                                }

                                int storeStatus = store.getInt("storeStatus");

                                String detail = store.getString("detail");
                                StoreBean sb = new StoreBean();
                                sb.setId(storeId);
                                sb.setLatitude(latitude);
                                sb.setLongitude(longitude);
                                sb.setDetail(detail);
                                sb.setName(name);
                                sb.setStoreAddress(storeAddress);
                                sb.setTelephone(telephone);
                                sb.setScore(score);
                                sb.setLogo(logoUrl);
                                sb.setLogoId(logoId);
                                sb.setStoreStatus(storeStatus);
                                sb.setQrCode(qrCode);

                                UserBean ub = new UserBean();
                                ub.setId(userId);
                                ub.setUsername(username);
                                ub.setStoreBean(sb);
                                ub.setAgent(agent);

                                UserAuthUtil.saveUserBean(ub);

                                Toast.makeText(LoginActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                                //跳转主页
                                ARouter.getInstance().build("/main/act/index").navigation();

                                //发送本地广播通知拦截器
                                Intent intent = new Intent(Tag.TAG_LOGIN_FILTER);
                                intent.putExtra(TAG_LOGIN_RESULT, CODE_LOGIN_SUCCESS);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, resultMsg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Tag.TAG_LOGIN_FILTER);
                                intent.putExtra(TAG_LOGIN_RESULT, CODE_LOGIN_FAILED);
                                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            LogUtil.e("login_JSONException",e.toString());
                            Toast.makeText(LoginActivity.this, "用户信息异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Toast.makeText(LoginActivity.this, "登录异常", Toast.LENGTH_SHORT).show();
                    LogUtil.e("loginonError",ex.toString());
                    Intent intent = new Intent(Tag.TAG_LOGIN_FILTER);
                    intent.putExtra(TAG_LOGIN_RESULT, CODE_LOGIN_ERROR);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    dismissLoading();
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    showLoading(false, "");
                }

                @Override
                public void onLoading(long total, long current) {

                }
            });

    }

    private boolean islogin(){
        if(StringUtil.isBlank(loginEdtUser.getText().toString())){
            Toast.makeText(this, "请输入账号", Toast.LENGTH_SHORT).show();
            return false;
        }else if(StringUtil.isBlank(loginEdtPass.getText().toString())){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;
    }
}
