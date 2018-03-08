package com.boatchina.imerit.app.features.message;

import com.boatchina.imerit.app.view.LoadDataView;
import com.boatchina.imerit.data.entity.MyNews;

import java.util.List;

/**
 * Created by fflamingogo on 2016/9/27.
 */

public interface InfoView extends LoadDataView {
    void showInfoList(List<MyNews> news,int page,int total);

}
