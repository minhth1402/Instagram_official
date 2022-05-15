package com.minhthieu.instagramofficial.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.minhthieu.instagramofficial.Models.SearchRecyclerItem;
import com.minhthieu.instagramofficial.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchRecyclerViewHolder> {

    private List<SearchRecyclerItem> userList;
    private Context mContext;
    private FirebaseUser firebaseUser;

    public SearchRecyclerAdapter(List<SearchRecyclerItem> userList, Context mContext) {
        this.userList = userList;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public SearchRecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.search_recycler_item, parent, false);

        return new SearchRecyclerAdapter.SearchRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchRecyclerAdapter.SearchRecyclerViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SearchRecyclerItem localUser = userList.get(position);

        holder.profileImage.setImageURI(Uri.parse(localUser.getImageUrl()));

        Toast.makeText(mContext, "image uri " + localUser.getImageUrl(), Toast.LENGTH_SHORT).show();
        holder.fullName.setText(localUser.getFullname().toString());
        holder.userName.setText(localUser.getUsername());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class SearchRecyclerViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profileImage;
        public TextView fullName;
        public TextView userName;


        public SearchRecyclerViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            fullName = itemView.findViewById(R.id.fullname_field);
            userName = itemView.findViewById(R.id.user_name_field);


        }
    }


}
