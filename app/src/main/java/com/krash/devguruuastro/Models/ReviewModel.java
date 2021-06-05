package com.krash.devguruuastro.Models;

public class ReviewModel {
    String message, rating, timestamp, username, reviewID;

    public ReviewModel(String message, String rating, String timestamp, String username, String reviewID) {
        this.message = message;
        this.rating = rating;
        this.timestamp = timestamp;
        this.username = username;
        this.reviewID = reviewID;
    }

    public ReviewModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
}
