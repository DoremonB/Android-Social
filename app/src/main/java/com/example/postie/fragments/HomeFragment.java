package com.example.postie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.postie.Adapters.FeedsAdapter;
import com.example.postie.Adapters.StoriesAdapter;
import com.example.postie.Models.PostModel;
import com.example.postie.Models.StoryModel;
import com.example.postie.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }
    private RecyclerView stories,feeds;
    private StoriesAdapter storiesAdapter;
    private FeedsAdapter feedsAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        LinearLayoutManager storiesLayoutManager=new LinearLayoutManager(getContext());
        storiesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stories.setLayoutManager(storiesLayoutManager);

        LinearLayoutManager feedsLayoutManager=new LinearLayoutManager(getContext());
        feedsLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feeds.setLayoutManager(feedsLayoutManager);

        List<String> images=new ArrayList<>();
        images.add("");
        images.add("");
        images.add("");
        images.add("");

        List<StoryModel> list=new ArrayList<>();
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"Johasasasasasasasasasasasn Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));
        list.add(new StoryModel(images,"John Doe"));

        storiesAdapter=new StoriesAdapter(list);
        stories.setAdapter(storiesAdapter);

        List<PostModel> feedsList=new ArrayList<>();
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsList.add(new PostModel("","","","","","","",""));
        feedsAdapter=new FeedsAdapter(feedsList);
        feeds.setAdapter(feedsAdapter);


    }

    private void init(View view){
        stories=view.findViewById(R.id.stories);
        feeds=view.findViewById(R.id.feeds);

    }
}

