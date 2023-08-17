package com.mshy.VInterestSpeed.common.ui.view.notification;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ellison.glide.translibrary.ImageUtils;
import com.ellison.glide.translibrary.base.LoaderBuilder;
import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.bean.AgoraMtachCallBean;
import com.mshy.VInterestSpeed.common.bean.video.VideoVquCallBean;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.constant.RouteKey;
import com.mshy.VInterestSpeed.common.constant.RouteUrl;
import com.mshy.VInterestSpeed.common.ext.ViewOnClickExtKt;
import com.mshy.VInterestSpeed.common.net.GlobalApiService;
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.utils.UiUtils;
import com.mshy.VInterestSpeed.uikit.common.http.CommonCallBack;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/21 14:47
 */

public class SwipeNotification extends LinearLayout {

    private static final String TAG = "SwipeNotification";

    private static final int AUTO_DISMISS_TIME = 2000;

    public static final int DIRECTION_LEFT = 1;

    private static final int DIRECTION_RIGHT = 2;

    private static final int SPEED_PX = 66;

    private GestureDetector mDetector;

    private OnDisappearListener mDisappearListener;

    private OnClickNotificationListener mClickNotificationListener;

    private boolean isDisappearing = false;


    private static float screenWidth;

    public SwipeNotification(@NonNull Context context) {
        this(context, null);
    }

    public SwipeNotification(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeNotification(@NonNull Context context, @Nullable AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    ImageView vqu_voice_callee_cancel_g;
    ImageView vqu_voice_callee_answer_g;
    ImageView headView;
    TextView nickName;
    TextView matchType;
    TextView vquNoCallZS;
    GlobalApiService agoraVquApiService;

    private void init(Context context) {
        agoraVquApiService = GlobalServiceManage.INSTANCE.getRetrofit().create(GlobalApiService.class);
        screenWidth = getScreenWidth(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_notification, this);
        vqu_voice_callee_cancel_g = inflate.findViewById(R.id.vqu_voice_callee_cancel_g);
        vqu_voice_callee_answer_g = inflate.findViewById(R.id.vqu_voice_callee_answer_g);
        headView = inflate.findViewById(R.id.image_view);
        nickName = inflate.findViewById(R.id.vquNoCallName);
        matchType = inflate.findViewById(R.id.vquNoCallType);
        vquNoCallZS = inflate.findViewById(R.id.vquNoCallZS);
        mDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: ");
                if (mClickNotificationListener != null) {
                    mClickNotificationListener.onClickNotification();
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float x1 = e1.getX();
                float x2 = e2.getX();
                int offsetX = (int) (x2 - x1);
                layoutWithAlpha(getLeft() + offsetX, getTop(), getRight() + offsetX, getBottom());
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.d(TAG, "onFling: =" + (e2.getX() - e1.getX()) + " velocityX=" + velocityX);
                float fling = e2.getX() - e1.getX();
                if (fling > 0) {
                    disappear(DIRECTION_LEFT);
                } else {
                    disappear(DIRECTION_RIGHT);
                }
                return false;
            }
        });
    }

