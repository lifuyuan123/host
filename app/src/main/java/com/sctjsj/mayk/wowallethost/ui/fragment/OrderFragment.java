package com.sctjsj.mayk.wowallethost.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.OrderAdapter;
import com.sctjsj.mayk.wowallethost.event.DataEvent;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.OrderBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.ui.activity.IndexActivity;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lifuy on 2017/5/15.
 */

public class OrderFragment extends Fragment {
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
    Unbinder unbinder;
    @BindView(R.id.order_rel_all)
    RelativeLayout orderRelAll;
    @BindView(R.id.order_rel)
    RecyclerView orderRel;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.order_layout)
    RelativeLayout orderLayout;
    @BindView(R.id.orderNo_layout)
    LinearLayout orderNoLayout;

    private OrderAdapter adapter;
    private List<OrderBean> data = new LinkedList<>();
    private LinearLayoutManager manager;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private boolean flag = false;//true  所有订单   false 今日订单
    private int type=0;
    private LoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_order, null);
        unbinder = ButterKnife.bind(this, view);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
        type=getArguments().getInt("type");
        init();
        if(type!=0){
            switch (type){
                case 1:
                    chooseTodaOrder();
                    break;
                case 2:
                    chooseAllOrder();
                    break;
            }
        }else {
            getToadyOrderData();
        }


    }



    @Subscribe
    public void onEventMainThread(DataEvent event) {
        if(event!=null){
            type=event.getDetaBean().getType();
            if(type!=0){
                switch (type){
                    case 1:
                        chooseTodaOrder();
                        break;
                    case 2:
                        chooseAllOrder();
                        break;
                }
            }
        }

    }



    //初始化  设置适配器和监听
    private void init() {
        adapter = new OrderAdapter(data, getActivity());
        manager = new LinearLayoutManager(getActivity());
        orderRel.setLayoutManager(manager);
        orderRel.setAdapter(adapter);

        adapter.setListener(new OrderAdapter.OnclickListener() {
            @Override
            public void Onclick(int position) {
                ARouter.getInstance().build("/main/act/order").navigation();
            }
        });

        //上啦刷新  下拉加载
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                if(flag){
                    getAllOrderData();
                }else {
                    getToadyOrderData();
                }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if(flag){
                    getAllOrderDataMore();
                }else {
                    getToadyOrderDataMore();
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //切换订单
    @OnClick({R.id.order_rel_today, R.id.order_rel_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //今日订单
            case R.id.order_rel_today:
               chooseTodaOrder();
                break;
            //所有订单
            case R.id.order_rel_all:
                chooseAllOrder();
                break;
        }
    }

    //选择今日订单
    private void chooseTodaOrder(){
        orderTvToday.setTextColor(getResources().getColor(R.color.color_deep_blue));
        orderTvTodayBottom.setVisibility(View.VISIBLE);
        orderIvToday.setVisibility(View.VISIBLE);
        orderTvAll.setTextColor(getResources().getColor(R.color.color_text));
        orderTvAllBottom.setVisibility(View.INVISIBLE);
        orderIvAll.setVisibility(View.INVISIBLE);
        flag = false;
        getToadyOrderData();
    }

    //选择累积订单
    private void chooseAllOrder(){
        orderTvToday.setTextColor(getResources().getColor(R.color.color_text));
        orderTvTodayBottom.setVisibility(View.INVISIBLE);
        orderIvToday.setVisibility(View.INVISIBLE);
        orderTvAll.setTextColor(getResources().getColor(R.color.color_deep_blue));
        orderTvAllBottom.setVisibility(View.VISIBLE);
        orderIvAll.setVisibility(View.VISIBLE);
        flag = true;
        getAllOrderData();
    }

    //加载今日订单数据
    private void getToadyOrderData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", pageIndex + "");
        body.put("pageSize",8+"");
        body.put("jf", "user|photo");
        service.doCommonPost(null, MainUrl.ToDayOrderUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getToadyOrderData",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject orderBean = array.getJSONObject(i);
                                OrderBean bean = new OrderBean();
                                bean.setId(orderBean.getInt("id"));
                                if(orderBean.has("buyerRemark")){
                                    bean.setBuyerRemark(orderBean.getString("buyerRemark"));
                                }
                                bean.setInsertTime(orderBean.getString("insertTime"));
                                bean.setPayValue(orderBean.getDouble("payValue"));
                                UserBean user = new UserBean();
                                JSONObject users = orderBean.getJSONObject("user");
                                user.setId(users.getInt("id"));
                                user.setUrl(users.getJSONObject("photo").getString("url"));
                                user.setRealName(users.getString("username"));
                                bean.setBean(user);
                                data.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        if(data.size()>0){
                            orderLayout.setVisibility(View.VISIBLE);
                            orderNoLayout.setVisibility(View.GONE);
                        }else {
                            orderLayout.setVisibility(View.GONE);
                            orderNoLayout.setVisibility(View.VISIBLE);
                        }
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();
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


    private void getToadyOrderDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", pageIndex + "");
        body.put("jf", "user|photo");
        service.doCommonPost(null, MainUrl.ToDayOrderUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject orderBean = array.getJSONObject(i);
                                OrderBean bean = new OrderBean();
                                bean.setId(orderBean.getInt("id"));
                                bean.setBuyerRemark(orderBean.getString("buyerRemark"));
                                bean.setInsertTime(orderBean.getString("insertTime"));
                                bean.setPayValue(orderBean.getDouble("payValue"));
                                UserBean user = new UserBean();
                                JSONObject users = orderBean.getJSONObject("user");
                                user.setId(users.getInt("id"));
                                user.setUrl(users.getJSONObject("photo").getString("url"));
                                user.setRealName(users.getString("username"));
                                bean.setBean(user);
                                data.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        if(data.size()>0){
                            orderLayout.setVisibility(View.VISIBLE);
                            orderNoLayout.setVisibility(View.GONE);
                        }else {
                            orderLayout.setVisibility(View.GONE);
                            orderNoLayout.setVisibility(View.VISIBLE);
                        }
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();
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

    //加载全部订单
    private void getAllOrderData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "orderform");
        body.put("cond", "{state:3,storeTbl:{id:"+UserAuthUtil.getStoreId()+"}}");
        LogUtil.e("idddddd",UserAuthUtil.getStoreId()+"");
        body.put("orderby","insertTime desc");
        body.put("pageSize",8+"");
        body.put("jf", "store|user|photo");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAllOrderData",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject orderBean = array.getJSONObject(i);
                                OrderBean bean = new OrderBean();
                                bean.setId(orderBean.getInt("id"));
                                bean.setBuyerRemark(orderBean.getString("buyerRemark"));
                                bean.setInsertTime(orderBean.getString("insertTime"));
                                bean.setPayValue(orderBean.getDouble("payValue"));
                                bean.setSellerRemark(orderBean.getString("sellerRemark"));
                                UserBean user = new UserBean();
                                JSONObject users = orderBean.getJSONObject("user");
                                user.setId(users.getInt("id"));
                                user.setUrl(users.getJSONObject("photo").getString("url"));
                                user.setRealName(users.getString("username"));
                                bean.setBean(user);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        if(data.size()>0){
                            orderLayout.setVisibility(View.VISIBLE);
                            orderNoLayout.setVisibility(View.GONE);
                        }else {
                            orderLayout.setVisibility(View.GONE);
                            orderNoLayout.setVisibility(View.VISIBLE);
                        }
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();
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

    private void getAllOrderDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "orderform");
        body.put("cond", "{state:3,store:{id:" + UserAuthUtil.getStoreId() + "}}");
        body.put("jf", "store|user|photo");
        body.put("orderby","insertTime desc");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject orderBean = array.getJSONObject(i);
                                OrderBean bean = new OrderBean();
                                bean.setId(orderBean.getInt("id"));
                                bean.setBuyerRemark(orderBean.getString("buyerRemark"));
                                bean.setInsertTime(orderBean.getString("insertTime"));
                                bean.setPayValue(orderBean.getDouble("payValue"));
                                UserBean user = new UserBean();
                                JSONObject users = orderBean.getJSONObject("user");
                                user.setId(users.getInt("id"));
                                user.setUrl(users.getJSONObject("photo").getString("url"));
                                user.setRealName(users.getString("username"));
                                bean.setBean(user);
                                data.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        if(data.size()>0){
                            orderLayout.setVisibility(View.VISIBLE);
                            orderNoLayout.setVisibility(View.GONE);
                        }else {
                            orderLayout.setVisibility(View.GONE);
                            orderNoLayout.setVisibility(View.VISIBLE);
                        }
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();

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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 显示加载dialog
     * @param str
     */
    public void showLoading(boolean b,String str){
        if(dialog==null){
            dialog=new LoadingDialog(this);
        }
        dialog.setCancelable(b);
        dialog.setLoadingStr(str);
        if(!dialog.isShowing()){
            dialog.show();
        }

    }

    /**
     * 关闭加载 dialog
     */
    public void dismissLoading(){
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}
