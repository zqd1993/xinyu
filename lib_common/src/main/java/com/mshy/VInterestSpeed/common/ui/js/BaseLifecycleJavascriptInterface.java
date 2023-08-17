package com.mshy.VInterestSpeed.common.ui.js;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.lang.ref.WeakReference;

/**
 * FileName: com.live.vquonline.view.base
 * Author: Reisen
 * Date: 2021/7/8 17:56
 * Description:
 * History:
 */
public abstract class BaseLifecycleJavascriptInterface implements BaseLifecycle {

    private final String TAG = getClass().getSimpleName();

    protected final WeakReference<FragmentActivity> weakReference;

    public BaseLifecycleJavascriptInterface(FragmentActivity activity) {
        this.weakReference = new WeakReference<>(activity);
        weakReference.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onCreate: ");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onPause: ");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onStop: ");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Log.e(TAG, "onDestroy: ");
        weakReference.get().getLifecycle().removeObserver(this);
    }

    @Override
    public void onAny(@NonNull LifecycleOwner owner, @NonNull Lifecycle.Event event) {
        Log.e(TAG, "onAny: ");
    }

    protected @Nullable
    FragmentActivity getActivity() {
        if (weakReference.get() != null) {
            return weakReference.get();
        }
        return null;
    }
}
