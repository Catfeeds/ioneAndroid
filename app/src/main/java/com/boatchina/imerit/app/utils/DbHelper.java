package com.boatchina.imerit.app.utils;

import android.os.Environment;

import com.boatchina.imerit.data.entity.DataEntitiy;
import com.boatchina.imerit.data.entity.TalkEntitiy;
import com.boatchina.imerit.data.entity.ValueEntitiy;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.sqlbrite.BriteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/2/27.
 */

class DbHelper {
    @Inject
    BriteDatabase instance;
    void dealtype(JSONObject json) {
        try {

            int type = json.getInt("type");



            if(type==1) {
                dealtype1(json, type);

            }else if(type==2) {
                dealtype2(json, type);


            }
            else if(type==3) {
                dealtype3(json, type);
            }else if(type==4) {
                dealtype4(json, type);
            }else if(type==5) {
                dealtype5(json, type);
            }


        } catch (JSONException e) {

        }

    }

     void dealtype5(JSONObject json, int type) throws JSONException {
        final String imei = json.getString("imei");
        int voice = json.getInt("voice");
        final double time = json.getDouble("time");
        final int duration = json.getInt("duration");
        final String url = json.getString("url");
//						BriteDatabase instance = BriteUtils.getInstance();
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(type)
                .isread(false)
                .create_at((long)time)
                .build();
        BriteDatabase.Transaction transaction = instance.newTransaction();
        try {
            long key_id =  instance.insert(DataEntitiy.TABLE_NAME, DataEntitiy.FACTORY.marshal(dataEntitiy).asContentValues());
            ValueEntitiy voiceValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("voice")
                    .value(voice+"")
                    .build();

            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(voiceValue).asContentValues());
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }


        String mDir = Environment.getExternalStorageDirectory() + "/ldm_voice";
        File dir = new File(mDir);
        if (!dir.exists()) {
            dir.mkdirs();//文件不存在，则创建文件
        }
        String fileName = UUID.randomUUID().toString() + ".amr";
        File file = new File(dir, fileName);
        FileDownloader.getImpl().create(url)
                .setPath(file.getAbsolutePath())
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
//										BriteDatabase instance = BriteUtils.getInstance();
                        TalkEntitiy talkEntitiy = TalkEntitiy.builder()
                                .create_at((long) time)
                                .duration(duration)
                                .filepath(url)
                                .imei(imei)
                                .name("")
                                .myimei("")
                                .status(false)
                                .myname("")
                                . success(true)
                                .build();
                        instance.insert(TalkEntitiy.TABLE_NAME, TalkEntitiy.FACTORY.marshal(talkEntitiy).asContentValues());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();
    }

     void dealtype4(JSONObject json, int type) throws JSONException {
        String imei = json.getString("imei");
        int result = json.getInt("result");
        double time = json.getDouble("time");
//						BriteDatabase instance = BriteUtils.getInstance();
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(type)
                .isread(false)
                .create_at((long)time)
                .build();
        BriteDatabase.Transaction transaction = instance.newTransaction();
        try {
            long key_id = instance.insert(DataEntitiy.TABLE_NAME, DataEntitiy.FACTORY.marshal(dataEntitiy).asContentValues());
            ValueEntitiy statusValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("result")
                    .value(result+"")
                    .build();

            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(statusValue).asContentValues());
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }
    }

     void dealtype3(JSONObject json, int type) throws JSONException {
        String imei = json.getString("imei");
        int user = json.getInt("user");
        String msg = json.getString("msg");
        double time = json.getDouble("time");
//						BriteDatabase instance = BriteUtils.getInstance();
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(type)
                .isread(false)
                .create_at((long)time)
                .build();
        BriteDatabase.Transaction transaction = instance.newTransaction();
        try {
            long key_id = instance.insert(DataEntitiy.TABLE_NAME, DataEntitiy.FACTORY.marshal(dataEntitiy).asContentValues());
            ValueEntitiy userValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("user")
                    .value(user+"")
                    .build();
            ValueEntitiy msgValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("msg")
                    .value(msg+"")
                    .build();


            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(userValue).asContentValues());
            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(msgValue).asContentValues());
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }
    }

     void dealtype2(JSONObject json, int type) throws JSONException {
        String imei = json.getString("imei");
        int power = json.getInt("power");
        double time = json.getDouble("time");
//						BriteDatabase instance = BriteUtils.getInstance();
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(type)
                .isread(false)
                .create_at((long)time)
                .build();
        BriteDatabase.Transaction transaction = instance.newTransaction();
        try {
            long key_id = instance.insert(DataEntitiy.TABLE_NAME, DataEntitiy.FACTORY.marshal(dataEntitiy).asContentValues());
            ValueEntitiy powerValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("power")
                    .value(power+"")
                    .build();

            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(powerValue).asContentValues());
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }
    }

    void dealtype1(JSONObject json, int type) throws JSONException {
        String imei = json.getString("imei");
        int status = json.getInt("status");
        int fine = json.getInt("fine");
        double time = json.getDouble("time");
//						BriteDatabase instance = BriteUtils.getInstance();
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(type)
                .isread(false)
                .create_at((long)time)
                .build();
        BriteDatabase.Transaction transaction = instance.newTransaction();
        try {
            long key_id = instance.insert(DataEntitiy.TABLE_NAME, DataEntitiy.FACTORY.marshal(dataEntitiy).asContentValues());
            ValueEntitiy fineValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("fine")
                    .value(fine+"")
                    .build();
            ValueEntitiy statusValue = ValueEntitiy.builder()
                    .key_id(key_id)
                    .key("status")
                    .value(status+"")
                    .build();
            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(fineValue).asContentValues());
            instance.insert(ValueEntitiy.TABLE_NAME, ValueEntitiy.FACTORY.marshal(statusValue).asContentValues());
            transaction.markSuccessful();
        }finally {
            transaction.end();
        }
    }
}
