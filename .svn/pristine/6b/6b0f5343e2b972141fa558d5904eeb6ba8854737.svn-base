package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.ClerkAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.ClerkBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ClerkTypeActivity")
public class ClerkTypeActivity extends BaseAppcompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.refreshlayout)
    MaterialRefreshLayout refreshlayout;
    @BindView(R.id.lin_nodata)
    LinearLayout linNodata;
    private ClerkAdapter adapter;
    private List<ClerkBean> list = new ArrayList<>();
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    isShowData();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        intData();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(manager);
        adapter = new ClerkAdapter(list, this);
        recyclerview.setAdapter(adapter);
        adapter.setCallback(new ClerkAdapter.Callback() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent();
                intent.putExtra("key", list.get(position));
                setResult(102, intent);
                finish();
            }
        });

        refreshlayout.setLoadMore(true);
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                intData();
                refreshlayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                intDataMore();
                refreshlayout.finishRefreshLoadMore();
            }
        });

    }

    //是否有数据，是否显示
    private void isShowData() {
        if(list.size()>0){
            linNodata.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
        }else {
            linNodata.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }

    //获取type数据
    private void intData() {
        list.clear();
        pageIndex = 1;
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "job");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("size","15");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("type_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    pageIndex++;
                    JSONArray array = object.getJSONArray("resultList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ClerkBean bean = new ClerkBean();
                        bean.setName(o.getString("name"));
                        bean.setId(o.getInt("id"));
                        bean.setIsdelete(o.getInt("isDelete"));
                        list.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    Message message=Message.obtain();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("type_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("type_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("type_onFinished", "onFinished");
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
    public int initLayout() {
        return R.layout.activity_clerk_type;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.addnewbeautician_back_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewbeautician_back_rl:
                finish();
                break;
        }
    }

    //获取type数据
    private void intDataMore() {
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "job");
        map.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("type_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    pageIndex++;
                    JSONArray array = object.getJSONArray("resultList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ClerkBean bean = new ClerkBean();
                        bean.setName(o.getString("name"));
                        bean.setId(o.getInt("id"));
                        bean.setIsdelete(o.getInt("isDelete"));
                        list.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    Message message=Message.obtain();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("type_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("type_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("type_onFinished", "onFinished");
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
