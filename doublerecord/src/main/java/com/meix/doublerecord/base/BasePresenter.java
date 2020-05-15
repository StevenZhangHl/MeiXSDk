package com.meix.doublerecord.base;

import android.content.Context;

import com.meix.doublerecord.rx.RxManager;

/**
 * @author zhl
 * @date 2019/4/30
 */
public abstract class BasePresenter<T extends BaseView,E extends BaseModel>{
    public Context mContext;
    public E mModel;
    public T mView;
    public RxManager mRxManager = new RxManager();

    public void setVM(T v, E m) {
        this.mView = v;
        this.mModel = m;
        this.onStart();
    }
    public void onStart(){
    }
    public void onDestroy() {
        mRxManager.clear();
    }
}
