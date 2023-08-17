package com.mshy.VInterestSpeed.uikit.common.media.imagepicker.adapter.vh;

import android.Manifest;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;

import com.mshy.VInterestSpeed.uikit.common.adapter.BaseViewHolder;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.Constants;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.ImagePicker;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.camera.CaptureActivity;
import com.mshy.VInterestSpeed.uikit.common.media.imagepicker.ui.ImageBaseActivity;
import com.netease.nim.uikit.R;


/**
 */

public class CameraViewHolder extends BaseViewHolder<Object> {

    private final ImagePicker imagePicker;
    private final ImageBaseActivity activity;

    public CameraViewHolder(ViewGroup parent, ImageBaseActivity activity, ImagePicker picker) {
        super(parent, R.layout.nim_adapter_image_list_camera);
        this.imagePicker = picker;
        this.activity = activity;
    }

    @Override
    public void findViews() {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagePicker.videoOnly()) {
                    CaptureActivity.RECORD_MAX_TIME = imagePicker.getOption().getMaxVideoDuration();
                    CaptureActivity.RECORD_MIN_TIME = 1;

                    CaptureActivity.start(activity);
                    return;
                } else if (imagePicker.imageOnly()) {
                    if (!(activity.checkPermission(Manifest.permission.CAMERA))) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                                                          Constants.REQUEST_PERMISSION_CAMERA);
                    } else {
                        ImagePickerLauncher.takePicture(activity, Constants.REQUEST_CODE_TAKE, imagePicker.getOption());
                    }
                } else {
                    CaptureActivity.start(activity, Constants.RESULT_CODE_RECORD_VIDEO);
                }
            }
        });
    }

    @Override
    protected void onBindViewHolder(Object data) {

    }
}
