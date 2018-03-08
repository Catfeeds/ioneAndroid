package com.boatchina.imerit.app.view.activity.login;

import android.content.Context;
import android.content.Intent;

import com.boatchina.imerit.app.features.login.LoginActivity;

/**
 * Created by fflamingogo on 2016/7/16.
 */
public class LoginInStatus implements Status {


    @Override
    public void navigateTo(Context context) {
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
