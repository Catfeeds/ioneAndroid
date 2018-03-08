package com.boatchina.imerit.app.features.message;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.modules.ActivityModule;
import com.boatchina.imerit.app.utils.OnLoadMoreListener;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.utils.T;
import com.boatchina.imerit.app.view.adapter.NewsAdapter;
import com.boatchina.imerit.app.view.fragment.MyBaseFragment;
import com.boatchina.imerit.data.ProgressSubscriber;
import com.boatchina.imerit.data.SubscriberOnNextListener;
import com.boatchina.imerit.data.device.DeviceRepo;
import com.boatchina.imerit.data.entity.BlankEntity;
import com.boatchina.imerit.data.entity.DataEntitiy;
import com.boatchina.imerit.data.entity.MyNews;
import com.boatchina.imerit.data.entity.ValueEntitiy;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InfoFragment extends MyBaseFragment<InfoView,InfoPresenter,InfoComponent> implements InfoView {

    @BindView(R.id.rcv)
    RecyclerView rcv;
    @Inject
    InfoPresenter infoPresenter;
    @Inject
    NewsAdapter newsAdapter;
    @Inject
    BriteDatabase instance;

    @Inject
    DeviceRepo deviceRepo;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout srLayout;
    int page=1;
    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        infoPresenter.setView(this);
        setupRecyclerview();
        srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page=1;
                newsAdapter.clearData();
                infoPresenter.getData(page);
            }
        });
        rcv.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                infoPresenter.getData(++page);
            }
        });
        infoPresenter.getData(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rcv.setAdapter(null);
    }

    private void setupRecyclerview() {
        rcv.setAdapter(newsAdapter);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onImeiItemClicked(final MyNews newsEntitiy) {
                if (newsEntitiy.getType() == 3) {
                    Cursor cursor = instance.query("select isread from Data where _id=?", newsEntitiy.get_id() + "");
                    int isread = 0;
                    while (cursor.moveToNext()) {
                        isread = cursor.getInt(cursor.getColumnIndex("isread"));
                    }
                    if (isread == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("请求");
                        builder.setMessage("是否同意对方的绑定请求");
                        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                String token = PreferencesUtils.getString(getActivity(), "Token");
                                deviceRepo.bindrsp(token, Constants.APP_TOKEN, newsEntitiy.getImei(), newsEntitiy.getUser() + "", 0 + "")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new ProgressSubscriber<>(getActivity(), new SubscriberOnNextListener<BlankEntity>() {

                                            @Override
                                            public void onNext(BlankEntity blank) {
                                                ContentValues values = new ContentValues();
                                                values.put("isread", "1");
                                                instance.update(DataEntitiy.TABLE_NAME, values, "_id=?", newsEntitiy.get_id() + "");
                                                dialog.dismiss();
                                            }
                                        }));
                            }
                        })
                                .setNeutralButton("同意", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(final DialogInterface dialog, int which) {
                                        String token = PreferencesUtils.getString(getActivity(), "Token");
                                        deviceRepo.bindrsp(token, Constants.APP_TOKEN, newsEntitiy.getImei(), newsEntitiy.getUser() + "", 1 + "")
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new ProgressSubscriber<>(getActivity(), new SubscriberOnNextListener<BlankEntity>() {

                                                    @Override
                                                    public void onNext(BlankEntity blank) {
                                                        ContentValues values = new ContentValues();
                                                        values.put("isread", "1");
                                                        instance.update(DataEntitiy.TABLE_NAME, values, "_id=?", newsEntitiy.get_id() + "");
                                                        dialog.dismiss();
                                                    }
                                                }));

                                    }
                                })
                                .show();
                    } else {

                    }

                } else {
                    ContentValues values = new ContentValues();
                    values.put("isread", "1");
                    instance.update(DataEntitiy.TABLE_NAME, values, "_id=?", newsEntitiy.get_id() + "");
                }


            }
        });
    }

    @Override
    public Context context() {
        return getActivity();
    }

    @Override
    public void showInfoList(List<MyNews> news,int page,int total) {
        if(page==1) {

        }else if(page>total) {
            T.showShort("最后一页");
            return;
        }
//        Collections.reverse(news);
        newsAdapter.setImeiList(news);
        srLayout.setRefreshing(false);
    }

    //    @OnClick(R.id.btn_delete)
//    public void onClick() {
//        BriteDatabase.Transaction transaction = instance.newTransaction();
//        instance.delete(DataEntitiy.TABLE_NAME,null);
//        instance.delete(ValueEntitiy.TABLE_NAME,null);
//        transaction.markSuccessful();
//        transaction.end();
//    }
    public void deleteAll() {
        BriteDatabase.Transaction transaction = instance.newTransaction();
        instance.delete(DataEntitiy.TABLE_NAME, null);
        instance.delete(ValueEntitiy.TABLE_NAME, null);
        transaction.markSuccessful();
        transaction.end();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        infoPresenter.onDestroy();
        instance.close();
        instance = null;
        newsAdapter = null;

    }

    @Override
    protected InfoComponent initializeDi() {
        return AndroidApplication.getBaseApplication().getApplicationComponent().infoComponent(new ActivityModule(getActivity()));
    }

    @Override
    protected void injectDependencies(InfoComponent component) {
        component.inject(this);
    }
}
