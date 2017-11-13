package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.BeauticanBean;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreEditBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/5/17.
 */

public class BeauticanAdapter extends RecyclerView.Adapter<BeauticanAdapter.BeauticanHolder> {
    private boolean flag = false;
    private Context mContext;
    private List<BeauticanBean> data;
    private LayoutInflater inflater;

    public BeauticanAdapter(Context mContext, List<BeauticanBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public BeauticanHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = inflater.inflate(R.layout.beautican_manage_item, null);
        BeauticanHolder holder=new BeauticanHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(BeauticanHolder holder, final int position) {
        BeauticanBean beauticanBean=data.get(position);
        holder.beauticanManageCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setFlag(isChecked);
            }
        });

//        PicassoUtil.getPicassoObject().
        Glide.with(mContext).
                load(beauticanBean.getPhoto()).
//                resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80)).
                error(R.mipmap.icon_load_faild).into(holder.beauticanManageIcon);
        holder.beauticanManageNameTxt.setText(beauticanBean.getName());

        holder.beauticanManageCb.setChecked(data.get(position).isFlag());
        if(flag){
            holder.beauticanManageCb.setVisibility(View.VISIBLE);
        }else {
            holder.beauticanManageCb.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class BeauticanHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.beautican_manage_cb)
        CheckBox beauticanManageCb;
        @BindView(R.id.beautican_manage_icon)
        ImageView beauticanManageIcon;
        @BindView(R.id.beautican_manage_nameTxt)
        TextView beauticanManageNameTxt;
        @BindView(R.id.beautican_manage_ll)
        LinearLayout beauticanManageLl;
        public BeauticanHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public boolean setVisible(){
        if(flag){
            flag=false;
        }else {
            flag=true;
        }
        notifyDataSetChanged();
        return flag;
    }

    public void changeData(List<BeauticanBean> lists){
        data.clear();
        data.addAll(lists);
        notifyDataSetChanged();
    }

    public List<BeauticanBean> getData() {
        return data;
    }
}