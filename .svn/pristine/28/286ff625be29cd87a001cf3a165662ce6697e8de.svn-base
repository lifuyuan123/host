package com.sctjsj.mayk.wowallethost.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.adapter.EvaluateAdapter;
import com.sctjsj.mayk.wowallethost.intf.MainUrl;
import com.sctjsj.mayk.wowallethost.model.javabean.EvaluateBean;
import com.sctjsj.mayk.wowallethost.model.javabean.StoreReplyBean;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
import com.sctjsj.mayk.wowallethost.util.UserAuthUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 评价管理页面
 */

@Route(path = "/main/act/EvaluateManageActivity")
public class EvaluateManageActivity extends BaseAppcompatActivity implements EvaluateAdapter.EvaLuateCallBack {

    @BindView(R.id.evaluate_back_rl)
    RelativeLayout evaluateBackRl;
    @BindView(R.id.evaluate_rv)
    RecyclerView evaluateRv;
    @BindView(R.id.evaluate_refresh)
    MaterialRefreshLayout evaluateRefresh;

    private DelegateAdapter adapter;

    /**
     * 对应的子适配器
     **/
    private List<DelegateAdapter.Adapter> subAdapterList = new LinkedList<>();

    private EvaluateAdapter lay_oneAdapter, Lay_twoAdapter, lay_threrAdapter;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    List<HashMap<String, Object>> Commentdata = new ArrayList<>();
    List<HashMap<String, Object>> CommentDataCopy = new ArrayList<>();
    private int bad = 0;
    private int banck = 0;
    private int flag = -1;

   /* private List<Object> dataObj=new ArrayList<>();
    private List<Object> AlldataObj=new ArrayList<>();*/

