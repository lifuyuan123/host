package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
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
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.ProjectClaassifyAdapter;
import com.sctjsj.mayk.wowallethost.callback.ModelCallback;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectClassifyBean;
import com.sctjsj.mayk.wowallethost.presenter.IProjectClassifyPresenter;
import com.sctjsj.mayk.wowallethost.presenter.impl.ProjectClassifyPresenterImpl;
import com.sctjsj.mayk.wowallethost.ui.customview.DiffCallBackClassfy;
import com.sctjsj.mayk.wowallethost.ui.customview.StoredeleteDialog;
import com.sctjsj.mayk.wowallethost.ui.intf.IProjectClassifyView;

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

@Route(path = "/main/act/project_classify")
public class ProjectClassifyActivity extends BaseAppcompatActivity implements IProjectClassifyView {

    @BindView(R.id.refreshlayout)
    MaterialRefreshLayout refreshlayout;
    @BindView(R.id.lin_nodata)
    LinearLayout linNodata;

    private IProjectClassifyPresenter presenter;

    private int pageIndex = 1;
    private boolean isshow;

    @Autowired(name = "key")
    String s;

    @BindView(R.id.act_project_classify_tv_status)
    TextView mTVStatus;
    @BindView(R.id.act_project_classify_rl_status)
    RelativeLayout mRLStatus;

    @BindView(R.id.act_project_classify_rl_op)
    RelativeLayout mRLOp;
    @BindView(R.id.act_project_classify_tv_op)
    TextView mTVOp;

    @BindView(R.id.rv)
    RecyclerView mRV;
    private HttpServiceImpl service;

    private ProjectClaassifyAdapter adapter;
    private List<ProjectClassifyBean> data = new ArrayList<>();
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
        presenter = new ProjectClassifyPresenterImpl(this);

        presenter.pullClassify(8, 1, new ModelCallback() {
            @Override
            public void onStart() {
                showLoading(true, "正在加载");
            }

            @Override
            public void onSuccess(String res) {
                LogUtil.e("act", res);
            }

            @Override
            public void onError(String err) {

            }

            @Override
            public void onFinish() {
                dismissLoading();
            }
        });

        initRV();

