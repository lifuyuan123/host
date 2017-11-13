package com.sctjsj.mayk.wowallethost.ui.intf;

import com.sctjsj.mayk.wowallethost.model.javabean.ProjectClassifyBean;

import java.util.List;

/**
 * Created by mayikang on 17/5/18.
 */

public interface IProjectClassifyView {

    //查询分类
    void pullClassify(int pageSize,int pageIndex);
    //单个删除
    void deleteClassifyById(String id);
    //批量删除
    void deleteClassifyList(List<String> idList);
    //更新分类信息
    void updateClassify(ProjectClassifyBean classifyBean);
}
