package com.online.Lyfe.Online.Model;

public class freindlist {
    String fullnaame;
    String email;
    String id;

    public String getFullnaame() {
        return fullnaame;
    }

    public void setFullnaame(String fullnaame) {
        this.fullnaame = fullnaame;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public freindlist(String fullnaame, String email, String id) {
        this.fullnaame = fullnaame;
        this.email = email;
        this.id = id;
    }

    public freindlist() {
    }

}
