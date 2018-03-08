package com.boatchina.imerit.data.exception;

import com.boatchina.imerit.data.MessageEvent;
import com.boatchina.imerit.data.entity.BaseEntity;

import org.greenrobot.eventbus.EventBus;


public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = -2;

    public ApiException(BaseEntity baseEntity) {
        this(getApiExceptionMessage(baseEntity));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }


    private static String getApiExceptionMessage(BaseEntity baseEntity){
        String message = "";
        switch (baseEntity.getErrcode()) {
            case USER_NOT_EXIST:
                message = "请先登录";
                EventBus.getDefault().post(new MessageEvent("x"));
                break;
            case -9:
                message = "请求认证不通过";
                break;
            case -10:
                message = "没有管理员权限";
                break;
            case -11:
                message = "设备离线，请开启设备";
                break;
            default:
                if(baseEntity.getErrmsg().equals("exceed max size")) {
                    message="超过的最大限制";
                }else {
                    message = baseEntity.getErrmsg();
                }

                break;

        }
        return message;
    }
}

