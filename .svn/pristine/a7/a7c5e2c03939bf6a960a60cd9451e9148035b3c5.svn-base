package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.BillBean;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/12.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.XViewHolder> {
    private List<BillBean> data;
    private LayoutInflater inflater;
    private Context mContext;

    public BillAdapter(Context mContext, List<BillBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getfType()==2){
            return 2;
        }
        return super.getItemViewType(position);
    }

    @Override
    public XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=null;
        if(viewType==2){
            v=inflater.inflate(R.layout.order_item,null);
        }else {
            v=inflater.inflate(R.layout.bill_item,null);
        }
        return new XViewHolder(v);
    }

    @Override
    public void onBindViewHolder(XViewHolder holder, final int position) {

        if(getItemViewType(position)==2){
           holder.orderRel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   ARouter.getInstance().build("/main/act/user/bill_detail").withInt("id",data.get(position).getId()).withInt("key",2).navigation();
               }
           });
//            PicassoUtil.getPicassoObject().
            Glide.with(mContext).
                    load(data.get(position).getUrl()).
//                    resize(DpUtils.dpToPx(mContext,60), DpUtils.dpToPx(mContext,60)).
                    error(R.mipmap.icon_load_faild).into(holder.friendItemIcon);
            holder.friendItemTime.setText(data.get(position).getInsertTime());
            holder.friendItemMoney.setText(new DecimalFormat("######0.00").format(data.get(position).getAmount()));
            holder.friendItemName.setText(data.get(position).getDesc());

        }else {

        //跳转账单详情
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/user/bill_detail").withInt("id",data.get(position).getId()).navigation();
                LogUtil.e("deatil","id="+data.get(position).getId());
            }
        });
//        PicassoUtil.getPicassoObject().
            Glide.with(mContext).
                load(data.get(position).getUrl()).
//                resize(DpUtils.dpToPx(mContext,60), DpUtils.dpToPx(mContext,60)).
                error(R.mipmap.icon_load_faild).into(holder.billItemIcon);
        holder.billItemMoney.setText("+"+new DecimalFormat("######0.00").format(data.get(position).getAmount()));
        holder.billItemTitle.setText(data.get(position).getDesc());
        holder.billItemDay.setText(data.get(position).getInsertTime());
        holder.income.setText(new DecimalFormat("######0.00").format(data.get(position).getPlatformIncome())+"");
        holder.turnover.setText(new DecimalFormat("######0.00").format(data.get(position).getUrnover())+"");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data==null?10:data.size();

    }




     class XViewHolder extends RecyclerView.ViewHolder{

        CircleImageView billItemIcon;
        TextView billItemTitle;
        TextView billItemMoney;
        TextView billItemDay;
        TextView billItemTime;
         TextView turnover;
         TextView income;
         LinearLayout llParent;



         @BindView(R.id.order_rel)
         RelativeLayout orderRel;
         @BindView(R.id.friend_item_icon)
         CircleImageView friendItemIcon;
         @BindView(R.id.friend_item_name)
         TextView friendItemName;
         @BindView(R.id.friend_item_money)
         TextView friendItemMoney;
         @BindView(R.id.friend_item_time)
         TextView friendItemTime;
        public XViewHolder(View itemView) {
            super(itemView);
            billItemIcon= (CircleImageView) itemView.findViewById(R.id.bill_item_icon);
            billItemTitle= (TextView) itemView.findViewById(R.id.bill_item_title);
            billItemMoney= (TextView) itemView.findViewById(R.id.bill_item_Money);
            billItemDay= (TextView) itemView.findViewById(R.id.bill_item_day);
            billItemTime= (TextView) itemView.findViewById(R.id.bill_item_time);
            llParent= (LinearLayout) itemView.findViewById(R.id.item_bill_rl_parent);
            turnover= (TextView) itemView.findViewById(R.id.tv_turnover);
            income= (TextView) itemView.findViewById(R.id.tv_income);

            orderRel= (RelativeLayout) itemView.findViewById(R.id.order_rel);
            friendItemIcon= (CircleImageView) itemView.findViewById(R.id.friend_item_icon);
            friendItemName= (TextView) itemView.findViewById(R.id.friend_item_name);
            friendItemMoney= (TextView) itemView.findViewById(R.id.friend_item_money);
            friendItemTime= (TextView) itemView.findViewById(R.id.friend_item_time);


        }

    }
}
