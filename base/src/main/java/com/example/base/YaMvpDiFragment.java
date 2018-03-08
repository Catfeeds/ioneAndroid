package com.example.base;

import android.os.Bundle;

import javax.inject.Inject;


/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */

public abstract class YaMvpDiFragment<V extends YaView, P extends YaPresenter<V>, C>
        extends YaMvpFragment<V, P> {

    @Inject
    protected P mPresenter;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        C component = initializeDi();
        injectDependencies(component);
        super.onCreate(savedInstanceState);
    }
    protected abstract C initializeDi();
    @Override
    protected final P createPresenter() {
        return mPresenter;
    }

    /**
     * inject dependencies.
     * Normally implementation should be {@code component.inject(this)}
     */
    protected abstract void injectDependencies(C component);
}
