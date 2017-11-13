package com.sctjsj.mayk.wowallethost.model.impl;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.callback.PresenterCallback;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mayikang on 17/5/18.
 */

public class ProjectClassifyModel {

    private HttpServiceImpl httpService;

    public ProjectClassifyModel(){
        httpService= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    public void getClassifyList(int pageSize, int pageIndex, final PresenterCallback callback){
        if(httpService==null){
            return;
        }
        Map<String,String> params=new HashMap<>();
        params.put("jf","accessory");
        httpService.doCommonPost(null, "http://www.lp-kd.com/obtionHomeSilde$ajax.htm?", params, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex) {
                callback.onError(ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                callback.onFinish();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                callback.onStart();
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


}
