package com.project.wegourmet.Repository.modelFirbase;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.wegourmet.model.User;


public class UserModelFirebase {
    final public static String COLLECTION_NAME = "users";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Methods
    public void addUser(User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        String id = user.getId();
        db.collection(COLLECTION_NAME).document(id).set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
