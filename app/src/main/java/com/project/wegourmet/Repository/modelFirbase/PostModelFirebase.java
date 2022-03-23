package com.project.wegourmet.Repository.modelFirbase;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.User;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;


public class PostModelFirebase {
    final public static String COLLECTION_NAME = "posts";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Methods
    public void addPost(Post post, OnSuccessListener successListener, OnFailureListener failureListener) {
        DocumentReference ref = db.collection(COLLECTION_NAME).document();
        post.setId(ref.getId());
        ref.set(post)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    public void setPost(Post post, OnSuccessListener listener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("id", post.getId())
                .get().addOnSuccessListener((querySnapshot) -> {
                   querySnapshot.getDocuments().get(0).getReference().set(post)
                   .addOnSuccessListener(listener);
                });
    }

    public void getPostByRestaurant(String restaurantName,OnSuccessListener successListener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("restaurantName", restaurantName)
                .whereEqualTo("deleted", false)
                .get()
                .addOnSuccessListener((querySnapshot) -> {
                    List<Post> list = new LinkedList<Post>();

                    for (QueryDocumentSnapshot doc : querySnapshot){
                        Post post = Post.create(doc.getData());
                        if (post != null){
                            list.add(post);
                        }
                    }

                    successListener.onSuccess(list);
                });
    };


    public void getPostByID(String id,OnSuccessListener successListener) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("id", id)
                .limit(1)
                .get()
                .addOnSuccessListener((querySnapshot) -> {
                    Post post = querySnapshot.getDocuments().get(0).toObject(Post.class);

                    if (post != null) {
                        successListener.onSuccess(post);
                    }
                });
    };

    public void saveImage(Bitmap imageBitmap, String imageName, PostModel.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("posts_images/" + imageName);

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
