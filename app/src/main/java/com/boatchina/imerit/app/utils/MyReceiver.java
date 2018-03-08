package com.boatchina.imerit.app.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.boatchina.imerit.app.AndroidApplication;
import com.boatchina.imerit.app.di.components.MyReceiverComponent;
import com.boatchina.imerit.app.view.activity.TabActivity;
import com.boatchina.imerit.app.view.talk.TalkActivity;
import com.boatchina.imerit.data.BoardcastEvent;
import com.boatchina.imerit.data.entity.DataEntitiy;
import com.boatchina.imerit.data.entity.TalkEntitiy;
import com.boatchina.imerit.data.entity.ValueEntitiy;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.squareup.sqlbrite.BriteDatabase;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.UUID;

import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "-JPush";
	@Inject
	BriteDatabase instance;
//	DbHelper dbHelper = new DbHelper();
	@Override
	public void onReceive(Context context, Intent intent) {
		MyReceiverComponent myReceiverComponent = AndroidApplication.getBaseApplication().getApplicationComponent().myReceiverComponent();
		myReceiverComponent.inject(this);
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction());
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
			EventBus.getDefault().post(new BoardcastEvent(regId));

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
			Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle,context));
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

				for(String key : bundle.keySet()) {
					if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
						Log.i(TAG, "This message has no Extra data");
						continue;
					}
					try {
						JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
						int type = json.getInt("type");
                        String imei = json.getString("imei");
                        PreferencesUtils.putString(context, "imei",imei);
                        switch (type) {
							case 5:
								//打开自定义的Activity
								Intent i3 = new Intent(context, TabActivity.class);
								Intent i2 = new Intent(context, TalkActivity.class);
								i2.putExtras(bundle);

								i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
								i3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
								context.startActivities(new Intent[]{i3,i2});
								break;
							default:
								//打开自定义的Activity
								Intent i = new Intent(context, TabActivity.class);
								i.putExtras(bundle);
								i.putExtra("index",1);
								//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
								context.startActivity(i);
								break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");



        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	// 打印所有的 intent extra 数据
	private  String printBundle(Bundle bundle,Context context) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {

					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Log.e(TAG, json.toString());
					int type = json.getInt("type");



					if(type==1) {
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

					}else if(type==2) {
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
					else if(type==3) {
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
					}else if(type==4) {
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
					}else if(type==5) {
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

//					Iterator<String> it =  json.keys();

//					while (it.hasNext()) {
//						String myKey = it.next().toString();
//						sb.append("\nkey:" + key + ", value: [" +
//								myKey + " - " +json.optString(yKey) + "]");
//					}

				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	//send msg to TalkActivity
	private void processCustomMessage(Context context, Bundle bundle) {
//		if (TalkActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(TalkActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(TalkActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(TalkActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}

//	MyReceiver() {
//		System.out.println("----------11");
//	}
}
