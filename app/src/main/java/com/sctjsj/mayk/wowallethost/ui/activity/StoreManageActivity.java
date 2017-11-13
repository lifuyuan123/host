package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.PopListDialog;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//店铺管理
@Route(path = "/main/act/storemanage")
public class StoreManageActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener {
    //店铺名
    @BindView(R.id.storemanage_edt_store_name)
    EditText etStoreName;
    //品牌介绍
    @BindView(R.id.storemanage_edt_brand_introduction)
    EditText etBrandDescrib;

    @BindView(R.id.storemanage_iv_logo)
    ImageView ivStoreLogo;

    @BindView(R.id.storemanage_edt_adress)
    EditText etStoreAddress;

    @BindView(R.id.storemanage_edt_phone)
    EditText etStoreTel;


    private HttpServiceImpl http;

    /**
     * 图片选择相关
     **/
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private int photoId = -1;
    private String filePath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        getTakePhoto().onCreate(savedInstanceState);
        //初始化店铺信息
        initStoreInfo();
        //实例化takePhoto对象

    }

    @Override
    public int initLayout() {
        return R.layout.activity_store_manage;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.storemanage_linear_back, R.id.storemanage_tv_save, R.id.tv_to_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.storemanage_linear_back:
                finish();
                break;
            //保存店铺信息
            case R.id.storemanage_tv_save:
                if (check()) {
                    //判断有没有选择新的图片
                    if (filePath != null && filePath.endsWith(".jpg")) {
                        uploadFile(new File(filePath));
                    } else {
                        StoreBean sb = UserAuthUtil.getCurrentUser().getStoreBean();

                        sb.setName(etStoreName.getText().toString());
                        sb.setDetail(etBrandDescrib.getText().toString());
                        sb.setStoreAddress(etStoreAddress.getText().toString());
                        sb.setTelephone(etStoreTel.getText().toString());

                        updateStoreInfo(UserAuthUtil.getCurrentUser().getStoreBean().getLogoId(), sb);
                    }

                }
                break;

            case R.id.tv_to_choose:

                final PopListDialog dia = new PopListDialog(StoreManageActivity.this);
                dia.setPopListCallback(new PopListDialog.PopListCallback() {
                    @Override
                    public void callCamera() {
                        dia.dismiss();
                        //裁剪参数
                        CropOptions cropOptions = new CropOptions.Builder().
                                setWithOwnCrop(false).create();
                        getTakePhoto().onPickFromCaptureWithCrop(getUri(), cropOptions);
                    }

                    @Override
                    public void callGallery() {
                        dia.dismiss();
                        //裁剪参数
                        CropOptions cropOptions1 = new CropOptions.Builder()
                                .setWithOwnCrop(false).create();

                        getTakePhoto().
                                onPickFromGalleryWithCrop(getUri(), cropOptions1);
                    }
                });
                if (dia != null && !dia.isShowing()) {
                    dia.show();
                }

                break;
        }

    }

    //初始化店铺信息
    private void initStoreInfo() {
        StoreBean sb = UserAuthUtil.getCurrentUser().getStoreBean();

        if (sb != null) {
            photoId = sb.getLogoId();
            etStoreName.setText(sb.getName() + "");
            etBrandDescrib.setText(sb.getDetail() + "");
//            PicassoUtil.getPicassoObject().
            Glide.with(StoreManageActivity.this).
                    load(sb.getLogo()).
//                    resize(DpUtils.dpToPx(this, 80), DpUtils.dpToPx(this, 80)).
                    error(R.mipmap.icon_load_faild).
                    into(ivStoreLogo);

            etStoreAddress.setText(sb.getStoreAddress() + "");
            etStoreTel.setText(sb.getTelephone());

        }


    }

    //上传更新店铺信息
    private void updateStoreInfo(int acyId, StoreBean store) {
        LogUtil.e("上传之后 acyId",acyId+"");
        HashMap<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(store.getId()));
        map.put("storeLogoId", String.valueOf(acyId));
        map.put("name", store.getName());
        map.put("detail", store.getDetail());
        map.put("storeAddress", store.getStoreAddress());
        map.put("telephone", store.getTelephone());
        map.put("jf","storeLogo");

        http.doCommonPost(null, MainUrl.updateStoreInfoUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("修改店铺信息", resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean  result=obj.getBoolean("result");
                        //修改成功
                        if(result){
                            Toast.makeText(StoreManageActivity.this, "店铺信息更新成功", Toast.LENGTH_SHORT).show();

                            JSONObject storeT=obj.getJSONObject("newStore");
                            String detail=storeT.getString("detail");
                            String name=storeT.getString("name");
                            String storeAddress=storeT.getString("storeAddress");
                            String telephone=storeT.getString("telephone");

                            String storeLogo=storeT.getString("storeLogo");
                            String logo = null;
                            int logoId = 0;
                            if(!StringUtil.isBlank(storeLogo)){
                                logo=storeT.getJSONObject("storeLogo").getString("url");
                                logoId=storeT.getJSONObject("storeLogo").getInt("id");
                            }




                            StoreBean sb=UserAuthUtil.getCurrentUser().getStoreBean();
                            sb.setName(name);
                            sb.setTelephone(telephone);
                            sb.setDetail(detail);
                            sb.setStoreAddress(storeAddress);
                            sb.setLogo(logo);
                            sb.setLogoId(logoId);
                            UserBean ub=UserAuthUtil.getCurrentUser();
                            ub.setStoreBean(sb);
                            UserAuthUtil.saveUserBean(ub);

                            finish();
                        } else {
                            Toast.makeText(StoreManageActivity.this, "店铺信息更新失败", Toast.LENGTH_SHORT).show();

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

    public boolean check() {

        if (etStoreName.getText().toString().isEmpty()) {
            Toast.makeText(this, "店铺名为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etBrandDescrib.getText().toString().isEmpty()) {
            Toast.makeText(this, "品牌介绍为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etStoreAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "店铺地址为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etStoreTel.getText().toString().isEmpty()) {
            Toast.makeText(this, "店铺联系电话为空", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    @Override
    public void takeSuccess(TResult result) {

        if (result != null) {
            //图片存储路径
            String path = result.getImage().getPath();
            filePath = path;
            //显示新选择的图片
            if (path != null && !path.isEmpty()) {
//                PicassoUtil.getPicassoObject().
                Glide.with(StoreManageActivity.this).
                        load(new File(path)).into(ivStoreLogo);
            }


        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }


    /**
     * 申请权限
     *
     * @param invokeParam
     * @return
     */
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //创建TakePhoto实例
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //处理运行时权限
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 图片保存路径
     *
     * @return
     */
    public Uri getUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/wowallet/images/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }


    //上传图片
    public void uploadFile(File file) {


        if (file == null) {
            return;
        }

        http.uploadFile(null, MainUrl.uploadFileUrl, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                 LogUtil.e("upimageonSuccess",resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        boolean result = obj.getBoolean("result");
                        Toast.makeText(StoreManageActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        if (result) {
                            String str = obj.getString("resultData");

                            if (!StringUtil.isBlank(str)) {
                                JSONArray array = new JSONArray(str);
                                if (array != null && array.length() > 0) {
                                    //上传后的 acyid
                                    int acyId = array.getJSONObject(0).getInt("acyId");

                                    StoreBean sb = UserAuthUtil.getCurrentUser().getStoreBean();
                                    //新的店铺信息
                                    sb.setName(etStoreName.getText().toString());
                                    sb.setDetail(etBrandDescrib.getText().toString());
                                    sb.setStoreAddress(etStoreAddress.getText().toString());
                                    sb.setTelephone(etStoreTel.getText().toString());

                                    updateStoreInfo(acyId, sb);

                                }

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("upimageonError",ex.toString());
                LogUtil.e(ex.toString());
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
                LogUtil.e("进度",current+";"+total);
            }
        });


    }


}