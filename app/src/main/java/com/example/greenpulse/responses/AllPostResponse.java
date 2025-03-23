package com.example.greenpulse.responses;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AllPostResponse {
    public String status;
    public String message;
    public int statusCode;
    public ArrayList<Datum> data;
    public class Datum{
        public int postId;
        public String title;
        public String content;
        public String image;
        public String createdAt;
        public int userId;
        public int commentCount;
    }
}
