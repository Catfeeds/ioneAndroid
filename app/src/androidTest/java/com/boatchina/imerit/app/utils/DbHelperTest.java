package com.boatchina.imerit.app.utils;

import com.boatchina.imerit.data.entity.DataEntitiy;
import com.boatchina.imerit.data.entity.ValueEntitiy;
import com.squareup.sqlbrite.BriteDatabase;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2017/2/27.
 */
public class DbHelperTest {

    String string4 = "{\"imei\":\"\",\"status\":1,\"type\":4,\"time\":1488185388}";
    String string2 = "{\"imei\":\"\",\"type\":2,\"power\":19,\"time\":1488185387}";
    String string3 = "{\"imei\":\"\",\"msg\":\"测试\",\"type\":3,\"user\":3623,\"time\":1488185387}";
    String string1 = "{\"imei\":\"\",\"status\":1,\"time\":1488185387,\"fine\":171,\"type\":1}";
   private  String string5 = "{\"url\":\"http:\\/\\/120.76.209.79:8000\\/zlt\\/upload\\/f953474d-124a-44d6-8f66-133ff976a71d.amr\",\"imei\":\"\",\"time\":1488185388,\"duration\":10,\"voice\":1,\"type\":5}";
    private BriteDatabase instance;
    @Before
   public void setUp() throws Exception {

//        instance = new ApplicationModule((AndroidApplication) InstrumentationRegistry.getContext()).provideBriteDb(new DbOpenHelper(InstrumentationRegistry.getContext()));
    }

    @Test
    public void dealtype5() throws Exception {
        JSONObject json = new JSONObject(string5);
        final String imei = json.getString("imei");
        int voice = json.getInt("voice");
        Assert.assertEquals(voice,77);
        final double time = json.getDouble("time");
        final int duration = json.getInt("duration");
        final String url = json.getString("url");
        final DataEntitiy dataEntitiy = DataEntitiy.builder()
                .imei(imei)
                .type(5)
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

    }

    @Test
    public void dealtype4() throws Exception {

    }

    @Test
    public void dealtype3() throws Exception {

    }

    @Test
    public void dealtype2() throws Exception {

    }

    @Test
    public void dealtype1() throws Exception {

    }

}