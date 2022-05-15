package com.minhthieu.instagramofficial.Models;

public class SearchRecyclerItem {

    // mấy cái key name phải match vs bên firebase

    private String Username;
    private String Password;
    private String Email;
    private String Fullname;
    private String UserID;
    private String imageUrl;

    public SearchRecyclerItem() {
    }

    public SearchRecyclerItem(String username, String password, String email, String fullname, String userID, String imageUrl) {
        Username = username;
        Password = password;
        Email = email;
        Fullname = fullname;
        UserID = userID;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {

        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
