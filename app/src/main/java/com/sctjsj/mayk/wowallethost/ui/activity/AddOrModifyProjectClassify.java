package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectClassifyBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/add_or_modify_project_classify")
public class AddOrModifyProjectClassify extends BaseAppcompatActivity {

    @BindView(R.id.act_add_or_modify_classify_tv_title)
    TextView mTVTitle;

    @BindView(R.id.act_add_or_modify_classify_et_input)
    EditText mETInput;

    @Autowired(name = "classify")
    public ProjectClassifyBean classifyBean;
    private HttpServiceImpl service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if (classifyBean == null) {
            mTVTitle.setText("新增项目分类");
        } else {
            mTVTitle.setText("修改项目分类");
            mETInput.setText(classifyBean.getName());
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_or_modify_project_classify;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.act_add_or_modify_classify_rl_back, R.id.act_add_or_modify_classify_tv_op})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_add_or_modify_classify_rl_back:
                finish();
                break;
            case R.id.act_add_or_modify_classify_tv_op:
                if(classifyBean == null){
                    saveProject();
                }else {
                    changeProject();
                }
                finish();
                break;
        }
    }

    //修改项目分类
    private void changeProject() {
        Map<String,String> map=new HashMap<>();
        String s=mETInput.getText().toString();
        map.put("ctype","goodsType");
        map.put("data","{id:"+classifyBean.getId()+",name:\"" + s.toString() + "\"}");
        if(!StringUtil.isBlank(s)){
            service.doCommonPost(null, MainUrl.changeProjectType, map, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("change_onSuccess",result.toString());
                    try {
                        JSONObject o=new JSONObject(result);
                        if(o.getBoolean("result")){
                            Toast.makeText(AddOrModifyProjectClassify.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddOrModifyProjectClassify.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("change_JSONException",e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("change_onError",ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("change_onFinished","onFinished");
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
        }else {
            Toast.makeText(this, "请填写分类名称。", Toast.LENGTH_SHORT).show();
        }

    }

    //保存新增项目分类
    private void saveProject() {
        Map<String,String> map=new HashMap<>();
        String s=mETInput.getText().toString();
        map.put("data","{name:\""+s.toString()+"\",isDelete:1}");
        map.put("ctype","goodsType");
        if(!StringUtil.isBlank(s)){
            service.doCommonPost(null, MainUrl.addProjectType, map, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("type_onSuccess",result.toString());
                    try {
                        JSONObject o=new JSONObject(result);
                        if(o.getBoolean("result")){
                            Toast.makeText(AddOrModifyProjectClassify.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(AddOrModifyProjectClassify.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("type_JSONException",e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("type_onError",ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("type_onFinished","onFinished");
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
        }else {
            Toast.makeText(this, "请填写分类名称。", Toast.LENGTH_SHORT).show();
        }

    }
}
