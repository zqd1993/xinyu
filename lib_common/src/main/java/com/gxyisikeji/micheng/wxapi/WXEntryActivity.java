package com.gxyisikeji.micheng.wxapi;


import android.app.ActivityManager;
import android.content.Context;

import com.mshy.VInterestSpeed.uikit.common.util.log.LogUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import java.util.List;

/**
 * Created by abby on 2016/12/11.
 */

public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    @Override
    public void onResp(BaseResp baseReq) {
        LogUtil.e("baseReq=" + baseReq.getType());
        if (baseReq.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) baseReq;
            String extraData = launchMiniProResp.extMsg;
//            ARouter.getInstance().build(RouteUrl.Bill.BillVquRechargeActivity).navigation();
            finish();
//            ActivityManager.moveTaskToFront(rti.id, ActivityManager.MOVE_TASK_WITH_HOME);
//            overridePendingTransition(0, 0);
            //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性

            //获取ActivityManager
            ActivityManager mAm = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

            //获得当前运行的task
            List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
            LogUtil.e("taskList=" + taskList.size());
            for (ActivityManager.RunningTaskInfo rti : taskList) {
                //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
                LogUtil.e("getPackageName=" + rti.topActivity.getPackageName());
                if (rti.topActivity.getPackageName().equals("com.mshy.VInterestSpeed")) {
                    LogUtil.e("moveTaskToFront(rti=" + rti.topActivity.getPackageName());
                    mAm.moveTaskToFront(rti.id, 0);
                    break;
                }
            }

        } else {
            finish();
        }
    }
}
