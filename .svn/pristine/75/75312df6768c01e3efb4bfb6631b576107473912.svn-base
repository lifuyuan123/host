package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.ui.widget.layout.SwipeMenuLayout;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectClassifyBean;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreEditBean;

import java.util.List;


/**SwipeMenuLayout.java
 * Created by mayikang on 17/5/18.
 */

public class ProjectClaassifyAdapter extends RecyclerView.Adapter<ProjectClaassifyAdapter.ClassifyHolder>{

    private List<ProjectClassifyBean> data;
    private Context context;
    private LayoutInflater inflater;
    private boolean isshow = false;

    public ProjectClaassifyAdapter(Context context,List<ProjectClassifyBean> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }

    @Override
    public ClassifyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_project_classify,null);
        return new ClassifyHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassifyHolder holder, final int position) {
//        //是否显示删除的 cb
//        if(data.get(position).isShowDelete()){
//            holder.mCB.setVisibility(View.VISIBLE);
//        }else {
//            holder.mCB.setVisibility(View.GONE);
//        }



        //点击选中
        holder.mCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    data.get(position).setCBChecked(true);
                }else {
                    data.get(position).setCBChecked(false);
                }
            }
        });

        //显示是否选中
        if(data.get(position).isCBChecked()){
            holder.mCB.setChecked(true);
        }else {
            holder.mCB.setChecked(false);
        }

        //是否显示删除的 cb
        if(isshow){
            holder.mCB.setVisibility(View.VISIBLE);
        }else {
            holder.mCB.setVisibility(View.GONE);
        }

        holder.textView.setText(data.get(position).getName());

        //点击 item 编辑

//        holder.mLLContainer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ARouter.getInstance().build("/main/act/add_or_modify_project_classify").withObject("classify",data.get(position)).navigation();
//
//            }
//        });
        holder.mLLContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    callback.onClick(position);
                }
            }
        });

        holder.mLLContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(callback!=null){
                    callback.onLongClick(position);
                }
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ClassifyHolder extends RecyclerView.ViewHolder{
        CheckBox mCB;
        LinearLayout mLLContainer;
        TextView textView;
        public ClassifyHolder(View itemView) {
            super(itemView);
            mCB= (CheckBox) itemView.findViewById(R.id.item_project_classify_cb);
            mLLContainer=(LinearLayout) itemView.findViewById(R.id.ll_classify_container);
            textView= (TextView) itemView.findViewById(R.id.tv_name);
        }
    }


    public void showDelete(){
        for (int i=0;i<data.size();i++){
            data.get(i).setShowDelete(true);
            notifyItemChanged(i);
        }

    }


    public void dismissDelete(){
        for (int i=0;i<data.size();i++){
            data.get(i).setShowDelete(false);
            data.get(i).setCBChecked(false);
            notifyItemChanged(i);
        }

    }

    public void changeData(List<ProjectClassifyBean> lists){
        data.clear();
        data.addAll(lists);
        notifyDataSetChanged();
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

    public interface Callback{
        void onClick(int position);
        void onLongClick(int position);
    }
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
