package com.live.module.agora;

import android.content.Context;

import com.live.module.agora.framework.PreprocessorFaceUnity;

import io.agora.capture.framework.modules.processors.IPreprocessor;
import io.agora.capture.video.camera.CameraVideoManager;

/**
 * author: Lau
 * date: 2022/7/19
 * description:
 */
public class VideoManager {
    private static CameraVideoManager mVideoManager;

    public static void init(Context context) {
        mVideoManager = new CameraVideoManager(context, new PreprocessorFaceUnity(context));
    }

    public static CameraVideoManager getManager() {
        return mVideoManager;
    }
}
