package com.mshy.VInterestSpeed.common.ui.view.notification;

import static android.view.Gravity.TOP;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;

import com.live.vquonline.base.BaseApplication;
import com.mshy.VInterestSpeed.common.R;
import com.mshy.VInterestSpeed.common.event.JumpToChatEvent;
import com.mshy.VInterestSpeed.common.net.GlobalApiService;
import com.mshy.VInterestSpeed.common.net.GlobalServiceManage;
import com.mshy.VInterestSpeed.common.utils.UserManager;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import org.greenrobot.eventbus.EventBus;

/**
 * @Description:
 * @Author: theobald wen
 * @CreateDate: 2022/6/21 15:09
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class SwipeMessageNotificationManager {

    private static final String TAG = "FloatingWindowManager";

    private static SwipeMessageNotificationManager sManager;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;


    private RelativeLayout mNotificationContainer;

    private Context mContext;

    // 用来记录包含通知的 LinearLayout 是否已经添加到窗口里了
    private boolean isAdded;

    // 最大允许显示的通知条目数量
    private int mMaxCount = 4;


    private RecentContact lastRecentContact;
    GlobalApiService agoraVquApiService;

    private SwipeMessageNotificationManager(Context context) {

        agoraVquApiService = GlobalServiceManage.INSTANCE.getRetrofit().create(GlobalApiService.class);
        mContext = context;
        mParams = new WindowManager.LayoutParams();

        // 获取 WindowManager
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // 新建一个包含所有通知的容器
        mNotificationContainer = new RelativeLayout(context);

        // addCustomTransition();
//        View view = new View(context);
//        view.setVisibility(View.GONE);
//        view.setLayoutParams(new LinearLayout.LayoutParams(0,0));
//        mNotificationContainer.addView(view);
        // 设定一下 WindowManager.LayoutParams 的一些参数值
        initWindowParams(mWindowManager, mParams);

        // 将 mNotificationContainer 添加到窗口上

        mWindowManager.addView(mNotificationContainer, mParams);
        isAdded = true;
    }

    private void addCustomTransition() {

        //移除View时view的DISAPPEARING动画
        LayoutTransition layoutTransition = new LayoutTransition();

//        ObjectAnimator removeAnimator = ObjectAnimator.ofFloat(null, "translationX", 0, 50,0).setDuration(1500);
//
//        layoutTransition.setAnimator( LayoutTransition.DISAPPEARING, removeAnimator);

        //添加view是view的APPEARING动画

        ObjectAnimator addAnimator = ObjectAnimator.ofFloat(null, "translationY", 0, 1).setDuration(2500);

        layoutTransition.setAnimator(LayoutTransition.APPEARING, addAnimator);
        mNotificationContainer.setLayoutTransition(layoutTransition);
    }


    public static SwipeMessageNotificationManager getInstance(Context context) {
        if (sManager == null) {
            synchronized (SwipeMessageNotificationManager.class) {
                if (sManager == null) {

                    sManager = new SwipeMessageNotificationManager(context);
                }
            }
        }
        return sManager;
    }


    public void removeNotification(int match_id) {
        if (mNotificationContainer != null) {
            if (mNotificationContainer.getChildCount() > 0) {
                for (int i = 0; i < mNotificationContainer.getChildCount(); i++) {
                    if (mNotificationContainer.getChildAt(i) instanceof SwipeMessageNotification) {
                        if (match_id == Integer.parseInt(mNotificationContainer.getChildAt(i).getTag() + "")) {
                            ((SwipeMessageNotification) mNotificationContainer.getChildAt(i)).disappear(SwipeMessageNotification.DIRECTION_LEFT);
                        }
                    }
                }
            }
        }
    }

    public void removeNotification(String contactId) {
        if (mNotificationContainer != null) {
            if (mNotificationContainer.getChildCount() > 0) {
                for (int i = 0; i < mNotificationContainer.getChildCount(); i++) {
                    if (mNotificationContainer.getChildAt(i) instanceof SwipeMessageNotification) {
                        if (contactId.equals(mNotificationContainer.getChildAt(i).getTag() + "")) {
                            ((SwipeMessageNotification) mNotificationContainer.getChildAt(i)).disappear(SwipeMessageNotification.DIRECTION_LEFT);
                        }
                    }
                }
            }
        }
    }

    public void removeNotificationAll() {
        if (mNotificationContainer != null) {
            if (mNotificationContainer.getChildCount() > 0) {
                for (int i = 0; i < mNotificationContainer.getChildCount(); i++) {
                    if (mNotificationContainer.getChildAt(i) instanceof SwipeMessageNotification) {
                        ((SwipeMessageNotification) mNotificationContainer.getChildAt(i)).disappear(SwipeMessageNotification.DIRECTION_LEFT);
                    }
                }
            }
        }
    }


    public void addNotification(RecentContact matchData) {
        if (UserManager.INSTANCE.isMatch()) {
            return;
        }
        if (UserManager.INSTANCE.isVideo()) {
            return;
        }
        String chattingId = UserManager.INSTANCE.getChattingId();
        String userId = UserManager.INSTANCE.getUserInfo().getUserId();
        if (chattingId.equals(matchData.getContactId())) {
            return;
        }
        if (matchData.getFromAccount() == null) {
            return;
        }
        if (matchData.getFromAccount().equals(userId)) {
            return;
        }
        if (UserManager.INSTANCE.isChatting()) {
            return;
        }
        if (lastRecentContact != null) {
            if (lastRecentContact.getRecentMessageId().equals(matchData.getRecentMessageId())) {
                return;
            }

        }
        if (mNotificationContainer != null) {
            if (mNotificationContainer.getChildCount() > 0) {
                for (int i = 0; i < mNotificationContainer.getChildCount(); i++) {
                    if (mNotificationContainer.getChildAt(i) instanceof SwipeMessageNotification) {
                        ((SwipeMessageNotification) mNotificationContainer.getChildAt(i)).disappear(SwipeMessageNotification.DIRECTION_LEFT);
                    }
                }
            }
        }
        // 新建一条通知
        final SwipeMessageNotification notification = new SwipeMessageNotification(BaseApplication.context);
        notification.setData(matchData);
        lastRecentContact = matchData;
        // 给通知添加点击事件
        notification.setOnClickNotificationListener(
                new SwipeMessageNotification.OnClickNotificationListener() {
                    @Override
                    public void onClickNotification() {

                        if (UserManager.INSTANCE.isChatting()) {
                            JumpToChatEvent jumpToChatEvent = new JumpToChatEvent();
                            jumpToChatEvent.setChattingId(matchData.getContactId());
                            EventBus.getDefault().post(jumpToChatEvent);
                        } else {
                            NimUIKit.startP2PSession(
                                    mContext,
                                    matchData.getContactId()
                            );
                        }
//                        mNotificationContainer.removeViewAt(0);
                    }
                });

        if (!isAdded) {
            mWindowManager.addView(mNotificationContainer, mParams);
            Log.d(TAG, "addNotification: addView");
            isAdded = true;
        }
        // 获取一共有多少条通知
        int childCount = mNotificationContainer.getChildCount();

        // 已经显示的通知数目大于了最大值，就将第一条移除
        if (childCount == mMaxCount) {
            mNotificationContainer.removeViewAt(0);
        }

        // 将通知添加进 mNotificationContainer
        notification.setTag(matchData.getContactId());
        mNotificationContainer.addView(notification);
        Animation animation = AnimationUtils.loadAnimation(notification.getContext(), R.anim.translate_notifition);
        notification.startAnimation(animation);

        // 通知已经通过滑动移除出屏幕的时候，将通知从 mNotificationContainer 中移除
        notification.setOnDisappearListener(new SwipeMessageNotification.OnDisappearListener() {
            @Override
            public void onDisappear() {
                mNotificationContainer.removeView(notification);
//                // 如果 mNotificationContainer 里面已经没有通知了，将 mNotificationContainer 从窗口移除
//                if (mNotificationContainer.getChildCount() == 0) {
//                    mWindowManager.removeViewImmediate(mNotificationContainer);
//                    isAdded = false;
//                }
//                agoraVquApiService.vquReceiveCall(matchData.getMatch_id() + "", "cancel").enqueue(new CommonCallBack<VideoVquCallBean>() {
//                    @Override
//                    public void onSuccess(VideoVquCallBean data) {
//
//                    }
//                });
            }
        });

    }

    private void initWindowParams(WindowManager manager, WindowManager.LayoutParams params) {
        // android 8.0 以上，要将 type 设为 TYPE_APPLICATION_OVERLAY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mParams.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为置顶
        params.gravity = TOP;
        //以屏幕左上角为原点，设置x、y初始值
        params.x = 0;
        params.y = 0;
        //设置悬浮窗口长宽
        params.width = MATCH_PARENT;
        params.height = WRAP_CONTENT;
        //  params.height = UiUtils.dip2px(BaseApplication.context,100);
    }

    /**
     * 设置一下最大的显示通知的数量
     */
    private void setMaxCount(int maxCount) {
        mMaxCount = maxCount;
    }
}