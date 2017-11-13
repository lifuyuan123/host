package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.AgentMangeAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.AgentBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/AgentMangeActivity")
public class AgentMangeActivity extends BaseAppcompatActivity {

    @BindView(R.id.agent_mange_back)
    RelativeLayout agentMangeBack;
    @BindView(R.id.agent_mange_tc)
    TextView agentMangeTc;
    @BindView(R.id.agent_mange_storeNumber)
    TextView agentMangeStoreNumber;
    @BindView(R.id.agent_mange_storeNumbers)
    TextView agentMangeStoreNumbers;
    @BindView(R.id.agent_mange_gmje)
    TextView agentMangeGmje;
    @BindView(R.id.agent_mange_list)
    ListView agentMangeList;
    @BindView(R.id.activity_agent_mange)
    LinearLayout activityAgentMange;
    @BindView(R.id.agent_message_refresh)
    MaterialRefreshLayout refreshlayout;
    @BindView(R.id.agentNo_layout)
    LinearLayout agentNo_layout;
    @BindView(R.id.agent_mange_addStore)
    RelativeLayout agent_mange_addStore;

    private HttpServiceImpl service;
    private List<AgentBean> data = new ArrayList();
    private AgentMangeAdapter adapter;
    private int pageIndex = 1;
    private int surplusNum=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter = new AgentMangeAdapter(data, this);
        agentMangeList.setAdapter(adapter);

        setListener();
    }

    private void setListener() {
        refreshlayout.setLoadMore(true);
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getAgent();
                refreshlayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getAgentListMore();
                refreshlayout.finishRefreshLoadMore();
            }
        });

        agentMangeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgentBean bean= (AgentBean) adapter.getItem(position);
                if(null!=bean){
                    ARouter.getInstance().build("/main/act/store").withInt("id",bean.getId()).withBoolean("key",true).navigation();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAgent();
//        getAgentList();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_agent_mange;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.agent_mange_back, R.id.agent_mange_addStore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agent_mange_back:
                finish();
                break;
            case R.id.agent_mange_addStore:
               if(surplusNum>0){
                   ARouter.getInstance().build("/main/act/AddAgentStoreActivity").navigation();
               }else {
                   Toast.makeText(this, "套餐剩余量不足，请购买！", Toast.LENGTH_SHORT).show();
               }
                break;
        }
    }


    private void getAgent() {
        pageIndex = 1;
        data.clear();
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "ap|store");
        service.doCommonPost(null, MainUrl.GetAgintlists, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("getAgent",result.toString());
                    try {
                        pageIndex++;
                        JSONObject object = new JSONObject(result);
                        JSONArray array=object.getJSONArray("uapList");
                        JSONObject datas = array.getJSONObject(0);
                        JSONObject ap = datas.getJSONObject("ap");
                        agentMangeTc.setText(datas.getJSONObject("ap").getString("name"));
                        surplusNum = datas.getInt("surplusNum");
                        int totNum = ap.getInt("totNum");
                        agentMangeStoreNumber.setText(surplusNum + "");
                        agentMangeStoreNumbers.setText((totNum - surplusNum) + "");
                        agentMangeGmje.setText(new DecimalFormat("######0.00").format(ap.getDouble("value")));


                        JSONArray resultList = object.getJSONArray("resultList");
                        if (null != resultList && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject objbean = resultList.getJSONObject(i);
                                AgentBean bean = new AgentBean();
                                bean.setName(objbean.getString("username"));
                                String store = objbean.getString("store");
                                if (!store.equals("null")) {
                                    JSONObject ojStore = objbean.getJSONObject("store");
                                    bean.setInsertTime(ojStore.getString("insertTime"));
                                    bean.setId(ojStore.getInt("id"));
                                    int state=ojStore.getInt("storeStatus");
                                    if(state==1){
                                        bean.setStoreState("待审核");
                                    }else if (state==2){
                                        bean.setStoreState("已审核");
                                    }else if (state==3) {
                                        bean.setStoreState("已关闭");
                                    }
                                }
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("getAgent_JSONException",e.toString());
                    }finally {
                        if (data.size() > 0) {
                            agentMangeList.setVisibility(View.VISIBLE);
                            agentNo_layout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            agentMangeList.setVisibility(View.GONE);
                            agentNo_layout.setVisibility(View.VISIBLE);
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

    private void getAgentListMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "store");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.GetAgintUser, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject object = new JSONObject(result);
                        JSONArray resultList = object.getJSONArray("resultList");
                        if (null != resultList && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject objbean = resultList.getJSONObject(i);
                                AgentBean bean = new AgentBean();
                                bean.setName(objbean.getString("username"));
                                String store = objbean.getString("store");
                                if (!store.equals("null")) {
                                    JSONObject ojStore = objbean.getJSONObject("store");
                                    bean.setInsertTime(ojStore.getString("insertTime"));
                                    int state = ojStore.getInt("storeStatus");
                                    if (state == 1) {
                                        bean.setStoreState("待审核");
                                    } else if (state == 2) {
                                        bean.setStoreState("已审核");
                                    } else if (state == 3) {
                                        bean.setStoreState("已关闭");
                                    }
                                }
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            agentMangeList.setVisibility(View.VISIBLE);
                            agentNo_layout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            agentMangeList.setVisibility(View.GONE);
                            agentNo_layout.setVisibility(View.VISIBLE);
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
                refreshlayout.finishRefresh();
                refreshlayout.finishRefreshLoadMore();
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


}
