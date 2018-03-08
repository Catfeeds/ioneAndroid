package com.example.base;

import android.os.Bundle;

import javax.inject.Inject;

/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */

public abstract class YaMvpDiActivity<V extends YaView, P extends YaPresenter<V>, C> extends YaMvpActivity<V, P>  {

    @Inject
    protected P mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        C component = initializeDi();
        injectDependencies(component);
        super.onCreate(savedInstanceState);
    }

    protected abstract C initializeDi();


    protected final P createPresenter() {
        return mPresenter;
    }

    protected abstract void injectDependencies(C component);
}
