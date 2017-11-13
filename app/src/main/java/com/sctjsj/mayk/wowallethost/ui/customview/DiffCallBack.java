package com.sctjsj.mayk.wowallethost.ui.customview;

import android.support.v7.util.DiffUtil;

import com.sctjsj.mayk.wowallethost.model.javabean.StoreEditBean;

import java.util.List;

/**
 * Created by lifuy on 2017/5/18.
 */

public class DiffCallBack extends DiffUtil.Callback  {
    private List<StoreEditBean> mOldDatas, mNewDatas;

    public DiffCallBack(List<StoreEditBean> mOldDatas, List<StoreEditBean> mNewDatas) {
        this.mOldDatas = mOldDatas;
        this.mNewDatas = mNewDatas;
    }

    @Override
    public int getOldListSize() {
        return mOldDatas != null ? mOldDatas.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewDatas != null ? mNewDatas.size() : 0;
    }
    //返回ture  则执行下个方法areContentsTheSame
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
   //返回false则进行局部刷新
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return  false;
    }
}