package com.sctjsj.mayk.wowallethost.ui.fragment;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lifuy on 2017/5/15.
 */

public class ShopsFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.iv_store_logo)
    ImageView ivStoreLogo;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;
    @BindView(R.id.tv_store_status)
    TextView tvStoreStatus;
    @BindView(R.id.store_iv_open_close)
    ImageView ivStoreStatus;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.shop_lin_open_close)
    RelativeLayout shopLinOpenClose;
    private HttpServiceImpl service;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_shop, null);
        unbinder = ButterKnife.bind(this, view);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initStoreStatus();
        getRate();
    }

    //获取佣金比例
    private void getRate() {
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("ctype", "store");
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("store", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject object1 = object.getJSONObject("data");
                        tvRate.setText(object1.getDouble("paymentRate") + "%");
                        UserAuthUtil.getCurrentUser().getStoreBean().setStoreStatus(object1.getInt("storeStatus"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("store_JSONException", e.toString());
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

    private void initStoreStatus() {
        if (UserAuthUtil.getCurrentUser() != null) {
            StoreBean sb = UserAuthUtil.getCurrentUser().getStoreBean();
//            PicassoUtil.getPicassoObject().
            Glide.with(this).
                    load(sb.getLogo()).
//                    resize(DpUtils.dpToPx(getActivity(), 60), DpUtils.dpToPx(getActivity(), 60)).
                    error(R.mipmap.icon_default_portrait).
                    into(ivStoreLogo);
            tvStoreName.setText(sb.getName() + "");

            //1待审核，2正常，3关闭
            switch (sb.getStoreStatus()) {
                case 1:
                    tvStoreStatus.setText("店铺待审核");
                    shopLinOpenClose.setEnabled(false);
                    break;
                case 2:
                    shopLinOpenClose.setEnabled(true);
                    tvStoreStatus.setText("正在营业中");
                    ivStoreStatus.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.store_open_icon));
                    break;
                case 3:
                    shopLinOpenClose.setEnabled(true);
                    tvStoreStatus.setText("打烊啦");
                    ivStoreStatus.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.store_close_icon));

                    break;
                default:
                    tvStoreStatus.setText("状态异常");
                    break;

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.shop_lin_open_close, R.id.shop_lin_manage, R.id.shop_lin_project, R.id.shop_lin_advertising, R.id.shop_lin_preview, R.id.shop_lin_beautician, R.id.shop_lin_evaluation, R.id.shop_fans})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //营业/未营业
            case R.id.shop_lin_open_close:
                ARouter.getInstance().build("/main/act/businessstatus").navigation();
                break;
            //店铺管理
            case R.id.shop_lin_manage:
                ARouter.getInstance().build("/main/act/storemanage").navigation();
                break;
            //店内项目
            case R.id.shop_lin_project:
                ARouter.getInstance().build("/main/act/editstore").navigation();
                break;
            //店铺广告
            case R.id.shop_lin_advertising:
                ARouter.getInstance().build("/main/act/storeadver").navigation();
                break;
            //店铺预览
            case R.id.shop_lin_preview:
                ARouter.getInstance().build("/main/act/store").navigation();
                break;
            //美容师
            case R.id.shop_lin_beautician:
                ARouter.getInstance().build("/main/act/BeauticianManageActivity").navigation();
                break;
            //评价管理
            case R.id.shop_lin_evaluation:
                ARouter.getInstance().build("/main/act/EvaluateManageActivity").navigation();
                break;
            //粉丝
            case R.id.shop_fans:
                ARouter.getInstance().build("/main/act/user/fan").navigation();
                break;
        }
    }

}
