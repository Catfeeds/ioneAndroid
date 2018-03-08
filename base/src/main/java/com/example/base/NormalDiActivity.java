package com.example.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Piasy{github.com/Piasy} on 17/09/2016.
 */

public abstract class NormalDiActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDi();
        super.onCreate(savedInstanceState);
    }

    protected abstract void initializeDi();
}
