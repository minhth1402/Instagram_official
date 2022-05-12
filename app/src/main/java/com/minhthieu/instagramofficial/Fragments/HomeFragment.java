package com.minhthieu.instagramofficial.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minhthieu.instagramofficial.Adapters.HomeRecyclerItemAdapter;
import com.minhthieu.instagramofficial.Models.HomeRecyclerItem;
import com.minhthieu.instagramofficial.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DiscreteScrollView horizontalScroller;
    private List<HomeRecyclerItem> homeRecyclerItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Anh xa
        horizontalScroller = view.findViewById(R.id.horizontal_scroller);

        AddItemToHomeCard();

        // set Adapter
        horizontalScroller.setAdapter(new HomeRecyclerItemAdapter(homeRecyclerItems));

        return view;
    }

    private void AddItemToHomeCard() {

        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("0");
        list.add("0");
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));
        homeRecyclerItems.add(new HomeRecyclerItem("0", "Minh Thieu", "hacker",list,"7749" ));

    }


}