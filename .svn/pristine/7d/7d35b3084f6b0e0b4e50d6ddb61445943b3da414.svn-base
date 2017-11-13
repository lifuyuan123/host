package com.sctjsj.basemodule.base.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sctjsj.basemodule.R;


/**
 * Created by liuha on 2017/4/7.
 */

public class CommonInputDialog extends Dialog{
    private LayoutInflater mLayoutInflater;
    private View mView;
    private TextView mTVTitle;
    private EditText mETInput;
    private Button mBtnCancel,mBtnConfirm;

    private CancelClickListener cancelClickListener;
    private ConfirmClickListener confirmClickListener;

    public CommonInputDialog(Context context, int style){
        super(context, style);
        init(context);
    }

    public CommonInputDialog(Context context){
        super(context, R.style.common_dialog);

        init(context);
    }

    //初始化
    private void init(Context context) {
        finView(context);
        myMeaSure(context);
        setListener();
    }
    //设置监听
    private void setListener() {

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cancelClickListener !=null){
                    cancelClickListener.clickCancel();
                }
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(confirmClickListener !=null){
                    confirmClickListener.clickConfirm(mETInput.getText().toString());
                }
            }
        });

    }

    //设置宽高
    private void myMeaSure(Context context) {
        mView.setBackgroundResource(R.drawable.dialog_back);
        setContentView(mView);
        Window window =getWindow();
        WindowManager.LayoutParams params =window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        params.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        window.setAttributes(params);

    }

    public void setInputType(int type){
        mETInput.setInputType(type);
    }



    //查找控件
    private void finView(Context mContext) {
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView=mLayoutInflater.inflate(R.layout.common_input_dialog_layout,null);
        mTVTitle = (TextView) mView.findViewById(R.id.MyDialog_Title);
        mETInput= (EditText) mView.findViewById(R.id.input_dialog_et);

        mBtnCancel= (Button) mView.findViewById(R.id.btn_cancel);
        mBtnConfirm= (Button) mView.findViewById(R.id.btn_confirm);
    }



    //创建按钮的监听的回调接口
    public interface CancelClickListener {
        public  void clickCancel();
    }
    public interface ConfirmClickListener {
        public  void clickConfirm(String inputStr);
    }

    /**
     * 点击取消按钮监听
     * @param cancelStr
     * @param
     */
    public void setCancelClickListener(String cancelStr,CancelClickListener cancelClickListenerListener){
        if(cancelStr!=null && !cancelStr.isEmpty() ){
            mBtnCancel.setText(cancelStr);
        }
        this.cancelClickListener=cancelClickListenerListener;

    }

    /**
     * 点击确认按钮监听
     * @param confirmStr
     * @param
     */
    public void setConfirmClickListener(String confirmStr,ConfirmClickListener confirmClickListener){
        if(confirmStr!=null && !confirmStr.isEmpty() ){
            mBtnConfirm.setText(confirmStr);
        }
        this.confirmClickListener = confirmClickListener;
    }

    public void setTitle(String title){
        if(title!=null && !title.isEmpty()){
            mTVTitle.setText(title);
        }
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }
}
