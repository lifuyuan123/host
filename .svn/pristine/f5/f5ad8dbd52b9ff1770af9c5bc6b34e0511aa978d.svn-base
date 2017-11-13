package com.sctjsj.mayk.wowallethost.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.util.permission.EasyPermissionsEx;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;

import io.github.xudaojie.qrcodelib.CaptureActivity;

//扫码
@Route(path = "/main/act/QRCode")
public class QRCodeActivity extends CaptureActivity {
    private HttpServiceImpl http;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        if(EasyPermissionsEx.hasPermissions(this, Manifest.permission.CAMERA)){
            ARouter.getInstance().build("/main/act/QRCode").navigation();
        }else {
            EasyPermissionsEx.requestPermissions(this,"扫描二维码需要授予相机权限",1,Manifest.permission.CAMERA);
        }

        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restartPreview();
    }

    // 处理扫码结果
    @Override
    protected void handleResult(String resultString) {
        Log.e("result", resultString);
    }

    //
    private void showAlert(String str){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage(str);
        builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
