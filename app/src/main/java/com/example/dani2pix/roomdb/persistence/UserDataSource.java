package com.example.dani2pix.roomdb.persistence;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by dani2pix on 9/23/2017.
 */

public interface UserDataSource {

    /**
     * Gets the user from the data source.
     *
     * @return the user from the data source.
     */
    Flowable<List<User>> getUsers();

    /**
     * Inserts the user into the data source, or, if this is an existing user, updates it.
     *
     * @param user the user to be inserted or updated.
     */
    void insertOrUpdateUser(User user);

    /**
     * Deletes all users from the data source.
     */
    void deleteAllUsers();

    /**
     * Delete user from the data source.
     *
     * @param id the user to be deleted.
     */
    void deleteUser(int id);
}
