package com.minhthieu.instagramofficial.Models;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerItem {

    private String imageUrl;
    private String fullName;
    private String userName;
    private List<String> demoImageUrl = new ArrayList<>();
    private String UID;


    public HomeRecyclerItem(String imageUrl, String fullName, String userName, List<String> demoImageUrl, String UID) {
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.userName = userName;
        this.demoImageUrl = demoImageUrl;
        this.UID = UID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getDemoImageUrl() {
        return demoImageUrl;
    }

    public void setDemoImageUrl(List<String> demoImageUrl) {
        this.demoImageUrl = demoImageUrl;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
