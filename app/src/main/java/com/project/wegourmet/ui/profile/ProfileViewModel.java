package com.project.wegourmet.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.model.User;

public class ProfileViewModel extends ViewModel {

    public MutableLiveData<User> user = new MutableLiveData<>();

    public ProfileViewModel() {
    }

    public void getUserById(String id) {
        user = UserModel.instance.getUserById(id);
    }

    public void setUser(User user, Runnable success) {
        UserModel.instance.setUser(user, success);
    }
}