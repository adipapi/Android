package com.project.wegourmet.Repository.modelFirbase;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.model.User;

import java.io.ByteArrayOutputStream;


public class UserModelFirebase {
    final public static String COLLECTION_NAME = "users";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Methods
    public void addUser(User user, OnSuccessListener successListener, OnFailureListener failureListener) {
        String id = user.getId();
        db.collection(COLLECTION_NAME).document(id).set(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void setUser(User user, OnSuccessListener listener) {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection(COLLECTION_NAME)
                .whereEqualTo("id", id)
                .get().addOnSuccessListener((querySnapshot) -> {
                   querySnapshot.getDocuments().get(0).getReference().set(user)
                   .addOnSuccessListener(listener);
                });
    }

    public void getUserByID(String id,OnSuccessListener successListener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .addOnSuccessListener((querySnapshot) -> {
                    User user = querySnapshot.getDocuments().get(0).toObject(User.class);

                    if (user != null) {
                        successListener.onSuccess(user);
                    }
                });
    };


    public void saveImage(Bitmap imageBitmap, String imageName, UserModel.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("user_profile_images/" + imageName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imgRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            Uri downloadUrl = uri;
                            listener.onComplete(downloadUrl.toString());
                        });
                    }
                });
    }
}
