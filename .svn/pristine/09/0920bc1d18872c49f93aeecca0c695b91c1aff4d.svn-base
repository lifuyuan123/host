package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
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
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.ClerkBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 增加美容师界面
 */
@Route(path = "/main/act/AddNewBeautican")
public class AddNewBeautican extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.addnewbeautician_back_rl)
    RelativeLayout addnewbeauticianBackRl;
    @BindView(R.id.addnewbeautician_save_txt)
    TextView addnewbeauticianSaveTxt;
    @BindView(R.id.addnewbeautician_edt)
    EditText addnewbeauticianEdt;
    @BindView(R.id.addnewbeautician_addImg)
    ImageView addnewbeauticianAddImg;
    @BindView(R.id.addnewbeautician_hint)
    TextView addnewbeauticianHint;
    @BindView(R.id.addnewbeautician_edts)
    TextView addnewbeauticianEdts;
    @BindView(R.id.addnewbeauticiansx_edt)
    TextView addnewbeauticiansxEdt;
    @BindView(R.id.activity_add_new_beautican)
    LinearLayout activityAddNewBeautican;
    private HttpServiceImpl service;
    private String iconUrlid = "";
    /**
     * 图片选择相关
     **/
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private ClerkBean bean=new ClerkBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean.setId(-1);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_new_beautican;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.addnewbeautician_back_rl, R.id.addnewbeautician_save_txt, R.id.addnewbeautician_addImg,
            R.id.lin_sex, R.id.lin_type })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewbeautician_back_rl:
                finish();
                break;
            case R.id.addnewbeautician_save_txt:
                saveData();
                break;
            case R.id.addnewbeautician_addImg:
                final PopListDialog dialog = new PopListDialog(this);
                dialog.setPopListCallback(new PopListDialog.PopListCallback() {
                    @Override
                    public void callCamera() {
                        dialog.dismiss();
                        //裁剪参数
                        CropOptions cropOptions = new CropOptions.Builder().
                                setWithOwnCrop(false).create();
                        getTakePhoto().onPickFromCaptureWithCrop(getUri(), cropOptions);
                    }

                    @Override
                    public void callGallery() {
                        dialog.dismiss();
                        //裁剪参数
                        CropOptions cropOptions1 = new CropOptions.Builder()
                                .setWithOwnCrop(false).create();

                        getTakePhoto().
                                onPickFromGalleryWithCrop(getUri(), cropOptions1);

                    }
                });
                dialog.show();

                break;
            case R.id.lin_sex:
             ARouter.getInstance().build("/main/act/ChooseSexActivity").navigation(this,100);
                break;
            case R.id.lin_type:
                ARouter.getInstance().build("/main/act/ClerkTypeActivity").navigation(this,100);
                break;

        }
    }

    //保存
    private void saveData() {
        final Map<String, String> map = new HashMap<>();
        if (bean.getId()!=-1&&
                !StringUtil.isBlank(addnewbeauticianEdt.getText().toString())
                && !StringUtil.isBlank(iconUrlid)
                && !StringUtil.isBlank(addnewbeauticiansxEdt.getText().toString())) {
                int sex=0;
            if ((addnewbeauticiansxEdt.getText().toString()).contains("男")) {
                   sex=2;
            }else {
                sex=1;
            }
//            map.put("staff", "{name:\"" + addnewbeauticianEdt.getText().toString() + "\",sex:"+sex+"}");
            map.put("name",addnewbeauticianEdt.getText().toString());
            map.put("sex",String.valueOf(sex));
            map.put("photoId",iconUrlid);
            map.put("jobId", String.valueOf(bean.getId()));
            map.put("storeId",UserAuthUtil.getStoreId()+"");
            service.doCommonPost(null, MainUrl.addClerk, map, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("add_Beauti_onSuccess", result.toString()+map.toString());
                    try {
                        JSONObject object = new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(AddNewBeautican.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddNewBeautican.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("add_Beauti_JSONException", e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("add_Beauti_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("add_Beauti_onFinished", "onFinished");
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
        } else {
            if (bean.getId()==-1) {
                Toast.makeText(this, "请选择店员类型。", Toast.LENGTH_SHORT).show();
            }
            if (StringUtil.isBlank(addnewbeauticianEdt.getText().toString())) {
                Toast.makeText(this, "请输入姓名。", Toast.LENGTH_SHORT).show();
            }
            if (StringUtil.isBlank(iconUrlid)) {
                Toast.makeText(this, "请上传图片。", Toast.LENGTH_SHORT).show();
            }
            if (StringUtil.isBlank(addnewbeauticiansxEdt.getText().toString())) {
                Toast.makeText(this, "请选择性别。", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            switch (resultCode){
                case 101:
                    addnewbeauticiansxEdt.setText(data.getSerializableExtra("key")+"");
                    break;
                case 102:
                    bean= (ClerkBean) data.getSerializableExtra("key");
                    addnewbeauticianEdts.setText(bean.getName());
                    break;
            }
        }
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

    @Override
    public void takeSuccess(TResult result) {
        LogUtil.e("22222","成功");
        File file=new File(result.getImage().getPath());
        //上传
        upImg(file);
//        PicassoUtil.getPicassoObject()
        Glide.with(this)
                .load(file).into(addnewbeauticianAddImg);

    }

    //上传图片
    private void upImg(File file) {
        service.uploadFile(null, MainUrl.uploadFileUrl, file,null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("upImg_onSuccess",result.toString());
                try {
                    JSONObject obj=new JSONObject(result);
                    if(obj.getBoolean("result")){
                        //上传成功
                        JSONArray arr=new JSONArray(obj.getString("resultData"));
                        for (int i = 0; i <arr.length() ; i++) {
                            JSONObject data=arr.getJSONObject(i);
                            String ImgId=data.getInt("acyId")+"";
                            iconUrlid=ImgId;
                            Toast.makeText(AddNewBeautican.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(AddNewBeautican.this, "上传失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("upImg_JSONException",e.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("upImg_onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("upImg_onFinished","onFinished");
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

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
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
    protected void onSaveInstanceState(Bundle outState) {
        //创建TakePhoto实例
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

}
