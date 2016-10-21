package com.emporia.common.util.crash;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import com.emporia.common.util.NetworkUtils;

/**
 * use intentService to crash file for testing, testing every 8 hours, service running in a separate thread
 * @author sky
 */
public class AlarmSenderService extends IntentService {
    private AlarmManager alarmManager;
    private PendingIntent checkPendingIntent;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public AlarmSenderService() {
        super("AlarmSenderService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (NetworkUtils.isConnect(this) &&
                CrashCollect.getInstance().isSend(
                        CrashExceptionHandler.getIntance().getCrashFileDir())) {
            NetworkUtils.NetworkType networkType = NetworkUtils.getNetworkType(this);
            if (!CrashCollect.getInstance().isOnlyWifiSend()) {
                CrashCollect.getInstance().send();
            } else {
                if (networkType == NetworkUtils.NetworkType.WIFI) {
                    CrashCollect.getInstance().send();
                }
            }
        }
        //check every 8 hours
        alarmManager.cancel(checkPendingIntent);
        long hours = CrashCollect.getInstance().checkSendTimeInterval();
        long triggerAtTime = SystemClock.elapsedRealtime() + hours;
        Intent i = new Intent(this, AlarmSenderReceiver.class);
        checkPendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, checkPendingIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }
    /** start AlarmSenderService service */
    public static void startService(Context context) {
        Intent senderService = new Intent(context.getApplicationContext(), AlarmSenderService.class);
        context.startService(senderService);
    }
}
