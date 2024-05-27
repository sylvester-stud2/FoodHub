package com.example.foodhub;

import androidx.recyclerview.widget.RecyclerView;

public class Comment {
    private String username;
    private String comment;
    private String createdAt;

    public Comment(community community, RecyclerView viewById) {
        // Default constructor

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setData(String createdAt) {

    }

    public void setTextContent(String comment) {

    }

    public void fetchComments(int postId) {

    }
}
