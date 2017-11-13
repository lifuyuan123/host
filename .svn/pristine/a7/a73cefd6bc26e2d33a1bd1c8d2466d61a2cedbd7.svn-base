package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.mayk.wowallethost.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 反馈界面
 */

@Route(path = "/main/act/SettingFeedBackActivity")
public class SettingFeedBackActivity extends BaseAppcompatActivity {

    @BindView(R.id.setting_feedback_back_rl)
    RelativeLayout settingFeedbackBackRl;
    @BindView(R.id.setting_feedback_submit_txt)
    TextView settingFeedbackSubmitTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_setting_feed_back;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.setting_feedback_back_rl, R.id.setting_feedback_submit_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_feedback_back_rl:
                finish();
                break;
            case R.id.setting_feedback_submit_txt:
                Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
