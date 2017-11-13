package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RelativeLayout;
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
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.squareup.picasso.MemoryPolicy;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/MessageInFoActivity")
public class MessageInFoActivity extends BaseAppcompatActivity {

    @BindView(R.id.message_info_backll)
    RelativeLayout messageInfoBackll;
//    @BindView(R.id.message_info_titleTxt)
//    TextView messageInfoTitleTxt;
    @BindView(R.id.message_info_dateTxt)
    TextView messageInfoDateTxt;
//    @BindView(R.id.message_info_timeTxt)
//    TextView messageInfoTimeTxt;
    @BindView(R.id.message_info_bodyTxt)
    TextView messageInfoBodyTxt;
    @Autowired(name = "key")
    int id;
    @BindView(R.id.cir_icon)
    CircleImageView cirIcon;
    private HttpServiceImpl service;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                MessageBean bean = (MessageBean) msg.obj;
                if (null != bean) {
                    initView(bean);
                    setMsssageStatus();
                    Log.e("setMsssageonError", "------");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        ButterKnife.bind(this);
        getMessageData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_message_in_fo;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.message_info_backll)
    public void onViewClicked() {
        finish();
    }

    //获取消息详情
    private void getMessageData() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("cond", "{id:" + id + "}");
        body.put("jf", "fromuserId|photo");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        MessageBean bean = new MessageBean();
                        bean.setContent(data.getString("content"));
                        bean.setType(data.getInt("type"));
                        bean.setTitle(data.getString("title"));
                        bean.setInsert_time(data.getString("insertTime"));
                        if(bean.getType()!=4)
                        bean.setUrl(data.getJSONObject("fromuserId").getJSONObject("photo").getString("url"));
                        Message message = new Message();
                        message.obj = bean;
                        message.what = 0;
                        handler.sendMessage(message);
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


    //给控件设置数据
    private void initView(MessageBean bean) {
//        messageInfoTitleTxt.setText(bean.getTitle());
        messageInfoBodyTxt.setText(bean.getContent());
        messageInfoDateTxt.setText(bean.getInsert_time());
        if (bean.getType()==4){
//            PicassoUtil.getPicassoObject().
            Glide.with(MessageInFoActivity.this).
                    load(R.mipmap.host_logo)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .resize(DpUtils.dpToPx(this,80),DpUtils.dpToPx(this,80))
                    .error(R.mipmap.host_logo)
                    .into(cirIcon);
        }else {
//            PicassoUtil.getPicassoObject().
            Glide.with(MessageInFoActivity.this).
                    load(bean.getUrl())
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .resize(DpUtils.dpToPx(this,80),DpUtils.dpToPx(this,80))
                    .error(R.mipmap.icon_load_faild)
                    .into(cirIcon);
        }

    }

    //修改数据的status状态
    private void setMsssageStatus() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("data", "{id:" + id + ",status:2}");
        service.doCommonPost(null, MainUrl.SetMessageStatus, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("setMsssageStatu", result.toString());
                if (!StringUtil.isBlank(result)) {
                    //查看数据成功
                    Log.e("setMsssageStatus", result.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("setMsssageonError", ex.toString());
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
