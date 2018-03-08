package com.boatchina.imerit.app.features.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.features.login.UserComponent;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.activity.BaseActivity;
import com.boatchina.imerit.app.view.activity.TabActivity;
import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.net.HttpMethods;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RegisterActivity extends BaseActivity<RegisterView,RegisterPresenter,UserComponent> implements RegisterView {

    @BindView(R.id.actv_register_username)
    AutoCompleteTextView actvRegisterUsername;
    @BindView(R.id.et_register_password)
    EditText etRegisterPassword;
    @BindView(R.id.et_register_again_password)
    EditText etRegisterAgainPassword;
    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.btn_code)
    Button btnCode;
    @BindView(R.id.btn_register)
    Button btnRegister;



    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_register);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

    }

    @Override
    protected UserComponent initializeDi() {
        return AndroidApplication.getBaseApplication().getApplicationComponent().userComponent(getActivityModule());

    }

    @Override
    protected void injectDependencies(UserComponent component) {
        component.inject(this);
    }


    @OnClick({R.id.btn_code, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_code:
                editCode.requestFocus();
                btnCode.setClickable(false);
                mPresenter.codeStart();
                HttpMethods.getInstance().getService().getverifycode(actvRegisterUsername.getText().toString().trim(), Constants.APP_TOKEN)
                        .map(new HttpResultFunc<BlankEntity>())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Subscriber<BlankEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(BlankEntity blankEntity) {
                                T.showShort("发送成功");
                            }
                        });
                break;
            case R.id.btn_register:
                mPresenter.attemptRegister();
                break;
        }
    }


    @Override
    public void codeStop() {
        btnCode.setText(R.string.desc_get_code);
        btnCode.setClickable(true);
    }

    @Override
    public void codeTurn(long time) {
        long second = time / 1000;
        btnCode.setText(second + getString(R.string.desc_second));
        if (second == 60) {
            btnCode.setText(59 + getString(R.string.desc_second));
        }
    }

    @Override
    public String getUsername() {
        return actvRegisterUsername.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etRegisterPassword.getText().toString().trim();
    }

    @Override
    public String getAgainPassword() {
        return etRegisterAgainPassword.getText().toString().trim();
    }

    @Override
    public String getCode() {
        return editCode.getText().toString().trim();
    }

    @Override
    public void showPasswordError(String message) {
        etRegisterPassword.setError(message);
        etRegisterPassword.requestFocus();
    }

    @Override
    public void showUsernameError(String message) {
        actvRegisterUsername.setError(message);
        actvRegisterUsername.requestFocus();
    }

    @Override
    public void showAgainPasswordError(String message) {
        etRegisterAgainPassword.setError(message);
        etRegisterAgainPassword.requestFocus();
    }

    @Override
    public void goToMain() {
        Intent intent= new Intent(RegisterActivity.this,TabActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public Context context() {
        return this;
    }


}
