package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.BeauticanBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


//店员详情
@Route(path = "/main/act/ClerkdetailsActivity")
public class ClerkdetailsActivity extends BaseAppcompatActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @Autowired(name = "key")
    int  staffid;
    private HttpServiceImpl service;
    private BeauticanBean bean;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if(bean!=null){
                    tvType.setText(bean.getJobName());
                    tvName.setText(bean.getName());
                    if(bean.getSex()==1){
                        tvSex.setText("男");
                    }else if (bean.getId()==2){
                        tvSex.setText("女");
                    }
//                    PicassoUtil.getPicassoObject().
                    Glide.with(ClerkdetailsActivity.this).
                            load(bean.getPhoto())
//                            .resize(DpUtils.dpToPx(ClerkdetailsActivity.this,100),DpUtils.dpToPx(ClerkdetailsActivity.this,100))
                            .into(ivIcon);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        //获取店员信息
        getClerkData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_clerkdetails;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.editstore_linear_back)
    public void onViewClicked() {
        finish();
    }

    //获取店员
    private void getClerkData() {
        HashMap<String, String> body = new HashMap<>();
        body.put("storeId", UserAuthUtil.getStoreId() + "");
        body.put("jf", "staffPhoto|job");
        body.put("staffId",String.valueOf(staffid));
        service.doCommonPost(null, MainUrl.GetStoreWorker, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray arr = object.getJSONArray("resultList");
                        if (null != arr && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject beaUtican = arr.getJSONObject(i);
                                bean = new BeauticanBean();
                                bean.setId(beaUtican.getInt("id"));
                                bean.setSex(beaUtican.getInt("sex"));
                                bean.setName(beaUtican.getString("name"));
                                JSONObject job = beaUtican.getJSONObject("job");
                                bean.setJobId(job.getInt("id"));
                                bean.setJobName(job.getString("name"));
                                bean.setPhoto(beaUtican.getJSONObject("staffPhoto").getString("url"));
                                Message message=Message.obtain();
                                message.what=1;
                                handler.sendMessage(message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
