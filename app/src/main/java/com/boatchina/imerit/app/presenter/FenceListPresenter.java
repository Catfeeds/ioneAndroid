package com.boatchina.imerit.app.presenter;

import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.FendListView;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.fence.FenceEntity;
import com.boatchina.imerit.data.fence.FenceRepo;
import com.example.base.YaRxPresenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fflamingogo on 2016/9/18.
 */
public class FenceListPresenter extends YaRxPresenter<FendListView> {

    private FenceRepo fenceRepo;
    @Inject
    FenceListPresenter(FenceRepo fenceRepo) {
        this.fenceRepo = fenceRepo;
    }


    public void loadFendList() {
        String token = PreferencesUtils.getString(getView().context(), "Token");
        String imei = PreferencesUtils.getString(getView().context(), "imei");
        addUtilDestroy(fenceRepo.getFineList(token==null?"":token,imei==null?"":imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<List<FenceEntity>>(getView().context(), new SubscriberOnNextListener<List<FenceEntity>>() {


                    @Override
                    public void onNext(List<FenceEntity> fenceEntities) {
                        getView().showFendList(fenceEntities);
                    }
                })))
        ;
    }

    public void deleteFence(String token,String id) {
        addUtilDestroy(fenceRepo.delFine(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<>(getView().context(), new SubscriberOnNextListener<BlankEntity>() {
                    @Override
                    public void onNext(BlankEntity blank) {
                        T.showShort("删除成功");
                    }
                })));

    }
}
