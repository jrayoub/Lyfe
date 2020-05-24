package com.online.Lyfe.Online.Model;

import java.util.HashMap;

public class replay_model {
    private String key;
    private String name, pic, replay, image;
    private HashMap<String, String> likes;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HashMap<String, String> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, String> likes) {
        this.likes = likes;
    }
}
