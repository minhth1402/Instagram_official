package com.minhthieu.instagramofficial.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.minhthieu.instagramofficial.Adapters.PostItemAdapter;
import com.minhthieu.instagramofficial.MainActivity;
import com.minhthieu.instagramofficial.Models.PostItem;
import com.minhthieu.instagramofficial.PostActivity;
import com.minhthieu.instagramofficial.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeAfterUseFragment extends Fragment {
    private RecyclerView recyclerViewToLoadPost;
    private PostItemAdapter postItemAdapter;

    private List<PostItem> postItemList = new ArrayList<>();
    private List<String> followingList = new ArrayList<>();

    private ImageView addPostBt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_after_use, container, false);
        // anh xa
        recyclerViewToLoadPost = view.findViewById(R.id.view_to_hold_post);
        addPostBt = view.findViewById(R.id.add_post_button);
        // OnClickListener
        ContainOnClickListener();

        recyclerViewToLoadPost.hasFixedSize();
        recyclerViewToLoadPost.setLayoutManager(new LinearLayoutManager(getContext()));

        // c?? 1 s??? c??i setting ????? cho n?? post latest ?????u ti??n, nhg t ?? hi???u l???m

        // set adapter
        postItemAdapter = new PostItemAdapter(getContext(),postItemList);
        recyclerViewToLoadPost.setAdapter(postItemAdapter);

        // check m??nh follow th???ng n??o
        CheckFollowingUsers();



        return view;
    }

    private void CheckFollowingUsers() {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    followingList.add(snapshot1.getKey().toString());
                }

                ReadPostFromThoseWeFollow();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void ReadPostFromThoseWeFollow() {
        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                postItemList.clear();

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    String userId = snapshot1.child("publisher").getValue(String.class);
                    String userName = snapshot1.child("publisher").getValue(String.class);
                    if(followingList.contains(userId)){
                        postItemList.add(snapshot1.getValue(PostItem.class));
                    }
                }

                postItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void ContainOnClickListener() {
        addPostBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ????y l?? c??ch chuy???n t??? fragment sang activity
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });
    }


}