    public void setData(AgoraMtachCallBean matchData) {
        nickName.setText(matchData.getNickname());
        if ("video".equals(matchData.getType())) {
            matchType.setText("邀你视频速配");
        } else {
            matchType.setText("邀你语音速配");
        }
        vquNoCallZS.setText("获得"+matchData.getPrice());
        LoaderBuilder builderConfig = new LoaderBuilder()
                .round(UiUtils.dip2px(BaseApplication.context, 12f))
                .circle(false)
                .width(UiUtils.dip2px(BaseApplication.context, 64f))
                .height(UiUtils.dip2px(BaseApplication.context, 64f))
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .round(new float[]{
                        UiUtils.dip2px(BaseApplication.context, 12f),
                        UiUtils.dip2px(BaseApplication.context, 12f),
                        UiUtils.dip2px(BaseApplication.context, 12f),
                        UiUtils.dip2px(BaseApplication.context, 12f)})
                .borderColor(ContextCompat.getColor(BaseApplication.context, R.color.color_FFFFFF))
                .borderWidth(UiUtils.dip2px(BaseApplication.context, 0f))
                .load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + matchData.getAvatar());
        ImageUtils.getInstance().bind(headView, builderConfig);
        ViewOnClickExtKt.click(vqu_voice_callee_cancel_g, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                disappear(DIRECTION_LEFT);
                agoraVquApiService.tantaReceiveCall(matchData.getMatch_id() + "", "cancel").enqueue(new CommonCallBack<VideoVquCallBean>() {
                    @Override
                    public void onSuccess(VideoVquCallBean data) {
//                        if ("video".equals(data.getType())) {
//                            ARouter.getInstance().build(RouteUrl.Agora.AgoraVquVideoActivity)
//                                    .withString(RouteKey.SOCKET_URL,data.getSocket_url() +"")
//                                    .withBoolean("isCaller", true)
//                                    .withBoolean("isMatch", true)
//                                    .withParcelable("CallBean", data)
//                                    .navigation();
//                        } else {
//                            ARouter.getInstance().build(RouteUrl.Agora.AgoraVquAudioActivity)
//                                    .withString(RouteKey.SOCKET_URL, data.getSocket_url() +"")
//                                    .withBoolean("isCaller", true)
//                                    .withBoolean("isMatch", true)
//                                    .withParcelable("CallBean", data)
//                                    .navigation();
//                        }
                    }
                });

                return null;
            }
        });
        ViewOnClickExtKt.click(vqu_voice_callee_answer_g, new Function1<View, Unit>() {
            @Override
            public Unit invoke(View view) {
                disappear(DIRECTION_LEFT);
                agoraVquApiService.tantaReceiveCall(matchData.getMatch_id() + "", "receive").enqueue(new CommonCallBack<VideoVquCallBean>() {
                    @Override
                    public void onSuccess(VideoVquCallBean data) {
                        if ("video".equals(data.getType())) {
                            ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaVideoActivity)
                                    .withString(RouteKey.SOCKET_URL,data.getSocket_url() +"")
                                                    .withBoolean("isCaller", true)
                                    .withBoolean("isMatch", true)
                                    .withParcelable("CallBean", data)
                                    .navigation();
                        } else {
                            ARouter.getInstance().build(RouteUrl.Agora2.AgoraTantaAudioActivity)
                                    .withString(RouteKey.SOCKET_URL, data.getSocket_url() +"")
                                                    .withBoolean("isCaller", true)
                                    .withBoolean("isMatch", true)
                                    .withParcelable("CallBean", data)
                                    .navigation();
                        }
                    }
                });
                return null;
            }
        });
    }

    private void layoutWithAlpha(int l, int t, int r, int b) {
        layout(l, t, r, b);
        float alpha = (1080f - Math.abs(getLeft())) / 1080f;
        Log.d(TAG, "layoutWithAlpha: alpha = " + alpha);
        setAlpha(alpha);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean detectedUp = event.getAction() == MotionEvent.ACTION_UP;
        if (!mDetector.onTouchEvent(event) && detectedUp) {
            onUp(event);
//            return super.onTouchEvent(event);
        }
        return true;
    }

    private void onUp(MotionEvent event) {
        Log.d(TAG, "onUp: ");
        if (isDisappearing) {
            return;
        }
        autoDisappear();
    }

    private void autoDisappear() {

        Log.d(TAG, "autoDisappear: ");
        if (getLeft() < UIUtil.dip2px(BaseApplication.context, 12)) {
            disappear(DIRECTION_LEFT);
        } else if (getRight() > screenWidth - UIUtil.dip2px(BaseApplication.context, 363)) {
            disappear(DIRECTION_RIGHT);
        }
    }

    public void disappear(int direction) {
        isDisappearing = true;
        final int speed = direction == DIRECTION_LEFT ? SPEED_PX : -SPEED_PX;
        Log.d(TAG, "disappear: speed=" + speed);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int left = getLeft();
                int right = getRight();
                layoutWithAlpha(left + speed, getTop(), right + speed, getBottom());
                Log.d(TAG, "run: layout");
                if (left < screenWidth && right > 0) {
                    post(this);
                } else {
                    if (mDisappearListener != null) {
                        mDisappearListener.onDisappear();
                    }
                }
            }
        };
        post(r);
    }

    public void setOnDisappearListener(OnDisappearListener listener) {
        mDisappearListener = listener;
    }

    public void setOnClickNotificationListener(OnClickNotificationListener listener) {
        mClickNotificationListener = listener;
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 通知滑动出去消失了的回调
     */
    public interface OnDisappearListener {
        void onDisappear();
    }

    /**
     * 点击通知的回调
     */
    public interface OnClickNotificationListener {
        void onClickNotification();
    }
}