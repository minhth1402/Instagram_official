package com.minhthieu.instagramofficial.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.minhthieu.instagramofficial.Adapters.HomeRecyclerItemAdapter;
import com.minhthieu.instagramofficial.MainActivity;
import com.minhthieu.instagramofficial.Models.HomeRecyclerItem;
import com.minhthieu.instagramofficial.PostActivity;
import com.minhthieu.instagramofficial.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.InfiniteScrollAdapter;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private DiscreteScrollView horizontalScroller;
    private List<HomeRecyclerItem> homeRecyclerItems = new ArrayList<>();
    
    // 
    private ImageView addPostBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // =================== +++ ===================
        // Anh xa
        horizontalScroller = view.findViewById(R.id.horizontal_scroller);
        addPostBt = view.findViewById(R.id.add_post_button);
        
        // add OnclickList cho cái post button
        ContainOnClickListener();
        
        // add them item vao list
        AddItemToHomeCard();

        // do some scrollview setting

        // the infinite scroll
        InfiniteScrollAdapter<?> infiniteScrollAdapter = InfiniteScrollAdapter.wrap(new HomeRecyclerItemAdapter(homeRecyclerItems));

        // set Adapter
        horizontalScroller.setAdapter(infiniteScrollAdapter);

        // scroller settings
        horizontalScroller.setSlideOnFling(true);
        horizontalScroller.setSlideOnFlingThreshold(1200);
        horizontalScroller.setItemTransitionTimeMillis(400);

        // set the zoom out / focus effect
        horizontalScroller.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());


        // this belong to the fragment
        // cai ly do ma no phai dung view như vầy là vì cái minh phải khai cái view trên kia mà tìm findViewById (vì có nhiều fragment mà)

        MoveToHomeAfterUse();

        return view;
    }

    private void MoveToHomeAfterUse() {
        Fragment homeAfterUseFragment= new HomeAfterUseFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, homeAfterUseFragment);
        fragmentTransaction.commit();

    }

    private void ContainOnClickListener() {
        addPostBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // đây là cách chuyển từ fragment sang activity
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });
    }




    private void AddItemToHomeCard() {

        // add mấy cái item sau nha, nhưng để t lm xong cái cn post cái
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