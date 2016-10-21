package com.emporia.common.util;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * service util
 * @author sky
 */
public final class ServiceUtil {
    /**
     * judge service is running, by serviceName
     * @param context @see Context
     * @param serviceName packageName + className
     * @return if service is running return true, otherwise return false
     */
    public static boolean isServiceRun(Context context, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = myAM.getRunningServices(40);
        if (serviceInfos.size() <= 0) {
            return false;
        }
        for (int i = 0; i < serviceInfos.size(); i++) {
            String mName = serviceInfos.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
