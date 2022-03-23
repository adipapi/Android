package com.project.wegourmet.Repository.modelFirbase;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Restaurant;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class RestaurantModelFirebase {
    final public static String COLLECTION_NAME = "restaurants";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    public RestaurantModelFirebase(){
//        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                .setPersistenceEnabled(false)
//                .build();
//        db.setFirestoreSettings(settings);
//    }

    public interface GetAllRestaurantsListener{
        void onComplete(List<Restaurant> list);
    }

    // Methods
    public void setRestaurant(Restaurant restaurant,OnSuccessListener successListener) {
        db.collection(Restaurant.COLLECTION_NAME).document(restaurant.getId()).set(restaurant).addOnSuccessListener(successListener);
    }

    // Methods
    public void addRestaurant(Restaurant restaurant,OnSuccessListener successListener) {
        DocumentReference ref = db.collection(COLLECTION_NAME).document();
        restaurant.setId(ref.getId());
        ref.set(restaurant)
                .addOnSuccessListener(successListener);
    }

    public void getAllRestaurants(RestaurantModelFirebase.GetAllRestaurantsListener listener) {
        db.collection(Restaurant.COLLECTION_NAME)
//                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Restaurant> list = new LinkedList<Restaurant>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Restaurant restaurant = Restaurant.create(doc.getData());
                            if (restaurant != null){
                                list.add(restaurant);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void getRestaurantsByHost(String hostId, RestaurantModelFirebase.GetAllRestaurantsListener listener) {
        db.collection(Restaurant.COLLECTION_NAME)
                .whereEqualTo("hostId", hostId )
//                .whereGreaterThanOrEqualTo("updateDate",new Timestamp(lastUpdateDate,0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Restaurant> list = new LinkedList<Restaurant>();
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot doc : task.getResult()){
                            Restaurant restaurant = Restaurant.create(doc.getData());
                            if (restaurant != null){
                                list.add(restaurant);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }


    public void getRestaurantById(String restaurantId, RestaurantModel.GetRestaurantById listener) {
        db.collection(Restaurant.COLLECTION_NAME)
                .document(restaurantId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Restaurant restaurant = null;
                        if (task.isSuccessful() & task.getResult()!= null && task.getResult().getData() != null){
                            restaurant = Restaurant.create(task.getResult().getData());
                        }
                        listener.onComplete(restaurant);
                    }
                });

    }

//    public void getRestaurantByName(String restaurantName, OnSuccessListener listener) {
//        db.collection(Restaurant.COLLECTION_NAME)
//                .document(restaurantId)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        Restaurant restaurant = null;
//                        if (task.isSuccessful() & task.getResult()!= null){
//                            restaurant = Restaurant.create(task.getResult().getData());
//                        }
//                        listener.onComplete(restaurant);
//                    }
//                });
//
//    }


    public void saveImage(Bitmap imageBitmap, String imageName, RestaurantModel.SaveImageListener listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference imgRef = storageRef.child("restaurants_images/" + imageName);

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




