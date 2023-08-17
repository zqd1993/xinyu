package com.mshy.VInterestSpeed.uikit.business.recent.holder;

import android.text.TextUtils;

import com.mshy.VInterestSpeed.uikit.business.team.helper.TeamHelper;
import com.mshy.VInterestSpeed.uikit.api.NimUIKit;
import com.mshy.VInterestSpeed.uikit.business.recent.TeamMemberAitHelper;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.model.RecentContact;

public class TeamRecentViewHolder extends CommonRecentViewHolder {

    public TeamRecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    protected String getContent(RecentContact recent) {
        String content = descOfMsg(recent);

        String fromId = recent.getFromAccount();
        if (!TextUtils.isEmpty(fromId)
                && !fromId.equals(NimUIKit.getAccount())
                && !(recent.getAttachment() instanceof NotificationAttachment)) {
            String tid = recent.getContactId();
            String teamNick = getTeamUserDisplayName(tid, fromId);
            content = teamNick + ": " + content;

            if (TeamMemberAitHelper.hasAitExtension(recent)) {
                if (recent.getUnreadCount() == 0) {
                    TeamMemberAitHelper.clearRecentContactAited(recent);
                } else {
                    content = TeamMemberAitHelper.getAitAlertString(content);
                }
            }
        }

        return content;
    }

    protected String getTeamUserDisplayName(String tid, String account) {
        return TeamHelper.getTeamMemberDisplayName(tid, account);
    }
}
