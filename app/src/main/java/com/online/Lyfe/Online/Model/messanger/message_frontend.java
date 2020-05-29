package com.online.Lyfe.Online.Model.messanger;

public class message_frontend {
    private String MYNAME, name, MY_pic, pic, lastMessage;
    private boolean seeing, opened;
    private String id, KEY;

    public String getKEY() {
        return KEY;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public String getMYNAME() {
        return MYNAME;
    }

    public String getMY_pic() {
        return MY_pic;
    }

    public void setMY_pic(String MY_pic) {
        this.MY_pic = MY_pic;
    }

    public void setMYNAME(String MYNAME) {
        this.MYNAME = MYNAME;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public boolean isSeeing() {
        return seeing;
    }

    public void setSeeing(boolean seeing) {
        this.seeing = seeing;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
