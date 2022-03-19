package com.project.wegourmet.Repository.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.UserModelFirebase;
import com.project.wegourmet.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserModel {
    public static final UserModel instance = new UserModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    UserModelFirebase modelFirebase = new UserModelFirebase();

    // Data Members

    // Methods
    MutableLiveData<User> user = new MutableLiveData<User>();

    public MutableLiveData<User> getUserById(String id) {
            executor.execute(() -> {
                User userById = AppLocalDb.db.userDao().getUserById(id);

                if(userById != null) {
                    user.postValue(userById);
                }
            });

            modelFirebase.getUserByID(id, (fbUser) -> {
                executor.execute(() -> {
                    AppLocalDb.db.userDao().insert((User) fbUser);
                    user.postValue((User) fbUser);
                });
            });

            return user;
    }

    public void addUser(User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.addUser(user, successListener, failureListener);
    }

    public void setUser(User editedUser, Runnable success) {
        modelFirebase.setUser(editedUser, (e) -> {
            executor.execute(() -> {
                AppLocalDb.db.userDao().insert(editedUser);
                user.postValue(editedUser);
                mainThread.post(() -> {
                    success.run();
                });

            });
        });
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }


}
