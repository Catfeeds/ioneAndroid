package com.example.base;


import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */


public class YaRxDelegate {
    private CompositeSubscription  mDisposables2Stop;
    private CompositeSubscription mDisposables2Destroy;

    public synchronized boolean addUtilStop(Subscription subscription) {
        if (mDisposables2Stop == null) {
            throw new IllegalStateException(
                    "addUtilStop should be called between onStart and onStop");
        }
        mDisposables2Stop.add(subscription);
        return true;
    }

    public synchronized boolean addUtilDestroy(Subscription disposable) {
        if (mDisposables2Destroy == null) {
            throw new IllegalStateException(
                    "addUtilDestroy should be called between onCreate and onDestroy");
        }
        mDisposables2Destroy.add(disposable);
        return true;
    }

    public synchronized void remove(Subscription disposable) {
        if (mDisposables2Stop == null && mDisposables2Destroy == null) {
            throw new IllegalStateException("remove should not be called after onDestroy");
        }
        if (mDisposables2Stop != null) {
            mDisposables2Stop.remove(disposable);
        }
        if (mDisposables2Destroy != null) {
            mDisposables2Destroy.remove(disposable);
        }
    }

    public synchronized void onCreate() {
        if (mDisposables2Destroy != null) {
            throw new IllegalStateException("onCreate called multiple times");
        }
        mDisposables2Destroy = new CompositeSubscription();
    }

    public synchronized void onStart() {
        if (mDisposables2Stop != null) {
            throw new IllegalStateException("onStart called multiple times");
        }
        mDisposables2Stop = new CompositeSubscription();
    }

    public synchronized void onStop() {
        if (mDisposables2Stop == null) {
            throw new IllegalStateException("onStop called multiple times or onStart not called");
        }
        mDisposables2Stop.unsubscribe();
        mDisposables2Stop = null;
    }

    public synchronized void onDestroy() {
        if (mDisposables2Destroy == null) {
            throw new IllegalStateException(
                    "onDestroy called multiple times or onCreate not called");
        }
        mDisposables2Destroy.unsubscribe();
        mDisposables2Destroy = null;
    }
}
