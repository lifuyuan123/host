package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
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
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/AuditStatusActivity")
public class AuditStatusActivity extends BaseAppcompatActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    private HttpServiceImpl service;
    private int status=-1;
    private String name;
    @Autowired(name = "id")
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getAuditAtatus();
    }

    //获取审核状态信息
    private void getAuditAtatus() {
        Map<String,String> map=new HashMap<>();
        map.put("ctype","agentApp");
        map.put("id",id+"");
        map.put("jf","applyAp");
        LogUtil.e("getStatus_map",map.toString());
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("getStatus_onSuccess---",result.toString());
                if(!StringUtil.isBlank(result.toString())){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject applyTbl=object.getJSONObject("data");
                        status=applyTbl.getInt("status");
                        if(status==1){
                            Toast.makeText(AuditStatusActivity.this, "通过审核", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/AgentMangeActivity").navigation();
                            finish();
                        }else if(status==2){
                            JSONObject applyAp=applyTbl.getJSONObject("applyAp");
                            name=applyAp.getString("name");
                            tvContent.setText("代理"+name+"购买成功，请等待管理员审核!");
                        }else if(status==4){
                            ARouter.getInstance().build("/main/act/ChooseAgentActivity").navigation();
                            finish();
                        }else if(status==3){
                            Toast.makeText(AuditStatusActivity.this, "审核不通过", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("getStatus_JSONException---",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("getStatus_onError---",ex.toString());
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
        return R.layout.activity_audit_status;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.editstore_linear_back, R.id.iv_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.editstore_linear_back:
                finish();
                break;
            //刷新
            case R.id.iv_refresh:
                getAuditAtatus();
                break;
        }
    }
}
