package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
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
import com.sctjsj.basemodule.base.ui.widget.rv.WrapGridLayoutManager;
import com.sctjsj.basemodule.base.ui.widget.rv.WrapLinearLayoutManager;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.AddNewProjectAdapter;
import com.sctjsj.mayk.wowallethost.event.ProjectIntroduceSaveEvent;
import com.sctjsj.mayk.wowallethost.event.StartUploadEvent;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.ImageBean;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectImageData;
import com.sctjsj.mayk.wowallethost.ui.xWidget.UploadProgressDialog;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

/**
 * 新增项目页面
 */
@Route(path = "/main/act/add_new_project")
public class AddNewProjectActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    private HttpServiceImpl http;
    /**
     * 图片选择相关
     **/
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    //判断点击的是项目 icon 还是项目图片列表 1:icon  2:list
    private int clickType = 0;
    @BindView(R.id.iv_project_icon_preview)
    ImageView mIVIconPreview;
    /**
     * 图片选择相关
     **/

    private List<ProjectImageData> data;

    private int goodsTypeids;

    //项目 icon 的路径
    private String iconFilePath = null;
    //编辑页跳转过来的position
    @Autowired(name = "id")
    int id;

    private boolean isNet=false;

    @BindView(R.id.rv)
    RecyclerView mRV;
    @BindView(R.id.editor)
    RichEditor mEditor;

    @BindView(R.id.tv_project_classify)
    TextView tvClassify;

    private int projectClassifyId = -1;
    @BindView(R.id.et_project_name)
    EditText etProjectName;

    private PChangeListener pListener;
    //上传图片总数
    private int uploadSize = 0;
    private int LogoId = -1;
    //项目详情图片连接
    private List<Integer> galleryList = new ArrayList();

    private MyThread myT = new MyThread();

    private Handler myHandler = new Handler() {
        int cur = 0;

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 1:
                    if (1 == (int) msg.obj) {
                        cur++;
                        if (cur == uploadSize) {
                            myT.setOk(true);
                        }
                    }
                    break;
                case 2:
                    projectClassifyId=goodsTypeids;
                    break;
            }
        }
    };

    //编辑项目图片列表
    @BindView(R.id.tv_ok_edit_project_pic)
    TextView mTVOkEditProjectPic;

    public TextView getmTVOkEditProjectPic() {
        return mTVOkEditProjectPic;
    }

    @Override
    protected void onDestroy() {
        if (myT != null && myT.isAlive()) {
            myT.setFlag(false);
            myT.interrupt();
        }
        super.onDestroy();
    }

    private AddNewProjectAdapter adapter;

    //富文本 H5
    private String projectIntroduceContent;

    private UploadProgressDialog dia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册 Eventbus
        EventBus.getDefault().register(this);

        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        //实例化takePhoto对象
        getTakePhoto().onCreate(savedInstanceState);

        initRV();
        mEditor = (RichEditor) findViewById(R.id.editor);

        mEditor.setPadding(10, 10, 10, 10);

        mEditor.setPlaceholder("请编辑项目介绍...");

