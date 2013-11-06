package com.keju.baby.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.keju.baby.R;
import com.keju.baby.activity.HomeActivity;
import com.keju.baby.helper.BusinessHelper;
import com.keju.baby.util.SharedPrefUtil;
/**
 * 信息推送服务
 * @author Zhoujun
 *
 */
public class PullService extends Service {
	public static BusinessHelper businessHelper;
	private NotificationManager mNM;
	public static final int PUSH_MESSAGE = 100;
	@Override
	public void onCreate() {
		businessHelper = new BusinessHelper();
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Thread notifyingThread = new Thread(null, mTask,"PullService");
		notifyingThread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mNM.cancelAll();
		isrun = false;
	}

	public boolean isrun = true;
	private Runnable mTask = new Runnable() {
		public void run() {
			try {
				while (isrun) {
//					if(NetUtil.checkNet(PullService.this)){
//						JSONObject messageJson = businessHelper.getMessage();
//						JSONArray jsonArray = messageJson.getJSONArray("value");
//						if(jsonArray !=null && jsonArray.length()>0){
//							for (int i = 0; i < jsonArray.length(); i++) {
//								JSONObject jsonObject = jsonArray.getJSONObject(i);
//								String content=jsonObject.getString("content");
//								String title=jsonObject.getString("title");
//								showNotification(i,title,content);
//							}
//						}
//					}
					try {
						Thread.sleep(SharedPrefUtil.getUpdateInterval(PullService.this));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				isrun = false;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	/**
	 * 显示通知
	 * 
	 * @param eventBean
	 */
	private void showNotification(int notifyId,String title,String content) {
		// The details of our fake message
//		CharSequence title = createTitle(eventtype);
//		CharSequence content = createContent(eventtype,count);

		// The PendingIntent to launch our activity if the user selects this
		// notification
		Intent intent = new Intent(this, HomeActivity.class);
		intent.putExtra("notifyId", notifyId);//消息id
		intent.putExtra("title", title);//消息内容
		/**
		 * requestCode 这个属性需要不一样，否则的话多个通知会指向相同的intent
		 */
		PendingIntent contentIntent = PendingIntent.getActivity(this, notifyId,intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// The ticker text, this uses a formatted string so our message could be
		// localized

		// construct the Notification object.
		Notification notif = new Notification(R.drawable.ic_launcher, null,System.currentTimeMillis());
		//点击通知后自动从通知栏消失
		notif.flags = Notification.FLAG_AUTO_CANCEL;
		// Set the info for the views that show in the notification panel.
		notif.setLatestEventInfo(this, title, content, contentIntent);

		// after a 100ms delay, vibrate for 250ms, pause for 100 ms and
		// then vibrate for 500ms.
//		notif.vibrate = new long[] { 100, 250, 100, 500 };
		notif.defaults = Notification.DEFAULT_SOUND;
		mNM.notify(notifyId, notif);
	}
}
