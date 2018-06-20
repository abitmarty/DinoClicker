package com.example.martijn.toolbar;

public class User {
    private String Name;
    private String Score;

    public User(){
    }

    public User(String name, String score) {
        Name = name;
        Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }
}
