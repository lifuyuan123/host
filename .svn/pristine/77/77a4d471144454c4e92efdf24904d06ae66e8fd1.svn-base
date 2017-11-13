package com.sctjsj.mayk.wowallethost.util;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.mayk.wowallethost.model.javabean.UserBean;
/**
 * Created by mayikang on 17/5/31.
 */

public class UserAuthUtil {

    private HttpServiceImpl http;

    public UserAuthUtil(){
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    /**
     * 判断用户是否已经登录
     * @return
     */
    public static boolean isUserLogin(){
        //1.没有 token
        String token= SPFUtil.get(Tag.TAG_TOKEN,"none").toString();

        if("none".equals(token)||StringUtil.isBlank(token)){
            return false;
        }

        //2.没有用户信息
        String data= SPFUtil.get(Tag.TAG_USER,"null").toString();

        if(StringUtil.isBlank(data)|| "null".equals(data)){
            return false;
        }

        UserBean ub=new Gson().fromJson(data,UserBean.class);

        if(ub==null){
            return false;
        }

        if(0==ub.getId()){
            return false;
        }

        if(ub.getStoreBean()==null){
            return false;
        }

        return true;
    }

    /**
     * 获取用户 id
     * @return
     */
    public static int getUserId(){
        //2.没有用户信息
        String data= SPFUtil.get(Tag.TAG_USER,"null").toString();

        if(StringUtil.isBlank(data)|| "null".equals(data)){
            return -1;
        }

        UserBean ub=new Gson().fromJson(data,UserBean.class);

        return ub.getId();
    }


    /**
     * 获取店铺 id
     * @return
     */
    public static int getStoreId(){
        String data= SPFUtil.get(Tag.TAG_USER,"null").toString();

        if(StringUtil.isBlank(data)|| "null".equals(data)){
            return -1;
        }

        UserBean ub=new Gson().fromJson(data,UserBean.class);

        return ub.getStoreBean().getId();

    }

    /**
     * 保存用户信息
     * @param ub
     */
    public static void saveUserBean(UserBean ub){
        if(ub==null){
            return;
        }

        SPFUtil.put(Tag.TAG_USER,new Gson().toJson(ub));


    }

    public static UserBean getCurrentUser(){
        String data= SPFUtil.get(Tag.TAG_USER,"null").toString();
        UserBean ub=new Gson().fromJson(data,UserBean.class);
        return ub;
    }

}
