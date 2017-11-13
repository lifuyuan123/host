package com.sctjsj.mayk.wowallethost.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sctjsj.mayk.wowallethost.R;

import butterknife.BindView;

/**
 * Created by lifuy on 2017/5/17.
 */

public class StoredeleteDialog extends Dialog {
    private String s;
    private TextView textView;
    private  TextView mTextView;
    private Context mContext;
    private LayoutInflater inflater;


    public StoredeleteDialog( Context context) {
        super(context, R.style.MyDialog);
        this.mContext=context;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        View mView=inflater.inflate(R.layout.store_delete_dialog,null);
        setContentView(mView);
        textView= (TextView) mView.findViewById(R.id.tv_number);
        mTextView= (TextView)mView.findViewById(R.id.tv_number2);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //关闭dialog
        mView.findViewById(R.id.lin_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //是否删除的确认按钮
        mView.findViewById(R.id.item_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deletelisten!=null){
                    deletelisten.Onclick();
                }
            }
        });
    }


    private OnclickCloseListener closelistener;
    private OnclickDeleteListener deletelisten;

    public void setDeletelisten(OnclickDeleteListener deletelisten) {
        this.deletelisten = deletelisten;
    }

    public void setCloseListener(OnclickCloseListener listener) {
        this.closelistener = listener;
    }


    public interface OnclickCloseListener {
        void Onclick();
    }
    public interface OnclickDeleteListener {
        void Onclick();
    }

    public void setText(String data){
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(data);
    }
    public void setS(String str){
        textView.setText(str);
    }

}

