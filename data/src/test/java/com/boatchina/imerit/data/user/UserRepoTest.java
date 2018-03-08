package com.boatchina.imerit.data.user;

import com.boatchina.imerit.data.entity.BlankEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willReturn;

/**
 * Created by Administrator on 2017/3/3.
 */
public class UserRepoTest {
    @Mock
    private UserApi userApi;
    private UserRepo userRepo;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        userRepo = new UserRepo(userApi);
    }



    @Test
    public void login() throws Exception {
        willReturn(Observable.just(new TokenEntity()))
                .given(userApi)
                .login(anyString(),anyString(),anyString());

        TestSubscriber subscriber = new TestSubscriber<>();
        userRepo.login(anyString(),anyString(),anyString()).subscribe(subscriber);
        subscriber.awaitTerminalEvent();
    }

    @Test
    public void register() throws Exception {

    }

    @Test
    public void rel_jpush() throws Exception {

    }
    @Test
    public void loginWithJpush() throws Exception {
        willReturn(Observable.just(new TokenEntity()))
                .given(userApi)
                .login(anyString(),anyString(),anyString());
        willReturn(Observable.just(new BlankEntity()))
                .given(userApi)
        .rel_jpush(anyString(),anyString());
        TestSubscriber subscriber = new TestSubscriber<>();
//        userRepo.loginWithJpush(anyString(),anyString(),anyString()).subscribe(subscriber);
        subscriber.awaitTerminalEvent();
    }

}