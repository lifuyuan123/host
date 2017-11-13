package com.sctjsj.mayk.wowallethost.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sctjsj.mayk.wowallethost.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lifuy on 2017/5/16.
 */

public class OrderDialog extends Dialog {
    private CircleImageView imageView;
    private TextView name;
    private TextView number;
    private LinearLayout close;
    private OnclickListener listener;


    private String names,numbers;
    private Bitmap bitmap;

    public void setNames(String names) {
        this.names = names;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.dialog);
          //按空白处不能取消动画
         setCanceledOnTouchOutside(false);
        initView();
        initdata();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.Onclick();
                }
            }
        });
    }

     //设置参数
    private void initdata() {
        if(names!=null){
           name.setText(names);
        }
        if(numbers!=null){
            number.setText(numbers);
        }
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
        }
    }

    //找到控件
    private void initView() {
        name= (TextView) findViewById(R.id.tv_name);
        number= (TextView) findViewById(R.id.tv_number);
        imageView= (CircleImageView) findViewById(R.id.iv_icon);
        close= (LinearLayout) findViewById(R.id.lin_close);

    }

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }



    public OrderDialog(@NonNull Context context) {
        super(context,R.style.MyDialog);
    }


    public interface OnclickListener{
        void  Onclick();
    }
}
