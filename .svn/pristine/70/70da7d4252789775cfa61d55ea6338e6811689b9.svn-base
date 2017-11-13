package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.mayk.wowallethost.R;

import butterknife.BindView;
import butterknife.OnClick;

//入驻
@Route(path = "/main/act/regist1")
public class Regist1Activity extends BaseAppcompatActivity {

    @BindView(R.id.regist1_rela_back)
    RelativeLayout regist1RelaBack;
    @BindView(R.id.regist1_edt_phone)
    EditText regist1EdtPhone;
    @BindView(R.id.regist1_edt_input_code)
    EditText regist1EdtInputCode;
    @BindView(R.id.regist1_Img_code)
    RelativeLayout regist1ImgCode;
    @BindView(R.id.regist1_edt_phoen_code)
    EditText regist1EdtPhoenCode;
    @BindView(R.id.regist1_rela_getphoencode)
    RelativeLayout regist1RelaGetphoencode;
    @BindView(R.id.act_register1_btn_go_next)
    Button actRegister1BtnGoNext;
    @BindView(R.id.act_register1_tv_to_login)
    TextView actRegister1TvToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_regist1;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.regist1_rela_back, R.id.regist1_Img_code, R.id.act_register1_btn_go_next, R.id.act_register1_tv_to_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.regist1_rela_back:
                finish();
                break;
            case R.id.regist1_Img_code:
                break;
            case R.id.act_register1_btn_go_next:
                break;
            case R.id.act_register1_tv_to_login:
                break;
        }
    }
}
