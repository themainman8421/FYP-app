package com.example.votingapp;

//class that get the user info from requests
public class User {

    private String email;

    private String _id;

    public User(String email, String _id) {
        this.email = email;
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public String get_id() {
        return _id;
    }
}
