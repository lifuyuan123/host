package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.Application.MyApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.MessageAdapter;
import com.sctjsj.mayk.wowallethost.event.MessageEvent;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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

@Route(path = "/main/act/message_list")
public class MessageListActivity extends BaseAppcompatActivity {

    @BindView(R.id.message_back_lr)
    RelativeLayout messageBackLr;
    @BindView(R.id.message_title_txt)
    TextView messageTitleTxt;
    @BindView(R.id.message_listView)
    RecyclerView messageListView;
    @BindView(R.id.message_refresh)
    MaterialRefreshLayout messageRefresh;
    @BindView(R.id.messageList_layput)
    RelativeLayout messageListLayput;
    @BindView(R.id.messageNo_layout)
    LinearLayout messageNoLayout;
    private MessageAdapter adapter;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private List<MessageBean> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        setAdapter();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMssageData();
    }



    //设置监听
    private void setListener() {
        messageRefresh.setLoadMore(true);
        messageRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getMssageData();
                messageRefresh.finishRefresh();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                loadDataMore();
            }
        });
    }

    //设置适配器
    private void setAdapter() {
        adapter = new MessageAdapter(data, MessageListActivity.this);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setAdapter(adapter);
        adapter.setCallBack(new MessageAdapter.CallBack() {
            @Override
            public void onClick(int position) {
                deleteItem(position);
            }


        });
    }

    //删除
    private void deleteItem(final int position) {
        Map<String,String> map=new HashMap<>();
        map.put("ctype","message");
        map.put("data","{id:"+data.get(position).getId()+",deletestatusTo:1}");
        service.doCommonPost(null, MainUrl.baseModifyUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
             LogUtil.e("Message_deleteonSuccess",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject o=new JSONObject(result);
                        if(o.getBoolean("result")) {
                            data.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(MessageListActivity.this,"删除成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("Message_deleteonError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("Message_deleteonFinished");
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
        return R.layout.activity_message_list;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.message_back_lr)
    public void onViewClicked() {
        finish();
    }


    //加载消息数据
    private void getMssageData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("cond", "{touser:{id:" + UserAuthUtil.getUserId() + "},deletestatusTo:2}");
        body.put("jf", "fromuserId|photo");
        body.put("orderby","insertTime desc");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(body, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("message_id",UserAuthUtil.getUserId()+"");
                LogUtil.e("message_onSuccess",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objBean = array.getJSONObject(i);
                                MessageBean bean = new MessageBean();
                                bean.setType(objBean.getInt("type"));
                                bean.setId(objBean.getInt("id"));
                                bean.setContent(objBean.getString("content"));
                                bean.setStatus(objBean.getInt("status"));
                                bean.setTitle(objBean.getString("title"));
                                bean.setInsert_time(objBean.getString("insertTime"));
                                bean.setDeletestatus_to(objBean.getInt("deletestatusTo"));
                                JSONObject userBean = objBean.getJSONObject("fromuserId");
                                UserBean userData=new UserBean();
                                userData.setId(userBean.getInt("id"));
                                bean.setBean(userData);
                                if(bean.getType()!=4) {
                                    JSONObject photo = userBean.getJSONObject("photo");
                                    if (photo != null && photo.has("url"))
                                        bean.setUrl(photo.getString("url"));
                                }
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("message_JSONException",e.toString());
                    } finally {
                       if(data.size()>0){
                           adapter.notifyDataSetChanged();
                           messageListLayput.setVisibility(View.VISIBLE);
                           messageNoLayout.setVisibility(View.GONE);
                       }else {
                           messageListLayput.setVisibility(View.GONE);
                           messageNoLayout.setVisibility(View.VISIBLE);
                       }
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("message_onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissLoading();
                messageRefresh.finishRefreshLoadMore();
                messageRefresh.finishRefresh();
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

    //上拉加载更多
    private void loadDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("cond", "{touser:{id:" + UserAuthUtil.getUserId() + "},deletestatusTo:2}");
        body.put("pageIndex", pageIndex + "");
        body.put("jf", "fromuserId|photo");
        body.put("orderby","insertTime desc");
        service.doCommonPost(body, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("message_id",UserAuthUtil.getUserId()+"");
                LogUtil.e("message_onSuccess",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objBean = array.getJSONObject(i);
                                MessageBean bean = new MessageBean();
                                bean.setType(objBean.getInt("type"));
                                bean.setId(objBean.getInt("id"));
                                bean.setContent(objBean.getString("content"));
                                bean.setStatus(objBean.getInt("status"));
                                bean.setTitle(objBean.getString("title"));
                                bean.setInsert_time(objBean.getString("insertTime"));
                                bean.setDeletestatus_to(objBean.getInt("deletestatusTo"));
                                JSONObject userBean = objBean.getJSONObject("fromuserId");
                                UserBean userData=new UserBean();
                                userData.setId(userBean.getInt("id"));
                                bean.setBean(userData);
                                if(bean.getType()!=4) {
                                    JSONObject photo = userBean.getJSONObject("photo");
                                    if (photo != null && photo.has("url"))
                                        bean.setUrl(photo.getString("url"));
                                }
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(data.size()>0){
                            adapter.notifyDataSetChanged();
                            messageListLayput.setVisibility(View.VISIBLE);
                            messageNoLayout.setVisibility(View.GONE);
                        }else {
                            messageListLayput.setVisibility(View.GONE);
                            messageNoLayout.setVisibility(View.VISIBLE);
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
                messageRefresh.finishRefreshLoadMore();
                messageRefresh.finishRefresh();
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
