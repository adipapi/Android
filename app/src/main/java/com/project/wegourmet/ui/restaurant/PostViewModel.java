package com.project.wegourmet.ui.restaurant;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.model.Post;

public class PostViewModel extends ViewModel {

    public MutableLiveData<Post> post = new MutableLiveData<>();

    public PostViewModel() {
    }

    public void getPostById(String id) {
        post = PostModel.instance.getPostById(id);
    }

    public void setPost(Post post, Runnable success) {
        PostModel.instance.setPost(post, success);
    }
}