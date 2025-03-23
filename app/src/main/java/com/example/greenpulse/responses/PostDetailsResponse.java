package com.example.greenpulse.responses;

import java.util.ArrayList;

public class PostDetailsResponse {
    public String status;
    public String message;
    public int statusCode;
    public Data data;
    public static class Comment{
        public String comment;
        public String createdAt;
        public User user;
        public int id;

        public Comment() {
        }
    }

    public class Data{
        public int postId;
        public String title;
        public String content;
        public String image;
        public String createdAt;
        public User user;
        public ArrayList<Comment> comments;
    }


    public class User{
        public int userId;
        public String username;
        public String email;
        public String dp;
        public String role;
    }


}
