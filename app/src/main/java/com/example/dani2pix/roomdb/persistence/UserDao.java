package com.example.dani2pix.roomdb.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by dani2pix on 9/23/2017.
 */
@Dao
public interface UserDao {

    /**
     * Get the user from the table.
     *
     * @return the user from the table
     */
    @Query("SELECT * FROM users")
    Flowable<List<User>> getUsers();

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM users")
    void deleteAllUsers();

    /**
     * Delete user in the database.
     *
     * @param id the user to be deleted.
     */
    @Query("DELETE FROM users WHERE userId = :id")
    void deleteUser(int id);
}
