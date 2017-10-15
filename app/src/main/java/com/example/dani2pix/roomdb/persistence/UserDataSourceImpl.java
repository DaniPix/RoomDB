package com.example.dani2pix.roomdb.persistence;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by dani2pix on 9/23/2017.
 */

public class UserDataSourceImpl implements UserDataSource {

    private final UserDao userDao;

    public UserDataSourceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Flowable<List<User>> getUsers() {
        return userDao.getUsers();
    }

    @Override
    public void insertOrUpdateUser(User user) {
        userDao.insertUser(user);
    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAllUsers();
    }

    @Override
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }
}
