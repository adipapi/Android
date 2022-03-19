package com.project.wegourmet.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.model.User;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<User> user = new MutableLiveData<>();

//    public ProfileViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is notifications fragment");
//    }

    public LiveData<User> getUser() {
//        if(user == null) {
//            user = UserModel.instance.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid(),
//                    (resUser) -> {
//                        user = resUser;
//                    }, (e) -> {
//
//                    });
//        }
//
        return user;
    }
}