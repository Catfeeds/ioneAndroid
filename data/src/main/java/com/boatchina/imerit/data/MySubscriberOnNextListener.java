package com.boatchina.imerit.data;

/**
 * Created by liukun on 16/3/10.
 */
public interface MySubscriberOnNextListener<T> extends SubscriberOnNextListener<T>{
    void onError(Throwable e);
}
