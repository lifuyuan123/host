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
import com.sctjsj.mayk.wowallethost.adapter.StoreEditAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreEditBean;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

//编辑店内项目
@Route(path = "/main/act/editstore")
public class EditStoreActivity extends BaseAppcompatActivity {
    @BindView(R.id.edit_store_recyclerview)
    RecyclerView editStoreRecyclerview;
    @BindView(R.id.edit_store_tv_add_move)
    TextView editStoreTvAddMove;
    @BindView(R.id.editstore_tv_delete)
    TextView editStoreTvDelete;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    @BindView(R.id.lin_nodata)
    LinearLayout linNodata;
    private List<StoreEditBean> list = new ArrayList<>();
    private LinearLayoutManager manager;
    private StoreEditAdapter adapter;
    private boolean isshow;
    private DiffUtil.DiffResult diffResult;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //是否有数据，是否显示
                    isShowData();
                    adapter.notifyDataSetChanged();
                    if(adapter.getList().size()<=0){
                        editStoreTvAddMove.setText("新增店内项目");
                        isshow=false;
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
//        initdata();
        init();
    }

    private void initdata() {
        //查询店内项目
        getProjectData();
    }

    //是否有数据，是否显示
    private void isShowData() {
        if(list.size()>0){
            linNodata.setVisibility(View.GONE);
            editStoreRecyclerview.setVisibility(View.VISIBLE);
            editStoreTvDelete.setVisibility(View.VISIBLE);
        }else {
            linNodata.setVisibility(View.VISIBLE);
            editStoreTvDelete.setVisibility(View.INVISIBLE);
            editStoreRecyclerview.setVisibility(View.GONE);
        }
    }

