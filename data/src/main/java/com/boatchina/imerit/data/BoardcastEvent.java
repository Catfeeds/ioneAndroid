package com.boatchina.imerit.data;

/**
 * Created by fflamingogo on 2016/7/22.
 */
public class BoardcastEvent {
    public final String message;

    public BoardcastEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
