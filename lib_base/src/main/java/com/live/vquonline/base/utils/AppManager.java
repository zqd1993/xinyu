package com.live.vquonline.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;
import java.util.Stack;

/**
 * @author geyifeng
 * @date 2019-04-23 13:46
 */
public class AppManager {

    private Stack<Activity> mActivities = new Stack<>();

    public static int mActivityCount = 0;

    public static boolean isMiniWindow = false;

    private static class Holder {
        private static final AppManager INSTANCE = new AppManager();
    }

    public static AppManager getInstance() {
        return Holder.INSTANCE;
    }

    public void addActivity(Activity activity) {
        mActivities.add(activity);
    }

    public void removeActivity(Activity activity) {
        hideSoftKeyBoard(activity);
        activity.finish();
        mActivities.remove(activity);
    }

    public void removeAllActivity() {
        for (Activity activity : mActivities) {
            hideSoftKeyBoard(activity);
            activity.finish();
        }
        mActivities.clear();
    }

    public <T extends Activity> boolean hasActivity(Class<T> tClass) {
        for (Activity activity : mActivities) {
            if (tClass.getName().equals(activity.getClass().getName())) {
                return !activity.isDestroyed() || !activity.isFinishing();
            }
        }
        return false;
    }

    public void hideSoftKeyBoard(Activity activity) {
        View localView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localView != null && imm != null) {
            imm.hideSoftInputFromWindow(localView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showSoftKeyBoard(Activity activity, View view) {
        View localView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localView != null && imm != null) {
            imm.showSoftInput(view, 0);
        }
    }


    /**
     * 判断服务是否在运行
     *
     * @param mContext
     * @param className 　　Service.class.getName();
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> seviceList = activityManager.getRunningServices(200); //200:是运行的service的集合大小，当设置太小时，我没有获取到正在运行的Serice
        if (seviceList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < seviceList.size(); i++) {
            if (seviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
