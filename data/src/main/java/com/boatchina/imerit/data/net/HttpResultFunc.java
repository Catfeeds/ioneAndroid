package com.boatchina.imerit.data.net;

import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.exception.ApiException;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;

import rx.functions.Func1;

/**
 * Created by fflamingogo on 2016/8/22.
 */
@RxLogSubscriber
public class HttpResultFunc<T> implements Func1<BaseEntity<T>, T> {

    @Override
    public T call(BaseEntity<T> baseEntity) {
        if (baseEntity.getErrcode() == 0) {
            if(baseEntity.getData()==null) {
                    return null;
            }else {
                return baseEntity.getData();
            }

        }else{
            throw new ApiException(baseEntity);
        }

    }


}
