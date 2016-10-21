package com.emporia.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
/**
 * Package Util, you can find some useful methods here, and of course, 
 * if you do not meet your needs, you can add methods to meet your needs in this class. 
 * @author sky
 * @version 1.0
 */
public final class PackageUtil {
	private PackageUtil() {}
	/**
	 * get application version code
	 * @param context @see Context
	 * @return version code if get failed or exception return -1
	 */
	public static int getVersionCode(Context context) {
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				PackageInfo pi;
				try {
					pi = pm.getPackageInfo(context.getPackageName(), 0);
					if (pi != null) {
						return pi.versionCode;
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return -1;
	}
	/**
	 * get application version name
	 * @param context @see Context
	 * @return version name if get failed or exception return ""
	 */
	public static String getVersionName(Context context) {
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				PackageInfo pi;
				try {
					pi = pm.getPackageInfo(context.getPackageName(), 0);
					if (pi != null) {
						return pi.versionName;
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}
	/**
	 * get application meta data value, you need to define in manifest file
	 * @param context @see Context
	 * @param key String 
	 * @return if get success return meta data, otherwise ""
	 */
	public static String getMetaDataValue(Context context, String key) {
		String metaData = "";
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				ApplicationInfo ai;
				try {
					ai = pm.getApplicationInfo(context.getPackageName(), 
							PackageManager.GET_META_DATA);
					if (ai != null && ai.metaData != null) {
						metaData = ai.metaData.getString(key);
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return metaData;
	}
	/** when you not define in manifest file return defValue @see getMetaDataValue(Context, String) */
	public static String getMetaDataValue(Context context, String key, String defValue) {
		return TextUtils.isEmpty(getMetaDataValue(context, key)) 
				? defValue : getMetaDataValue(context, key);
	}
	/**
	 * get package infomation
	 * @param context @see Context
	 * @return @see PackageInfo
	 */
	public static PackageInfo getPackageInfo(Context context) {
		return context == null ? null 
				: getPackageInfo(context, context.getPackageName());
	}
	/**
	 * get package infomation by package name
	 * @param context @see Context
	 * @return @see PackageInfo
	 */
	public static PackageInfo getPackageInfo(Context context, String packageName) {
		PackageInfo pi = null;
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				try {
					pi = pm.getPackageInfo(packageName, 0);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return pi;
	}
	/**
	 * get application infomation
	 * @param context @see Context
	 * @param flags Additional option flags. Use any combination of
     * 				{@link #GET_META_DATA}, {@link #GET_SHARED_LIBRARY_FILES},
     * 				{@link #GET_UNINSTALLED_PACKAGES} to modify the data returned.
	 * @return ApplicationInfo
	 */
	public static ApplicationInfo getApplicationInfo(Context context, int flags) {
		ApplicationInfo ai = null;
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				try {
					ai = pm.getApplicationInfo(context.getPackageName(), flags);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return ai;
	}
	/**
	 * get application name
	 * @param context @see Context
	 * @return application name, if get application name failed return ""
	 */
	public static String getApplicationName(Context context) {
		String appName = "";
		if (context != null) {
			ApplicationInfo ai = getApplicationInfo(context, 0);
			if (ai != null) {
				appName = ai.loadLabel(context.getPackageManager()).toString();
			} else {
				PackageInfo pi = getPackageInfo(context);
				if (pi != null && pi.applicationInfo != null) {
					appName = pi.applicationInfo.loadLabel(
							context.getPackageManager()).toString();
				}
			}
		}
		return appName;
	}
	/**
	 * get application icon
	 * @param context @see Context
	 * @return application icon, if succes return drawable, otherwise null
	 */
	public static Drawable getApplicationIcon(Context context) {
		Drawable drawable = null;
		if (context != null) {
			ApplicationInfo ai = getApplicationInfo(context, 0);
			if (ai != null) {
				drawable = ai.loadIcon(context.getPackageManager());
			} else {
				PackageInfo pi = getPackageInfo(context);
				if (pi != null && pi.applicationInfo != null) {
					drawable = pi.applicationInfo.loadIcon(context.getPackageManager());
				}
			}
		}
		return drawable;
	}
	/**
	 * to determine whether the packageName system application 
	 * @param context @see Context
	 * @param packageName application package name
	 * @return if packageName application is system application return true, otherwise false
	 */
	public static boolean isSystemApplication(Context context, String packageName) {
		if (context != null && !TextUtils.isEmpty(packageName)) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				try {
					ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
					return ai != null 
							&& (ai.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	/**
	 * get all application infomation
	 * @param context @see Context
	 * @return application infomation list
	 */
	public static List<PackageInfo> getInstalledPackages(Context context) {
		List<PackageInfo> packageInfos = new ArrayList<PackageInfo>();
		if (context != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				packageInfos = pm.getInstalledPackages(0);
			}
		}
		return packageInfos;
	}
	/**
	 * install application by file
	 * @param context @see Context
	 * @param file @see File
	 * @return install success return true, otherwise false
	 */
	public static boolean installApplication(Context context, File file) {
		if (file == null || !file.isFile() || !file.exists()) 
			return false;
		return installApplication(context, Uri.fromFile(file));
	}
	/**
	 * install application by Uri
	 * @param context @see Context
	 * @param uri @see Uri
	 * @return install success return true, otherwise false
	 */
	public static boolean installApplication(Context context, Uri uri) {
		if (context == null || uri == null)
			return false;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		return true;
	}
	/**
	 * uninstall application by package name
	 * @param context @see Context
	 * @param packageName package name
	 */
	public static void uninstallApplication(Context context, String packageName) {
		if (context == null 
				|| TextUtils.isEmpty(packageName))
			return;
		Intent intent = new Intent(Intent.ACTION_DELETE);
		intent.setData(Uri.parse("package:" + packageName));
		context.startActivity(intent);
	}
	/**
	 * judge whether the application of the package installation
	 * @param context @see Context
	 * @param packageName package name
	 * @return if packageName application installed return true, otherwise false
	 */
	public static boolean isInstalled(Context context, String packageName) {
		if (context == null 
				|| TextUtils.isEmpty(packageName))
			return false;
		List<PackageInfo> infos = getInstalledPackages(context);
		if (infos == null || infos.isEmpty())
			return false;
		for (PackageInfo info : infos) {
			if (packageName.equalsIgnoreCase(info.packageName)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * query resolve infomation by intent
	 * @param context @see Context
	 * @param intent contains the intent of the action
	 * @return resolve information list
	 */
	public static List<ResolveInfo> queryResolveInfos(Context context, Intent intent) {
		List<ResolveInfo> resolveInfos = new ArrayList<ResolveInfo>();
		if (context != null && intent != null) {
			PackageManager pm = context.getPackageManager();
			if (pm != null) {
				resolveInfos = pm.queryIntentActivities(intent, 0);
			}
		}
		return resolveInfos;
	}
	/** call queryResolveInfos(Context context, Intent intent) */
	public static List<ResolveInfo> queryResolveInfos(Context context, String action) {
		return queryResolveInfos(context, new Intent(action));
	}
	/**
	 * implicit intent transform explicit intent by implicit intent
	 * @param context @see Context context
	 * @param implicitIntent implicit intent
	 * @return explicit intent or null
	 */
	public static Intent implicitTransformExplicit(Context context, Intent implicitIntent) {
		List<ResolveInfo> resolveInfos = queryResolveInfos(context, implicitIntent);
		if (resolveInfos == null 
				|| resolveInfos.size() != 1) {
			return null;
		}
		return convertIntent(implicitIntent, resolveInfos.get(0));
	}
	/** call implicitTransformExplicit(Context context, Intent implicitIntent) */
	public static Intent implicitTransformExplicit(Context context, String implicitAction) {
		return implicitTransformExplicit(context, new Intent(implicitAction));
	}
	/**
	 * query activity, choose the first intent
	 * @param context @Context
	 * @param action intent action
	 * @return if the activity is to return to the first intent, otherwise null
	 */
	public static Intent getFirstIntent(Context context, Intent action) {
		List<ResolveInfo> resolveInfos = queryResolveInfos(context, action);
		if (resolveInfos == null 
				|| resolveInfos.size() < 1) {
			return null;
		}
		return convertIntent(action, resolveInfos.get(0));
	}
	/** call getFirstIntent(Context context, Intent action) */
	public static Intent getFirstIntent(Context context, String action) {
		return getFirstIntent(context, new Intent(action));
	}
	/** Converted to show intent */
	private static Intent convertIntent(Intent action, final ResolveInfo resolveInfo) {
		String pkg = "";
		String cls = "";
		if (resolveInfo.activityInfo != null) {
			pkg = resolveInfo.activityInfo.packageName;
			cls = resolveInfo.activityInfo.name;
		} else if (resolveInfo.serviceInfo != null) {
			pkg = resolveInfo.serviceInfo.packageName;
			cls = resolveInfo.serviceInfo.name;
		} else {
			pkg = resolveInfo.providerInfo.packageName;
			cls = resolveInfo.providerInfo.name;
		}
		Intent covertIntent = new Intent(action);
		covertIntent.setComponent(new ComponentName(pkg, cls));
		return covertIntent;
	}
	/**
	 * get android build model
	 * @return agent mode name
	 */
	public static String getAgentModel() {
		String agentMode = android.os.Build.MODEL;
		if (!TextUtils.isEmpty(agentMode))
			agentMode = agentMode.replaceAll(" |\\t|&", "");
		return agentMode;
	}
	/**
	 * get android release version
	 * @return android release version 
	 */
	public static String getAgentRelease() {
		String agentRelease = android.os.Build.VERSION.RELEASE;
		if (!TextUtils.isEmpty(agentRelease))
			agentRelease = agentRelease.replaceAll(" |\\t|&", "");
		return agentRelease;
	}

}
