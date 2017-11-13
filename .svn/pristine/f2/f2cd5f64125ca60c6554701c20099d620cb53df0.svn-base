package com.sctjsj.mayk.wowallethost.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bumptech.glide.Glide;
import com.github.ornolfr.ratingview.RatingView;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.EvaluateBean;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreReplyBean;
import com.sctjsj.mayk.wowallethost.ui.activity.EvaluateManageActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/18.
 */

public class EvaluateAdapter extends DelegateAdapter.Adapter<EvaluateAdapter.EvaluateHolder>{
    /***
     * 评价页面对应的三种布局
     * @return
     */
    public  static  final int TYPE_1=1;//最上面的大坨坨
    public  static  final int TYPE_2=2;//筛选评价
    public  static  final int TYPE_3=3;//用户评价

    private Context mContext;
    private LayoutInflater inflater;
    // private HashMap<String,List<Object>> data;
    private List<HashMap<String,Object>> data;
    private RecyclerView.LayoutParams layoutParams;
    private LayoutHelper layoutHelper;
    private int count = 0;
    private int type=0;
    private EvaLuateCallBack callback;
    private HttpServiceImpl servre;
    private Listener listener;

    //构造函数(传入每个的数据列表 & 展示的Item数量)
    public EvaluateAdapter(Context context, LayoutHelper layoutHelper, int count,   List<HashMap<String,Object>> data, int type) {
        //宽度占满，高度随意
        this(context, layoutHelper, count, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), data,type);
    }


    public EvaluateAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull RecyclerView.LayoutParams layoutParams,  List<HashMap<String,Object>> data, int type){
        this.mContext=context;
        this.layoutHelper=layoutHelper;
        this.count=count;
        this.layoutParams = layoutParams;
        this.data = data;
        this.type=type;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.servre= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        this.listener=new Listener();
    }




    public int getCount() {
        return data == null ? 0 : data.size();
    }

    public int getType() {
        return type;
    }

    public RecyclerView.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public   List<HashMap<String,Object>> getData() {
        return data;
    }



    @Override
    public LayoutHelper onCreateLayoutHelper() {

        return layoutHelper;
    }

    @Override
    public int getItemViewType(int position) {
        switch (type){
            case 1:
                return  TYPE_1;
            case 2:
                return  TYPE_2;
            case 3:
                return  TYPE_3;
            default:
                return -1;
        }
    }

    @Override
    public EvaluateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=null;
        switch (viewType){
            case TYPE_1:
                mView=inflater.inflate(R.layout.evaluate_lay_1,parent,false);
                break;
            case TYPE_2:
                mView=inflater.inflate(R.layout.evaluate_lay_2,parent,false);
                break;
            case TYPE_3:
                mView=inflater.inflate(R.layout.evaluate_lay_3,null);
                break;
        }

        return new EvaluateHolder(mView,viewType);
    }

    @Override
    public void onBindViewHolder(final EvaluateHolder holder, int position) {
        int types=getItemViewType(position);
        switch (types){
            case TYPE_1:
                HashMap<String,Object> score=data.get(0);
                if(score.containsKey("score")){
                    double score1 = (double) score.get("score");
                    holder.evaluate_lay1_fraction.setText(score1+"分");
                    float s= (float) score1;
                    holder.evaluate_lay1_ratingView.setRating(s);
                }
                break;
            case TYPE_2:
                HashMap<String,Object> Choose=data.get(0);
                holder.evaluate_lay2_count.setText((Choose.get("all")==null?0+"":Choose.get("all")+"")+"人评价");
                holder.allcomment_rb.setText("全部"+(Choose.get("all")==null?0+"":Choose.get("all")+""));
                holder.NO_comment_rb.setText("未回复"+(Choose.get("back")==null?0+"":Choose.get("back")+""));
                holder.bad_comment_rb.setText("差评"+ (Choose.get("bad")==null?0+"":Choose.get("bad")));
                holder.evaluate_lay2_rg.setOnCheckedChangeListener(listener);
                break;
            case TYPE_3:
                final EvaluateBean evaluateBean= (EvaluateBean) data.get(position).get("comment");
                Log.e("type_3",""+data.size());
//                PicassoUtil.getPicassoObject().
                Glide.with(mContext).
                        load(evaluateBean.getReviewer().getUrl())
//                        .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                        .error(R.mipmap.icon_load_faild).into(holder.lay_comment_icon);
                holder.lay_comment_name.setText(evaluateBean.getReviewer().getUsername());
                holder.lay_comment_date.setText(evaluateBean.getInsertTime());
                holder.lay_comment_grade.setRating(evaluateBean.getScore());
                holder.lay_comment_cont.setText(evaluateBean.getContent());
                if(null!=evaluateBean.getCommentPhoto()){
                    holder.lay3_Imglay.setVisibility(View.VISIBLE);
                    if(evaluateBean.getCommentPhoto().size()>=1){
//                        PicassoUtil.getPicassoObject().
                        Glide.with(mContext).
                                load(evaluateBean.getCommentPhoto().get(0))
//                                .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                                .error(R.mipmap.icon_load_faild).into(holder.lay_comment_one);
                        holder.lay_comment_one.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.
                                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                                Bundle b=new Bundle();

                                b.putCharSequenceArrayList("list",(ArrayList) evaluateBean.getCommentPhoto());
                                ARouter.getInstance().build("/main/act/gallery_scan").
                                        withInt("index",0).
                                        withBundle("data",b).
                                        withOptionsCompat(compat).navigation();
                            }
                        });
                    }

                    if(evaluateBean.getCommentPhoto().size()>=2){
//                        PicassoUtil.getPicassoObject().
                        Glide.with(mContext).
                                load(evaluateBean.getCommentPhoto().get(1)) .
//                                resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80)).
                                error(R.mipmap.icon_load_faild).into(holder.lay_comment_tow);
                        holder.lay_comment_tow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.
                                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                                Bundle b=new Bundle();

                                b.putCharSequenceArrayList("list",(ArrayList) evaluateBean.getCommentPhoto());
                                ARouter.getInstance().build("/main/act/gallery_scan").
                                        withInt("index",1).
                                        withBundle("data",b).
                                        withOptionsCompat(compat).navigation();
                            }
                        });
                    }

                    if(evaluateBean.getCommentPhoto().size()>=3){
//                        PicassoUtil.getPicassoObject().
                        Glide.with(mContext).
                                load(evaluateBean.getCommentPhoto().get(2)) .
//                                resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80)).
                                error(R.mipmap.icon_load_faild).into(holder.lay_comment_three);
                        holder.store_rv_photo.setVisibility(View.VISIBLE);
                        holder.store_tv_photo.setText(evaluateBean.getCommentPhoto().size()+"张");
                        holder.lay_comment_three.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.
                                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                                Bundle b=new Bundle();

                                b.putCharSequenceArrayList("list",(ArrayList) evaluateBean.getCommentPhoto());
                                ARouter.getInstance().build("/main/act/gallery_scan").
                                        withInt("index",2).
                                        withBundle("data",b).
                                        withOptionsCompat(compat).navigation();
                            }
                        });
                    }else {
                        holder.store_rv_photo.setVisibility(View.GONE);
                    }
                }else {
                    holder.lay3_Imglay.setVisibility(View.GONE);
                }

                //有无回复
                if(evaluateBean.isboosEva()){
                    //有店家的评价的情况
                    holder.evaluate_bossSpeak_ll.setVisibility(View.VISIBLE);
                    holder. evaluate_reply_ll.setVisibility(View.GONE);
                    holder.evaluate_replyedt_ll.setVisibility(View.GONE);
                    StoreReplyBean store=evaluateBean.getStoreReplyBean();
                    holder.lay_comment_merchant_data.setText(store.getInsertTime());
                    holder.lay_comment_merchant_cont.setText(store.getContent());
                }else {
                    //店家没有评论的情况
                    holder.evaluate_bossSpeak_ll.setVisibility(View.GONE);
                    holder.evaluate_reply_ll.setVisibility(View.VISIBLE);
                }

                holder.evaluate_reply_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //店家点击回复按钮
                        holder.evaluate_replyedt_ll.setVisibility(View.VISIBLE);
                    }
                });


                holder.evaluate_replyedt_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(holder.evaluate_lay3_edt.getText())){
                            Submit(evaluateBean.getId(),holder.evaluate_lay3_edt.getText().toString());
                        }else {
                            Toast.makeText(mContext, "回复的内容不可以为空！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {

        return data == null ? 0 : data.size();

    }

    class EvaluateHolder extends RecyclerView.ViewHolder{
        //type1 页面的控件
        RatingView evaluate_lay1_ratingView;//打分控件
        TextView evaluate_lay1_fraction;//分数
        //type2页面的控件
        LinearLayout evaluate_lay2_all_ll;
        TextView evaluate_lay2_count;
        RadioGroup evaluate_lay2_rg;
        RadioButton allcomment_rb;
        RadioButton NO_comment_rb;
        RadioButton bad_comment_rb;



        //type3页面控件
        LinearLayout evaluate_reply_ll; //回复按钮布局
        Button evaluate_reply_btn;//回复按钮
        RelativeLayout evaluate_replyedt_ll;//回复输入的布局
        Button evaluate_replyedt_img;//提交
        LinearLayout evaluate_bossSpeak_ll;//显示店家回复的布局
        LinearLayout lay3_Imglay;//图片

        CircleImageView lay_comment_icon;//用户头像
        TextView lay_comment_name;//用户name
        TextView lay_comment_date;//评论时间
        RatingView lay_comment_grade;//分数
        TextView lay_comment_cont;//用户评论内容
        ImageView lay_comment_one;
        ImageView lay_comment_tow;
        ImageView lay_comment_three;
        EditText evaluate_lay3_edt;//回复的输入框
        TextView lay_comment_merchant_data;//回复时间
        TextView lay_comment_merchant_cont;//回复的内容
        TextView store_tv_photo;
        RelativeLayout store_rv_photo;



        public EvaluateHolder(View itemView,int viewType) {
            super(itemView);
            switch (viewType){
                case TYPE_1:
                    evaluate_lay1_ratingView= (RatingView) itemView.findViewById(R.id.evaluate_lay1_ratingView);
                    evaluate_lay1_fraction= (TextView) itemView.findViewById(R.id.evaluate_lay1_fraction);
                    break;
                case TYPE_2:
                    evaluate_lay2_all_ll= (LinearLayout) itemView.findViewById(R.id.evaluate_lay2_all_ll);
                    evaluate_lay2_count= (TextView) itemView.findViewById(R.id.evaluate_lay2_count);
                    evaluate_lay2_rg= (RadioGroup) itemView.findViewById(R.id.evaluate_lay2_rg);
                    allcomment_rb= (RadioButton) itemView.findViewById(R.id.allcomment_rb);
                    NO_comment_rb= (RadioButton) itemView.findViewById(R.id.NO_comment_rb);
                    bad_comment_rb= (RadioButton) itemView.findViewById(R.id.bad_comment_rb);
                    break;
                case TYPE_3:
                    evaluate_reply_ll= (LinearLayout) itemView.findViewById(R.id.evaluate_reply_ll);
                    evaluate_reply_btn= (Button) itemView.findViewById(R.id.evaluate_reply_btn);
                    evaluate_replyedt_ll= (RelativeLayout) itemView.findViewById(R.id.evaluate_replyedt_ll);
                    evaluate_replyedt_img= (Button) itemView.findViewById(R.id.evaluate_replyedt_img);
                    evaluate_bossSpeak_ll= (LinearLayout) itemView.findViewById(R.id.evaluate_bossSpeak_ll);
                    lay_comment_icon= (CircleImageView) itemView.findViewById(R.id.lay_comment_icon);
                    lay_comment_name= (TextView) itemView.findViewById(R.id.lay_comment_name);
                    lay_comment_date= (TextView) itemView.findViewById(R.id.lay_comment_date);
                    lay_comment_grade= (RatingView) itemView.findViewById(R.id.lay_comment_grade);
                    lay_comment_cont= (TextView) itemView.findViewById(R.id.lay_comment_cont);
                    lay_comment_one= (ImageView) itemView.findViewById(R.id.lay_comment_one);
                    lay_comment_tow= (ImageView) itemView.findViewById(R.id.lay_comment_tow);
                    lay_comment_three= (ImageView) itemView.findViewById(R.id.lay_comment_three);
                    evaluate_lay3_edt= (EditText) itemView.findViewById(R.id.evaluate_lay3_edt);
                    lay_comment_merchant_data= (TextView) itemView.findViewById(R.id.lay_comment_merchant_data);
                    lay_comment_merchant_cont= (TextView) itemView.findViewById(R.id.lay_comment_merchant_cont);
                    lay3_Imglay= (LinearLayout) itemView.findViewById(R.id.lay3_Imglay);
                    store_tv_photo= (TextView) itemView.findViewById(R.id.store_tv_photo);
                    store_rv_photo= (RelativeLayout) itemView.findViewById(R.id.store_rv_photo);
                    break;
            }
        }
    }



    private class Listener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId){
                case R.id.allcomment_rb:
                    if(null!=callback){
                        callback.allComment();
                    }
                    break;
                case R.id.NO_comment_rb:
                    if(null!=callback){
                        callback.noComment();
                    }
                    break;
                case R.id.bad_comment_rb:
                    if(null!=callback){
                        callback.badComment();
                    }
                    break;
            }

        }
    }

    //提交评论
    private void Submit(int id,String cont){
        HashMap<String,String> body=new HashMap<>();
        body.put("commentId",id+"");
        body.put("content",cont);

        servre.doCommonPost(null, MainUrl.SubmitCommit, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("Submit",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                      /*  if(obj.getBoolean("result")){*/
                        Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                        if(null!=callback){
                            callback.submitSucceed();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    public interface  EvaLuateCallBack{
        public void allComment();
        public void noComment();
        public void badComment();
        public void submitSucceed();

    }

    public void setEvaLuateCallBack(EvaLuateCallBack callback){
        this.callback=callback;
    }

}
