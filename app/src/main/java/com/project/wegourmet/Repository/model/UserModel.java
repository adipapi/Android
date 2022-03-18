package com.project.wegourmet.Repository.model;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.UserModelFirebase;
import com.project.wegourmet.model.User;

public class UserModel {
    public static final UserModel instance = new UserModel();

    UserModelFirebase modelFirebase = new UserModelFirebase();
    // Data Members

    // Methods
    public void addUser(User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.addUser(user, successListener, failureListener);
    }
}
