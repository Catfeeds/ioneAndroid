package com.boatchina.imerit.data.user;

import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.data.PerActivity;
import com.boatchina.imerit.data.entity.BlankEntity;

import javax.inject.Inject;

import rx.Observable;



/**
 * Created by Administrator on 2017/2/21.
 */
@PerActivity
public class UserRepo {
    private UserApi userApi;
    @Inject
    UserRepo(UserApi userApi) {
        this.userApi = userApi;
    }



    public Observable<TokenEntity> login(String token, String username, String password) {
        return userApi.login(token,username, password)
                .map(new HttpResultFunc<>());
    }

    public Observable<TokenEntity> register(String username, String password, String code, String token) {
        return userApi.register(username, password, code, token)
                .map(new HttpResultFunc<>());
    }
    public Observable<BlankEntity> rel_jpush(String token, String jpushid) {
        return userApi.rel_jpush(token, jpushid)
                .map(new HttpResultFunc<>());
    }
//    public Observable<?> loginWithJpush(String token, String username, String password) {
//        return userApi.login(token,username, password)
//                .map(new HttpResultFunc<>())
//                .flatMap(new Func1<TokenEntity, Observable<?>>() {
//                    @Override
//                    public Observable<?> call(TokenEntity tokenEntity) {
//                        return userApi.rel_jpush(tokenEntity.getToken(), "")
//                                .map(new HttpResultFunc<>());
//                    }
//                });
//
//    }
}
