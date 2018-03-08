package com.boatchina.imerit.app.view;

import com.boatchina.imerit.data.fence.FenceEntity;

import java.util.List;

/**
 * Created by fflamingogo on 2016/8/11.
 */
public interface FendListView extends LoadDataView {

    void showFendList(List<FenceEntity> fendList);
}
