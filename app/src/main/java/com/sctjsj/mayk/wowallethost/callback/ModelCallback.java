package com.sctjsj.mayk.wowallethost.callback;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mayikang on 17/5/18.
 */

public interface ModelCallback {

    void onStart();

    void onSuccess(String res);


    void onError(String err);


    void onFinish();

}
