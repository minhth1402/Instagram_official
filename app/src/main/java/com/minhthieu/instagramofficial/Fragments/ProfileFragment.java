package com.minhthieu.instagramofficial.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhthieu.instagramofficial.Models.SearchRecyclerItem;
import com.minhthieu.instagramofficial.R;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView fullName;
    private TextView userName;
    private TextView following;
    private TextView followers;
    private TextView posts;
    private CircleImageView profileImage;
    private Button editProfileBt;

    private FirebaseUser user;
    private String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Anh Xa
        AnhXa(view);

        // get the user info
        UserInfo();
        
        return view;
    }

    private void UserInfo() {
        FirebaseDatabase.getInstance().getReference("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                SearchRecyclerItem currentUser = snapshot.getValue(SearchRecyclerItem.class);

                // set info
                fullName.setText(currentUser.getFullname());
                userName.setText(currentUser.getUsername());
                profileImage.setImageURI(Uri.parse(currentUser.getImageUrl()));


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void AnhXa(View view) {
            
        fullName = view.findViewById(R.id.fullname);
        userName = view.findViewById(R.id.user_name);
        following = view.findViewById(R.id.following);
        followers = view.findViewById(R.id.followers);
        posts = view.findViewById(R.id.post);
        profileImage = view.findViewById(R.id.profile_image);
        editProfileBt = view.findViewById(R.id.edit_profile_bt);

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid();
        
    }
}