//        //判断是否有返回的id
//        if(!StringUtil.isBlank(String.valueOf(id))){
//            tvTitle.setText("编辑店内项目");
//            //获取要编辑的店内项目资料
//            getProjectData();
//        }
    }

    //获取要编辑的店内项目资料
    private void getProjectData() {
        Map<String,String> body=new HashMap<>();
        body.put("ctype", "goods");
        body.put("cond", "{store:{id:" + UserAuthUtil.getStoreId() + "},isDelete:1}");
        body.put("jf", "photo|goodsGallery|goodsType");
        body.put("id",String.valueOf(id));
        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("edit_onSuccess",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject object1=object.getJSONObject("data");
                        String goodsname=object1.getString("goodsName");
                        String describe=object1.getString("describe");
                        String detail=object1.getString("detail");
                        JSONArray array=object1.getJSONArray("goodsGallery");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object2=array.getJSONObject(i);
                            JSONObject object3=object2.getJSONObject("photo");
                            galleryList.add(object3.getInt("id"));
                            LogUtil.e("22222222",object3.getString("url"));
                            ImageBean imageBean=new ImageBean(object3.getString("url"));
                            imageBean.setType("net");
                            ProjectImageData imgData = new ProjectImageData(1);
                            imgData.setImageBean(imageBean);
                            adapter.getData().add(adapter.getData().size() - 1, imgData);
                            adapter.notifyItemInserted(adapter.getData().size() - 2);
                        }
                        JSONObject object2=object1.getJSONObject("photo");
                        LogoId=object2.getInt("id");
                        JSONObject object3=object1.getJSONObject("goodsType");
                        goodsTypeids=object3.getInt("id");
                        Message message=Message.obtain();
                        message.what=2;
                        myHandler.sendMessage(message);
                        mEditor.setHtml(detail);
                        etProjectName.setText(describe);
                        tvClassify.setText(goodsname);
                        iconFilePath = object2.getString("url");
                        isNet=true;
//                        PicassoUtil.getPicassoObject().
                        Glide.with(AddNewProjectActivity.this).
                                load(object2.getString("url")).into(mIVIconPreview);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("edit_JSONException",e.toString());
                    }
                }

            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("edit_onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("edit_onFinished");
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
    public int initLayout() {
        return R.layout.activity_add_new_project;
    }

    @Override
    public void reloadData() {

    }

    private void initRV() {
        data = new ArrayList<>();
        data.add(new ProjectImageData(2));
        adapter = new AddNewProjectAdapter(this, data);
        mRV.setAdapter(adapter);
        WrapLinearLayoutManager manager = new WrapLinearLayoutManager(this);
        WrapGridLayoutManager manager1 = new WrapGridLayoutManager(this, 2);
        mRV.setNestedScrollingEnabled(false);
        mRV.setLayoutManager(manager1);
        manager.setAutoMeasureEnabled(false);
        mRV.setHasFixedSize(false);
    }


    @OnClick({R.id.ll_to_edit_project_introduce, R.id.ll_add_project_icon, R.id.tv_ok_edit_project_pic,
            R.id.act_add_new_project_rl_to_classify, R.id.save_new_project,R.id.mine_balance_back})

    public void onViewClicked(View view) {

        switch (view.getId()) {
            //点击去富文本编辑
            case R.id.ll_to_edit_project_introduce:
                ARouter.getInstance().build("/main/act/edit_project_introduce").withString("html", mEditor.getHtml()).navigation(this, 100);
                break;
            //添加项目 icon
            case R.id.ll_add_project_icon:
                clickType = 1;
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
            //点击完成项目图片列表的编辑
            case R.id.tv_ok_edit_project_pic:
                adapter.dismissDeleteBadge();
                break;

            //点击去选择项目分类
            case R.id.act_add_new_project_rl_to_classify:
                ARouter.getInstance().build("/main/act/project_classify").withString("key", "100").navigation(this, 100);
                break;


            //保存
            case R.id.save_new_project:
                if (check()) {

                    if (dia == null) {
                        dia = new UploadProgressDialog(this);

                    }


                    if (iconFilePath != null) {
                        ImageBean igb = new ImageBean(iconFilePath);
                        ProjectImageData imgData = new ProjectImageData(1);
                        if(isNet){
                            igb.setType("net");
                        }else {
                            igb.setType("");
                        }

                        imgData.setImageBean(igb);
                        dia.setLogo(imgData);
                    }

                    data = adapter.getData();

                    dia.setUploadList(data);

                    if (!dia.isShowing()) {
                        dia.show();
                    }

                }

                break;

            case R.id.mine_balance_back:
                finish();
                break;



        }


    }


    @Subscribe
    public void onMainEvent(ProjectIntroduceSaveEvent event) {
        if (event != null) {
            projectIntroduceContent = event.getContent();
            mEditor.setHtml(projectIntroduceContent);
        }
    }

    @Subscribe
    public void onMainEvent(StartUploadEvent event) {
        if (event != null) {
            if (1 == event.getOp()) {
                uploadList(adapter.getData());
            }

        }

    }


    /**
     * 图片选取成功后回调
     *
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {

        //选择的文件的路径
        String path = result.getImage().getPath();
        if (StringUtil.isBlank(path)) {
            return;
        }

        if (1 == clickType) {
            Log.e("gg", "icon 选取成功");
            iconFilePath = path;
//            PicassoUtil.getPicassoObject()
            Glide.with(AddNewProjectActivity.this)
                    .load(new File(path)).into(mIVIconPreview);
        } else {
            Log.e("gg", "list 选取成功");

            //获取选中的文件
            ImageBean igb = new ImageBean(result.getImage().getPath());
            ProjectImageData imgData = new ProjectImageData(1);
            igb.setType("");
            imgData.setImageBean(igb);
            adapter.getData().add(adapter.getData().size() - 1, imgData);
            adapter.notifyItemInserted(adapter.getData().size() - 2);
        }

        //点击选择事件归零
        clickType = 0;

    }

    /**
     * 图片选取失败后回调
     *
     * @param result
     * @param msg
     */
    @Override
    public void takeFail(TResult result, String msg) {
        clickType = 0;
    }

    /**
     * 图片选取取消后回调
     */
    @Override
    public void takeCancel() {
        clickType = 0;
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

        if (requestCode == 100) {
            if (resultCode == 101) {
                String s = data.getStringExtra("key");
                projectClassifyId = data.getIntExtra("id", -1);
                tvClassify.setText(s);
            }
        }

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


    public void submit() {

        if (dia.isShowing()) {
            dia.dismiss();
        }

        StringBuffer sb = new StringBuffer();

        String str1 = "";
        for (int i = 0; i < galleryList.size(); i++) {

            String str = galleryList.get(i).toString();
            sb.append("," + str);

        }
        str1 = sb.toString();
        str1 = str1.replaceFirst(",", "");

        HashMap<String, String> map = new HashMap<>();
        map.put("goodsLogo", String.valueOf(LogoId));
        map.put("Goodsgallery", str1);
        String name = etProjectName.getText().toString();
        String html = mEditor.getHtml().toString();
//        map.put("goods","{goodsName:"+"\""+etProjectName.getText().toString()+"\""+
//                "goodsType:"+projectClassifyId+"describe"+mEditor.getHtml().toString()+"}");

//        map.put("goodsName", tvClassify.getText().toString());
        map.put("goodsName",name);
        map.put("storeId", String.valueOf(UserAuthUtil.getStoreId()));
        map.put("goodsTypeId", String.valueOf(projectClassifyId));
        map.put("detail", mEditor.getHtml().toString());
        http.doCommonPost(null, MainUrl.AddNewProject, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("提交结果", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);

                        boolean result = obj.getBoolean("result");
                        if (result) {
                            Toast.makeText(AddNewProjectActivity.this, "项目新增成功，等待后台审核", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AddNewProjectActivity.this, "项目新增失败", Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("ex", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                myT.setFlag(false);
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

    private boolean check() {

        if (StringUtil.isBlank(etProjectName.getText().toString())) {
            Toast.makeText(this, "请填写项目名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (-1 == projectClassifyId) {
            Toast.makeText(this, "请选择项目所属分类", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (iconFilePath == null) {
            Toast.makeText(this, "请先选择项目 Logo", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (adapter.getData().size() <= 1) {
            Toast.makeText(this, "请至少选择一张项目内图片", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtil.isBlank(mEditor.getHtml())) {
            Toast.makeText(this, "请先编写项目描述", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //上传列表
    public void uploadList(final List<ProjectImageData> list) {


        if (iconFilePath == null) {
            Toast.makeText(this, "项目 logo 为空", Toast.LENGTH_SHORT).show();
            return;
        }


        if (list == null || list.size() <= 0) {
            Toast.makeText(this, "上传列表为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //最终需要上传的个数
        uploadSize = list.size();

        myT.start();

        //上传 logo
        http.uploadFile(null, MainUrl.uploadFileUrl, new File(iconFilePath), null, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("logoonSuccess", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        boolean result = obj.getBoolean("result");
                        //上传成功
                        if (result) {
                            String resultData = obj.getString("resultData");
                            JSONArray info = new JSONArray(resultData);
                            //上传成功后的图片 id
                            int acyId = info.getJSONObject(0).getInt("acyId");
                            LogoId = acyId;
                            Message msg = new Message();
                            msg.obj = 1;
                            msg.what = 1;
                            myHandler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("logo上传失败", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("logo上传结束");

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {
                double a = Double.valueOf(total);
                double b = Double.valueOf(current);
                double c = b / a;
                DecimalFormat df = new DecimalFormat("#");
                //百分比
                final int p = Integer.valueOf(df.format(c * 100));
                LogUtil.e("logo", p + "进度");
                if (pListener != null) {
                    pListener.onPChanged(0, p);
                }
            }
        });

        //循环上传图片文件,最后一位为 viewtype==2的视图
        for (int ii = 0; ii < list.size() - 1; ii++) {

            final int finalIi = ii;
            http.uploadFile(null, MainUrl.uploadFileUrl, new File(list.get(finalIi).getImageBean().getUrl()), null, new XProgressCallback() {
                @Override
                public void onSuccess(String resultStr) {
                    LogUtil.e("列表上传-onSuccess", resultStr.toString());
                    if (!StringUtil.isBlank(resultStr)) {
                        try {
                            JSONObject obj = new JSONObject(resultStr);
                            boolean result = obj.getBoolean("result");
                            //上传成功
                            if (result) {
                                String resultData = obj.getString("resultData");
                                JSONArray info = new JSONArray(resultData);
                                //上传成功后的图片 id
                                int acyId = info.getJSONObject(0).getInt("acyId");
                                galleryList.add(acyId);
                                Message msg = new Message();
                                msg.obj = 1;
                                msg.what = 1;
                                myHandler.sendMessage(msg);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("列表上传-error", ex.toString());

                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("上传结束");
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {

                }

                @Override
                public void onLoading(long total, long current) {

                    double a = Double.valueOf(total);
                    double b = Double.valueOf(current);
                    double c = b / a;
                    DecimalFormat df = new DecimalFormat("#");
                    //百分比
                    final int p = Integer.valueOf(df.format(c * 100));
                    LogUtil.e("第" + finalIi + "个", p + "进度");
                    list.get(finalIi).getImageBean().setCurrentP(p);
                    dia.getAdapter().notifyItemChanged(finalIi);

                }
            });

        }


    }


    public interface PChangeListener {
        void onPChanged(int position, int percent);
    }

    public void setOnPChangeListener(PChangeListener listener) {
        this.pListener = listener;
    }


    class MyThread extends Thread {

        boolean flag = true;
        boolean isOk = false;
        //10分钟后自动结束
        long time = 10 * 1000 * 60;

        public MyThread() {

        }

        public boolean isOk() {
            return isOk;
        }

        public void setOk(boolean ok) {
            isOk = ok;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    time = time - 100;

                    if (time <= 0l) {
                        flag = false;
                    }

                    LogUtil.e("扫描了一次");
                    MyThread.sleep(100);

                    if (isOk) {
                        LogUtil.e("扫描结束");
                        flag = false;
                        submit();
                        interrupt();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
