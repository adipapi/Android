package com.project.wegourmet.Repository.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.PostModelFirebase;
import com.project.wegourmet.Repository.modelFirbase.UserModelFirebase;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostModel {
    public static final PostModel instance = new PostModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    PostModelFirebase modelFirebase = new PostModelFirebase();

    // Methods
    MutableLiveData<Post> post = new MutableLiveData<Post>();

    public MutableLiveData<Post> getPostsByRestaurant(String restaurantName) {
            executor.execute(() -> {
                Post postsByRestaurant = AppLocalDb.db.postDao().getPostsByRestaurant(restaurantName);

                if(postsByRestaurant != null) {
                    post.postValue(postsByRestaurant);
                }
            });

            modelFirebase.getPostByRestaurant(restaurantName, (fbPostsByRestaurant) -> {
                executor.execute(() -> {
                    AppLocalDb.db.userDao().insert((User) fbPostsByRestaurant);
                    post.postValue((Post) fbPostsByRestaurant);
                });
            });

            return post;
    }

    public void addPost(Post post, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.addPost(post, successListener, failureListener);
    }

    public void setPost(Post editedPost, Runnable success) {
        modelFirebase.setPost(editedPost, (e) -> {
            executor.execute(() -> {
                AppLocalDb.db.postDao().insert(editedPost);
                post.postValue(editedPost);
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
