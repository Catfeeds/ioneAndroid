package com.boatchina.imerit.app.view.talk;

import android.Manifest;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.R;
import com.boatchina.imerit.app.di.components.TalkComponent;
import com.boatchina.imerit.data.net.HttpMethods;
import com.boatchina.imerit.app.utils.PreferencesUtils;
import com.boatchina.imerit.app.view.activity.NormalActivity;
import com.boatchina.imerit.app.view.custom.ImeiSelectView;
import com.boatchina.imerit.data.net.HttpResultFunc;
import com.boatchina.imerit.data.entity.BaseEntity;
import com.boatchina.imerit.data.entity.TalkEntitiy;
import com.boatchina.imerit.data.entity.VoiceEntity;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.QueryObservable;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;




public class TalkActivity extends NormalActivity {
    @BindView(R.id.imeiselectview)
    ImeiSelectView imeiselectview;
    private ListView mListView;
    private RecoderAdapter mAdapter;
    private AudioRecorderButton mAudioRecorderButton;
    private View animView;
    private View animView1;
    @Inject
    BriteDatabase instance;
    Subscription subscribe;
    Subscription subscribe1;
    @Override
    protected String getToolbarTitle() {
        return "语音对讲";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);

        mListView = (ListView) findViewById(R.id.id_listview);
        mAudioRecorderButton = (AudioRecorderButton) findViewById(R.id.id_recorder_button);
        mAdapter = new RecoderAdapter(TalkActivity.this);
        mListView.setAdapter(mAdapter);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        String imei = PreferencesUtils.getString(TalkActivity.this, "imei");
        QueryObservable query = instance.createQuery(TalkEntitiy.TABLE_NAME, TalkEntitiy.GET_THE_MEI,imei);
        Observable<List<TalkEntitiy>> listObservable = query.mapToList(new Func1<Cursor, TalkEntitiy>() {
            @Override
            public TalkEntitiy call(Cursor cursor) {
                return TalkEntitiy.MAPPER.map(cursor);
            }
        });

        subscribe = listObservable.subscribe(new Subscriber<List<TalkEntitiy>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<TalkEntitiy> talkEntitiys) {
                mAdapter.setList(talkEntitiys);
                mListView.setSelection(mAdapter.getTalkList().size() - 1);

            }
        });

        imeiselectview.setOnSelect(new ImeiSelectView.OnSelect() {
            @Override
            public void select(String item) {
                QueryObservable query = instance.createQuery(TalkEntitiy.TABLE_NAME, TalkEntitiy.GET_THE_MEI,item);
                Observable<List<TalkEntitiy>> listObservable = query.mapToList(new Func1<Cursor, TalkEntitiy>() {
                    @Override
                    public TalkEntitiy call(Cursor cursor) {
                        return TalkEntitiy.MAPPER.map(cursor);
                    }
                });

                subscribe1 = listObservable.subscribe(new Subscriber<List<TalkEntitiy>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<TalkEntitiy> talkEntitiys) {
                        mAdapter.setList(talkEntitiys);
                        mListView.setSelection(mAdapter.getTalkList().size() - 1);
                    }
                });
            }
        });
        mAudioRecorderButton.setFinishRecorderCallBack(new AudioRecorderButton.AudioFinishRecorderCallBack() {

            public void onFinish(float seconds, String filePath) {
                long l = saveData((int) seconds, filePath);

                upLoadData(filePath,l);

            }
        });

        mAdapter.setClickListener(new RecoderAdapter.ClickListener() {
            @Override
            public void clickListener(View view, int position) {
                int itemViewType = mAdapter.getItemViewType(position);
                switch (itemViewType) {
                    case 0:
                        if (animView != null) {
                            animView.setBackgroundResource(R.drawable.chatfrom_voice_playing_f3);
                            animView = null;
                        }
                        animView = view.findViewById(R.id.id_recoder_anim);
                        animView.setBackgroundResource(R.drawable.play_anim_left);
                        AnimationDrawable animation = (AnimationDrawable) animView.getBackground();
                        animation.start();
                        // 播放录音
                        MediaPlayerManager.playSound(mAdapter.getTalkList().get(position).filepath(), new MediaPlayer.OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                //播放完成后修改图片
                                animView.setBackgroundResource(R.drawable.chatfrom_voice_playing_f3);
                            }
                        });
                        break;
                    case 1:
                        if (animView1 != null) {
                            animView1.setBackgroundResource(R.drawable.chatto_voice_playing);
                            animView1 = null;
                        }
                        animView1 = view.findViewById(R.id.id_recoder_anim);
                        animView1.setBackgroundResource(R.drawable.play_anim);
                        AnimationDrawable animation1 = (AnimationDrawable) animView1.getBackground();
                        animation1.start();
                        // 播放录音
                        MediaPlayerManager.playSound(mAdapter.getTalkList().get(position).filepath(), new MediaPlayer.OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                //播放完成后修改图片
                                animView1.setBackgroundResource(R.drawable.chatto_voice_playing);
                            }
                        });
                        break;
                }
                // 声音播放动画

            }

            @Override
            public void retry(String filepath, Integer integer) {
                upLoadData(filepath,integer.longValue());
            }
        });

