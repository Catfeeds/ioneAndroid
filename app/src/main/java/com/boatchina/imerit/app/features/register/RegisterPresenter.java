package com.boatchina.imerit.app.features.register;

import android.os.CountDownTimer;
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
 * Created by fflamingogo on 2016/7/21.
 */
@PerActivity
public class RegisterPresenter extends YaRxPresenter<RegisterView> {

    private MyCount mc;

    private UserRepo userRepo;

    @Inject
    public RegisterPresenter(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    public void codeStart() {
        mc = new MyCount(60000, 1000);
        mc.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mc != null)
            mc.cancel();
    }

    public void attemptRegister() {
//        String s = "[1][34578]\\d{9}";
        final String username = getView().getUsername();

        if(TextUtils.isEmpty(username)) {
            getView().showUsernameError(getView().context().getString(R.string.error_username_no_null));
            return;
        }
//        else if(username.length()<4) {
//            registerView.showUsernameError(registerView.context().getString(R.string.error_username_short));
//            return;
//        }else if(username.length()>20) {
//            registerView.showUsernameError(registerView.context().getString(R.string.error_username_long));
//            return;
//        }
        else if(!username.matches("[1][34578]\\d{9}")) {
            getView().showUsernameError("格式不对");
            return;
        }
        String password = getView().getPassword();
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
        String againPassword = getView().getAgainPassword();
        if(!againPassword.equals(password)) {
            getView().showAgainPasswordError(getView().context().getString(R.string.error_password_defference));
            return;
        }
        String code = getView().getCode();
        String md5Password = MD5Util.getMD5String(password);
        addUtilStop(userRepo.register(username,md5Password,code, Constants.APP_TOKEN)
                .flatMap(new Func1<TokenEntity, Observable<?>>() {
                    @Override
                    public Observable<?> call(TokenEntity tokenEntity) {
                        PreferencesUtils.putString(getView().context(),"Token",tokenEntity.getToken());
                        return userRepo.rel_jpush(tokenEntity.getToken(), JPushInterface.getRegistrationID(getView().context()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {
                    @Override
                    public void onNext(BlankEntity blankEntity) {
                        PreferencesUtils.putString(getView().context(),"username",username);
                        getView().goToMain();
                    }
                }))
        );

    }

    class MyCount extends CountDownTimer {
        /**
         * MyCount的构造方法
         *
         * @param millisInFuture    要倒计时的时间
         * @param countDownInterval 时间间隔
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 在进行倒计时的时候执行的操作
            getView().codeTurn(millisUntilFinished);
        }

        @Override
        public void onFinish() {// 倒计时结束后要做的事情
            getView().codeStop();
        }

    }


}
