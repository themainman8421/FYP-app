package com.example.votingapp;

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
