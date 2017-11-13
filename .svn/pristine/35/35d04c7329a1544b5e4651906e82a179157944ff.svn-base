package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.DetailBean;

import java.util.List;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class MyDetailAdapter extends BaseAdapter {
    private List<DetailBean> list;
    private Context context;

    public void setList(List<DetailBean> list) {
        this.list = list;
    }

    public MyDetailAdapter(List<DetailBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if(convertView==null){
            holder=new MyViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.detail_list_item,parent,false);
            holder.balance= (TextView) convertView.findViewById(R.id.item_tv_balance);
            holder.time= (TextView) convertView.findViewById(R.id.item_tv_time);
            holder.money= (TextView) convertView.findViewById(R.id.item_tv_money);
            holder.type= (TextView) convertView.findViewById(R.id.item_tv_type);
            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }
        DetailBean bean= list.get(position);
        int type=bean.getType();
        //1 转账收入  2为返利收入 3余额消费支出 4转账支出
       switch (type){
           case 1:
               holder.type.setText("转账收入");
               break;
           case 2:
               holder.type.setText("返利收入");
               break;
           case 3:
               holder.type.setText("消费支出");
               break;
           case 4:
               holder.type.setText("转账支出");
               break;
       }
        holder.time.setText(bean.getInsertTime()+"");
        holder.balance.setText(bean.getAfterAmount()+"");
        holder.money.setText("+"+bean.getAmount()+"");
        return convertView;
    }

    class MyViewHolder{
        TextView balance,time,money,type;
    }
}
