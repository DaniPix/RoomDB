package com.example.dani2pix.roomdb;

import android.content.Context;

import com.example.dani2pix.roomdb.persistence.AppDatabase;
import com.example.dani2pix.roomdb.persistence.UserDataSource;
import com.example.dani2pix.roomdb.persistence.UserDataSourceImpl;
import com.example.dani2pix.roomdb.ui.ViewModelFactory;

/**
 * Created by dani2pix on 9/23/2017.
 */

public class Injection {

    public static UserDataSource provideUserDataSource(Context context) {
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return new UserDataSourceImpl(appDatabase.userDao());
    }

    public static ViewModelFactory provideViewModelFactory(Context context) {
        UserDataSource dataSource = provideUserDataSource(context);
        return new ViewModelFactory(dataSource);
    }
}
