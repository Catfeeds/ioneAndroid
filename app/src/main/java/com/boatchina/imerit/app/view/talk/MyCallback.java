package com.boatchina.imerit.app.view.talk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/12/14.
 */

public class MyCallback<T> implements Callback<T>{
    private long id;
    private TheCall<T> theCall;
    public MyCallback(long id,TheCall<T> theCall) {
        this.id = id;
        this.theCall = theCall;
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        theCall.onResponse(call,response, id);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        theCall.onFailure(call,t,id);
    }
}
interface TheCall<T> {
    void onResponse(Call<T> call, Response<T> response, long id);
    void onFailure(Call<T> call, Throwable t,long id);
}
