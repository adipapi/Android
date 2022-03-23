package com.project.wegourmet.Repository.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.PostModelFirebase;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PostModel {
    public static final PostModel instance = new PostModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    PostModelFirebase modelFirebase = new PostModelFirebase();

    // Methods
    MutableLiveData<Post> post = new MutableLiveData<Post>();
    MutableLiveData<List<Post>> posts = new MutableLiveData<List<Post>>();


    public MutableLiveData<List<Post>>
    getPostsByRestaurant(String restaurantName) {
            executor.execute(() -> {
                List<Post> postsByRestaurant = AppLocalDb.db.postDao().getPostsByRestaurant(restaurantName);

                if(postsByRestaurant != null) {
                    posts.postValue(postsByRestaurant);
                }
            });

            modelFirebase.getPostByRestaurant(restaurantName, (fbPostsByRestaurant) -> {
                executor.execute(() -> {
                    AppLocalDb.db.postDao().insertMany((List<Post>) fbPostsByRestaurant);
                    posts.postValue((List<Post>) fbPostsByRestaurant);
                });
            });

            return posts;
    }

    public void addPost(Post post, OnSuccessListener successListener, OnFailureListener failureListener) {
        modelFirebase.addPost(post, successListener, failureListener);
    }

    public void delete(Post deletedPost, Runnable success) {
        deletedPost.setDeleted(true);
        modelFirebase.setPost(deletedPost, (e) -> {
            executor.execute(() -> {
                AppLocalDb.db.postDao().delete(deletedPost);
                post.postValue(deletedPost);
                mainThread.post(() -> {
                    success.run();
                });
            });
        });
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


    public MutableLiveData<Post> getPostById(String id) {
        executor.execute(() -> {
            Post postById = AppLocalDb.db.postDao().getById(id);

            if(postById != null) {
                post.postValue(postById);
            }
        });

        modelFirebase.getPostByID(id, (fbPost) -> {
            executor.execute(() -> {
                AppLocalDb.db.postDao().insert((Post) fbPost);
                post.postValue((Post) fbPost);
            });
        });

        return post;
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }


}
