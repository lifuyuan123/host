package com.sctjsj.mayk.wowallethost.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.CallUtil;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.ui.customview.StoredeleteDialog;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lifuy on 2017/5/15.
 */

public class OwnFragment extends Fragment {

    @BindView(R.id.setting_userIcon_img)
    ImageView ivStoreLogo;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_address)
    TextView tvStoreAddress;
    @BindView(R.id.tv_store_tel)
    TextView tvStoreTel;
    @BindView(R.id.own_tv_money)
    TextView ownTvMoney;
    @BindView(R.id.setting_contans_ll)
    LinearLayout settingContansLl;
    @BindView(R.id.setting_feedBack_ll)
    LinearLayout settingFeedBackLl;
    @BindView(R.id.own_agent_mange)
    LinearLayout ownAgentMange;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;
    private int state = -1;


    private HttpServiceImpl http;
    private String callNum = "4001001234";
    private StoredeleteDialog dialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (state == 1) {
                    ownAgentMange.setVisibility(View.VISIBLE);
                } else {
                    ownAgentMange.setVisibility(View.GONE);
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.frg_mine, null);
        ButterKnife.bind(this, mView);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        EventBus.getDefault().register(this);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
        getMoney();
    }

    private void initView() {
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getMoney();
                refresh.finishRefresh();
            }
        });
        UserBean ub = UserAuthUtil.getCurrentUser();
        if (ub != null) {

//            PicassoUtil.getPicassoObject().
                    Glide.with(this).
                    load(ub.getStoreBean().getLogo()).
//                    resize(DpUtils.dpToPx(getActivity(), 80), DpUtils.dpToPx(getActivity(), 80)).
                    error(R.mipmap.icon_default_portrait).
                    into(ivStoreLogo);

            tvStoreName.setText(ub.getStoreBean().getName() + "");
            tvStoreAddress.setText(ub.getStoreBean().getStoreAddress() + "");
            tvStoreTel.setText(ub.getStoreBean().getTelephone() + "");
            dialog = new StoredeleteDialog(getActivity());
            dialog.setS("沃钱包商户将要拨打电话：");
            dialog.setText("400-100-5799");
            dialog.setDeletelisten(new StoredeleteDialog.OnclickDeleteListener() {
                @Override
                public void Onclick() {
                    Log.e("打电话", "---------");
                    CallUtil.makeCall(getActivity(), callNum);
                }
            });

        }


    }


    @OnClick({R.id.setting_contans_ll, R.id.setting_feedBack_ll, R.id.mine_setting_layout,
            R.id.lin_store_phone, R.id.mine_showQr, R.id.own_agent_mange,R.id.lin_own})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_contans_ll:
                dialog.show();
                break;
            case R.id.setting_feedBack_ll:
                ARouter.getInstance().build("/main/act/SettingFeedBackActivity").navigation();
                break;
            case R.id.lin_store_phone:
                break;
            //商家二维码
            case R.id.mine_showQr:
                ARouter.getInstance().build("/main/act/GatheringQrActivity").navigation();
                break;
            //设置页面
            case R.id.mine_setting_layout:
                ARouter.getInstance().build("/main/act/SettingActivity").navigation(getActivity(), 100);
                break;
            //代理商管理
            case R.id.own_agent_mange:
                ARouter.getInstance().build("/main/act/AgentMangeActivity").navigation();
                break;
            case R.id.lin_own:
                ARouter.getInstance().build("/main/act/UserInfoActivity").navigation();
                break;
        }
    }

    private void getMoney() {
        Map<String, String> map = new HashMap<>();
        map.put("cond", "{id:" + UserAuthUtil.getUserId() + "}");
        map.put("jf", "userSpend");
        map.put("ctype", "user");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("Mybalance_id", UserAuthUtil.getUserId() + "");
                LogUtil.e("Mybalance_onSuccess", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONArray array = object.getJSONArray("resultList");
                            if (array != null && array.length() > 0) {
                                if (array.getJSONObject(0).getJSONArray("userSpend") != null && array.getJSONObject(0).getJSONArray("userSpend").length() > 0) {
                                    double balance = array.getJSONObject(0).getJSONArray("userSpend").getJSONObject(0).getDouble("amount");
                                    DecimalFormat df = new DecimalFormat("######0.00");
                                    ownTvMoney.setText("¥" + df.format(balance));
                                }
                                state = array.getJSONObject(0).getInt("agent");
                                Message message = Message.obtain();
                                message.what = 1;
                                handler.sendMessage(message);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("Mybalance_JSONException", e.toString());
                    }


                }

            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("Mybalance_Throwable", ex.toString());
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

    @Subscribe
    public void onEventMainThread(String s) {
        if (s.equals("out")) {
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
