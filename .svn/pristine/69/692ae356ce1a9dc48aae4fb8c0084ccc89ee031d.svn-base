package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.Application.MyApplication;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//营业状态
@Route(path = "/main/act/businessstatus")
public class BusinessStatusActivity extends BaseAppcompatActivity {

    @BindView(R.id.businessstatus_switchbt)
    SwitchButton switchButton;

    @BindView(R.id.tv_store_status)
    TextView tvStoreStatus;


    private HttpServiceImpl http;

    //1待审核，2正常，3关闭
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        initStoreStatus();

        //点击设置店铺状态
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //点击设置营业
                 if(isChecked){
                        setStoreStatus(true);
                 }else {
                     //点击设置打烊
                     setStoreStatus(false);
                 }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initStoreStatus() {
        StoreBean sb= UserAuthUtil.getCurrentUser().getStoreBean();
        if(sb!=null){
            switch (sb.getStoreStatus()){
                case 1:
                    switchButton.setChecked(false);
                    switchButton.setEnableEffect(false);
                    tvStoreStatus.setText("店铺待审核");
                    break;
                case 2:
                    switchButton.setChecked(true);
                    switchButton.setEnableEffect(true);
                    tvStoreStatus.setText("营业中");
                    break;
                case 3:
                    switchButton.setChecked(false);
                    switchButton.setEnableEffect(true);
                    tvStoreStatus.setText("打烊啦");
                    break;

            }

        }
    }



    @Override
    public int initLayout() {
        return R.layout.activity_business_status;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.businessstatus_linear_back)
    public void onViewClicked() {
       // setResult(102,new Intent().putExtra("ischeck",ischeck));
        finish();
    }

    private void setStoreStatus(boolean flag){

        HashMap<String,String> map=new HashMap<>();
        int status=0;
        if(flag){
            status=2;
        }else {
            status=3;
        }
        map.put("ctype","store");
        map.put("data","{id:"+UserAuthUtil.getStoreId()+",storeStatus:"+status+"}");
        final int finalStatus = status;
        http.doCommonPost(null, MainUrl.baseModifyUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean result=obj.getBoolean("result");
                        //数据更新成功
                        if(result){
                            //
                            if(2== finalStatus){
                                UserBean ub= UserAuthUtil.getCurrentUser();
                                ub.getStoreBean().setStoreStatus(2);
                                UserAuthUtil.saveUserBean(ub);

                            }

                            if(3==finalStatus){
                                UserBean ub= UserAuthUtil.getCurrentUser();
                                ub.getStoreBean().setStoreStatus(3);
                                UserAuthUtil.saveUserBean(ub);
                            }


                        }else{

                            if(2==finalStatus){
                                switchButton.setChecked(false);
                            }

                            if(3==finalStatus){
                                switchButton.setChecked(true);
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        initStoreStatus();
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
                showLoading(false,"加载中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


    @OnClick({R.id.businessstatus_linear_back})
    public void clickView(View view){
        switch(view.getId()){
            case R.id.businessstatus_linear_back:
                finish();
                break;
        }
       // return super.onKeyDown(keyCode, event);
    }

}
