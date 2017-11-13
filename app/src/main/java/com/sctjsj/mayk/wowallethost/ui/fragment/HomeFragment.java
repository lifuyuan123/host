package com.sctjsj.mayk.wowallethost.ui.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.MainLooper;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.Application.MyApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.HomeLayoutAdapter;
import com.sctjsj.mayk.wowallethost.event.MessageEvent;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.HomeData;
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.sctjsj.mayk.wowallethost.ui.activity.MessageListActivity;
import com.sctjsj.mayk.wowallethost.ui.xWidget.UploadProgressDialog;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Created by lifuy on 2017/5/15.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.home_lay1_iv_message)
    ImageView homeLay1IvMessage;
    @BindView(R.id.home_rl_message)
    RelativeLayout homeRlMessage;
    @BindView(R.id.frg_home_xrv)
    RecyclerView frgHomeXrv;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.home_msg_Layout)
    LinearLayout homeMsgLayout;
    private List<HomeData> data;
    private HomeLayoutAdapter turnoverAdapter, orderNumAdapter, receivablesAdapter, scancodeAdapter;
    //代理适配器
    private DelegateAdapter adapter;

    /**
     * 对应的子适配器
     **/
    private List<DelegateAdapter.Adapter> adapterList = new LinkedList<>();
    //数据
    private HashMap<String, Object> turnoverMap = new HashMap<>();
    private HashMap<String, Object> orderNumMap = new HashMap<>();
    private HashMap<String,Object> auditAtatus=new HashMap<>();
    private HashMap<String, Object> rebate = new HashMap<>();
    private HttpServiceImpl server;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.frg_home, null);
        ButterKnife.bind(this, mView);
        server = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        orderNumAdapter.notifyDataSetChanged();
    }

    private void initView() {
        initAdapter();
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                initData();
            }
        });

    }

    private void initAdapter() {
        //虚拟适配器
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        frgHomeXrv.setLayoutManager(layoutManager);

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        frgHomeXrv.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(4, 10);

        initTurnoverHelper();
        initOrderNumHelper();
        initrReceivablesHelper();
        initScancodeHelper();

        //设置代理适配器
        adapter = new DelegateAdapter(layoutManager, false);
        adapter.setAdapters(adapterList);
        frgHomeXrv.setAdapter(adapter);
    }

    private void initScancodeHelper() {
        SingleLayoutHelper singleLayoutHelper_Scancode = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_Scancode.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(rebate);
        scancodeAdapter = new HomeLayoutAdapter(getActivity(), singleLayoutHelper_Scancode, 1, data, 4);
        adapterList.add(scancodeAdapter);
    }

    private void initrReceivablesHelper() {
        SingleLayoutHelper singleLayoutHelper_rReceivables = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_rReceivables.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(auditAtatus);
        receivablesAdapter = new HomeLayoutAdapter(getActivity(), singleLayoutHelper_rReceivables, 1, data, 3);
        adapterList.add(receivablesAdapter);
    }

    private void initOrderNumHelper() {
        SingleLayoutHelper singleLayoutHelper_OrderNum = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_OrderNum.setItemCount(1);
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(orderNumMap);
        orderNumAdapter = new HomeLayoutAdapter(getActivity(), singleLayoutHelper_OrderNum, 1, data, 2);
        adapterList.add(orderNumAdapter);

    }

    private void initTurnoverHelper() {
        SingleLayoutHelper singleLayoutHelper_Turnover = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_Turnover.setItemCount(1);
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(turnoverMap);
        turnoverAdapter = new HomeLayoutAdapter(getActivity(), singleLayoutHelper_Turnover, 1, data, 1);
        adapterList.add(turnoverAdapter);

    }

    private void initData() {
           //获取营业额
           getTurnoverMap();
           //获取订单数
           getOrderNumMap();
           //获取申请代理套餐状态
           getAuditStatus();
           //获取授信额度/剩余授信额度
           getRebate();

    }

    //获取授信额度/剩余授信额度
    private void getRebate() {
        rebate.clear();
        Map<String,String> map=new HashMap<>();
        map.put("id", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("ctype", "store");
        LogUtil.e("getRebate_map",map.toString());
        server.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getRebate_onSuccess",result.toString());
                if(!StringUtil.isBlank(result.toString())){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject object1=object.getJSONObject("data");
                        double maxRebate=object1.getDouble("maxRebate");
                        double remainRebate=object1.getDouble("remainRebate");
                        rebate.put("maxRebate",maxRebate);
                        rebate.put("remainRebate",remainRebate);
                        scancodeAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("getRebate_JSONException",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("getRebate_onError",ex.toString());
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

    //获取申请代理套餐状态
    private void getAuditStatus() {
        auditAtatus.clear();
        Map<String,String> map=new HashMap<>();
        map.put("userId", UserAuthUtil.getUserId()+"");
        LogUtil.e("getStatus_map",map.toString());
        server.doCommonPost(null, MainUrl.queryAuditStatus, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getStatus_onSuccess",result.toString());
                if(!StringUtil.isBlank(result.toString())){
                    try {
                        JSONObject object=new JSONObject(result);
                        boolean results = object.getBoolean("result");
                        auditAtatus.put("status",results);
                        JSONObject applyTbl=object.getJSONObject("applyTbl");
                        int id=applyTbl.getInt("id");
                        auditAtatus.put("id",id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("getStatus_JSONException",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("getStatus_onError",ex.toString());
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


    //获取营业额
    private void getTurnoverMap() {
        turnoverMap.clear();
        server.doCommonPost(null, MainUrl.Turnover, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("Turnover_onSuccess", result.toString());
                try {
                    JSONObject o = new JSONObject(result);
                    turnoverMap.put("todayTurnover", o.getDouble("todayTurnover"));
                    turnoverMap.put("todayTurnovers", o.getDouble("todayTurnovers"));
                    turnoverAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("Turnover_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("Turnover_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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


    //获取订单数
    private void getOrderNumMap() {
        orderNumMap.clear();
        server.doCommonPost(null, MainUrl.Order, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("order_onSuccess", result.toString());
                try {
                    JSONObject o = new JSONObject(result);
                    orderNumMap.put("count", o.getInt("count"));
                    orderNumMap.put("counts", o.getInt("counts"));
                    orderNumAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
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
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.home_msg_Layout)
    public void onViewClicked() {
        ARouter.getInstance().build("/main/act/message_list").navigation();
    }

}