        refreshlayout.setLoadMore(true);
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getdata();
                refreshlayout.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getdataMore();
                refreshlayout.finishRefreshLoadMore();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getdata();
    }

    //是否有数据，是否显示
    private void isShowData() {
        if(data.size()>0){
            linNodata.setVisibility(View.GONE);
            mRV.setVisibility(View.VISIBLE);
            mTVStatus.setVisibility(View.VISIBLE);
        }else {
            linNodata.setVisibility(View.VISIBLE);
            mRV.setVisibility(View.GONE);
            mTVStatus.setVisibility(View.INVISIBLE);
        }
    }

    private void initRV() {
        adapter = new ProjectClaassifyAdapter(this, data);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRV.setLayoutManager(manager);
        mRV.setAdapter(adapter);

        adapter.setCallback(new ProjectClaassifyAdapter.Callback() {
            @Override
            public void onClick(int position) {
                if (!StringUtil.isBlank(s)) {
                    if (s.equals("100")) {
                        Intent intent = new Intent();
                        intent.putExtra("key", data.get(position).getName());
                        intent.putExtra("id",data.get(position).getId());
                        setResult(101, intent);
                        finish();
                    }
                } else {
                    ARouter.getInstance().build("/main/act/add_or_modify_project_classify").withObject("classify", data.get(position)).navigation(ProjectClassifyActivity.this, 100);
                }
            }

            @Override
            public void onLongClick(int position) {

            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_project_classify;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.mine_balance_back, R.id.act_project_classify_rl_status, R.id.act_project_classify_rl_op})
    public void onViewClicked(View view) {
        //点击返回
        switch (view.getId()) {
            case R.id.mine_balance_back:
                finish();
                break;
            //点击删除
            case R.id.act_project_classify_rl_status:
                String str = mTVStatus.getText().toString();

                isshow = adapter.isShouwchek();
                if (isshow) {
                    mTVStatus.setText("取消");
                    mTVOp.setText("确认删除选中分类");
                    adapter.showDelete();
                } else {
                    mTVStatus.setText("编辑");
                    mTVOp.setText("新增分类");
                    adapter.dismissDelete();
                }
                break;

            //操作
            case R.id.act_project_classify_rl_op:
                String strOp = mTVOp.getText().toString();
                //执行批量删除
                if ("确认删除选中分类".equals(strOp)) {

                    final List<ProjectClassifyBean> l = new ArrayList<ProjectClassifyBean>();
                    final List<ProjectClassifyBean> deletedata = new ArrayList<ProjectClassifyBean>();
                    for (int i = 0; i < data.size(); i++) {
                        if (data.get(i).isCBChecked()) {
                            deletedata.add(data.get(i));
                        } else {
                            l.add(data.get(i));
                        }
                    }

                    if (data.size() > l.size()) {
                        final StoredeleteDialog dia = new StoredeleteDialog(this);
                        dia.setS("确认删除选中的分类？");
                        dia.setDeletelisten(new StoredeleteDialog.OnclickDeleteListener() {
                            @Override
                            public void Onclick() {
                                dia.dismiss();
                                //更换数据
                                DiffUtil.DiffResult diffResult =
                                        DiffUtil.calculateDiff(new DiffCallBackClassfy(data, l), true);
                                diffResult.dispatchUpdatesTo(adapter);
                                adapter.changeData(l);
                                    delete(deletedata);
                            }
                        });
                        dia.show();
                    } else {
                        Toast.makeText(this, "您还没选择项目", Toast.LENGTH_SHORT).show();
                    }
                }

                //执行新增分类
                if ("新增分类".equals(strOp)) {
                    ARouter.getInstance().build("/main/act/add_or_modify_project_classify").navigation();
                }

                break;
        }
    }

    @Override
    public void pullClassify(int pageSize, int pageIndex) {

    }

    @Override
    public void deleteClassifyById(String id) {

    }

    @Override
    public void deleteClassifyList(List<String> idList) {

    }

    @Override
    public void updateClassify(ProjectClassifyBean classifyBean) {

    }

    //获取数据
    private void getdata() {
        final Map<String, String> map = new HashMap<>();
        pageIndex = 1;
        data.clear();
        map.put("ctype", "goodsType");
        map.put("cond", "{isDelete:1}");
        map.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.ProjectClassification, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("classfy_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    pageIndex++;
                    JSONArray array = object.getJSONArray("resultList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ProjectClassifyBean bean = new ProjectClassifyBean();
                        bean.setId(o.getInt("id"));
                        bean.setName(o.getString("name"));
                        bean.setIsDelete(o.getInt("isDelete"));
                        data.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    Message message=Message.obtain();
                    message.what=1;
                    handler.sendMessage(message);

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("classfy_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("classfy_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("classfy_onFinished", "onFinished");
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

    //获取数据
    private void getdataMore() {
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "goodsType");
        map.put("cond", "{isDelete:1}");
        map.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.ProjectClassification, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("classfy_onSuccess", result.toString());
                try {
                    pageIndex++;
                    JSONObject object = new JSONObject(result);
                    JSONArray array = object.getJSONArray("resultList");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        ProjectClassifyBean bean = new ProjectClassifyBean();
                        bean.setId(o.getInt("id"));
                        bean.setName(o.getString("name"));
                        bean.setIsDelete(o.getInt("isDelete"));
                        data.add(bean);
                    }
                    adapter.notifyDataSetChanged();
                    Message message=Message.obtain();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("classfy_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("classfy_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("classfy_onFinished", "onFinished");
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


    //关闭项目分类
    private void delete(List<ProjectClassifyBean> deletedata) {
        Map<String, String> map = new HashMap<>();
        StringBuffer buffre=new StringBuffer();
        if(null!=deletedata&&deletedata.size()>0){
            for (int i = 0; i <deletedata.size() ; i++) {
                if (deletedata.size()==1){
                    buffre.append(deletedata.get(i).getId());
                }
                if(deletedata.size()>1){
                    if(deletedata.size()-1==i){
                        buffre.append(deletedata.get(i).getId());
                    }else {
                        buffre.append(deletedata.get(i).getId()+",");
                    }
                }

            }
        }
        map.put("ids",buffre.toString());
        service.doCommonPost(null, MainUrl.DeleteProjectType, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("delete_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        Log.e("delete_Success", object.getString("msg"));
                    } else {
                        Log.e("delete_Error", object.getString("msg"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("delete_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("delete_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("delete_onFinished", "onFinished");
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
