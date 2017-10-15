package com.example.dani2pix.roomdb.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by dani2pix on 9/23/2017.
 */
@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "location")
    private String location;


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
