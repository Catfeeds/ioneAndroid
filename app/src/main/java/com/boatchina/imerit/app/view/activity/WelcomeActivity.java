package com.boatchina.imerit.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.features.login.LoginActivity;
import com.boatchina.imerit.app.features.register.RegisterActivity;
import com.boatchina.imerit.app.utils.PreferencesUtils;

public class WelcomeActivity extends NormalActivity {

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String token = PreferencesUtils.getString(WelcomeActivity.this, "Token");
                if(token==null) {
                    Intent intent = new Intent(WelcomeActivity.this, TabActivity.class);
                    Intent intent2 = new Intent(WelcomeActivity.this, RegisterActivity.class);
                    Intent[] intents= new Intent[]{intent,intent2};
                    startActivities(intents);
                }else if(token.equals("")){
                    Intent intent = new Intent(WelcomeActivity.this, TabActivity.class);
                    Intent intent2 = new Intent(WelcomeActivity.this, LoginActivity.class);
                    Intent[] intents= new Intent[]{intent,intent2};
                    startActivities(intents);
                }else {
                    Intent intent = new Intent(WelcomeActivity.this, TabActivity.class);
                    startActivity(intent);
                }

                finish();
            }
        },2000);

    }

    @Override
    protected void initializeDi() {

    }



}
