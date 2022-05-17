package com.minhthieu.instagramofficial.Models;

public class PostItem {
    private String imageCaption;
    private String imageUrl;
    private int likeCount;
    private String postId;
    private String publisher;

    public PostItem() {
    }

    public PostItem(String imageCaption, String imageUrl, int likeCount, String postId, String publisher) {
        this.imageCaption = imageCaption;
        this.imageUrl = imageUrl;
        this.likeCount = likeCount;
        this.postId = postId;
        this.publisher = publisher;
    }

    public String getImageCaption() {
        return imageCaption;
    }

    public void setImageCaption(String imageCaption) {
        this.imageCaption = imageCaption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
