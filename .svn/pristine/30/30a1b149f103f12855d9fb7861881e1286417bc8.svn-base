package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.MyDetailAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.DetailBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/balance_detail")
public class BalanceDetailActivity extends BaseAppcompatActivity {
    @BindView(R.id.detail_qrefresh)
    MaterialRefreshLayout detailQrefresh;
    @BindView(R.id.gone_linear)
    LinearLayout goneLinear;
    @BindView(R.id.detail_listview)
    ListView detailListview;
    @BindView(R.id.detail_iv_centent)
    ImageView detailIvCentent;
    @BindView(R.id.detail_iv_total_right)
    ImageView detailIvTotalRight;
    @BindView(R.id.detail_iv_expenditure_right)
    ImageView detailIvExpenditureRight;
    @BindView(R.id.detail_iv_income_right)
    ImageView detailIvIncomeRight;
    private MyDetailAdapter adapter;
    private List<DetailBean> data = new ArrayList<>();
    private List<DetailBean> dataout = new ArrayList<>();
    private List<DetailBean> dataint = new ArrayList<>();
    private boolean isgone = true;
    private Animation animation;
    private IHttpService service;
    private int pageIndex = 1;
    private int showType=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        init();
        pullBalanceDetailProject();


        detailQrefresh.setLoadMore(true);
        detailQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullBalanceDetailProject();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                pullBalanceDetailMore();
            }
        });

    }


    //加载收支明细
    private void pullBalanceDetailProject() {
        pageIndex=1;
        data.clear();
        dataout.clear();
        dataint.clear();
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","paymentFlow");
        body.put("pageIndex",String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        pageIndex++;
                        if(obj.getBoolean("result")){
                            JSONArray arrlist=obj.getJSONArray("resultList");
                            if(null!=arrlist&&arrlist.length()>0){
                                for (int i = 0; i <arrlist.length() ; i++) {
                                    JSONObject beanObj=arrlist.getJSONObject(i);
                                    DetailBean bean=new DetailBean();
                                    bean.setId(beanObj.getInt("id"));
                                    bean.setType(beanObj.getInt("type"));
                                    bean.setAfterAmount(beanObj.getInt("afterAmount"));
                                    bean.setAmount(beanObj.getInt("amount"));
                                    bean.setInfo(beanObj.getString("info"));
                                    bean.setInsertTime(beanObj.getString("insertTime"));
                                    data.add(bean);
                                    if(bean.getType()==3||bean.getType()==4){
                                        dataout.add(bean);
                                    }
                                    if(bean.getType()==1||bean.getType()==2){
                                        dataint.add(bean);
                                    }
                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                detailQrefresh.finishRefresh();
                detailQrefresh.finishRefreshLoadMore();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


    //上拉加载更多
    private void pullBalanceDetailMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","paymentFlow");
        body.put("pageIndex",String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        pageIndex++;
                        if(obj.getBoolean("result")){
                            JSONArray arrlist=obj.getJSONArray("resultList");
                            if(null!=arrlist&&arrlist.length()>0){
                                for (int i = 0; i <arrlist.length() ; i++) {
                                    JSONObject beanObj=arrlist.getJSONObject(i);
                                    DetailBean bean=new DetailBean();
                                    bean.setId(beanObj.getInt("id"));
                                    bean.setType(beanObj.getInt("type"));
                                    bean.setAfterAmount(beanObj.getInt("afterAmount"));
                                    bean.setAmount(beanObj.getInt("amount"));
                                    bean.setInfo(beanObj.getString("info"));
                                    bean.setInsertTime(beanObj.getString("insertTime"));
                                    switch (showType){
                                        case 1:
                                            data.add(bean);
                                            break;
                                        case 2:
                                            if(bean.getType()==3||bean.getType()==4){
                                                dataout.add(bean);
                                            }
                                            break;
                                        case 3:
                                            if(bean.getType()==1||bean.getType()==2){
                                                dataint.add(bean);
                                            }
                                            break;
                                    }

                                }
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                detailQrefresh.finishRefresh();
                detailQrefresh.finishRefreshLoadMore();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }


    private void init() {
        adapter=new MyDetailAdapter(data,this);
        detailListview.setAdapter(adapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_balance_detail;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.shops_linear_back, R.id.detail_linear_centent,R.id.detail_ralative_total,
            R.id.detail_ralative_expenditure, R.id.detail_ralative_income,R.id.detail_frame_text_buttom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shops_linear_back:
                finish();
                break;
            case R.id.detail_linear_centent:
                if (isgone) {
                    goneLinear.setVisibility(View.VISIBLE);
                    //让listview失去焦点不能点击
                    detailListview.setEnabled(false);
                    animation = AnimationUtils.loadAnimation(BalanceDetailActivity.this, R.anim.rotate_start);
                    detailIvCentent.startAnimation(animation);
                    animation.setFillAfter(true);
                    isgone = false;
                } else {
                    goneLinear.setVisibility(View.GONE);
                    //让listview获取焦点能点击
                    detailListview.setEnabled(true);
                    animation = AnimationUtils.loadAnimation(BalanceDetailActivity.this, R.anim.rotate_back);
                    detailIvCentent.startAnimation(animation);
                    animation.setFillAfter(true);
                    isgone = true;
                }
                break;
            //支出
            case R.id.detail_ralative_expenditure:
                showType=2;
                detailIvExpenditureRight.setVisibility(View.VISIBLE);
                detailIvTotalRight.setVisibility(View.GONE);
                detailIvIncomeRight.setVisibility(View.GONE);
                adapter.setList(dataout);
                adapter.notifyDataSetChanged();

                goneLinear.setVisibility(View.GONE);
                //让listview获取焦点能点击
                detailListview.setEnabled(true);

                animation= AnimationUtils.loadAnimation(BalanceDetailActivity.this,R.anim.rotate_back);
                detailIvCentent.startAnimation(animation);
                animation.setFillAfter(true);
                isgone=true;

                break;
            //收入
            case R.id.detail_ralative_income:
                showType=3;
                detailIvExpenditureRight.setVisibility(View.GONE);
                detailIvTotalRight.setVisibility(View.GONE);
                detailIvIncomeRight.setVisibility(View.VISIBLE);
                adapter.setList(dataint);
                adapter.notifyDataSetChanged();
                goneLinear.setVisibility(View.GONE);
                //让listview获取焦点能点击
                detailListview.setEnabled(true);

                animation= AnimationUtils.loadAnimation(BalanceDetailActivity.this,R.anim.rotate_back);
                detailIvCentent.startAnimation(animation);
                animation.setFillAfter(true);
                isgone=true;


                break;
            // 全部
            case R.id.detail_ralative_total:
                showType=1;
                detailIvExpenditureRight.setVisibility(View.GONE);
                detailIvTotalRight.setVisibility(View.VISIBLE);
                detailIvIncomeRight.setVisibility(View.GONE);
                adapter.setList(data);
                adapter.notifyDataSetChanged();
                goneLinear.setVisibility(View.GONE);
                //让listview获取焦点能点击
                detailListview.setEnabled(true);

                animation= AnimationUtils.loadAnimation(BalanceDetailActivity.this,R.anim.rotate_back);
                detailIvCentent.startAnimation(animation);
                animation.setFillAfter(true);
                isgone=true;

                break;
            case R.id.detail_frame_text_buttom:
                goneLinear.setVisibility(View.GONE);
                //让listview获取焦点能点击
                detailListview.setEnabled(true);

                animation= AnimationUtils.loadAnimation(BalanceDetailActivity.this,R.anim.rotate_back);
                detailIvCentent.startAnimation(animation);
                animation.setFillAfter(true);
                isgone=true;
                break;
        }
        }


}
