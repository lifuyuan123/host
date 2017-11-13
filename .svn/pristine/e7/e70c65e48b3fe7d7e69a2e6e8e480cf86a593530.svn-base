package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.ClerkBean;

import java.util.List;

/**
 * Created by lifuy on 2017/6/20.
 */

public class ClerkAdapter extends RecyclerView.Adapter<ClerkAdapter.ViewHolder> {
    private List<ClerkBean> list;
    private Context context;

    public ClerkAdapter(List<ClerkBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.clerk_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
       holder.textView.setText(list.get(position).getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.tv_type);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.lin);
        }
    }

    public interface Callback{
        void onClick(int position);
    }
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
