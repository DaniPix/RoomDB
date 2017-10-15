package com.example.dani2pix.roomdb.ui;

import android.arch.lifecycle.ViewModel;

import com.example.dani2pix.roomdb.persistence.User;
import com.example.dani2pix.roomdb.persistence.UserDataSource;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;
import io.reactivex.internal.operators.completable.CompletableFromAction;

/**
 * Created by dani2pix on 9/23/2017.
 */

public class UserViewModel extends ViewModel {

    private UserDataSource userDataSource;
    private User user;

    public UserViewModel(UserDataSource userDataSource) {
        this.userDataSource = userDataSource;
    }

    public Flowable<List<User>> getUsers() {
        return userDataSource.getUsers();
    }

    public Completable insertUser(final User user) {
        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                userDataSource.insertOrUpdateUser(user);
            }
        });
    }
}
