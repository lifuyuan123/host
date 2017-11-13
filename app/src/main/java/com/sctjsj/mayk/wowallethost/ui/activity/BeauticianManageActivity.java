package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.BeauticanAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.BeauticanBean;
import com.sctjsj.mayk.wowallethost.ui.customview.DiffCallBack;
import com.sctjsj.mayk.wowallethost.ui.customview.StoredeleteDialog;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 管理美容师界面
 */
@Route(path = "/main/act/BeauticianManageActivity")
public class BeauticianManageActivity extends BaseAppcompatActivity {

    @BindView(R.id.beautician_back_rl)
    RelativeLayout beauticianBackRl;

    @BindView(R.id.beautician_rcv)
    RecyclerView beauticianRcv;
    @BindView(R.id.beautician_buttom_txt)
    TextView beauticianButtomTxt;
    @BindView(R.id.beautican_mange_del)
    TextView beauticanMangeDel;
    @BindView(R.id.beautician_refresh)
    MaterialRefreshLayout beauticianRefresh;
    @BindView(R.id.beautician_layout)
    LinearLayout beauticianLayout;
    @BindView(R.id.beauticanNo_layout)
    LinearLayout beauticanNoLayout;

    private BeauticanAdapter adapter;
    private List<BeauticanBean> data = new ArrayList<>();
    private boolean Flag = false;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private List<BeauticanBean> deldata=new ArrayList<>();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                adapter.notifyDataSetChanged();
                if(adapter.getData().size()<=0){
                    beauticanMangeDel.setVisibility(View.INVISIBLE);
                    beauticianLayout.setVisibility(View.GONE);
                    beauticanNoLayout.setVisibility(View.VISIBLE);
                    beauticianButtomTxt.setText("增加店员");
                    Flag=false;
                }
            }
        }
    };

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
        getClerkData();
    }

    private void setListener() {
        beauticianRefresh.setLoadMore(true);
        beauticianRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getClerkData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getClerkDataMore();
            }
        });

    }

    private void setAdapter() {
        adapter = new BeauticanAdapter(this, data);
        beauticianRcv.setLayoutManager(new LinearLayoutManager(this));
        beauticianRcv.setAdapter(adapter);
    }


    @Override
    public int initLayout() {
        return R.layout.activity_beautician_manage;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.beautician_back_rl, R.id.beautican_mange_del, R.id.beautician_buttom_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.beautician_back_rl:
                finish();
                break;
            case R.id.beautican_mange_del:
                deleteShow();
                break;
            case R.id.beautician_buttom_txt:
                addOrDel();
                break;
        }
    }

    private void addOrDel() {
        final List<BeauticanBean> datas = new ArrayList<>();
        if (Flag) {
            deldata.clear();
            for (int i = 0; i < data.size(); i++) {
                if (!data.get(i).isFlag()) {
                    datas.add(data.get(i));
                }else {
                    deldata.add(data.get(i));
                }
            }
//            data.clear();
//            data.addAll(datas);

            //判断是否有选中的要删除
            if (data.size() >= deldata.size()) {
                //弹出dialog
                final StoredeleteDialog dialog = new StoredeleteDialog(BeauticianManageActivity.this);
                dialog.setS("是否确认删除");
                //删除监听
                dialog.setDeletelisten(new StoredeleteDialog.OnclickDeleteListener() {
                    @Override
                    public void Onclick() {
                        adapter.changeData(datas);
                        delSalesClerk(deldata);
                        dialog.dismiss();

                    }
                });
                //关闭dialog
                dialog.setCloseListener(new StoredeleteDialog.OnclickCloseListener() {
                    @Override
                    public void Onclick() {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                //表示没有选中项目
            } else {
                Toast.makeText(this, "您还没选择项目", Toast.LENGTH_SHORT).show();
            }


        } else {
            //添加美容师
            ARouter.getInstance().build("/main/act/AddNewBeautican").navigation();
        }

    }

    private void deleteShow() {
        Flag = adapter.setVisible();
        if (Flag) {
            beauticianButtomTxt.setText("删除店员");
            beauticanMangeDel.setText("取消");
        } else {
            beauticianButtomTxt.setText("增加店员");
            beauticanMangeDel.setText("删除");
        }
    }


    //获取店员
    private void getClerkData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("storeId", UserAuthUtil.getStoreId() + "");
        body.put("jf", "staffPhoto|job");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.GetStoreWorker, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject object = new JSONObject(result);
                        JSONArray arr = object.getJSONArray("resultList");
                        if (null != arr && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject beaUtican = arr.getJSONObject(i);
                                BeauticanBean bean = new BeauticanBean();
                                bean.setId(beaUtican.getInt("id"));
                                bean.setSex(beaUtican.getInt("sex"));
                                bean.setName(beaUtican.getString("name"));
                                JSONObject job = beaUtican.getJSONObject("job");
                                bean.setJobId(job.getInt("id"));
                                bean.setJobName(job.getString("name"));
                                bean.setPhoto(beaUtican.getJSONObject("staffPhoto").getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                       if(data.size()>0){
                           beauticianLayout.setVisibility(View.VISIBLE);
                           beauticanNoLayout.setVisibility(View.GONE);
                           beauticanMangeDel.setVisibility(View.VISIBLE);
                       }else {
                           beauticanMangeDel.setVisibility(View.INVISIBLE);
                           beauticianLayout.setVisibility(View.GONE);
                           beauticanNoLayout.setVisibility(View.VISIBLE);
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
                beauticianRefresh.finishRefreshLoadMore();
                beauticianRefresh.finishRefresh();
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


    private void getClerkDataMore() {

        HashMap<String, String> body = new HashMap<>();
        body.put("storeId", UserAuthUtil.getStoreId() + "");
        body.put("jf", "staffPhoto|job");
        body.put("pageIndex", pageIndex + "");

        service.doCommonPost(null, MainUrl.GetStoreWorker, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        pageIndex++;
                        JSONArray arr = object.getJSONArray("resultList");
                        if (null != arr && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject beaUtican = arr.getJSONObject(i);
                                BeauticanBean bean = new BeauticanBean();
                                bean.setId(beaUtican.getInt("id"));
                                bean.setSex(beaUtican.getInt("sex"));
                                bean.setName(beaUtican.getString("name"));
                                JSONObject job = beaUtican.getJSONObject("job");
                                bean.setJobId(job.getInt("id"));
                                bean.setJobName(job.getString("name"));
                                bean.setPhoto(beaUtican.getJSONObject("staffPhoto").getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
                        if(data.size()>0){
                            beauticianLayout.setVisibility(View.VISIBLE);
                            beauticanNoLayout.setVisibility(View.GONE);
                        }else {
                            beauticianLayout.setVisibility(View.GONE);
                            beauticanNoLayout.setVisibility(View.VISIBLE);
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
                beauticianRefresh.finishRefreshLoadMore();
                beauticianRefresh.finishRefresh();
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

    //删除店员
    private void delSalesClerk(List<BeauticanBean> data){
        StringBuffer buffre=new StringBuffer();
        if(null!=data&&data.size()>0){
            for (int i = 0; i <data.size() ; i++) {
                if (data.size()==1){
                    buffre.append(data.get(i).getId());
                }
                if(data.size()>1){
                    if(data.size()-1==i){
                        buffre.append(data.get(i).getId());
                    }else {
                        buffre.append(data.get(i).getId()+",");
                    }
                }

            }
        }
        HashMap<String,String> body=new HashMap<>();
        body.put("staffList",buffre.toString());
        service.doCommonPost(null, MainUrl.DelSalesClerk, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        Log.e("delSalesClerk",result.toString());
                        if(obj.getBoolean("result")){
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                        }else {
                            Toast.makeText(BeauticianManageActivity.this, "删除店员失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("delSalesClerk",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("onerror",ex.toString());
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
