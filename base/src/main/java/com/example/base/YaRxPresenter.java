package com.example.base;

import rx.Subscription;

/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */

public abstract class YaRxPresenter<V extends YaView> extends YaPresenter<V> {

    private final YaRxDelegate mYaRxDelegate;

    protected YaRxPresenter() {
        mYaRxDelegate = new YaRxDelegate();
        mYaRxDelegate.onCreate();
    }

    protected boolean addUtilStop(Subscription disposable) {
        return mYaRxDelegate.addUtilStop(disposable);
    }

    public boolean addUtilDestroy(Subscription disposable) {
        return mYaRxDelegate.addUtilDestroy(disposable);
    }

    public void remove(Subscription disposable) {
        mYaRxDelegate.remove(disposable);
    }

    @Override
    public void attachView(V view) {
        super.attachView(view);
        mYaRxDelegate.onStart();
    }

    @Override
    public void detachView() {
        super.detachView();
        mYaRxDelegate.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mYaRxDelegate.onDestroy();
    }
}
