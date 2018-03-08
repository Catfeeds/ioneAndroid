package com.boatchina.imerit.app.view;

/**
 * Created by fflamingogo on 2016/8/3.
 */
public interface ScanView  extends LoadDataView{
    void scanAndBind(String result);
    void showError(String message);
}
