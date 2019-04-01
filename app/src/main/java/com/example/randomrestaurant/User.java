package com.example.randomrestaurant;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class User {
    private String UserId;
    private String UserName;
    private List<String> restaurants;
    public User(){
        //this constructor is required
    }

    public User(String UserId, String UserName) {
        this.UserId = UserId;
        this.UserName = UserName;
    }
    public User(String UserId, String UserName, List<String> favorites ) {
        this.UserId = UserId;
        this.UserName = UserName;
        this.restaurants = favorites;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public List<String> getFavoritesList(){
        return restaurants;
    }
    public void setFavorites(List<String> favorites){
        this.restaurants = favorites;
    }
    public void addRestaurant(String restaurant){
        restaurants.add(restaurant);
    }

    public void deleteRestaurant(String restaurant){
        restaurants.remove(restaurant);
    }

}

