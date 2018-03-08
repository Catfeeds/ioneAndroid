package com.example.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */

public abstract class YaMvpFragment<V extends YaView, P extends YaPresenter<V>> extends Fragment
        implements YaView {

    private YaViewDelegate<V, P> mYaViewDelegate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mYaViewDelegate = new YaViewDelegate<V, P>() {
            @Override
            protected P createPresenter() {
                return YaMvpFragment.this.createPresenter();
            }
        };
        mYaViewDelegate.onCreate(savedInstanceState);
        mYaViewDelegate.onStart((V) this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        mYaViewDelegate.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mYaViewDelegate.onDestroy();
    }

    protected abstract P createPresenter();
}
