package com.project.wegourmet.Repository.model;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.UserModelFirebase;
import com.project.wegourmet.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserModel {
    public static final UserModel instance = new UserModel();

    Executor executor = Executors.newSingleThreadExecutor();
    UserModelFirebase modelFirebase = new UserModelFirebase();

    // Data Members

    // Methods
    public void getUserById(String id, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.getUserByID(id, successListener, failureListener);
    }

    public void addUser(User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.addUser(user, successListener, failureListener);
    }
}
