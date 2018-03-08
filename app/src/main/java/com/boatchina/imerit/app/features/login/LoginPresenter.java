package com.boatchina.imerit.app.features.login;

import android.text.TextUtils;

import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.utils.MD5Util;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.user.TokenEntity;
import com.boatchina.imerit.data.user.UserRepo;
import com.example.base.YaRxPresenter;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by flamingo on 2016/7/14.
 */
@PerActivity
public class LoginPresenter extends YaRxPresenter<LoginView> {


    private UserRepo userRepo;

    @Inject
    LoginPresenter(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public void attemptLogin() {
        getView().cleanError();

        final String username = getView().getUsername();
        if(TextUtils.isEmpty(username)) {
            getView().showUsernameError(getView().context().getString(R.string.error_username_no_null));
            return;
        }else if(!username.matches("[1][34578]\\d{9}")) {
            getView().showUsernameError("格式不对");
            return;
        }
        String password =getView().getPassword();
        if(TextUtils.isEmpty(password)) {
            getView().showPasswordError(getView().context().getString(R.string.error_password_no_null));
            return;
        }else if(password.length()<4) {
            getView().showPasswordError(getView().context().getString(R.string.error_password_short));
            return;
        }else if(password.length()>20) {
            getView().showPasswordError(getView().context().getString(R.string.error_password_long));
            return;
        }
        String md5Password = MD5Util.getMD5String(password);
        addUtilStop(
        userRepo.login(Constants.APP_TOKEN,username,md5Password)
                .flatMap(new Func1<TokenEntity, Observable<BlankEntity>>() {
                    @Override
                    public Observable<BlankEntity> call(TokenEntity tokenEntity) {
                        PreferencesUtils.putString(getView().context(),"Token",tokenEntity.getToken());
                        return userRepo.rel_jpush(tokenEntity.getToken(), JPushInterface.getRegistrationID(getView().context()));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {

                    @Override
                    public void onNext(BlankEntity blankEntity) {
                        getView().showResult(getView().context().getString(R.string.success_login)) ;
                        PreferencesUtils.putString(getView().context(),"username",username);
                        getView().goToMain();
                    }
                }))
        );


    }


}
