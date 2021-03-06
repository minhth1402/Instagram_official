package com.minhthieu.instagramofficial.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhthieu.instagramofficial.Models.PostItem;
import com.minhthieu.instagramofficial.Models.SearchRecyclerItem;
import com.minhthieu.instagramofficial.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostItemAdapter extends RecyclerView.Adapter<PostItemAdapter.PostItemViewHolder> {
    private Context mContext;
    private List<PostItem> postList;

    private FirebaseUser user;

    public PostItemAdapter(Context mContext, List<PostItem> postList) {
        this.mContext = mContext;
        this.postList = postList;
    }

    @NonNull
    @NotNull
    @Override
    public PostItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);

        return new PostItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostItemAdapter.PostItemViewHolder holder, int position) {


        PostItem post = postList.get(position);

        Picasso.get().load(post.getImageUrl()).into(holder.postImage);

        // nhảy tới cái vị trí user trên firebase Database
        FirebaseDatabase.getInstance().getReference("Users").child(post.getPublisher())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        SearchRecyclerItem userInfo = snapshot.getValue(SearchRecyclerItem.class);

                        holder.fullName.setText(userInfo.getFullname());

                        holder.profileImage.setImageURI(Uri.parse(userInfo.getImageUrl()));
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        // ContainOnClickListener
        ContainOnClickListener(holder, post);

    }

    private void ContainOnClickListener(PostItemAdapter.PostItemViewHolder holder, PostItem post) {
        holder.likeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLiked(post.getPostId(),holder.likeBt);
                LikesCounter(post.getPostId(), holder.likeCount);

                if (holder.likeBt.getTag()=="like") {
                    FirebaseDatabase.getInstance().getReference("Likes").child(post.getPostId()).child(user.getUid())
                            .setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference("Likes").child(post.getPostId()).child(user.getUid())
                            .setValue(null);

                }
            }
        });


    }

    private void isLiked(String postId, ImageView likeBt) {
        FirebaseDatabase.getInstance().getReference("Likes").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child(user.getUid()).exists()){
                    likeBt.setImageResource(R.drawable.ic_baseline_favorite_24);
                    likeBt.setTag("liked");
                } else{
                    likeBt.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    likeBt.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    void LikesCounter(String postId, TextView likeCount){
        FirebaseDatabase.getInstance().getReference("Likes").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                likeCount.setText(String.valueOf(snapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public class PostItemViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView profileImage;
        public TextView fullName;
        public ImageView likeBt;
        public ImageView commentBt;
        public TextView likeCount;
        public ImageView postImage;

        public PostItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            fullName = itemView.findViewById(R.id.user_name);
            likeBt = itemView.findViewById(R.id.like_bt);
            commentBt = itemView.findViewById(R.id.comment_bt);
            likeCount = itemView.findViewById(R.id.like_count);
            postImage = itemView.findViewById(R.id.post_image);

            // firebase
            user = FirebaseAuth.getInstance().getCurrentUser();


        }
    }



}
