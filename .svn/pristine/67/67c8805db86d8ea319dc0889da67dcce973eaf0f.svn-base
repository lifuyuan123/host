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
import com.suke.widget.SwitchButton;

import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreEditBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/5/16.
 */

public class StoreEditAdapter extends RecyclerView.Adapter<StoreEditAdapter.StoreEidtHolder> {
    public List<StoreEditBean> getList() {
        return list;
    }

    public void setList(List<StoreEditBean> list) {
        this.list = list;
    }

    private List<StoreEditBean> list;
    private Context context;
    private Callback callback;
    private boolean isshow = false;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public StoreEditAdapter(List<StoreEditBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public StoreEidtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_store_item, null);
        StoreEidtHolder holder = new StoreEidtHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final StoreEidtHolder holder, final int position) {
        final StoreEditBean storeEditBean = list.get(position);
            //item点击事件
            holder.itemLin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback!=null){
                        callback.Onclick(position);
                    }
                }
            });

          holder.itemTogbt.setEnableEffect(false);
          holder.itemTogbt.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                  if(isChecked){
                      holder.itemShowClose.setText("已显示");
                      storeEditBean.setShow("已显示");
                      if(linstener!=null){
                          linstener.openProject(position);
                      }
                  }else {
                      holder.itemShowClose.setText("已关闭");
                      storeEditBean.setShow("已关闭");
                      if(linstener!=null){
                          linstener.closeProject(position);
                      }
                  }
                  storeEditBean.setIscheck(isChecked);
              }
          });
            holder.itemTogbt.setChecked(storeEditBean.ischeck());

        if(storeEditBean.getState()==1){
            holder.itemShowClose.setText("已关闭");
        }else if(storeEditBean.getState()==2){
            holder.itemShowClose.setText("已显示");
        }


        if(storeEditBean.getShow()!=null) {
            holder.itemShowClose.setText(storeEditBean.getShow());
        }

            //checkbox监听
            holder.itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    storeEditBean.setCheckbox(isChecked);
                }
            });

            //设置上checkbox状态
            holder.itemCheck.setChecked(storeEditBean.isCheckbox());

            //是否显示checkbox
            if (isshow) {
                holder.itemCheck.setVisibility(View.VISIBLE);
            } else {
                holder.itemCheck.setVisibility(View.GONE);
            }


            if(storeEditBean.getState()==2){
                holder.itemTogbt.setChecked(true);
            }else {
                holder.itemTogbt.setChecked(false);
            }
            holder.itemType.setText("["+storeEditBean.getGoodsType()+"]");
            holder.itemContent.setText(storeEditBean.getGoodsName());
//            PicassoUtil.getPicassoObject().
        Glide.with(context).
                    load(storeEditBean.getIconurl()).
//                resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                error(R.mipmap.icon_load_faild).into(holder.itemIcon);

    }

    class StoreEidtHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_icon)
        ImageView itemIcon;
        @BindView(R.id.item_type)
        TextView itemType;
        @BindView(R.id.item_content)
        TextView itemContent;
        @BindView(R.id.item_togbt)
        SwitchButton itemTogbt;
        @BindView(R.id.item_show_close)
        TextView itemShowClose;
        @BindView(R.id.item_check)
        CheckBox itemCheck;
        @BindView(R.id.item_lin)
        LinearLayout itemLin;
        public StoreEidtHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface Callback {
        void Onclick(int position);
    }
    public interface  Linstener{
        void openProject(int position);
        void closeProject(int position);
    }
    private Linstener linstener;

    public void setLinstener(Linstener linstener) {
        this.linstener = linstener;
    }

    //是否显示checkbox
    public boolean isShouwchek(){
        if(isshow){
            isshow=false;
        }else {
            isshow=true;
        }
        notifyDataSetChanged();
        return isshow;
    }

    public void changeData(List<StoreEditBean> lists){
        list.clear();
        list.addAll(lists);
    }


}
