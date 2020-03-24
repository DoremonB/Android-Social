package com.example.postie.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.postie.Models.StoryModel;
import com.example.postie.R;
import com.example.postie.StoryActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {

    private List<StoryModel> list;

    public StoriesAdapter(List<StoryModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,parent,false);

        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StoryViewHolder holder, final int position) {

        Glide
                .with(holder.itemView.getContext())
                .load(list.get(position).getImages().get(0))
                .placeholder(R.drawable.default_profile_pic)
                .into(holder.thumnail);
        holder.name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storyIntent=new Intent(holder.itemView.getContext(), StoryActivity.class);
                holder.itemView.getContext().startActivity(storyIntent);
                StoryActivity.images=list.get(position).getImages();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class StoryViewHolder extends  RecyclerView.ViewHolder{

        private CircleImageView thumnail;
        private TextView name;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            thumnail=itemView.findViewById(R.id.story);
            name=itemView.findViewById(R.id.name);


        }
    }
}
