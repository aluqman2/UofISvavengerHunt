package com.example.uofisvavengerhunt;

import com.google.android.gms.location.Geofence;

import java.util.List;
import java.util.ArrayList;

public class User {

    private int score = 0;
    private String name;
    private ArrayList<Geofence> visited = new ArrayList<>();

    public User() {
        name = "Default";
    }

    public User(String newName) {
        this.name = newName;
    }

    public void addScore(Geofence fence) {
        if (visited.contains(fence)) {
            return;
        }
        this.visited.add(fence);
        this.score++;
    }
}
