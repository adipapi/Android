package com.project.wegourmet.ui.restaurant;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wegourmet.R;
import com.project.wegourmet.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<ViewHolder>{

    public List<Post> posts;
    OnPostClickListener listener;
    OnPostClickListener deleteListener;

    public void setOnEditClickListener(OnPostClickListener listener){
        this.listener = listener;
    }
    public void setOnDeleteClickListener(OnPostClickListener listener){
        this.deleteListener = listener;
    }

    // data is passed into the constructor
    PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_in_grid,parent,false);
        ViewHolder holder = new ViewHolder(view,listener, deleteListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        if(posts == null){
            return 0;
        }
        return posts.size();
    }
}

interface OnPostClickListener{
    void onItemClick(View v,int position);
}

// stores and recycles views as they are scrolled off screen
class ViewHolder extends RecyclerView.ViewHolder{
    ImageView postImage;
    ImageButton deleteBtn;

    public ViewHolder(View itemView, OnPostClickListener listener, OnPostClickListener deleteListener) {
        super(itemView);
        postImage = itemView.findViewById(R.id.post_in_list);
        deleteBtn = itemView.findViewById(R.id.delete_post_btn);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(view,pos);
            }
        });

        if(deleteListener != null) {
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    deleteListener.onItemClick(view, pos);
                }
            });
        } else {
            deleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    void bind(Post post){
        if (post.getImageUrl() != null) {
            Picasso.get()
                    .load(post.getImageUrl())
                    .into(postImage);
        }
    }
}
