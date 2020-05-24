package com.online.Lyfe.Online.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class comment_model {
    private String key;
    private String name, pic, comment, image;
    private ArrayList<replay_model> replay_models;
    private HashMap<String, String> likes;

    public HashMap<String, String> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, String> likes) {
        this.likes = likes;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<replay_model> getReplay_models() {
        return replay_models;
    }

    public void setReplay_models(ArrayList<replay_model> replay_models) {
        this.replay_models = replay_models;
    }
}
