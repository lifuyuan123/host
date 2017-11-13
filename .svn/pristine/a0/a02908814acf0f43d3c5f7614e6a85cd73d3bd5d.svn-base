package com.sctjsj.mayk.wowallethost.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sctjsj.mayk.wowallethost.R;
import com.sctjsj.mayk.wowallethost.presenter.IIndexPresenter;
import com.sctjsj.mayk.wowallethost.ui.fragment.HomeFragment;
import com.sctjsj.mayk.wowallethost.ui.fragment.OrderFragment;
import com.sctjsj.mayk.wowallethost.ui.fragment.OwnFragment;
import com.sctjsj.mayk.wowallethost.ui.fragment.ShopsFragment;


/**
 * Created by mayikang on 17/5/8.
 */

public class IndexPresenterImpl implements IIndexPresenter {

    private HomeFragment homeFragment;
    private OrderFragment orderFragment;
    private ShopsFragment shopsFragment;
    private OwnFragment ownFragment;

    private Context context;

    private FragmentManager fragmentManager;
    public IndexPresenterImpl(Context context, FragmentManager fragmentManager){
        this.context=context;
        this.fragmentManager=fragmentManager;


        if(homeFragment==null){
            homeFragment=new HomeFragment();
            fragmentManager.beginTransaction().add(R.id.act_index_frg_container,homeFragment).show(homeFragment).commit();
        }


    }

    @Override
    public void replaceFragment(String frgName,int type) {

        if(frgName==null || frgName.isEmpty()){
            return;
        }


        switch (frgName){
            case "HOME":
                hideAllFgIfNotNull();
                if(homeFragment==null){
                    homeFragment=new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,homeFragment).show(homeFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(homeFragment).commit();

                }
                break;

            case "ORDER":
                hideAllFgIfNotNull();
                if(orderFragment==null){
                    orderFragment=new OrderFragment();
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",type);
                    orderFragment.setArguments(bundle);
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,orderFragment).show(orderFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(orderFragment).commit();
                }

                break;

            case "STORE":
                hideAllFgIfNotNull();
                if(shopsFragment==null){
                    shopsFragment=new ShopsFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,shopsFragment).show(shopsFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(shopsFragment).commit();
                }
                break;

            case "OWN":
                hideAllFgIfNotNull();
                if(ownFragment==null){
                    ownFragment=new OwnFragment();
                    fragmentManager.beginTransaction().add(R.id.act_index_frg_container,ownFragment).show(ownFragment).commit();
                }else {
                    fragmentManager.beginTransaction().show(ownFragment).commit();
                }
                break;
        }



    }


    /**
     * 隐藏所有fragment
     */
    private void hideAllFgIfNotNull(){
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        if(homeFragment!=null){
            transaction.hide(homeFragment);
        }
        if(orderFragment!=null){
            transaction.hide(orderFragment);
        }

        if(shopsFragment!=null){
            transaction.hide(shopsFragment);
        }

        if(ownFragment!=null){
            transaction.hide(ownFragment);
        }

        //提交
        transaction.commit();
    }




}
