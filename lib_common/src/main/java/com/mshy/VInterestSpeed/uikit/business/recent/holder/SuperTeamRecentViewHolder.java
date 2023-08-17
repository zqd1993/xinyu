package com.mshy.VInterestSpeed.uikit.business.recent.holder;

import com.mshy.VInterestSpeed.uikit.business.team.helper.TeamHelper;
import com.mshy.VInterestSpeed.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;

public class SuperTeamRecentViewHolder extends TeamRecentViewHolder {

    public SuperTeamRecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    @Override
    public String getTeamUserDisplayName(String tid, String account) {
        return TeamHelper.getSuperTeamMemberDisplayName(tid, account);
    }
}
