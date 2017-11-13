package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.ui.xWidget.WithdrawMsgDialog;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 确认提现页面
 */

@Route(path = "/main/act/confirm_deposit")
public class ConfirmDepositActivity extends BaseAppcompatActivity {

    @BindView(R.id.mine_withdraw_WenXinImg)
    ImageView mineWithdrawWenXinImg;
    @BindView(R.id.mine_withdraw_alipayImg)
    ImageView mineWithdrawAlipayImg;
    @BindView(R.id.mine_withdraw_edt)
    EditText mineWithdrawEdt;
    @BindView(R.id.activity_mine_withdraw)
    LinearLayout activityMineWithdraw;
    @BindView(R.id.tv_all_balance)
    TextView tvAllBalance;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    private HttpServiceImpl service;

    private double amount=0;
    private WithdrawMsgDialog dialog;
    private UserBean bean;
    private int flag=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getUserData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_deposit;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.mine_withdraw_WenXin, R.id.mine_withdraw_alipay, R.id.mine_withdraw_Btnsure, R.id.mine_withdraw_back, R.id.tv_right_balance, R.id.tv_all_balance_click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_withdraw_WenXin:
                checkWeiXin();
                break;
            case R.id.mine_withdraw_alipay:
                checkAlipay();
                break;
            case R.id.mine_withdraw_Btnsure:
                confirmIsOrNot();
                break;
            case R.id.mine_withdraw_back:
                finish();
                break;
//            跳转余额明细
            case R.id.tv_right_balance:
                ARouter.getInstance().build("/main/act/balance_detail").navigation();
                break;
            case R.id.tv_all_balance_click:
                mineWithdrawEdt.setText(new DecimalFormat("######0.00").format(Double.parseDouble( tvBalance.getText().toString())));
                break;
        }
    }

    //选择支付宝
    private void checkAlipay() {
        mineWithdrawWenXinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        mineWithdrawAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        flag=2;
    }


    //选择微信
    private void checkWeiXin() {
        mineWithdrawWenXinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        mineWithdrawAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        flag=1;
    }

    //获取我的余额
    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("cond", "{id:" + UserAuthUtil.getUserId() + "}");
        map.put("jf", "userSpend");
        map.put("ctype", "user");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("Mybalance_id",UserAuthUtil.getUserId()+"");
                LogUtil.e("Mybalance_onSuccess", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONArray array = object.getJSONArray("resultList");
                            if (array != null && array.length() > 0) {
                                amount = array.getJSONObject(0).getJSONArray("userSpend").getJSONObject(0).getDouble("amount");
                                DecimalFormat df = new DecimalFormat("######0.00");
                                tvBalance.setText(df.format(amount));
                                tvAllBalance.setText("当前余额"+df.format(amount)+"元，");
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("Mybalance_JSONException", e.toString());
                    }


                }

            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("Mybalance_Throwable", ex.toString());
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
                        bean = new UserBean();
                        bean.setRealName(data.getString("realName"));
                        bean.setAlipayNumber(data.getString("alipayNumber"));

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


    //判断是否可以提现
    private void confirmIsOrNot() {
        String inputValue=mineWithdrawEdt.getText().toString();
        String realName=bean.getRealName();
        String alipayNumber=bean.getAlipayNumber();
        if(flag!=-1){
            if(!StringUtil.isBlank(inputValue)&&!inputValue.equals(".")){
                double inputMoney=Double.valueOf(inputValue);
                if(inputMoney>amount){
                    Toast.makeText(this, "余额不足，请重新输入！", Toast.LENGTH_SHORT).show();
                }else {
                    //开始提现
                    if(!StringUtil.isBlank(realName)&&!StringUtil.isBlank(alipayNumber)){//判断是否设置真实姓名
                        confirmBalance(realName,inputMoney);
                    }else {
                        Toast.makeText(this, "您还没有设置真实姓名或支付宝账号！", Toast.LENGTH_SHORT).show();
                        ARouter.getInstance().build("/main/act/UserInfoActivity").navigation();
                    }
                }
            }else {
                Toast.makeText(this, "请输入提现得金额！", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "请选择提现方式！", Toast.LENGTH_SHORT).show();
        }
    }


    //提现
    private void confirmBalance(String realName,double inputMoney){
        HashMap<String,String> body=new HashMap<>();
        body.put("realName",realName);
        body.put("amount",inputMoney+"");
        service.doCommonPost(null, MainUrl.ConfirmAli, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("confirmBalance",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            dialog=new WithdrawMsgDialog(ConfirmDepositActivity.this,R.style.Qr_dialog);
                            dialog.setCountent(object.getString("msg"));
                            dialog.onWithdrawMsgOnclick(new WithdrawMsgDialog.WithdrawMsgOnclick() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }else {
                            dialog=new WithdrawMsgDialog(ConfirmDepositActivity.this,R.style.Qr_dialog);
                            dialog.setImageResId(R.drawable.ic_defult_dialog);
                            dialog.setTitle("提现失败");
                            dialog.setCountent(object.getString("msg"));
                            dialog.onWithdrawMsgOnclick(new WithdrawMsgDialog.WithdrawMsgOnclick() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("confirmBalance",e.toString());
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


}
