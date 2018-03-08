package com.boatchina.imerit.data;

import android.content.Context;

/**
 * Created by fflamingogo on 2016/11/25.
 */

public class ErrorProgressSubscriber<T> extends ProgressSubscriber<T> {
    private MySubscriberOnNextListener mSubscriberOnNextListener;
    public ErrorProgressSubscriber(Context context, MySubscriberOnNextListener mSubscriberOnNextListener) {
        super(context, mSubscriberOnNextListener);
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
    }

    @Override
    public void onError(Throwable e) {
//        if(e instanceof ApiException) {
//            e.getMessage();
            mSubscriberOnNextListener.onError(e);
//        }
        if(e.getMessage().equals("authorization require")) {
            super.onError(e);
        }else {
            dismissProgressDialog();
        }

    }
}
