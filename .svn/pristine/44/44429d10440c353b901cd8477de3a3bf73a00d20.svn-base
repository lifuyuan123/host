package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.pay.OrderBean;
import com.sctjsj.mayk.wowallethost.pay.StoreBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


/***
 * 支付成详情页
 */

@Route(path = "/main/act/PayDetailActivity")
public class PayDetailActivity extends BaseAppcompatActivity {

    @BindView(R.id.pay_detail_back_rl)
    RelativeLayout payDetailBackRl;
    @BindView(R.id.pay_detail_storeIcon)
    ImageView payDetailStoreIcon;
    @BindView(R.id.pay_detail_storeName)
    TextView payDetailStoreName;
    @BindView(R.id.pay_detail_Money)
    TextView payDetailMoney;
    @BindView(R.id.pay_detial_payWay)
    TextView payDetialPayWay;
    @BindView(R.id.pay_detail_insterTime)
    TextView payDetailInsterTime;
    @BindView(R.id.Pay_detial_orderName)
    TextView PayDetialOrderName;
    @BindView(R.id.activity_pay_detail)
    LinearLayout activityPayDetail;
    private HttpServiceImpl service;
    @Autowired(name = "OrderId")
    int OrderId=-1;
    @Autowired(name = "payWay")
    int payWay=-1;
    //区分订单类型 1为普通订单  2为购买代理店铺 3为购买分享者
    @Autowired(name = "Flag")
    int Flag=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderMsg();
    }

    //刷新控件
    private void flushView(OrderBean bean) {
        if(null!=bean){

            payDetailMoney.setText(bean.getTotalprice()+"");
            switch (payWay){
                case 1:
                    payDetialPayWay.setText("支付宝付款");
                    break;
                case 2:
                    payDetialPayWay.setText("微信付款");
                    break;
            }
            payDetailInsterTime.setText(bean.getPaytime());
            PayDetialOrderName.setText(bean.getName());
        }

    }


    @Override
    public int initLayout() {
        return R.layout.activity_pay_detail;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.pay_detail_back_rl)
    public void onViewClicked() {
        finish();
    }

    //获取订单详情
    private void  getOrderMsg(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype", "orderform");
        body.put("id", OrderId + "");
        body.put("jf", "storeTbl|storeLogo");
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getOrderMsg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject data=new JSONObject(result);
                        if(data.getBoolean("result")){
                            JSONObject object=data.getJSONObject("data");
                            OrderBean bean=new OrderBean();
                            bean.setState(object.getInt("state"));
                            bean.setTotalprice(object.getDouble("totalprice"));
                            bean.setName(object.getString("name"));
                            bean.setPaytime(object.getString("insertTime"));
                            if(Flag!=1){
                                payDetailStoreIcon.setImageResource(R.mipmap.host_logo);
                                payDetailStoreName.setText("平台");
                            }else {
                                StoreBean storeBean=new StoreBean();
                                JSONObject store=object.getJSONObject("storeTbl");
                                storeBean.setName(store.getString("name"));
                                payDetailStoreName.setText(store.getString("name"));
                                storeBean.setLogo(store.getJSONObject("storeLogo").getString("url"));
                                bean.setBean(storeBean);
//                                PicassoUtil.getPicassoObject().
                                Glide.with(PayDetailActivity.this).
                                        load(bean.getBean().getLogo())
//                                        .resize(DpUtils.dpToPx(PayDetailActivity.this,80), DpUtils.dpToPx(PayDetailActivity.this,80))
                                        .into(payDetailStoreIcon);
                            }
                            flushView(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getOrderMsgJson",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getOrderMsgJson",ex.toString());
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
                showLoading(true,"加载中。。");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }




}
