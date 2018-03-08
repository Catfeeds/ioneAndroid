package com.boatchina.imerit.app.features.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.view.activity.BaseActivity;
import com.boatchina.imerit.app.features.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity<LoginView,LoginPresenter,UserComponent> implements LoginView {


    @BindView(R.id.actv_login_username)
    AutoCompleteTextView actvLoginUsername;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;


    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_login);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);




    }






    @Override
    public Context context() {
        return this;
    }


    @Override
    public String getUsername() {
        return actvLoginUsername.getText().toString();
    }

    @Override
    public String getPassword() {
        return etLoginPassword.getText().toString();
    }

    @Override
    public void showResult(String result) {
        Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToMain() {
        finish();
    }

    @Override
    public void showUsernameError(String message) {
        actvLoginUsername.setError(message);
        actvLoginUsername.requestFocus();
    }

    @Override
    public void showPasswordError(String message) {
        etLoginPassword.setError(message);
        actvLoginUsername.requestFocus();
    }

    @Override
    public void cleanError() {
        actvLoginUsername.setError(null);
        etLoginPassword.setError(null);
    }

    @OnClick({R.id.btn_login,R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.tv_register:
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                mPresenter.attemptLogin();
                break;
        }
    }
    @Override
    protected UserComponent initializeDi() {
        return AndroidApplication.getBaseApplication().getApplicationComponent().userComponent(getActivityModule());
//        userComponent.inject(this);
    }

    @Override
    protected void injectDependencies(UserComponent component) {
        component.inject(this);
    }

}

