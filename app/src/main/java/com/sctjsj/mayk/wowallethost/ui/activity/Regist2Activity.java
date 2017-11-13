package com.sctjsj.mayk.wowallethost.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.mayk.wowallethost.R;

//注册
@Route(path = "/main/act/regist2")
public class Regist2Activity extends BaseAppcompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_regist2;
    }

    @Override
    public void reloadData() {

    }
}
