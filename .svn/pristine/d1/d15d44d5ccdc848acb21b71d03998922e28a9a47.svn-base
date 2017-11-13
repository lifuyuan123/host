package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// 账单明细
@Route(path = "/main/act/user/bill_detail")
public class BillDetailActivity extends BaseAppcompatActivity {
    @Autowired(name = "id")
    int id;
    @Autowired(name="key")
    int type=-1;

    @BindView(R.id.billing_iv_shopicon)CircleImageView civStoreLogo;
    @BindView(R.id.act_bill_detail_tv_storeName)TextView tvStoreName;
    @BindView(R.id.money)TextView tvPayValue;
    @BindView(R.id.billing_tv_istransaction)TextView tvStatus;
    @BindView(R.id.billing_tv_payment_method)TextView tvPayAccount;
    @BindView(R.id.billing_tv_account)TextView tvGatheringAccount;
    @BindView(R.id.billing_tv_time)TextView tvInsertTime;
    @BindView(R.id.billing_tv_order_number)TextView tvOrderNum;
    @BindView(R.id.billing_tv_transfer_instructions)TextView tvDescribe;
    @BindView(R.id.rl_order_num)RelativeLayout rlOrderNum;
    private HttpServiceImpl http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if (-1 != id) {
            queryBillDetail(id);
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_bill_detail;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.shops_linear_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 查询账单详情
     *
     * @param id
     */
    public void queryBillDetail(final int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ofId",String.valueOf(id));
        map.put("jf","flowOrder|storeTbl|manager|storeLogo|user|photo|ifPayment|incomeUser|expenditureUser");
        http.doCommonPost(null, MainUrl.queryBill, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("deatil_onSuccess",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){

                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray array=obj.getJSONArray("transFlow");
                        if(array!=null&&array.length()>0){
                        JSONObject data=array.getJSONObject(0);
                        if(data!=null) {
                            //操作金额
                            double amount = data.getDouble("amount");
                            //创建时间
                            String insertTime = data.getString("insertTime");
                            tvInsertTime.setText(insertTime);
//***************************根据流水类型获取交易对方账户信息***********************************************************************************
                            //交易类型
                            int fType = data.getInt("fType");
                            int id = data.getJSONObject("incomeUser").getInt("id");
                            if (UserAuthUtil.getUserId() == id) {
                                fType = 1;
                            }
                            //收入金流
                            if (1 == fType) {
                                tvPayValue.setText("+" + new DecimalFormat("######0.00").format(amount));
                                JSONObject expend = data.getJSONObject("expenditureUser");
                                String expendLogo = null;
                                String expendStr = expend.getString("photo");
                                if (!StringUtil.isBlank(expendStr)) {
                                    expendLogo = expend.getJSONObject("photo").getString("url");
//                                    PicassoUtil.getPicassoObject().
                                    Glide.with(BillDetailActivity.this).
                                            load(expendLogo).
//                                            resize(DpUtils.dpToPx(BillDetailActivity.this, 80), DpUtils.dpToPx(BillDetailActivity.this, 80)).
                                            error(R.mipmap.icon_load_faild).into(civStoreLogo);
                                }

                                String username = expend.getString("username");
                                tvStoreName.setText(username);
                            }

                            //支出金流
                            if (2 == fType) {
                                tvPayValue.setText("-" + new DecimalFormat("######0.00").format(amount));

                                JSONObject income = data.getJSONObject("incomeUser");
                                String incomeLogo = null;
                                String incomeStr = income.getString("photo");
                                if (!StringUtil.isBlank(incomeStr)) {
                                    incomeLogo = income.getJSONObject("photo").getString("url");
//                                    PicassoUtil.getPicassoObject().
                                    Glide.with(BillDetailActivity.this).
                                            load(incomeLogo).
//                                            resize(DpUtils.dpToPx(BillDetailActivity.this, 80), DpUtils.dpToPx(BillDetailActivity.this, 80)).
                                            error(R.mipmap.icon_load_faild).into(civStoreLogo);
                                }

                                String username = income.getString("username");
                                tvStoreName.setText(username);

                            }

//******************************根据 tTypeT 判断交易类型* 1-内部交易，平台帐户金额不变；2-接口交易，平台帐户金额会变化*******************************************************************************
                            int tType = data.getInt("tType");
                            //无订单
//                            if(1==tType){
//                                rlOrderNum.setVisibility(View.GONE);
                            JSONObject order = data.getJSONObject("flowOrder");
                            double paymentRate=order.getJSONObject("storeTbl").getDouble("paymentRate");
                            //订单号
                            String orderNum = order.getString("name");
                            tvOrderNum.setText(orderNum);
                            //营业额
                            tvPayAccount.setText(new DecimalFormat("######0.00").format(amount) + "");
                            //平台收入
                            JSONArray array1=obj.getJSONArray("orderset");
                            JSONObject object=array1.getJSONObject(0);
                            double platform=object.getDouble("charges")+object.getDouble("rebateFee")+object.getDouble("shareFee");
                            tvDescribe.setText(new DecimalFormat("######0.00").format(platform)+"");
                            //   商家收入
                            tvGatheringAccount.setText(new DecimalFormat("######0.00").format((amount-platform))+"");
                        }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("deatil_JSONException",e.toString());
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("deatil_onError",ex.toString());
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
                showLoading(true, "正在加载中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


}
