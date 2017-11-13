package com.sctjsj.mayk.wowallethost.callback;

/**
 * Created by mayikang on 17/5/18.
 */

public interface PresenterCallback {
    void onStart();

    void onSuccess(String res);


    void onError(String err);


    void onFinish();
}
