package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
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
import com.sctjsj.basemodule.base.ui.widget.rv.WrapGridLayoutManager;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.StorePhotoAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.StorePhoto;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//店铺广告
@Route(path = "/main/act/storeadver")
public class StoreAdvertActivity extends BaseAppcompatActivity implements StorePhotoAdapter.StortAdapterCallBack, TakePhoto.TakeResultListener, InvokeListener {


    @BindView(R.id.storeadver_recycler)
    RecyclerView storeadverRecycler;
    @BindView(R.id.store_advert_save)
    TextView storeAdvertSave;

    private List<StorePhoto> data = new ArrayList<>();

    private StorePhotoAdapter adapter;
    private WrapGridLayoutManager manager;
    private HttpServiceImpl service;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private String filePath = null;
    private List<StorePhoto> imgId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        init();
        getStorePhoto();
    }

    private void init() {
        adapter = new StorePhotoAdapter(this, data);
        manager = new WrapGridLayoutManager(this, 2);
        storeadverRecycler.setNestedScrollingEnabled(false);
        storeadverRecycler.setLayoutManager(manager);
        storeadverRecycler.setAdapter(adapter);
        adapter.setCallBack(this);

    }

    @OnClick({R.id.storemanage_linear_back, R.id.store_advert_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.storemanage_linear_back:
                finish();
                break;
            case R.id.store_advert_save:
                Log.e("save","-------");
                savePhoto();
                break;
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_store_advert;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.storemanage_linear_back)
    public void onViewClicked() {
        finish();
    }

    //获取商家相册
    private void getStorePhoto() {
        data.clear();
        data.add(new StorePhoto("add"));
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "storeGallery");
        body.put("cond", "{store:{id:"+UserAuthUtil.getStoreId()+"},isDelete:1}");
        body.put("jf", "photo");
        body.put("size", "999");
        service.doCommonPost(null, MainUrl.Projectquery, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        Log.e("getStorePhoto",result.toString());
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject photo = array.getJSONObject(i);
                                StorePhoto mStorePhoto = new StorePhoto("net");
                                mStorePhoto.setId(photo.getInt("id"));
                                mStorePhoto.setUrl(photo.getJSONObject("photo").getString("url"));
                                data.add(0, mStorePhoto);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
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


    //adapter的回调
    @Override
    public void delPhoto(StorePhoto photo,int position) {
        switch (photo.getType()){
            case "net":
                delImg(photo,position);
                break;
            case "photo":
                for (int i = 0; i <imgId.size() ; i++) {
                    if(imgId.get(i).getId()==photo.getId()){
                        imgId.remove(i);
                    }
                }
                data.remove(position);
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void addImg() {

        final PopListDialog dia = new PopListDialog(StoreAdvertActivity.this);
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

    }

    @Override
    public void takeSuccess(TResult result) {
        Log.e("takeSuccess",result.toString());
        if (result != null) {
            //图片存储路径
            String path = result.getImage().getPath();
            filePath = path;
            UpFile(new File(path));
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
    private void UpFile(File file) {
        service.uploadFile(null, MainUrl.uploadFileUrl, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("UpFile",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            String str = object.getString("resultData");
                            if (!StringUtil.isBlank(str)) {
                                JSONArray array = new JSONArray(str);
                                if (array != null && array.length() > 0) {
                                    //上传后的 acyid
                                    int acyId = array.getJSONObject(0).getInt("acyId");
                                    StorePhoto photo=new StorePhoto("photo");
                                    photo.setUrl(filePath);
                                    photo.setId(acyId);
                                    imgId.add(photo);
                                    data.add(data.size()-1,photo);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(StoreAdvertActivity.this, "上传图片失败！", Toast.LENGTH_SHORT).show();
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
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"上传照片中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }


    //保存图片
    private void savePhoto(){
        if(imgId.size()>0){
            StringBuffer buffre=new StringBuffer();
            for (int i = 0; i <imgId.size() ; i++) {
                if(imgId.size()-1==i){
                    buffre.append(imgId.get(i).getId());
                }else {
                    buffre.append(imgId.get(i).getId()+",");
                }
            }
            HashMap<String,String> body=new HashMap<>();
            body.put("acyIdList",buffre.toString());
            body.put("storeId",UserAuthUtil.getStoreId()+"");
            service.doCommonPost(null, MainUrl.SaveStorePhoto, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("savePhoto",result.toString());
                    if(!StringUtil.isBlank(result)){
                        try {
                            JSONObject object=new JSONObject(result);
                            if(object.getBoolean("result")){
                                Toast.makeText(StoreAdvertActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                                finish();
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
                    dismissLoading();
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    showLoading(true,"保存中。。");
                }

                @Override
                public void onLoading(long total, long current) {

                }
            });

        }else {
            Toast.makeText(this, "您没有修改任何信息！", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    //删除图片
    private void delImg(final StorePhoto photo, final int position){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","storeGallery");
        body.put("data","{id:"+photo.getId()+",isDelete:2}");
        service.doCommonPost(null, MainUrl.baseModifyUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("delImg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            data.remove(position);
                            adapter.notifyItemRemoved(position);
                            Log.e("position",position+"---");
                            //         adapter.notifyItemChanged(position);
                            //adapter.notifyDataSetChanged();
                            Toast.makeText(StoreAdvertActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(StoreAdvertActivity.this, "删除失败请重试！", Toast.LENGTH_SHORT).show();
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
