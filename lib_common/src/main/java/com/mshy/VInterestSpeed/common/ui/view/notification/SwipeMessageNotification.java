package com.mshy.VInterestSpeed.common.ui.view.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
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

import com.ellison.glide.translibrary.ImageUtils;
import com.ellison.glide.translibrary.base.LoaderBuilder;
import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.common.ui.view.magicindicator.buildins.UIUtil;
import com.mshy.VInterestSpeed.common.utils.UiUtils;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.mshy.VInterestSpeed.uikit.api.model.user.UserInfoObserver;
import com.mshy.VInterestSpeed.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.List;


/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/21 14:47
 */

public class SwipeMessageNotification extends LinearLayout {

    private static final String TAG = "SwipeMessageNotification";

    private static final int AUTO_DISMISS_TIME = 2000;

    public static final int DIRECTION_LEFT = 1;

    private static final int DIRECTION_RIGHT = 2;

    private static final int SPEED_PX = 66;

    private GestureDetector mDetector;

    private OnDisappearListener mDisappearListener;

    private OnClickNotificationListener mClickNotificationListener;

    private boolean isDisappearing = false;

    private String contactId = "";
    private static float screenWidth;

    public SwipeMessageNotification(@NonNull Context context) {
        this(context, null);
    }

    public SwipeMessageNotification(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMessageNotification(@NonNull Context context, @Nullable AttributeSet attrs,
                                    int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    ImageView headView;
    TextView nickName;
    TextView matchType;

    private void init(Context context) {
        screenWidth = getScreenWidth(context);
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_notification_message, this);
        headView = inflate.findViewById(R.id.image_view);
        nickName = inflate.findViewById(R.id.vquNoCallName);
        matchType = inflate.findViewById(R.id.vquNoCallType);
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

    public String descOfMsg(RecentContact recent) {
        if (recent.getMsgType() == MsgTypeEnum.text) {
            return recent.getContent();
        } else if (recent.getMsgType() == MsgTypeEnum.tip) {
            String digest = null;
            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent);
            }
            return digest;
        } else if (recent.getAttachment() != null) {
            String digest = null;
            if (digest == null) {
                digest = NimUIKitImpl.getRecentCustomization().getDefaultDigest(recent);
            }
            return digest;
        }
        return "[未知]";
    }

    public void setData(RecentContact data) {
        contactId = data.getContactId();
        UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(data.getContactId());
        String head = "";
        if (userInfo == null) {
            nickName.setText(data.getFromNick() != null ? data.getFromNick() : "");
            NimUIKit.getUserInfoObservable().registerObserver(new UserInfoObserver() {

                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    UserInfo user = NimUIKit.getUserInfoProvider().getUserInfo(data.getContactId());
                    if (user != null) {
                        String headStr = user.getAvatar();
                        setInfo(headStr);
                    }
                }
            }, true);
        } else {
            head = userInfo.getAvatar();
            nickName.setText(userInfo.getName());
        }
        matchType.setText(descOfMsg(data));
        setInfo(head);
        this.postDelayed(new Runnable() {
            @SuppressLint("NewApi")
            @Override
            public void run() {
                if(isDisappearing){
                    return;
                }
                SwipeMessageNotificationManager.getInstance(BaseApplication.context).removeNotification(contactId);
            }
        }, 1000 * 3);
    }

    private void setInfo(String head) {
        LoaderBuilder builderConfig = new LoaderBuilder()
                .round(UiUtils.dip2px(BaseApplication.context, 12f))
                .circle(false)
                .width(UiUtils.dip2px(BaseApplication.context, 42f))
                .height(UiUtils.dip2px(BaseApplication.context, 42f))
                .scaleType(ImageView.ScaleType.CENTER_CROP)
                .round(new float[]{
                        UiUtils.dip2px(BaseApplication.context, 21f),
                        UiUtils.dip2px(BaseApplication.context, 21f),
                        UiUtils.dip2px(BaseApplication.context, 21f),
                        UiUtils.dip2px(BaseApplication.context, 21f)})
                .borderColor(ContextCompat.getColor(BaseApplication.context, R.color.color_FFFFFF))
                .borderWidth(UiUtils.dip2px(BaseApplication.context, 0f))
                .placeholder(R.mipmap.ic_common_head_def)
                .load(NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + head);
        ImageUtils.getInstance().bind(headView, builderConfig);
    }

    private void layoutWithAlpha(int l, int t, int r, int b) {
        layout(l, t, r, b);
        float alpha = (1080f - Math.abs(getLeft())) / 1080f;
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
        if (isDisappearing) {
            return;
        }
        autoDisappear();
    }

    private void autoDisappear() {
        if (getLeft() < UIUtil.dip2px(BaseApplication.context, 12)) {
            disappear(DIRECTION_LEFT);
        } else if (getRight() > screenWidth - UIUtil.dip2px(BaseApplication.context, 363)) {
            disappear(DIRECTION_RIGHT);
        }
    }

    public void disappear(int direction) {
        isDisappearing = true;
        final int speed = direction == DIRECTION_LEFT ? SPEED_PX : -SPEED_PX;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int left = getLeft();
                int right = getRight();
                layoutWithAlpha(left + speed, getTop(), right + speed, getBottom());
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