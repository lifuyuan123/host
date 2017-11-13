package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.MessageBean;
import com.squareup.picasso.MemoryPolicy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/15.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public MessageAdapter(List<MessageBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=inflater.inflate(R.layout.message_item, null);

        return new MessageViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {
        final MessageBean bean=data.get(position);
        //设置flag（已读，未读）
        if(bean.getStatus()==1){
            holder.messagItemFlagImg.setVisibility(View.VISIBLE);
        }else if(bean.getStatus()==2){
            holder.messagItemFlagImg.setVisibility(View.INVISIBLE);
        }


        holder.messagItemBodyTxt.setText(bean.getContent());
        holder.messagItemDateTxt.setText(bean.getInsert_time());
        if (bean.getType()==4){
            holder.messagItemTitleTxt.setText("平台推送");
//            PicassoUtil.getPicassoObject().
            Glide.with(mContext).
                    load(R.mipmap.host_logo)
//                    .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                    .error(R.mipmap.host_logo)
                    .into(holder.messagItemIconImg);
        }else {
            holder.messagItemTitleTxt.setText("交易提醒");
//            PicassoUtil.getPicassoObject().
            Glide.with(mContext).
                    load(bean.getUrl())
//                    .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                    .error(R.mipmap.icon_load_faild)
                    .into(holder.messagItemIconImg);
        }

        holder.messagItemDelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.onClick(position);
                }
            }
        });

//        PicassoUtil.getPicassoObject().
        Glide.with(mContext).
                load(bean.getUrl());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ARouter.getInstance().build("/main/act/MessageInFoActivity").withInt("key",bean.getId()).navigation();

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.messag_item_flagImg)
        ImageView messagItemFlagImg;
        @BindView(R.id.messag_item_iconImg)
        CircleImageView messagItemIconImg;
        @BindView(R.id.messag_item_titleTxt)
        TextView messagItemTitleTxt;
        @BindView(R.id.messag_item_dateTxt)
        TextView messagItemDateTxt;
        @BindView(R.id.messag_item_bodyTxt)
        TextView messagItemBodyTxt;
        @BindView(R.id.messag_item_delTxt)
        Button messagItemDelTxt;
        @BindView(R.id.message_item_layout)
        LinearLayout layout;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBack{
        void onClick(int position);
    }
    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
