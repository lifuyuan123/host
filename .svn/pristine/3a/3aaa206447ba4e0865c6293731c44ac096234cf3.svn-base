package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jph.takephoto.model.CropOptions;
import com.sctjsj.basemodule.base.ui.widget.dialog.PopListDialog;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectImageData;
import com.sctjsj.mayk.wowallethost.ui.activity.AddNewProjectActivity;
import com.sctjsj.mayk.wowallethost.ui.activity.EditProjectActivity;

import java.io.File;
import java.util.List;

/**
 * Created by lifuy on 2017/6/27.
 */

public class EditProjectAdapter extends RecyclerView.Adapter<EditProjectAdapter.ProjectImageHolder>{
    private Context context;

    private List<ProjectImageData> data;
    private LayoutInflater inflater;

    public List<ProjectImageData> getData() {
        return data;
    }

    public static final int TYPE_1 = 1;//预览图片
    public static final int TYPE_2 = 2;//添加的按钮
    private boolean isInEdit;//是否正在编辑项目图片

    public EditProjectAdapter(Context context, List<ProjectImageData> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }


    @Override
    public EditProjectAdapter.ProjectImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case TYPE_1:
                view=inflater.inflate(R.layout.item_add_project_lay_1,null);
                break;
            case TYPE_2:
                view=inflater.inflate(R.layout.item_add_project_lay_2,null);
                break;
        }


        return new EditProjectAdapter.ProjectImageHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(final EditProjectAdapter.ProjectImageHolder holder, final int position) {
        int type=getItemViewType(position);

        switch (type){
            case TYPE_1:
                if(data.get(position).getImageBean().getType().equals("net")){
//                    PicassoUtil.getPicassoObject().
                    Glide.with(context).
                            load(data.get(position).getImageBean().getUrl()).
//                            resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                            into(holder.mIVPreview);
                }else {
//                    PicassoUtil.getPicassoObject().
                    Glide.with(context).
                            load(new File(data.get(position).getImageBean().getUrl())).
//                            resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                            into(holder.mIVPreview);
                }


                //判断是否显示删除角标
                if(data.get(position).isShowDelete()){
                    holder.mIVDelegeBadege.setVisibility(View.VISIBLE);
                }else {
                    holder.mIVDelegeBadege.setVisibility(View.GONE);
                }



                //点击删除选中的图片
                holder.mIVDelegeBadege.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callback!=null){
                            callback.callback(position);
                        }
                        data.remove(holder.getLayoutPosition());
                        notifyItemRemoved(holder.getLayoutPosition());
//                        dismissDeleteBadge();
                    }
                });

                //长按显示删除角标
                holder.mCVPicContainer.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showDeleteBadge();
                        return true;
                    }
                });



                break;
            case TYPE_2:
                //点击添加图片
                holder.mCVAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //隐藏
                        dismissDeleteBadge();
                        final PopListDialog dialog=new PopListDialog(context);
                        dialog.setPopListCallback(new PopListDialog.PopListCallback() {
                            @Override
                            public void callCamera() {

                                if(context instanceof EditProjectActivity){
                                    //裁剪参数
                                    CropOptions cropOptions=new CropOptions.Builder().
                                            setWithOwnCrop(false).create();
                                    ((EditProjectActivity)context).getTakePhoto().
                                            onPickFromCaptureWithCrop(  ((EditProjectActivity)context).getUri(),cropOptions);
                                }
                                dialog.dismiss();
                            }

                            @Override
                            public void callGallery() {

                                if(context instanceof EditProjectActivity){
                                    //裁剪参数
                                    CropOptions cropOptions1=new CropOptions.Builder()
                                            .setWithOwnCrop(false).create();

                                    ((EditProjectActivity)context).getTakePhoto().
                                            onPickFromGalleryWithCrop( ((EditProjectActivity)context).getUri(),cropOptions1);
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).getViewType()){
            case 1:
                return TYPE_1;

            case 2:
                return TYPE_2;

            default:
                return -1;
        }

    }


    class ProjectImageHolder extends RecyclerView.ViewHolder{
        int viewType;

        //type=2
        CardView mCVAdd;

        //type=1
        ImageView mIVDelegeBadege,mIVPreview;
        CardView mCVPicContainer;

        public ProjectImageHolder(View itemView, int viewType) {
            super(itemView);

            switch (viewType){
                case TYPE_1:
                    mIVDelegeBadege= (ImageView) itemView.findViewById(R.id.iv_delete_badge);
                    mCVPicContainer= (CardView) itemView.findViewById(R.id.cardview_pic_container);
                    mIVPreview= (ImageView) itemView.findViewById(R.id.iv_lay_1_preview);
                    break;
                case TYPE_2:
                    mCVAdd= (CardView) itemView.findViewById(R.id.lay_2_add_img);

                    break;
            }


        }
    }

    /**
     * 隐藏删除的角标
     */
    public void dismissDeleteBadge(){

        if(context instanceof EditProjectActivity){
            ((EditProjectActivity)context).getmTVOkEditProjectPic().setVisibility(View.GONE);
        }

        for (int i=0;i<data.size();i++){
            if(1==data.get(i).getViewType()){

                data.get(i).setShowDelete(false);
                notifyItemChanged(i);
            }
        }

    }

    /**
     * 显示删除图标
     */
    public void showDeleteBadge(){
        if(context instanceof EditProjectActivity){
            ((EditProjectActivity)context).getmTVOkEditProjectPic().setVisibility(View.VISIBLE);
        }
        for (int i=0;i<data.size();i++){
            if(1==data.get(i).getViewType()){

                data.get(i).setShowDelete(true);
                notifyItemChanged(i);
            }
        }

    }

    public interface Callback{
        void callback(int position);
    }
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
