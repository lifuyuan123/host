package com.sctjsj.mayk.wowallethost.presenter.impl;

import com.sctjsj.mayk.wowallethost.callback.PresenterCallback;
import com.sctjsj.mayk.wowallethost.callback.ModelCallback;
import com.sctjsj.mayk.wowallethost.model.impl.ProjectClassifyModel;
import com.sctjsj.mayk.wowallethost.model.javabean.ProjectClassifyBean;
import com.sctjsj.mayk.wowallethost.presenter.IProjectClassifyPresenter;
import com.sctjsj.mayk.wowallethost.ui.activity.ProjectClassifyActivity;

import java.util.List;

/**
 * Created by mayikang on 17/5/18.
 */

public class ProjectClassifyPresenterImpl implements IProjectClassifyPresenter{

    private ProjectClassifyActivity activity;

    private ProjectClassifyModel model;
    public ProjectClassifyPresenterImpl(ProjectClassifyActivity activity){
        this.activity=activity;
        model=new ProjectClassifyModel();
    }


    @Override
    public void pullClassify(int pageSize, int pageIndex, final ModelCallback callback) {

        model.getClassifyList(pageSize,pageIndex,new PresenterCallback(){

            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onSuccess(String res) {
                callback.onSuccess(res);
            }

            @Override
            public void onError(String err) {
                callback.onError(err);
            }

            @Override
            public void onFinish() {
                callback.onFinish();
            }
        });


    }

    @Override
    public void deleteClassifyById(String id, ModelCallback callback) {

    }

    @Override
    public void deleteClassifyList(List<String> idList, ModelCallback callback) {

    }

    @Override
    public void updateClassify(ProjectClassifyBean classifyBean, ModelCallback callback) {

    }
}
