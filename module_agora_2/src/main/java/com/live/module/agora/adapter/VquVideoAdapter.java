package com.live.module.agora.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.gson.Gson;
import com.live.module.agora.R;
import com.live.module.agora.bean.AgoraVquNoticeBean;
import com.live.module.agora.bean.AgoraVquNoticeGiftBean;
import com.live.module.agora.bean.AgoraVquReduceSuccess;
import com.mshy.VInterestSpeed.common.bean.websocket.WebSocketChatBean;
import com.mshy.VInterestSpeed.common.constant.NetBaseUrlConstant;
import com.mshy.VInterestSpeed.uikit.impl.cache.NimUserInfoCache;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VquVideoAdapter extends BaseQuickAdapter<WebSocketChatBean<AgoraVquNoticeBean>, BaseViewHolder> {
    private final String nickName;
    private String USER_ID;
    //默认气泡地址
    private final String defaultBubble = NetBaseUrlConstant.INSTANCE.getIMAGE_URL() + "/v1.3/dress/bubble/lALPD3zUOqg7eJA7bg_110_59.png";

    public VquVideoAdapter(@Nullable List<WebSocketChatBean<AgoraVquNoticeBean>> data
            , String nickName, String user_id) {
        super(R.layout.agora_tanta_item_video_chat, data);
        this.nickName = nickName;
        this.USER_ID = user_id;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull @NotNull BaseViewHolder helper, final WebSocketChatBean<AgoraVquNoticeBean> noticeBean) {
        TextView chatView;
        View giftView;
        TextView giftTextView;
        TextView giftCount;
        chatView = helper.getView(R.id.video_chat_text);
        giftView = helper.getView(R.id.video_chat_gift_layout);
        giftTextView = helper.getView(R.id.video_chat_gift_name);
        giftCount = helper.getView(R.id.video_chat_gift_count);

        if (noticeBean.getData().getType() == 3) {
            giftView.setVisibility(View.VISIBLE);
            chatView.setVisibility(View.GONE);
            Gson gson = new Gson();
            AgoraVquNoticeGiftBean noticeGiftBean = gson.fromJson(noticeBean.getData().getContent(),
                    AgoraVquNoticeGiftBean.class);
            if (noticeBean.getData().getFrom_uid()
                    .equals(USER_ID)) {
                giftTextView.setText("我 ");
                giftTextView.setTextColor(Color.parseColor("#FF8AEA"));
            } else {
                giftTextView.setText(nickName + " ");
                giftTextView.setTextColor(Color.parseColor("#FFDD8D"));
            }
            giftCount.setText("赠送了" + noticeGiftBean.getGiftcount() + "个" + noticeGiftBean.getGiftname());
          //  LoadDian9TuUtil.loadDian9Tu(getContext(), giftView, defaultBubble);
        } else {
            giftView.setVisibility(View.GONE);
            chatView.setVisibility(View.VISIBLE);
            if (noticeBean.getData().getType() == 1 || noticeBean.getData().getType() == 6) {
                StringBuilder content = new StringBuilder("系统消息：");
                if (noticeBean.getData().getType() == 6) {
                    AgoraVquReduceSuccess reduceBean = new Gson().fromJson(noticeBean.getData().getContent(),
                            AgoraVquReduceSuccess.class);
                    content.append("已减免")
                            .append(reduceBean.getReducetime())
                            .append("分钟,预计节约")
                            .append(reduceBean.getReducecost())
                            .append("金币。");
                } else {
                    content.append(noticeBean.getData().getContent());
                }
                chatView.setText(content);
                chatView.setTextColor(Color.parseColor("#FFACDB"));
             //   LoadDian9TuUtil.loadDian9Tu(getContext(), chatView, defaultBubble);
            } else {
                getBubble(chatView, noticeBean.getData().getFrom_uid());
                if (noticeBean.getData().getFrom_uid().equals(USER_ID)) {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder("我：" +
                            noticeBean.getData().getContent());
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFF8AEA"));
                    stringBuilder.setSpan(colorSpan, 0, 2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    chatView.setText(stringBuilder);
                } else {
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder(nickName + "："
                            + noticeBean.getData().getContent());
                    ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFDD8D"));
                    stringBuilder.setSpan(colorSpan, 0, nickName.length() + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    chatView.setText(stringBuilder);
                }
            }
        }
        addChildClickViewIds(R.id.video_chat_text);

    }

    private void getBubble(final View view, String userId) {
        NimUserInfoCache.getInstance().getUserInfoFromRemote(userId
                , new RequestCallback<NimUserInfo>() {
                    @Override
                    public void onSuccess(NimUserInfo nimUserInfo) {
                        if (nimUserInfo != null && nimUserInfo.getExtensionMap() != null) {
                            String bubble = (String) nimUserInfo.getExtensionMap().get("bubble");
                            //LoadDian9TuUtil.loadDian9Tu(mContext, view, ConstUrl.getImageUrl() + bubble);
                        }
                    }

                    @Override
                    public void onFailed(int i) {
                    }

                    @Override
                    public void onException(Throwable throwable) {
                    }
                });
    }


}
