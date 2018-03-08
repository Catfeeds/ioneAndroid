package com.boatchina.imerit.app.features.message;

import android.database.Cursor;

import com.boatchina.imerit.app.Constants;
import com.boatchina.imerit.data.entity.DataEntitiy;
import com.boatchina.imerit.data.entity.MyNews;
import com.boatchina.imerit.data.entity.ValueEntitiy;
import com.example.base.YaRxPresenter;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by fflamingogo on 2016/9/27.
 */

public class InfoPresenter extends YaRxPresenter<InfoView> {
    private InfoView infoView;
    @Inject
    BriteDatabase instance;
    Subscription subscribe;
    @Inject
    InfoPresenter() {
    }


    public void getData(final int page) {
        List<String> list = new ArrayList<>();
        list.add(DataEntitiy.TABLE_NAME);
        list.add(ValueEntitiy.TABLE_NAME);
        QueryObservable query1 = instance.createQuery(list, "SELECT d.*,t.fine,t.status,t.power,t.user,t.voice,t.msg,t.result from Data d LEFT JOIN (" +
                "SELECT v.key_id," +
                "MAX(CASE v.key WHEN 'fine' THEN value ELSE '' END) as fine," +
                "MAX(CASE v.key WHEN 'status' THEN value ELSE '' END) as status," +
                "MAX(CASE v.key WHEN 'power' THEN value ELSE '' END) as power, " +
                "MAX(CASE v.key WHEN 'user' THEN value ELSE '' END) as user, " +
                "MAX(CASE v.key WHEN 'msg' THEN value ELSE '' END) as msg, " +
                "MAX(CASE v.key WHEN 'result' THEN value ELSE '' END) as result, " +
                "MAX(CASE v.key WHEN 'voice' THEN value ELSE '' END) as voice " +
                "from Value v GROUP BY v.key_id) t ON t.key_id = d._id Order by d.create_at desc limit ?,?;", String.valueOf((page-1)* Constants.PAGENUM), String.valueOf(page*Constants.PAGENUM));

        Observable<List<MyNews>> listObservable = query1.mapToList(new Func1<Cursor, MyNews>() {
            @Override
            public MyNews call(Cursor cursor) {
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                String imei = cursor.getString(cursor.getColumnIndex("imei"));
                long create_at = cursor.getLong(cursor.getColumnIndex("create_at"));
                String fine = cursor.getString(cursor.getColumnIndex("fine"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                String result = cursor.getString(cursor.getColumnIndex("result"));
                String power = cursor.getString(cursor.getColumnIndex("power"));
                String user = cursor.getString(cursor.getColumnIndex("user"));
                String msg = cursor.getString(cursor.getColumnIndex("msg"));
                String voice = cursor.getString(cursor.getColumnIndex("voice"));
                int isread = cursor.getInt(cursor.getColumnIndex("isread"));
                int _id = cursor.getInt(cursor.getColumnIndex("_id"));
                return new MyNews(_id,imei,type,fine,status,power,isread==1,create_at,user,result,voice,msg);
            }
        });

        subscribe = listObservable.subscribe(new Subscriber<List<MyNews>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<MyNews> newsEntitiys) {
                QueryObservable query = instance.createQuery(DataEntitiy.TABLE_NAME, "select count(*) from Data");
                Observable<Integer> integerObservable = query.mapToOne(new Func1<Cursor, Integer>() {
                    @Override
                    public Integer call(Cursor cursor) {
                        return cursor.getInt(cursor.getColumnIndex("count(*)"));
                    }
                });
                integerObservable.subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        int total = Math.round((integer + Constants.PAGENUM-1) / Constants.PAGENUM);
                        infoView.showInfoList(newsEntitiys,page,total);
                    }
                });


            }
        });


    }


    public void setView(InfoView infoView) {
        this.infoView = infoView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        instance.close();
        subscribe.unsubscribe();
    }
}
