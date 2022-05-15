package com.minhthieu.instagramofficial.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhthieu.instagramofficial.Adapters.SearchRecyclerAdapter;
import com.minhthieu.instagramofficial.Models.SearchRecyclerItem;
import com.minhthieu.instagramofficial.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<SearchRecyclerItem> userList = new ArrayList<>();
    private TextInputLayout searchBarLayout;
    private TextInputEditText searchBarText;

    // the adpater
    SearchRecyclerAdapter userAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // recycler view
        RecyclerView showUsersView = view.findViewById(R.id.recycler_show_user_view);
        showUsersView.setHasFixedSize(true);
        showUsersView.setLayoutManager(new LinearLayoutManager(getContext()));

        // search bar
        searchBarLayout = view.findViewById(R.id.search_bar_layout);
        searchBarText = view.findViewById(R.id.search_bar_edittext);


        // time to set the adapter, bitchhhh
        userAdapter = new SearchRecyclerAdapter(userList, getContext());
        showUsersView.setAdapter(userAdapter);

        // notify cái text nó change
        searchBarText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                readUsers();

            }
        });

        // read users from database

        return view;
    }

    private void readUsers() {
        DatabaseReference userRealTimeRef = FirebaseDatabase.getInstance().getReference("Users");
        userRealTimeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (searchBarText.getText().toString().isEmpty()) {
                    userList.clear();
                }
                for (DataSnapshot data : snapshot.getChildren()) {
                    SearchRecyclerItem userInfo = data.getValue(SearchRecyclerItem.class);

                    // add them to the list
                    userList.add(userInfo);

                }

                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}