//        mListView.setOnItemClickListener(new OnItemClickListener() {
//
//            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
//
//            }
//
//
//        });

    }

    @Override
    protected void initializeDi() {
        TalkComponent talkComponent = AndroidApplication.getBaseApplication().getApplicationComponent().talkComponent();
        talkComponent.inject(this);
    }

    private long saveData(int seconds, String filePath) {
        String imei = PreferencesUtils.getString(TalkActivity.this, "imei");
        TalkEntitiy recorder = TalkEntitiy.builder()
                .create_at(System.currentTimeMillis() / 1000)
                .duration(seconds)
                .filepath(filePath)
                .imei(imei)
                .name("")
                .myimei("")
                .status(true)
                .myname("")
                .success(true)
                .build();
        return instance.insert(TalkEntitiy.TABLE_NAME, TalkEntitiy.FACTORY.marshal(recorder).asContentValues());
    }

    private void upLoadData(String filePath,long l) {
        File file = new File(filePath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        // finally, execute the request
        final String token = PreferencesUtils.getString(TalkActivity.this, "Token");
        Call<ResponseBody> call = HttpMethods.getInstance().getService().uploadFile(token, body);
        call.enqueue(new MyCallback<ResponseBody>(l, new TheCall<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response,long id) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("success","1");
                instance.update(TalkEntitiy.TABLE_NAME,contentValues,"_id=?",String.valueOf(id));

                String imei = PreferencesUtils.getString(TalkActivity.this, "imei");


                try {
                    String s = response.body().string();
                    System.out.println("---"+s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject data = jsonObject.getJSONObject("data");
                    String url = data.getString("url");
                    System.out.println("---"+url);
                    Observable<BaseEntity<List<VoiceEntity>>> postvoice = HttpMethods.getInstance().getService().postvoice(token, imei, url);
                    postvoice.map(new HttpResultFunc<List<VoiceEntity>>())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<List<VoiceEntity>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(List<VoiceEntity> voiceEntities) {

                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call call, Throwable t, long id) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("success","0");

                instance.update(TalkEntitiy.TABLE_NAME,contentValues,"_id=?",String.valueOf(id));

            }
        }));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayerManager.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.release();
        instance.close();
        subscribe.unsubscribe();
        if(subscribe1!=null) {
            subscribe1.unsubscribe();
        }

        mAudioRecorderButton.destory();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);

        menu.getItem(0).setTitle("全部删除");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                String imei = PreferencesUtils.getString(TalkActivity.this, "imei");
                instance.delete(TalkEntitiy.TABLE_NAME,"imei=?",String.valueOf(imei));

                String dir = Environment.getExternalStorageDirectory() + "/ldm_voice";
                File file = new File(dir);

                File files[] = file.listFiles();
                for (File file1 : files) {
                    file1.delete();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}