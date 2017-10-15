package com.example.dani2pix.roomdb.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.dani2pix.roomdb.Injection;
import com.example.dani2pix.roomdb.R;
import com.example.dani2pix.roomdb.persistence.User;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private UserViewModel userViewModel;
    private ViewModelFactory viewModelFactory;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Button insertUsers;
    private Button displayUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertUsers = findViewById(R.id.insertUsers);
        displayUsers = findViewById(R.id.displayUsers);
        insertUsers.setOnClickListener(this);
        displayUsers.setOnClickListener(this);

        viewModelFactory = Injection.provideViewModelFactory(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void getAllUsers() {
        compositeDisposable.add(userViewModel.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        for (User user : users) {
                            Log.d(TAG, user.getUsername());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getMessage(), throwable);
                    }
                }));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.insertUsers) {

            for (int i = 0; i < 10; i++) {

                User user = new User();
                user.setUserId(i);
                user.setUsername("Dan" + i);
                user.setLocation("Stockholm" + i);
                user.setAge(i + 26);

                insertUser(user);
            }
        } else {
            getAllUsers();
        }
    }

    private void insertUser(User user) {
        compositeDisposable.add(userViewModel.insertUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }
}
