package com.sctjsj.mayk.wowallethost.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.TimePickerView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.AllOrderFromAdapter;
import com.sctjsj.mayk.wowallethost.adapter.BillAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.BillBean;
import com.sctjsj.mayk.wowallethost.model.javabean.IndentBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账单页面
 */
@Route(path = "/main/act/BillActivity")
public class BillActivity extends BaseAppcompatActivity {

    @BindView(R.id.mine_bill_back)
    RelativeLayout mineBillBack;

    @BindView(R.id.bill_Txtday)
    TextView billTxtday;

//    @BindView(R.id.bill_Txtexpend)
//    TextView billTxtexpend;

    @BindView(R.id.bill_Txtincome)
    TextView billTxtIncome;

    @BindView(R.id.bill_Imgdate)
    LinearLayout billImgdate;

    @BindView(R.id.act_bill_rv)
    RecyclerView mRV;

    @BindView(R.id.bill_img)
    ImageView billImg;
    @BindView(R.id.act_bill_refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.order_tv_today)
    TextView orderTvToday;
    @BindView(R.id.order_iv_today)
    ImageView orderIvToday;
    @BindView(R.id.order_tv_today_bottom)
    TextView orderTvTodayBottom;
    @BindView(R.id.order_tv_all)
    TextView orderTvAll;
    @BindView(R.id.order_iv_all)
    ImageView orderIvAll;
    @BindView(R.id.order_tv_all_bottom)
    TextView orderTvAllBottom;
    @BindView(R.id.order_rel_today)
    RelativeLayout orderRelToday;
    @BindView(R.id.lin_basic_bill)
    LinearLayout linBasicBill;
    @BindView(R.id.orderNo_layout)
    LinearLayout orderNoLayout;
    @BindView(R.id.rel_item)
    RelativeLayout relItem;
    private boolean flag = false;//true  普通账单   false 代理商账单


    private HttpServiceImpl http;
    private BillAdapter adapter;
    private List<BillBean> data = new ArrayList<>();
    private List<IndentBean> datas = new ArrayList<>();
    private AllOrderFromAdapter adapters;
    private int index = 1;
    private String currentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        initRV();

        /**
         * 默认查询当前月份的账单
         */
        DateFormat format = new SimpleDateFormat("yyyy-MM");
        currentMonth = format.format(new Date());
        chooseBaseBill();


    }

    private void initRV() {
        adapter = new BillAdapter(BillActivity.this, data);
        adapters=new AllOrderFromAdapter(this,datas);
        mRV.setLayoutManager(new LinearLayoutManager(BillActivity.this));
        mRV.setAdapter(adapter);
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if (flag) {
                    pullDeviendfans(currentMonth);
                } else {
                    queryBillByMonth(currentMonth);
                }

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (flag) {
                    pullDeviendfanMore(currentMonth);
                } else {
                    queryMoreBillByMonth(currentMonth);
                }

            }
        });
    }

    /**
     * 按月份查询账单
     *
     * @param month
     */
    public void queryBillByMonth(String month) {
        index = 1;
        data.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(index));
        map.put("time", month);
