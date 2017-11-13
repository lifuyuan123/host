package com.sctjsj.mayk.wowallethost.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.mayk.wowallethost.R;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ChooseSexActivity")
public class ChooseSexActivity extends BaseAppcompatActivity {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    String sex = "";
    @BindView(R.id.radbt_boy)
    RadioButton radbtBoy;
    @BindView(R.id.radbt_girl)
    RadioButton radbtGirl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        radbtBoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sex="男";
                }
            }
        });
        radbtGirl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sex="女";
                }
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_sex;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.addnewbeautician_back_rl, R.id.addnewbeautician_save_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addnewbeautician_back_rl:
                finish();
                break;
            case R.id.addnewbeautician_save_txt:
                save();
                break;
        }
    }

    //保存
    private void save() {
        Intent intent = new Intent();
        if(!StringUtil.isBlank(sex)){
            intent.putExtra("key", sex);
            setResult(101, intent);
            finish();
        }else {
            Toast.makeText(this, "请选择后再保存。", Toast.LENGTH_SHORT).show();
        }

    }
}
