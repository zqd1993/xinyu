package com.mshy.VInterestSpeed.uikit.common.media.imagepicker.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.mshy.VInterestSpeed.uikit.common.activity.UI;

public abstract class ImageBaseActivity extends UI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        clearRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearMemoryCache();
    }

    public abstract void clearRequest();

    public abstract void clearMemoryCache();

    protected void asFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
