package com.boatchina.imerit.app.features.register;

import com.boatchina.imerit.app.view.LoadDataView;

/**
 * Created by fflamingogo on 2016/7/21.
 */
public interface RegisterView extends LoadDataView {
    void codeStop();


    void codeTurn(long time);
    String getUsername();
    String getPassword();
    String getAgainPassword();
    String getCode();

    void showPasswordError(String message);

    void showUsernameError(String message);

    void showAgainPasswordError(String message);

    void goToMain();
}
