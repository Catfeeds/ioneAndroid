package com.boatchina.imerit.app.view.activity.login;

import android.content.Context;

/**
 * Created by fflamingogo on 2016/7/16.
 */
public class LoginManager {
    Status status;
    private void setStatus(Status status) {
        this.status = status;
    }

    private void navigateTo(Context context) {
        status.navigateTo(context);
    }
}
