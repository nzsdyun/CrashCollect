package com.emporia.common.util.crash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 *receive AlarmSenderService send radio startup AlarmSenderService crash testing whether send documents
 *@author sky
 */
public class AlarmSenderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmSenderService.startService(context);
    }
}
