package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sctjsj.basemodule.base.ui.widget.dialog.PopListDialog;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.AdvertBean;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectData;

import java.util.List;

/**
 * Created by lifuy on 2017/5/18.
 */

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.AdvertHolder> {

    private Context context;
    private List<AdvertBean> data;
    private LayoutInflater inflater;

    public static final int TYPE_1 = 1;//预览图片
    public static final int TYPE_2 = 2;//添加的按钮

    public AdvertAdapter(Context context, List<AdvertBean> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }


    @Override
    public AdvertAdapter.AdvertHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case TYPE_1:
                view=inflater.inflate(R.layout.item_add_project_lay_1,null);
                break;
            case TYPE_2:
                view=inflater.inflate(R.layout.item_add_project_lay_2,null);
                break;
        }


        return new AdvertAdapter.AdvertHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(AdvertAdapter.AdvertHolder holder, int position) {
        int type=getItemViewType(position);

        switch (type){
            case TYPE_1:

                break;
            case TYPE_2:
                holder.mCVAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        data.add(0,new ProjectData(1));
//                        notifyItemInserted(0);

                        PopListDialog dialog=new PopListDialog(context);
                        dialog.setPopListCallback(new PopListDialog.PopListCallback() {
                            @Override
                            public void callCamera() {

                            }

                            @Override
                            public void callGallery() {

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

    class AdvertHolder extends RecyclerView.ViewHolder{
        int viewType;
        CardView mCVAdd;
        public AdvertHolder(View itemView,int viewType) {
            super(itemView);

            switch (viewType){
                case TYPE_1:


                    break;
                case TYPE_2:
                    mCVAdd= (CardView) itemView.findViewById(R.id.lay_2_add_img);

                    break;
            }


        }
    }
}