//        map.put("orderby","id desc");
        map.put("pageSize", 8 + "");
        map.put("storeId", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("jf", "flowUser|orderformSettle|photo");
        http.doCommonPost(null, MainUrl.queryStoreBill, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("账单id", UserAuthUtil.getStoreId() + "");
                LogUtil.e("账单", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        index++;
                        JSONObject obj = new JSONObject(resultStr);
                        //查询月份
                        String month = obj.getString("time");
                        billTxtday.setText(month);
                        //总支出
//                        Double totAmount=obj.getDouble("totExpenditureAmount");
//                        billTxtexpend.setText("总支出￥"+totAmount);
                        //总收入 monthTurnover  todayTurnover
                        Double totIncomeAmount = obj.getDouble("monthTurnover");
                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));
                        JSONArray array = obj.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            BillBean billBean = new BillBean();
                            billBean.setId(object.getJSONObject("orderformSettle").getInt("id"));
                            billBean.setInsertTime(object.getJSONObject("orderformSettle").getString("insertTime"));

                            //平台收入
                            double PlatformIncome = object.getDouble("charges") + object.getDouble("shareFee") + object.getDouble("rebateFee");
                            billBean.setPlatformIncome(PlatformIncome);
                            //营业额
                            billBean.setUrnover(object.getDouble("payValue"));
                            //商家收入
                            billBean.setAmount(object.getDouble("payValue") - PlatformIncome);

                            billBean.setDesc(object.getJSONObject("flowUser").getString("username"));

                            billBean.setUrl(object.getJSONObject("flowUser").getJSONObject("photo").getString("url"));
                            data.add(billBean);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("账单JSONException", e.toString());
                    } finally {
                        if (data.size() <= 0) {
                            linBasicBill.setVisibility(View.GONE);
                            relItem.setVisibility(View.GONE);
                            orderNoLayout.setVisibility(View.VISIBLE);
                        }else {
                            linBasicBill.setVisibility(View.VISIBLE);
                            orderNoLayout.setVisibility(View.GONE);
                            relItem.setVisibility(View.VISIBLE);
                        }
                        mRV.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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
                dismissLoading();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    public void queryMoreBillByMonth(String month) {

        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(index));
        map.put("time", month);
//        map.put("orderby","id desc");
        map.put("pageSize", 8 + "");
        map.put("storeId", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("jf", "flowUser|orderformSettle|photo");
        http.doCommonPost(null, MainUrl.queryStoreBill, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("账单more", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //查询月份
                        String month = obj.getString("time");
                        billTxtday.setText(month);
                        //总支出
//                        Double totAmount=obj.getDouble("totExpenditureAmount");
////                        billTxtexpend.setText("总支出￥"+totAmount);
//
                        //总收入
                        Double totIncomeAmount = obj.getDouble("monthTurnover");
                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));
                        if (index > 1) {
                            index++;
                        }
                        JSONArray array = obj.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            BillBean billBean = new BillBean();
                            billBean.setId(object.getJSONObject("orderformSettle").getInt("id"));
                            billBean.setInsertTime(object.getJSONObject("orderformSettle").getString("insertTime"));

                            //平台收入
                            double PlatformIncome = object.getDouble("charges") + object.getDouble("shareFee") + object.getDouble("rebateFee");
                            billBean.setPlatformIncome(PlatformIncome);
                            //营业额
                            billBean.setUrnover(object.getDouble("payValue"));
                            //商家收入
                            billBean.setAmount(object.getDouble("payValue") - PlatformIncome);

                            billBean.setDesc(object.getJSONObject("flowUser").getString("username"));

                            billBean.setUrl(object.getJSONObject("flowUser").getJSONObject("photo").getString("url"));
                            data.add(billBean);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
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
                dismissLoading();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_bill;
    }

    @Override
    public void reloadData() {

    }



    //加载分红数据
    private void pullDeviendfans(String month) {
        datas.clear();
        index = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", String.valueOf(index));
        body.put("time", month);
//        map.put("orderby","id desc");
        body.put("pageSize", 8 + "");
        body.put("userId", String.valueOf(UserAuthUtil.getUserId()));
        body.put("jf", "flowOrder|user");
        LogUtil.e("pullDeviendfans___ id",UserAuthUtil.getUserId()+"");
        http.doCommonPost(null, MainUrl.queryBillFans, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                index++;
                if (!StringUtil.isBlank(result)) {
                    try {
                        Log.e("pullDeviendfans___", result.toString());
                        JSONObject object = new JSONObject(result);
                        JSONArray arry=object.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                IndentBean bean=new IndentBean();
                                JSONObject fanObj=arry.getJSONObject(i);
                                bean.setFinishTime(fanObj.getString("insertTime"));
                                bean.setPayValue(fanObj.getInt("amount"));
                                bean.setName(fanObj.getJSONObject("flowOrder").getJSONObject("user").getString("username"));
                                bean.setRebateValue(fanObj.getJSONObject("flowOrder").getInt("payValue"));
                                datas.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullDeviendfans_JSONE", result.toString());
                    } finally {
                        linBasicBill.setVisibility(View.GONE);
                        if(datas.size()<=0){
                            orderNoLayout.setVisibility(View.VISIBLE);
                            relItem.setVisibility(View.GONE);
                        }else {
                            relItem.setVisibility(View.VISIBLE);
                        }
                        mRV.setAdapter(adapters);
                        adapters.notifyDataSetChanged();
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
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                    dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                    showLoading(true, "加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }



    //加载分红数据
    private void pullDeviendfanMore(String month) {
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", String.valueOf(index));
        body.put("time", month);
//        map.put("orderby","id desc");
        body.put("pageSize", 8 + "");
        body.put("userId", String.valueOf(UserAuthUtil.getUserId()));
        body.put("jf", "flowOrder|user");
        http.doCommonPost(null, MainUrl.queryBillFans, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    index++;
                    try {
                        Log.e("pullDeviendfans", result.toString());
                        JSONObject object = new JSONObject(result);
                        JSONArray arry=object.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                IndentBean bean=new IndentBean();
                                JSONObject fanObj=arry.getJSONObject(i);
                                bean.setFinishTime(fanObj.getString("insertTime"));
                                bean.setPayValue(fanObj.getInt("amount"));
                                bean.setName(fanObj.getJSONObject("flowOrder").getJSONObject("user").getString("username"));
                                datas.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapters.notifyDataSetChanged();
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
                dismissLoading();
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
              showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


//    /**
//     * 按月份查询账单(代理商账单)
//     *
//     * @param month
//     */
//    public void queryBillByMonthAgent(String month) {
//        index = 1;
//        data.clear();
//        HashMap<String, String> map = new HashMap<>();
//        map.put("pageIndex", String.valueOf(index));
//        map.put("time", month);
////        map.put("orderby","id desc");
//        map.put("pageSize", 8 + "");
//        map.put("userId", String.valueOf(UserAuthUtil.getUserId()));
//        map.put("jf", "flowUser|orderformSettle|photo|user");
//        http.doCommonPost(null, MainUrl.queryStoreBillAgent, map, new XProgressCallback() {
//            @Override
//            public void onSuccess(String resultStr) {
//                LogUtil.e("账单agentid", UserAuthUtil.getUserId() + "");
//                LogUtil.e("账单agent", resultStr.toString());
//                if (!StringUtil.isBlank(resultStr)) {
//                    try {
//                        index++;
//                        JSONObject obj = new JSONObject(resultStr);
//                        //查询月份
//                        String month = obj.getString("time");
//                        billTxtday.setText(month);
//                        Double totIncomeAmount = obj.getDouble("monthTurnover");
//                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));
//                        JSONArray array = obj.getJSONArray("resultList");
//                        for (int i = 0; i < array.length(); i++) {
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        LogUtil.e("账单JSONException", e.toString());
//                    } finally {
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Throwable ex) {
//
//            }
//
//            @Override
//            public void onCancelled(Callback.CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//                refreshLayout.finishRefresh();
//                refreshLayout.finishRefreshLoadMore();
//            }
//
//            @Override
//            public void onWaiting() {
//
//            }
//
//            @Override
//            public void onStarted() {
//
//            }
//
//            @Override
//            public void onLoading(long total, long current) {
//
//            }
//        });
//    }




    @OnClick({R.id.mine_bill_back, R.id.bill_Imgdate, R.id.order_rel_today, R.id.order_rel_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_bill_back:
                finish();
                break;
            //弹出选择时间
            case R.id.bill_Imgdate:
                popMonthChoose();
                break;
            //选择营业账单
            case R.id.order_rel_today:
                chooseBaseBill();
                break;
            //选择分销账单
            case R.id.order_rel_all:
                chooseDistributionBill();
                break;
        }
    }


    //选择营业账单
    private void chooseBaseBill() {
        orderTvToday.setTextColor(getResources().getColor(R.color.color_deep_blue));
        orderTvTodayBottom.setVisibility(View.VISIBLE);
        orderIvToday.setVisibility(View.VISIBLE);
        orderTvAll.setTextColor(getResources().getColor(R.color.color_text));
        orderTvAllBottom.setVisibility(View.INVISIBLE);
        orderIvAll.setVisibility(View.INVISIBLE);
        flag = false;
        queryBillByMonth(currentMonth);
    }

    //选择分销提成账单
    private void chooseDistributionBill() {
        orderTvToday.setTextColor(getResources().getColor(R.color.color_text));
        orderTvTodayBottom.setVisibility(View.INVISIBLE);
        orderIvToday.setVisibility(View.INVISIBLE);
        orderTvAll.setTextColor(getResources().getColor(R.color.color_deep_blue));
        orderTvAllBottom.setVisibility(View.VISIBLE);
        orderIvAll.setVisibility(View.VISIBLE);
        flag = true;
        pullDeviendfans(currentMonth);
    }


    /**
     * 弹出时间选择
     */
    private void popMonthChoose() {
//        TimePickerView tpv = new TimePickerView(BillActivity.this, TimePickerView.Type.YEAR_MONTH);
//
//        tpv.setTitle("选择月份");
//        tpv.setCancelable(true);
//        tpv.setCancelText("取消");
//        tpv.setSubmitText("确认");
//        tpv.setTime(new Date());
//
//        tpv.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//
//                DateFormat format = new SimpleDateFormat("yyyy-MM");
//                currentMonth = format.format(date);
//                if(flag){
//                    pullDeviendfans(currentMonth);
//                    getMouthAll(currentMonth);
//                }else {
//                    queryBillByMonth(currentMonth);
//                }
//            }
//
//        });
//        tpv.show();

        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        Calendar startDate = Calendar.getInstance();
        startDate.set(calendar.get(Calendar.YEAR) - 10,0,1);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                DateFormat format = new SimpleDateFormat("yyyy-MM");
                currentMonth = format.format(date);
                if(flag){
                    pullDeviendfans(currentMonth);
                    getMouthAll(currentMonth);
                }else {
                    queryBillByMonth(currentMonth);
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, false, false, false, false})
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setTitleText("选择月份")
                .setRange(calendar.get(Calendar.YEAR)-10, calendar.get(Calendar.YEAR))
                .setRangDate(startDate,selectedDate)
                .setLabel("年","月","","","","")
                .isCenterLabel(true)//是否每项item都有label，false代表是
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

        pvTime.show();
    }

    //选择月份提成时获取月份和总金额
    private void getMouthAll(String currentMonth) {
        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(index));
        map.put("time", currentMonth);
        map.put("storeId", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("jf", "flowUser|orderformSettle|photo");
        http.doCommonPost(null, MainUrl.queryStoreBill, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //查询月份
                        String month = obj.getString("time");
                        billTxtday.setText(month);
                        //总收入 monthTurnover  todayTurnover
                        Double totIncomeAmount = obj.getDouble("monthTurnover");
                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("账单JSONException", e.toString());
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