    HashMap<String, Object> checkData = new HashMap<>();
    HashMap<String, Object> comlistData = new LinkedHashMap<>();
    HashMap<String, Object> score = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initRv();
        getCommentNumber();
        getCommintData();
        getStoreScore();
        setListener();
    }

    //设置监听
    private void setListener() {
        evaluateRefresh.setLoadMore(true);
        evaluateRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getCommintData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getCommintDataMore();
            }
        });
    }


    private void initRv() {
        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        evaluateRv.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        evaluateRv.setRecycledViewPool(recycledViewPool);

        initTitle();
        initChoose();
        initList();

        adapter = new DelegateAdapter(layoutManager, false);
        adapter.setAdapters(subAdapterList);
        evaluateRv.setAdapter(adapter);

    }

    //评论列表
    private void initList() {
        LinearLayoutHelper helper = new LinearLayoutHelper();
        //Commentdata.add(comlistData);
        lay_threrAdapter = new EvaluateAdapter(this, helper, 2, Commentdata, 3);
        lay_threrAdapter.setEvaLuateCallBack(this);
        subAdapterList.add(lay_threrAdapter);

    }

    //选择
    private void initChoose() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setItemCount(1);
        List<HashMap<String, Object>> datas = new ArrayList<>();
        datas.add(checkData);
        Lay_twoAdapter = new EvaluateAdapter(this, helper, 1, datas, 2);
        Lay_twoAdapter.setEvaLuateCallBack(this);
        subAdapterList.add(Lay_twoAdapter);
    }

    //初始化最顶上的一大坨
    private void initTitle() {
        SingleLayoutHelper helper = new SingleLayoutHelper();
        helper.setItemCount(1);
        List<HashMap<String, Object>> scoreData = new ArrayList<>();
        scoreData.add(score);
        lay_oneAdapter = new EvaluateAdapter(this, helper, 1, scoreData, 1);
        subAdapterList.add(lay_oneAdapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_evaluate_manage;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.evaluate_back_rl)
    public void onViewClicked() {
        finish();
    }

    //获取店铺评分
    private void getStoreScore(){
        Map<String,String> map=new HashMap<>();
        map.put("ctype","store");
        map.put("id",String.valueOf(UserAuthUtil.getStoreId()));
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("Score_onSuccess",result.toString());
                if(!TextUtils.isEmpty(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            JSONObject object1=object.getJSONObject("data");
                            score.put("score",object1.getDouble("score"));
                        }else {
                            Toast.makeText(EvaluateManageActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("Score_JSONException",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("Score_onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                lay_oneAdapter.notifyDataSetChanged();
                LogUtil.e("Score_onFinished");
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


    //获取评论列表
    private void getCommintData() {
        CommentDataCopy.clear();
        pageIndex = 1;
        bad = 0;
        banck = 0;
        Commentdata.clear();
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "comment");
        body.put("cond", "{store:{id:" + UserAuthUtil.getStoreId() + "}}");
        body.put("jf", "reviewer|photo|commentDetail|commentPhoto");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("getCommintData", result.toString());
                    try {
                        pageIndex++;
                        JSONObject object = new JSONObject(result);
                        JSONArray arr = object.getJSONArray("resultList");
                        if (null != arr && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject evBean = arr.getJSONObject(i);
                                EvaluateBean mEvaluateBean = new EvaluateBean();
                                JSONArray commentDetail = evBean.getJSONArray("commentDetail");
                                //店家回复
                                if (null != commentDetail && commentDetail.length() > 0) {
                                    mEvaluateBean.setIsboosEva(true);
                                    JSONObject storeReply = commentDetail.getJSONObject(0);
                                    StoreReplyBean mStoreReplyBean = new StoreReplyBean();
                                    mStoreReplyBean.setId(storeReply.getInt("id"));
                                    mStoreReplyBean.setInsertTime(storeReply.getString("insertTime"));
                                    mStoreReplyBean.setContent(storeReply.getString("content"));
                                    //回复者的javabean.....
                                    mEvaluateBean.setStoreReplyBean(mStoreReplyBean);

                                } else {
                                    mEvaluateBean.setIsboosEva(false);
                                }

                                //用户上传的实物图
                                List<String> photo = new ArrayList<String>();
                                JSONArray photoArr = evBean.getJSONArray("commentPhoto");
                                if (null != photoArr && photoArr.length() > 0) {
                                    for (int j = 0; j < photoArr.length(); j++) {
                                        JSONObject objPhoto = photoArr.getJSONObject(j);
                                        JSONObject photoUrl = objPhoto.getJSONObject("commentPhoto");
                                        photo.add(photoUrl.getString("url"));
                                    }
                                    mEvaluateBean.setCommentPhoto(photo);
                                }

                                //获取评论其他信息
                                mEvaluateBean.setContent(evBean.getString("content"));
                                mEvaluateBean.setId(evBean.getInt("id"));
                                mEvaluateBean.setInsertTime(evBean.getString("insertTime"));
                                mEvaluateBean.setScore(evBean.getInt("score"));
                                mEvaluateBean.setIsAnonymous(evBean.getInt("isAnonymous"));

                                //评论者的信息
                                JSONObject evaUserBean = evBean.getJSONObject("reviewer");
                                UserBean evUserBean = new UserBean();
                                evUserBean.setId(evaUserBean.getInt("id"));
                                evUserBean.setUsername(evaUserBean.getString("username"));
                                evUserBean.setUrl(evaUserBean.getJSONObject("photo").getString("url"));
                                mEvaluateBean.setReviewer(evUserBean);

                                HashMap<String, Object> data = new HashMap<String, Object>();
                                data.put("comment", mEvaluateBean);
                                switch (flag) {
                                    case -1:
                                        Commentdata.add(data);
                                        break;
                                    case 0:
                                        if (!mEvaluateBean.isboosEva()) {
                                            Commentdata.add(data);
                                        }
                                        break;
                                    case 1:
                                        if (evBean.getInt("score") < 2) {
                                            Commentdata.add(data);
                                        }
                                        break;
                                }
                                CommentDataCopy.add(data);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (Commentdata.size() > 0) {
                            lay_threrAdapter.notifyDataSetChanged();
                        }
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
                evaluateRefresh.finishRefresh();
                evaluateRefresh.finishRefreshLoadMore();
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


    //上拉
    private void getCommintDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "comment");
        body.put("cond", "{store:{id:" + UserAuthUtil.getStoreId() + "}}");
        body.put("jf", "reviewer|photo|commentDetail|commentPhoto");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("getCommintData", result.toString());
                    try {
                        pageIndex++;
                        JSONObject object = new JSONObject(result);
                        JSONArray arr = object.getJSONArray("resultList");
                        if (null != arr && arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject evBean = arr.getJSONObject(i);
                                EvaluateBean mEvaluateBean = new EvaluateBean();
                                JSONArray commentDetail = evBean.getJSONArray("commentDetail");
                                //店家回复
                                if (null != commentDetail && commentDetail.length() > 0) {
                                    mEvaluateBean.setIsboosEva(true);
                                    JSONObject storeReply = commentDetail.getJSONObject(0);
                                    StoreReplyBean mStoreReplyBean = new StoreReplyBean();
                                    mStoreReplyBean.setId(storeReply.getInt("id"));
                                    mStoreReplyBean.setInsertTime(storeReply.getString("insertTime"));
                                    mStoreReplyBean.setContent(storeReply.getString("content"));
                                    //回复者的javabean.....
                                    mEvaluateBean.setStoreReplyBean(mStoreReplyBean);

                                } else {
                                    mEvaluateBean.setIsboosEva(false);
                                }

                                //用户上传的实物图
                                List<String> photo = new ArrayList<String>();
                                JSONArray photoArr = evBean.getJSONArray("commentPhoto");
                                if (null != photoArr && photoArr.length() > 0) {
                                    for (int j = 0; j < photoArr.length(); j++) {
                                        JSONObject objPhoto = photoArr.getJSONObject(j);
                                        JSONObject photoUrl = objPhoto.getJSONObject("commentPhoto");
                                        photo.add(photoUrl.getString("url"));
                                    }
                                    mEvaluateBean.setCommentPhoto(photo);
                                }

                                //获取评论其他信息
                                mEvaluateBean.setContent(evBean.getString("content"));
                                mEvaluateBean.setId(evBean.getInt("id"));
                                mEvaluateBean.setInsertTime(evBean.getString("insertTime"));
                                mEvaluateBean.setScore(evBean.getInt("score"));

                                mEvaluateBean.setIsAnonymous(evBean.getInt("isAnonymous"));

                                //评论者的信息
                                JSONObject evaUserBean = evBean.getJSONObject("reviewer");
                                UserBean evUserBean = new UserBean();
                                evUserBean.setId(evaUserBean.getInt("id"));
                                evUserBean.setUsername(evaUserBean.getString("username"));
                                evUserBean.setUrl(evaUserBean.getJSONObject("photo").getString("url"));
                                mEvaluateBean.setReviewer(evUserBean);

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("comment", mEvaluateBean);
                                CommentDataCopy.add(data);
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    } finally {
                        if (Commentdata.size() > 0) {
                            lay_threrAdapter.notifyDataSetChanged();
                        }
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
                evaluateRefresh.finishRefresh();
                evaluateRefresh.finishRefreshLoadMore();
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


    //查询评论条数
    private void getCommentNumber(){
        HashMap<String,String> body=new HashMap<>();
        body.put("storeId",UserAuthUtil.getStoreId()+"");
        service.doCommonPost(null, MainUrl.QueryComment, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            checkData.put("bad",object.getInt("BadComment")+"");
                            checkData.put("back", object.getInt("noReply")+"");
                            checkData.put("all", object.getInt("allComment")+"");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        Lay_twoAdapter.notifyDataSetChanged();
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


    //adapter里面的三个回掉
    @Override
    public void allComment() {
        flag = -1;
        Commentdata.clear();
        Commentdata.addAll(CommentDataCopy);
        lay_threrAdapter.notifyDataSetChanged();

    }


    @Override
    public void noComment() {
        flag = 0;
        Commentdata.clear();
        for (int i = 0; i < CommentDataCopy.size(); i++) {
            HashMap<String, Object> data = CommentDataCopy.get(i);
            EvaluateBean bean = (EvaluateBean) data.get("comment");
            if (!bean.isboosEva()) {
                Commentdata.add(data);
            }
        }
        lay_threrAdapter.notifyDataSetChanged();
    }


    @Override
    public void badComment() {
        flag = 1;
        Commentdata.clear();
        for (int i = 0; i < CommentDataCopy.size(); i++) {
            HashMap<String, Object> data = CommentDataCopy.get(i);
            EvaluateBean bean = (EvaluateBean) data.get("comment");
            if (bean.getScore() < 2) {
                Commentdata.add(data);
            }
        }
        lay_threrAdapter.notifyDataSetChanged();
    }


    @Override
    public void submitSucceed() {
        getCommintData();
        initTitle();
        initChoose();
        getCommentNumber();
        getStoreScore();
    }
}
