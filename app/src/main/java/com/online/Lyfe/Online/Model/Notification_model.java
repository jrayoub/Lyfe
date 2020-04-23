package com.online.Lyfe.Online.Model;

public class Notification_model {
    String userid;
    String text;
    String postid;
    Boolean ispost;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public Boolean getIspost() {
        return ispost;
    }

    public void setIspost(Boolean ispost) {
        this.ispost = ispost;
    }
}
