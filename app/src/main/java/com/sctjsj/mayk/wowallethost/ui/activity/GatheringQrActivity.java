package com.sctjsj.mayk.wowallethost.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.util.QRCodeUtil;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;


import butterknife.BindView;
import butterknife.OnClick;

/***
 * 展示店家收款二维码页面
 */
@Route(path = "/main/act/GatheringQrActivity")
public class GatheringQrActivity extends BaseAppcompatActivity {

    @BindView(R.id.gatheringQr_back_rl)
    RelativeLayout gatheringQrBackRl;
    @BindView(R.id.gatheringQr_Img)
    ImageView gatheringQrImg;
    @BindView(R.id.gatheringQr_save_rl)
    RelativeLayout gatheringQrSaveRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        int invitationCode = UserAuthUtil.getCurrentUser().getStoreBean().getInvitationCode();
        String s="http://woqianbao/share/storeId_"+UserAuthUtil.getStoreId()+".htm?iid="+invitationCode;
        Bitmap bmp= QRCodeUtil.createQRCode(UserAuthUtil.getCurrentUser().getStoreBean().getQrCode(),
                DpUtils.dpToPx(this,160),DpUtils.
                        dpToPx(GatheringQrActivity.this,160),null);
        if(bmp==null){
            Toast.makeText(this, "收款二维码初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
        gatheringQrImg.setImageBitmap(bmp);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_gathering_qr;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.gatheringQr_back_rl, R.id.gatheringQr_save_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gatheringQr_back_rl:
                finish();
                break;
            case R.id.gatheringQr_save_rl:
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
