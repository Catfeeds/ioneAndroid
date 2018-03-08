package com.boatchina.imerit.app.features.login;

import com.boatchina.imerit.app.view.LoadDataView;

/**
 * Created by Win10 on 2016/7/14.
 */
public interface LoginView extends LoadDataView {
     String getUsername();
    String getPassword();
    void showResult(String result);
    void goToMain();

    void showUsernameError(String message);

    void showPasswordError(String message);

    void cleanError();
}
