package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
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
import com.sctjsj.mayk.wowallethost.adapter.RecordAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.RecordBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

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

import static com.sctjsj.mayk.wowallethost.R.id.choose_layout;

@Route(path = "/main/act/ApplyRecordActivity")

//代理套餐申请记录
public class ApplyRecordActivity extends BaseAppcompatActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.Choose_noData_layout)
    LinearLayout ChooseNoDataLayout;
    private HttpServiceImpl service;
    private List<RecordBean> list=new ArrayList<>();
    private RecordAdapter adapter;
    private LinearLayoutManager manager;
    private int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getData();
        initView();

    }

    private void initView() {
        manager=new LinearLayoutManager(this);
        adapter=new RecordAdapter(list,this);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(adapter);
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh.finishRefresh();
                getData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                refresh.finishRefreshLoadMore();
                getDataMore();
            }
        });

    }

    //获取数据
    private void getData() {
        list.clear();
        pageIndex=1;
        Map<String,String> body=new HashMap<>();
        body.put("ctype","agentApp");
        body.put("cond","{appltUser:{id:"+ UserAuthUtil.getUserId()+"},isDelete:1}");
        body.put("pageIndex",pageIndex+"");
        body.put("jf","applyAp");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("record_onSuccess",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        pageIndex++;
                        JSONArray array=object.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1=array.getJSONObject(i);
                            RecordBean bean=new RecordBean();
                            bean.setName(object1.getJSONObject("applyAp").getString("name"));
                            String insertTime = object1.getString("insertTime");
                            String time=insertTime.substring(0, insertTime.indexOf(" "));
                            bean.setTime(time);
                            if(object1.getInt("status")==1){
                                bean.setContent("购买成功");
                            }else if(object1.getInt("status")==3){
                                bean.setContent("购买失败");
                            }
                            list.add(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("record_JSONException",e.toString());
                }finally {
                    if (list.size() > 0) {
                        refresh.setVisibility(View.VISIBLE);
                        ChooseNoDataLayout.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    } else {
                        refresh.setVisibility(View.GONE);
                        ChooseNoDataLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("record_onError",ex.toString());
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
                showLoading(true,"加载中...");

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    //获取数据
    private void getDataMore() {
        Map<String,String> body=new HashMap<>();
        body.put("ctype","agentApp");
        body.put("cond","{appltUser:{id:"+ UserAuthUtil.getUserId()+"},isDelete:1}");
        body.put("pageIndex",pageIndex+"");
        body.put("jf","applyAp");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("record_onSuccess",result.toString());
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        pageIndex++;
                        JSONArray array=object.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1=array.getJSONObject(i);
                            RecordBean bean=new RecordBean();
                            bean.setName(object1.getJSONObject("applyAp").getString("name"));
                            String insertTime = object1.getString("insertTime");
                            String time=insertTime.substring(0, insertTime.indexOf(" "));
                            bean.setTime(time);
                            if(object1.getInt("status")==1){
                                bean.setContent("购买成功");
                            }else if(object1.getInt("status")==3){
                                bean.setContent("购买失败");
                            }
                            list.add(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("record_JSONException",e.toString());
                }finally {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("record_onError",ex.toString());
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
                showLoading(true,"加载中...");

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_apply_record;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.choose_agent_back)
    public void onViewClicked() {
        finish();
    }
}
