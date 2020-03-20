package com.example.postie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postie.Models.PostModel;
import com.example.postie.R;

import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsViewHolder> {

    private List<PostModel> feeds;

    public FeedsAdapter(List<PostModel> feeds) {
        this.feeds = feeds;
    }

    @NonNull
    @Override
    public FeedsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);

        return new FeedsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public class FeedsViewHolder extends RecyclerView.ViewHolder{
        public FeedsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
