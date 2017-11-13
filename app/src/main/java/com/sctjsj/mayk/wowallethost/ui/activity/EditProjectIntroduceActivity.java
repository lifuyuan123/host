package com.sctjsj.mayk.wowallethost.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonInputDialog;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.event.ProjectIntroduceSaveEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.richeditor.RichEditor;

@Route(path = "/main/act/edit_project_introduce")
public class EditProjectIntroduceActivity extends BaseAppcompatActivity {
    @Autowired(name = "html")
    public String html;

    @BindView(R.id.editor)
    RichEditor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        editor.setPadding(10, 10, 10, 10);
        editor.setPlaceholder("请编辑项目介绍...");

        editor= (RichEditor) findViewById(R.id.editor);
        editor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.e("edit-html-change",text+"");
            }
        });
        if(!StringUtil.isBlank(html)){
            editor.setHtml(html);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_edit_project_introduce;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.back)
    public void onBackClicked() {
        if(StringUtil.isBlank(editor.getHtml())){
            finish();
        }else {
            final CommonDialog dialog=new CommonDialog(this);
            dialog.setTitle("退出");
            dialog.setContent("您尚未保存编辑内容，直接退出？");
            dialog.setCancelClickListener("再看看", new CommonDialog.CancelClickListener() {
                @Override
                public void clickCancel() {
                    dialog.dismiss();
                }
            });
            dialog.setConfirmClickListener("直接退出", new CommonDialog.ConfirmClickListener() {
                @Override
                public void clickConfirm() {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
        }
    }

    @OnClick(R.id.save)
    public void onSaveClicked(){

        if(StringUtil.isBlank(editor.getHtml())){
            final CommonDialog dialog=new CommonDialog(this);
            dialog.setTitle("保存");
            dialog.setContent("当前暂无需要保存的内容");
            dialog.setCancelClickListener("取消", new CommonDialog.CancelClickListener() {
                @Override
                public void clickCancel() {
                    dialog.dismiss();
                }
            });
            dialog.setConfirmClickListener("直接退出", new CommonDialog.ConfirmClickListener() {
                @Override
                public void clickConfirm() {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
        } else {

            final CommonDialog dialog=new CommonDialog(this);
            dialog.setTitle("保存");
            dialog.setContent("确认保存编辑内容？");
            dialog.setCancelClickListener("再看看", new CommonDialog.CancelClickListener() {
                @Override
                public void clickCancel() {
                    dialog.dismiss();
                }
            });
            dialog.setConfirmClickListener("保存", new CommonDialog.ConfirmClickListener() {
                @Override
                public void clickConfirm() {
                    dialog.dismiss();
                    Log.e("发送",editor.getHtml());
                    EventBus.getDefault().post(new ProjectIntroduceSaveEvent(editor.getHtml()));
                    finish();
                }
            });
            dialog.show();
        }
    }

    @OnClick(R.id.undo)
    public void onUndoClicked() {
        editor.undo();
    }

    @OnClick(R.id.redo)
    public void onRedoClicked() {
        editor.redo();
    }

    @OnClick(R.id.bold)
    public void onBoldClicked() {
        editor.setBold();
    }

    @OnClick(R.id.italic)
    public void onItalicClicked() {
        editor.setItalic();
    }

    @OnClick(R.id.txt_color)
    public void onTxtColorClicked() {

        ColorPicker picker=new ColorPicker(this);

        picker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(String color) {

            }

            @Override
            public void onColorChosen(@ColorInt int color) {
                editor.setTextColor(color);
            }

        });
        picker.show();
    }

    @OnClick(R.id.txt_size)
    public void onTxtSizeClicked() {
        final CommonInputDialog dialog=new CommonInputDialog(this);
        dialog.setTitle("字体大小");
        dialog.setCancelable(false);
        dialog.setCancelClickListener("取消", new CommonInputDialog.CancelClickListener() {
            @Override
            public void clickCancel() {
                dialog.dismiss();
            }
        });
        dialog.setConfirmClickListener("确认", new CommonInputDialog.ConfirmClickListener() {
            @Override
            public void clickConfirm(String inputStr) {

                if(StringUtil.isBlank(inputStr)){
                    Toast.makeText(EditProjectIntroduceActivity.this, "请输入有效的字体大小", Toast.LENGTH_SHORT).show();
                } else {
                    if(StringUtil.isNumeric(inputStr)){
                        Toast.makeText(EditProjectIntroduceActivity.this, inputStr+"", Toast.LENGTH_SHORT).show();
                        editor.setFontSize(Integer.valueOf(inputStr));
                        dialog.dismiss();
                    }else {
                        Toast.makeText(EditProjectIntroduceActivity.this, inputStr+"为无效字体大小", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        dialog.show();
    }

    @OnClick(R.id.h1)
    public void onH1Clicked() {
        editor.setHeading(1);
    }

    @OnClick(R.id.h2)
    public void onH2Clicked() {
        editor.setHeading(2);

    }

    @OnClick(R.id.h3)
    public void onH3Clicked() {
        editor.setHeading(3);
    }

    @OnClick(R.id.h4)
    public void onH4Clicked() {
        editor.setHeading(4);
    }

    @OnClick(R.id.h5)
    public void onH5Clicked() {
        editor.setHeading(5);
    }

    @OnClick(R.id.h6)
    public void onH6Clicked() {
        editor.setHeading(6);
    }

    @OnClick(R.id.justify_center)
    public void onJustifyCenterClicked() {
        editor.setAlignCenter();
    }

    @OnClick(R.id.justify_left)
    public void onJustifyLeftClicked() {
        editor.setAlignLeft();
    }

    @OnClick(R.id.justify_right)
    public void onJustifyRightClicked() {
        editor.setAlignRight();
    }

    @OnClick(R.id.superscript)
    public void onSuperscriptClicked() {
        editor.setSuperscript();
    }

    @OnClick(R.id.subscript)
    public void onSubscriptClicked() {
        editor.setSubscript();
    }

    @OnClick(R.id.underline)
    public void onUnderlineClicked() {
        editor.setUnderline();
    }

    @OnClick(R.id.strike_through)
    public void onStrikeThroughClicked() {
        editor.setStrikeThrough();
    }

    @OnClick(R.id.indent)
    public void onIndentClicked() {
        editor.setIndent();
    }

    @OnClick(R.id.outdent)
    public void onOutdentClicked() {
        editor.setOutdent();
    }

    @OnClick(R.id.dot_list)
    public void onDotListClicked() {
        editor.setBullets();
    }

    @OnClick(R.id.num_list)
    public void onNumListClicked() {
        editor.setNumbers();
    }
}
