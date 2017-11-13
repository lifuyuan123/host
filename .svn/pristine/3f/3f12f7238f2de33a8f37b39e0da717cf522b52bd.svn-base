package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.OrderBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lifuy on 2017/5/16.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {
    private List<OrderBean> list;
    private Context context;
    private OnclickListener listener;

    public void setListener(OnclickListener listener) {
        this.listener = listener;
    }

    public OrderAdapter(List<OrderBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, null);
        OrderHolder holder = new OrderHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {
        final OrderBean orderBean = list.get(position);
        UserBean bean=orderBean.getBean();
//        PicassoUtil.getPicassoObject().
        Glide.with(context).
                load(bean.getUrl())
//                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                .error(R.mipmap.icon_load_faild)
                .into(holder.friendItemIcon);
        holder.friendItemName.setText(orderBean.getBean().getRealName());
        holder.friendItemMoney.setText("￥"+new DecimalFormat("######0.00").format(orderBean.getPayValue()));
        holder.friendItemTime.setText(orderBean.getInsertTime());
        holder.orderRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/order").withInt("key",orderBean.getId()).navigation();
                LogUtil.e("order_id",orderBean.getId()+"");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class OrderHolder extends RecyclerView.ViewHolder {
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

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnclickListener {
        void Onclick(int position);
    }
}
