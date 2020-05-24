package com.online.Lyfe.Online.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class blog_model {
    private String name, date, dec, image, pic, id, key;
    private ArrayList<String> comment;
    private HashMap<String, String> likes;
    private String owner_nmae, owner_pic, owner_id;

    public HashMap<String, String> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void setComment(ArrayList<String> comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public blog_model() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
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


    // owber info <On sharing post >

    public String getOwner_nmae() {
        return owner_nmae;
    }

    public void setOwner_nmae(String owner_nmae) {
        this.owner_nmae = owner_nmae;
    }

    public String getOwner_pic() {
        return owner_pic;
    }

    public void setOwner_pic(String owner_pic) {
        this.owner_pic = owner_pic;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}