    private void init() {
        manager = new LinearLayoutManager(this);
        editStoreRecyclerview.setLayoutManager(manager);
        adapter = new StoreEditAdapter(list, EditStoreActivity.this);
        editStoreRecyclerview.setAdapter(adapter);
        adapter.setCallback(new StoreEditAdapter.Callback() {
            @Override
            public void Onclick(int position) {
                ARouter.getInstance().build("/main/act/EditProjectActivity").withInt("id",list.get(position).getId()).navigation();
            }
        });
        //局部刷新
        diffResult =
                DiffUtil.calculateDiff(new DiffCallBack(list, list), true);


        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getProjectData();
                adapter.notifyDataSetChanged();
                refresh.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                getProjectMoreData();
                refresh.finishRefreshLoadMore();
            }
        });

        //设置监听
        adapter.setLinstener(new StoreEditAdapter.Linstener() {
            //打开
            @Override
            public void openProject(int position) {
                open(position);
            }

            //关闭
            @Override
            public void closeProject(int position) {
                close(position);
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_edit_store;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.editstore_linear_back, R.id.edit_store_tv_add_move, R.id.editstore_linear_delete, R.id.editstore_linear_classify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editstore_linear_back:
                finish();
                break;
            //删除或添加
            case R.id.edit_store_tv_add_move:
                addOrdelete();
                break;
            //显示checkbox
            case R.id.editstore_linear_delete:
                isdelete();
                break;
            //项目分类
            case R.id.editstore_linear_classify:
                ARouter.getInstance().build("/main/act/project_classify").navigation();
                break;


        }
    }

    //删除或添加
    private void addOrdelete() {
        //判断是新增店内项目还是删除项目
        if (isshow) {
            //用于装选中的bean
            final List<StoreEditBean> l = new ArrayList<StoreEditBean>();
            final List<StoreEditBean> deletedata = new ArrayList<StoreEditBean>();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).isCheckbox()) {
                    l.add(list.get(i));
                } else {
                    deletedata.add(list.get(i));
                }
            }
            //判断是否有选中的要删除
            if (list.size() >=l.size()) {
                //弹出dialog
                final StoredeleteDialog dialog = new StoredeleteDialog(EditStoreActivity.this);
                dialog.setS("是否确认删除");
                //删除监听
                dialog.setDeletelisten(new StoredeleteDialog.OnclickDeleteListener() {
                    @Override
                    public void Onclick() {

                        dialog.dismiss();
                        //更换数据
                        DiffUtil.DiffResult diffResult =
                                DiffUtil.calculateDiff(new DiffCallBack(list, l), true);
                        diffResult.dispatchUpdatesTo(adapter);
                        adapter.changeData(l );
                            delete(deletedata);
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


            //新增店内项目
        } else {
            ARouter.getInstance().build("/main/act/add_new_project").navigation();
        }

    }

    //显示checkbox
    public void isdelete() {
        //获取显示状态
        diffResult.dispatchUpdatesTo(adapter);
        isshow = adapter.isShouwchek();
        if (isshow) {
            editStoreTvAddMove.setText("删除");
            editStoreTvDelete.setText("取消");
        } else {
            editStoreTvAddMove.setText("新增店内项目");
            editStoreTvDelete.setText("删除");
            //点击取消后将所有chekbox状态改为非选中
            for (StoreEditBean bean : list) {
                if (bean.isCheckbox()) {
                    bean.setCheckbox(false);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProjectData();
    }

    //查询店内项目
    private void getProjectData() {
        list.clear();
        pageIndex = 1;
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        int id = UserAuthUtil.getStoreId();
        map.put("cond", "{store:{id:" + id + "},isDelete:1,isCertify:2,state:2}");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("jf", "photo|goodsType");
        service.doCommonPost(null, MainUrl.Projectquery, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("EditStore_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        pageIndex++;
                        JSONArray array = object.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            StoreEditBean bean = new StoreEditBean();
//                            bean.setAuditReason(o.getString("auditReason"));
//                            bean.setCont(o.getInt("cont"));
                            if(o.has("describe")){
                                bean.setDescribe(o.getString("describe"));
                            }
//                            bean.setDetail(o.getString("detail"));
//                            bean.setDiscount(o.getInt("discount"));
                            bean.setGoodsName(o.getString("goodsName"));
                            bean.setId(o.getInt("id"));
                            bean.setIsCertify(o.getInt("isCertify"));
                            bean.setIsDelete(o.getInt("isDelete"));

                            String url=null;
                            String photo=o.getString("photo");
                            if(!StringUtil.isBlank(photo)){
                                url=o.getJSONObject("photo").getString("url");
                                bean.setIconurl(url);
                            }

                            bean.setGoodsType(o.getJSONObject("goodsType").getString("name"));
                            bean.setGoodsTypeId(o.getJSONObject("goodsType").getInt("id"));
//                            bean.setPrice(o.getInt("price"));
//                            bean.setSalenum(o.getInt("salenum"));
                            bean.setState(o.getInt("state"));
                            list.add(bean);
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("EditStore_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("EditStore_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissLoading();
                LogUtil.e("EditStore_onFinished", "onFinished");
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


    //查询店内更多项目
    private void getProjectMoreData() {
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        int id = UserAuthUtil.getStoreId();
        map.put("cond", "{store:{id:" + id + "},isDelete:1,isCertify:2,state:2}");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("jf", "photo|goodsType");
        service.doCommonPost(null, MainUrl.Projectquery, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("EditStore_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        pageIndex++;
                        JSONArray array = object.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            StoreEditBean bean = new StoreEditBean();
//                            bean.setAuditReason(o.getString("auditReason"));
//                            bean.setCont(o.getInt("cont"));
                            if(o.has("describe")){
                                bean.setDescribe(o.getString("describe"));
                            }
                            bean.setDetail(o.getString("detail"));
//                            bean.setDiscount(o.getInt("discount"));
                            bean.setGoodsName(o.getString("goodsName"));
                            bean.setId(o.getInt("id"));
                            bean.setIsCertify(o.getInt("isCertify"));
                            bean.setIsDelete(o.getInt("isDelete"));

                            String url=null;
                            String photo=o.getString("photo");
                            if(!StringUtil.isBlank(photo)){
                                url=o.getJSONObject("photo").getString("url");
                                bean.setIconurl(url);
                            }

                            bean.setGoodsType(o.getJSONObject("goodsType").getString("name"));
                            bean.setGoodsTypeId(o.getJSONObject("goodsType").getInt("id"));
//                            bean.setPrice(o.getInt("price"));
//                            bean.setSalenum(o.getInt("salenum"));
                            bean.setState(o.getInt("state"));
                            list.add(bean);
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("EditStore_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("EditStore_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissLoading();
                LogUtil.e("EditStore_onFinished", "onFinished");
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

    //开启店内项目
    private void open(final int position) {
        Map<String, String> map = new HashMap<>();
        int id = list.get(position).getId();
        map.put("data", "{id:" + id + ",state:2}");
        map.put("ctype", "goods");
        service.doCommonPost(null, MainUrl.OpenProject, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
              Log.e("open",result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        list.get(position).setState(2);
                        Toast.makeText(EditStoreActivity.this, "开启店铺成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditStoreActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("open_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("open_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("open_onFinished", "onFinished");
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


    //关闭店内项目
    private void close(final int position) {
        Map<String, String> map = new HashMap<>();
        int id = list.get(position).getId();
        map.put("data", "{id:" + id + ",state:1}");
        map.put("ctype", "goods");
        service.doCommonPost(null, MainUrl.CloseProject, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("close_onSuccess", result.toString());
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getBoolean("result")) {
                        list.get(position).setState(1);
                        Toast.makeText(EditStoreActivity.this, "项目已关闭！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditStoreActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("close_JSONException", e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("close_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("close_onFinished", "onFinished");
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

    //删除店内项目
    private void delete(List<StoreEditBean> data) {
        Map<String, String> map = new HashMap<>();
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
        map.put("spCodesTemp",buffre.toString());
        service.doCommonPost(null, MainUrl.DleteProjectItem, map, new XProgressCallback() {
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
