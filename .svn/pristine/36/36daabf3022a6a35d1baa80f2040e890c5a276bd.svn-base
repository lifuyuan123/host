package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.StorePhoto;

import java.io.File;
import java.util.List;

/**
 * Created by liuha on 2017/6/20.
 */

public class StorePhotoAdapter extends RecyclerView.Adapter<StorePhotoAdapter.StoreViewHolder> {
    private List<StorePhoto> data;
    private Context mContext;
    private LayoutInflater inflater;
    private int type=0;

    public StorePhotoAdapter(Context mContext, List<StorePhoto> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getType().equals("add")){
            type=1;
        }else {
            type=2;
        }
        return type;
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = null;
        switch (viewType){
            case 1:
                mView=inflater.inflate(R.layout.store_photo_head,null);
                break;
            case 2:
                mView=inflater.inflate(R.layout.item_store_gallery,null);
                break;
        }
        return new StoreViewHolder(mView,viewType);

    }

    @Override
    public void onBindViewHolder(final StoreViewHolder holder, final int position) {
        int type=getItemViewType(position);
        final StorePhoto photo= data.get(position);
        switch (type){
            case 1:
//                holder.del.setVisibility(View.GONE);
//                Drawable drawable=mContext.getResources().getDrawable(R.mipmap.icon_add_img);
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=callBack&&photo.getType().equals("add")){
                            callBack.addImg();
                        }
                    }
                });
                break;
            case 2:
                if(photo.getType().equals("net")){
                    holder.del.setVisibility(View.VISIBLE);
//                    PicassoUtil.getPicassoObject().
                    Glide.with(mContext).
                            load(photo.getUrl())
//                            .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                            .into(holder.store_photo);
                }else {
                    holder.del.setVisibility(View.VISIBLE);
//                    PicassoUtil.getPicassoObject().
                    Glide.with(mContext).
                            load(new File(photo.getUrl()))
//                            .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                            .into(holder.store_photo);
                }

                holder.del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=callBack){
                            callBack.delPhoto(data.get(holder.getLayoutPosition()),holder.getLayoutPosition());
                        }
                    }
                });
                break;
        }

//        holder.del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(null!=callBack){
//                    callBack.delPhoto(data.get(holder.getLayoutPosition()),holder.getLayoutPosition());
//                }
//            }
//        });
//
//        holder.store_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(null!=callBack&&photo.getType().equals("add")){
//                    callBack.addImg();
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class  StoreViewHolder extends RecyclerView.ViewHolder{
        ImageView del;
        ImageView store_photo;
        CardView cardView;
        public StoreViewHolder(View itemView,int type) {
            super(itemView);
            switch (type){
                case 1:
                    cardView= (CardView) itemView.findViewById(R.id.lay_2_add_img);
                    break;
                case 2:
                    del= (ImageView) itemView.findViewById(R.id.store_photo_del);
                    store_photo= (ImageView) itemView.findViewById(R.id.store_photo);
                    break;
            }

        }
    }


    public interface  StortAdapterCallBack {
        public void delPhoto(StorePhoto photo,int position);
        public void addImg();
    }

    public StortAdapterCallBack callBack;

    public void setCallBack(StortAdapterCallBack callBack){
        this.callBack=callBack;
    }


}
