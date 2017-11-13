package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
@Route(path = "/main/act/forget_pwd")
public class ForgetPwdActivity extends BaseAppcompatActivity {

    @BindView(R.id.edt_phoneNum)
    EditText edtPhoneNum;
    @BindView(R.id.edt_piccode)
    EditText edtPiccode;
    @BindView(R.id.rela_picCode)
    RelativeLayout relaPicCode;
    @BindView(R.id.act_forget_pwd_btn_to_next)
    Button actForgetPwdBtnToNext;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    private HttpServiceImpl service;

    private OkHttpClient okHttpClient;
    private Glide glide;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        // 获取图片验证码
        getPicVerifyCode();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.rela_back, R.id.act_forget_pwd_btn_to_next, R.id.rela_picCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rela_back:
                finish();
                break;
            //下一步
            case R.id.act_forget_pwd_btn_to_next:
                //验证手机号码  然后判断验证码
                isOccupy();
                break;
            //切换图片验证码
            case R.id.rela_picCode:
                getPicVerifyCode();
                break;
        }
    }

    //判断电话号码是否被占用
    private void isOccupy() {
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPhoneNum.getText().toString())) {
            body.put("phoneNumber", edtPhoneNum.getText().toString());
            service.doCommonPost(null, MainUrl.isOccupy, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_onSuccess", result.toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                        boolean aBoolean = obj.getBoolean("result");
                        //判断注册的电话号码是否可用
                        if (aBoolean) {
                            //图片验证码是否正确
                            Toast.makeText(ForgetPwdActivity.this, "用户未注册，请确认。", Toast.LENGTH_SHORT).show();
                        } else {
                            getPhoenCode();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_JSONException", e.toString());
                    }

                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_onFinished", "onFinished");
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
        } else {
            Toast.makeText(this, "请输入电话号码。", Toast.LENGTH_SHORT).show();
        }

    }


    //获取短信验证码
    private void getPhoenCode() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", cookie);
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPiccode.getText().toString())) {
            body.put("code", edtPiccode.getText().toString());
            body.put("mobile", edtPhoneNum.getText().toString());
            service.doCommonPost(headers, MainUrl.getPhoenCode, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_getphcode_onSuccess", result.toString());
                    try {
                        JSONObject object = new JSONObject(result);
                        boolean result1 = object.getBoolean("result");
                        if (result1) {
                            ARouter.getInstance().build("/main/act/forgetpass")
                                    .withString("cookie", cookie)
                                    .withString("phone", edtPhoneNum.getText().toString()).navigation();
                            Toast.makeText(ForgetPwdActivity.this, object.getString("resultMsg"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetPwdActivity.this, "图片验证码错误。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_getphcode_JSONException", e.toString());
                    }

                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_getphcode_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_getphcode_onFinished", "onFinished");
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
        } else {
            Toast.makeText(this, "请填写图片验证码。", Toast.LENGTH_SHORT).show();
        }

    }


    private void getPicVerifyCode() {
        relaPicCode.removeAllViews();
        ImageView view = new ImageView(this);

        OkHttpClient client = new OkHttpClient();
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        final OkHttpDownloader downloader = new OkHttpDownloader(client);

        Picasso picasso = new Picasso.Builder(ForgetPwdActivity.this).downloader(downloader).build();
        picasso.load(MainUrl.getPicCode).skipMemoryCache().into(view, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                List<HttpCookie> list = cookieManager.getCookieStore().getCookies();

                //获取此次请求的cookie
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).toString().contains("JSESSIONID=")) {
                            cookie = list.get(i).toString();
                            LogUtil.e("cookie-get", cookie);
                        }
                    }
                }
            }

            @Override
            public void onError() {
                Snackbar.make(coordinatorLayout, "图片验证码加载失败", Snackbar.LENGTH_SHORT).show();
            }
        });
        relaPicCode.addView(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }


}
