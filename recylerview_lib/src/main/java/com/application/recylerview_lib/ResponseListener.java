package com.application.recylerview_lib;

public interface ResponseListener {
    <F> void onSuccess(F response,boolean isSwipRefresh);
    void onFail();
}
