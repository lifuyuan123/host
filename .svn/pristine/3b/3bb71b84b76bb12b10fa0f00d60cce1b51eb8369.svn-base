package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.mayk.wowallethost.R;

import butterknife.OnClick;
//入驻成功
@Route(path = "/main/act/settled")
public class SettledActivity extends BaseAppcompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_settled;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.settled_lin_back, R.id.settled_tv_gonhome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settled_lin_back:
                finish();
                break;
            //去主页
            case R.id.settled_tv_gonhome:
                ARouter.getInstance().build("/main/act/indext").navigation();
                break;
        }
    }
}
