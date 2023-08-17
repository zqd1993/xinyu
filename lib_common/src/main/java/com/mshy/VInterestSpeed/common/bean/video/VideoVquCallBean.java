package com.mshy.VInterestSpeed.common.bean.video;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoVquCallBean implements Parcelable {
    private String room_id;
    private int from_uid;
    private String from_nickname;
    private String from_avatar;
    private int to_uid;
    private String to_nickname;
    private String to_avatar;
    private int is_live;
    private int chat_open_reduce;
    private String socket_url;
    private VideoVquCallSkillBean skill;
    private VideoVquPornCheckBean porn_check;
    private VideoVquPornCheckBeanV2 porn_check_v2;
    private VideoVquGuardBean guard;
    private VideoVquFairyBean fairy;
    private String from_age;
    private int from_gender;
    private String to_age;
    private int to_gender;
    private String from_height;
    private String from_weight;
    private String to_height;
    private String to_weight;
    private String from_age_text;
    private String to_age_text;
    private String status;
    private String type;
    private String from_city;
    private String from_occupation;
    private String to_city;
    private String to_occupation;

    protected VideoVquCallBean(Parcel in) {
        room_id = in.readString();
        from_uid = in.readInt();
        from_nickname = in.readString();
        from_avatar = in.readString();
        to_uid = in.readInt();
        to_nickname = in.readString();
        to_avatar = in.readString();
        is_live = in.readInt();
        chat_open_reduce = in.readInt();
        socket_url = in.readString();
        skill = in.readParcelable(VideoVquCallSkillBean.class.getClassLoader());
        porn_check = in.readParcelable(VideoVquPornCheckBean.class.getClassLoader());
        porn_check_v2 = in.readParcelable(VideoVquPornCheckBeanV2.class.getClassLoader());
        guard = in.readParcelable(VideoVquGuardBean.class.getClassLoader());
        fairy = in.readParcelable(VideoVquFairyBean.class.getClassLoader());
        from_age = in.readString();
        from_gender = in.readInt();
        to_age = in.readString();
        to_gender = in.readInt();
        from_height = in.readString();
        from_weight = in.readString();
        to_height = in.readString();
        to_weight = in.readString();
        from_age_text = in.readString();
        to_age_text = in.readString();
        status = in.readString();
        type = in.readString();
        from_city=in.readString();
        from_occupation = in.readString();
        to_city = in.readString();
        to_occupation = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(room_id);
        dest.writeInt(from_uid);
        dest.writeString(from_nickname);
        dest.writeString(from_avatar);
        dest.writeInt(to_uid);
        dest.writeString(to_nickname);
        dest.writeString(to_avatar);
        dest.writeInt(is_live);
        dest.writeInt(chat_open_reduce);
        dest.writeString(socket_url);
        dest.writeParcelable(skill, flags);
        dest.writeParcelable(porn_check, flags);
        dest.writeParcelable(porn_check_v2, flags);
        dest.writeParcelable(guard, flags);
        dest.writeParcelable(fairy, flags);
        dest.writeString(from_age);
        dest.writeInt(from_gender);
        dest.writeString(to_age);
        dest.writeInt(to_gender);
        dest.writeString(from_height);
        dest.writeString(from_weight);
        dest.writeString(to_height);
        dest.writeString(to_weight);
        dest.writeString(from_age_text);
        dest.writeString(to_age_text);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeString(from_city);
        dest.writeString(from_occupation);
        dest.writeString(to_city);
        dest.writeString(to_occupation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideoVquCallBean> CREATOR = new Creator<VideoVquCallBean>() {
        @Override
        public VideoVquCallBean createFromParcel(Parcel in) {
            return new VideoVquCallBean(in);
        }

        @Override
        public VideoVquCallBean[] newArray(int size) {
            return new VideoVquCallBean[size];
        }
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrom_age_text() {
        return from_age_text;
    }

    public void setFrom_age_text(String from_age_text) {
        this.from_age_text = from_age_text;
    }

    public String getTo_age_text() {
        return to_age_text;
    }

    public void setTo_age_text(String to_age_text) {
        this.to_age_text = to_age_text;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(int from_uid) {
        this.from_uid = from_uid;
    }

    public String getFrom_nickname() {
        return from_nickname;
    }

    public void setFrom_nickname(String from_nickname) {
        this.from_nickname = from_nickname;
    }

    public String getFrom_avatar() {
        return from_avatar;
    }

    public void setFrom_avatar(String from_avatar) {
        this.from_avatar = from_avatar;
    }

    public int getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(int to_uid) {
        this.to_uid = to_uid;
    }

    public String getTo_nickname() {
        return to_nickname;
    }

    public void setTo_nickname(String to_nickname) {
        this.to_nickname = to_nickname;
    }

    public String getTo_avatar() {
        return to_avatar;
    }

    public void setTo_avatar(String to_avatar) {
        this.to_avatar = to_avatar;
    }

    public int getIs_live() {
        return is_live;
    }

    public void setIs_live(int is_live) {
        this.is_live = is_live;
    }

    public int getChat_open_reduce() {
        return chat_open_reduce;
    }

    public void setChat_open_reduce(int chat_open_reduce) {
        this.chat_open_reduce = chat_open_reduce;
    }

    public String getSocket_url() {
        return socket_url;
    }

    public void setSocket_url(String socket_url) {
        this.socket_url = socket_url;
    }

    public VideoVquCallSkillBean getSkill() {
        return skill;
    }

    public void setSkill(VideoVquCallSkillBean skill) {
        this.skill = skill;
    }

    public VideoVquPornCheckBean getPorn_check() {
        return porn_check;
    }

    public void setPorn_check(VideoVquPornCheckBean porn_check) {
        this.porn_check = porn_check;
    }

    public VideoVquPornCheckBeanV2 getPorn_check_v2() {
        return porn_check_v2;
    }

    public void setPorn_check_v2(VideoVquPornCheckBeanV2 porn_check_v2) {
        this.porn_check_v2 = porn_check_v2;
    }

    public VideoVquGuardBean getGuard() {
        return guard;
    }

    public void setGuard(VideoVquGuardBean guard) {
        this.guard = guard;
    }

    public VideoVquFairyBean getFairy() {
        return fairy;
    }

    public void setFairy(VideoVquFairyBean fairy) {
        this.fairy = fairy;
    }

    public String getFrom_age() {
        return from_age;
    }

    public void setFrom_age(String from_age) {
        this.from_age = from_age;
    }

    public int getFrom_gender() {
        return from_gender;
    }

    public void setFrom_gender(int from_gender) {
        this.from_gender = from_gender;
    }

    public String getTo_age() {
        return to_age;
    }

    public void setTo_age(String to_age) {
        this.to_age = to_age;
    }

    public int getTo_gender() {
        return to_gender;
    }

    public void setTo_gender(int to_gender) {
        this.to_gender = to_gender;
    }

    public String getFrom_height() {
        return from_height;
    }

    public void setFrom_height(String from_height) {
        this.from_height = from_height;
    }

    public String getFrom_weight() {
        return from_weight;
    }

    public void setFrom_weight(String from_weight) {
        this.from_weight = from_weight;
    }

    public String getTo_height() {
        return to_height;
    }

    public void setTo_height(String to_height) {
        this.to_height = to_height;
    }

    public String getTo_weight() {
        return to_weight;
    }

    public void setTo_weight(String to_weight) {
        this.to_weight = to_weight;
    }


    public String getFrom_city() {
        return from_city;
    }

    public void setFrom_city(String from_city) {
        this.from_city = from_city;
    }

    public String getFrom_occupation() {
        return from_occupation;
    }

    public void setFrom_occupation(String from_occupation) {
        this.from_occupation = from_occupation;
    }

    public String getTo_city() {
        return to_city;
    }

    public void setTo_city(String to_city) {
        this.to_city = to_city;
    }

    public String getTo_occupation() {
        return to_occupation;
    }

    public void setTo_occupation(String to_occupation) {
        this.to_occupation = to_occupation;
    }
}
