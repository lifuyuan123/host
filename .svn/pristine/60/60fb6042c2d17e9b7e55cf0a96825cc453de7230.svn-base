package com.sctjsj.mayk.wowallethost.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.event.DataEvent;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.DetaBean;
import com.sctjsj.mayk.wowallethost.model.javabean.HomeData;
import com.sctjsj.mayk.wowallethost.presenter.impl.IndexPresenterImpl;
import com.sctjsj.mayk.wowallethost.ui.fragment.HomeFragment;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liuha on 2017/5/17.
 */

public class HomeLayoutAdapter extends DelegateAdapter.Adapter<HomeLayoutAdapter.HomeLayoutHolder> {
    public static final int TYPE_1=1;
    public static final int TYPE_2=2;
    public static final int TYPE_3=3;
    public static final int TYPE_4=4;
    public static final int TYPE_5=5;

    private Context mContext;
    private ArrayList<HashMap<String, Object>> data;
    // 用于存放数据列表
    private RecyclerView.LayoutParams layoutParams;
    private LayoutInflater inflater;
    private LayoutHelper layoutHelper;
    private int count = 0;
    private int type = 0;//本 item 的类型

    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

    public void setData(ArrayList<HashMap<String, Object>> data) {
        this.data = data;
    }

    public RecyclerView.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void setLayoutParams(RecyclerView.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //构造函数(传入每个的数据列表 & 展示的Item数量)
    public HomeLayoutAdapter(Context context, LayoutHelper layoutHelper, int count,
                                 ArrayList<HashMap<String, Object>> data, int type) {
        //宽度占满，高度随意
        this(context, layoutHelper, count,
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT), data, type);
    }


    public HomeLayoutAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull RecyclerView.LayoutParams layoutParams,
                                 ArrayList<HashMap<String, Object>> data, int type) {
        this.mContext = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.layoutParams = layoutParams;
        this.data = data;
        this.type = type;

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
    }


    @Override
    public HomeLayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View mView=null;
        switch (viewType){
            case TYPE_1:
                mView=LayoutInflater.from(mContext).inflate(R.layout.home_lay_1,parent,false);
                break;
            case TYPE_2:
                mView=LayoutInflater.from(mContext).inflate(R.layout.home_lay_2,parent,false);
                break;
            case TYPE_3:
                mView=LayoutInflater.from(mContext).inflate(R.layout.home_lay_3,parent,false);
                break;
            case TYPE_4:
                mView=LayoutInflater.from(mContext).inflate(R.layout.home_lay_4,parent,false);
                break;
            case TYPE_5:
                break;
        }
        return new HomeLayoutHolder(mView,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        switch (type){
            case 1:
                return TYPE_1;
            case 2:
                return TYPE_2;
            case 3:
                return TYPE_3;
            case 4:
                return TYPE_4;
            case 5:
                return TYPE_5;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(HomeLayoutHolder holder, int position) {
        int type=getItemViewType(position);
        switch (type){
            case TYPE_1:
                HashMap<String, Object> map1 = new HashMap<>();
                map1 = data.get(0);
                if(map1.containsKey("todayTurnover")){
                    double todayTurnover = (double) map1.get("todayTurnover");
                    holder.todayMoney.setText("￥"+new DecimalFormat("######0.00").format(todayTurnover));
                }
                if(map1.containsKey("todayTurnovers")){
                    double todayTurnovers = (double) map1.get("todayTurnovers");
                    holder.allMoney.setText("￥"+new DecimalFormat("######0.00").format(todayTurnovers));
                }
                //今日营业额
                holder.home_turnover_today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetaBean bean=new DetaBean();
                        bean.setContent("ORDER");
                        bean.setType(1);
                        EventBus.getDefault().post(new DataEvent(bean));
                    }
                });
                //累计营业额
                holder.home_turnover_accumulate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetaBean bean=new DetaBean();
                        bean.setContent("ORDER");
                        bean.setType(2);
                        EventBus.getDefault().post(new DataEvent(bean));
                    }
                });
                break;
            case TYPE_2:
                HashMap<String, Object> map = new HashMap<>();
                map = data.get(0);
                Log.e("111_map",map.toString());
                if(map.containsKey("count")){
                    int count = (int) map.get("count");
                    holder.today.setText("今日"+count+"单");
                }
                if(map.containsKey("counts")){
                    int counts = (int) map.get("counts");
                    holder.history.setText("历史"+counts+"单");
                }
                //历史单
                holder.home_lin_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetaBean bean=new DetaBean();
                        bean.setContent("ORDER");
                        bean.setType(2);
                        EventBus.getDefault().post(new DataEvent(bean));
                    }
                });
                //今日单
                holder.home_lin_today.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetaBean bean=new DetaBean();
                        bean.setContent("ORDER");
                        bean.setType(1);
                        EventBus.getDefault().post(new DataEvent(bean));
                    }
                });

                break;
            case TYPE_3:
                holder.home_lay3_collection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转收款
                        ARouter.getInstance().build("/main/act/GatheringQrActivity").navigation();
                    }
                });
                holder.home_lay3_withdarw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到提现
                        ARouter.getInstance().build("/main/act/confirm_deposit").navigation();
                    }
                });

                holder.home_lay3_message.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到代理商
                        HashMap<String, Object> maps = new HashMap<>();
                        maps = data.get(0);
                        boolean status=false;
                        int id=-1;
                        if(maps.containsKey("status")){
                            status= (boolean) maps.get("status");
                        }
                        if(maps.containsKey("id")){
                            id= (int) maps.get("id");
                        }
                        if(status){
                            ARouter.getInstance().build("/main/act/AuditStatusActivity").withInt("id",id).navigation();
                        }else{
                            ARouter.getInstance().build("/main/act/ChooseAgentActivity").navigation();
                        }

                    }
                });

                holder.home_lay3_bill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到账单
                        ARouter.getInstance().build("/main/act/BillActivity").navigation();
                    }
                });

                break;
            case TYPE_4:
                HashMap<String,Object> rebate=new HashMap<>();
                rebate=data.get(0);
                if(rebate.containsKey("maxRebate")){
                  double maxRebate= (double) rebate.get("maxRebate");
                    holder.maxRebate.setText(new DecimalFormat("######0.00").format(maxRebate));
                }
                if(rebate.containsKey("remainRebate")){
                    double remainRebate= (double) rebate.get("remainRebate");
                    holder.remainRebate.setText(new DecimalFormat("######0.00").format(remainRebate));
                }
            case TYPE_5:
                break;

        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }


    class HomeLayoutHolder extends RecyclerView.ViewHolder{
        int type;
        LinearLayout home_lay3_collection,home_lay3_withdarw,home_lay3_message,home_lay3_bill;
        LinearLayout home_turnover_today,home_turnover_accumulate,home_lin_all,home_lin_today;
        TextView today,history,allMoney,todayMoney;
        TextView maxRebate,remainRebate;

        public HomeLayoutHolder(View itemView,int type) {
            super(itemView);
            this.type=type;

            switch (type){
                case TYPE_1:
                    allMoney= (TextView) itemView.findViewById(R.id.tv_all_Money);
                    todayMoney= (TextView) itemView.findViewById(R.id.tv_today_Money);
                    home_turnover_today= (LinearLayout) itemView.findViewById(R.id.home_lin_turnover_today);
                    home_turnover_accumulate= (LinearLayout) itemView.findViewById(R.id.home_lin_turnover_accumulate);
                    break;
                case TYPE_2:
                    today= (TextView) itemView.findViewById(R.id.tv_order_today);
                    history= (TextView) itemView.findViewById(R.id.tv_order_history);
                    home_lin_all= (LinearLayout) itemView.findViewById(R.id.home_lin_all);
                    home_lin_today= (LinearLayout) itemView.findViewById(R.id.home_lin_today);
                    break;
                case TYPE_3:
                    home_lay3_collection= (LinearLayout) itemView.findViewById(R.id.home_lay3_collection_ll);
                    home_lay3_withdarw= (LinearLayout) itemView.findViewById(R.id.home_lay3_withdarw_ll);
                    home_lay3_message= (LinearLayout) itemView.findViewById(R.id.home_lay3_message_ll);
                    home_lay3_bill= (LinearLayout) itemView.findViewById(R.id.home_lay3_bill_ll);
                     break;
                case TYPE_4:
                    maxRebate= (TextView) itemView.findViewById(R.id.tv_maxRebate);
                    remainRebate= (TextView) itemView.findViewById(R.id.tv_remainRebate);
                    break;
                case TYPE_5:

                    break;
            }
        }
    }




}
