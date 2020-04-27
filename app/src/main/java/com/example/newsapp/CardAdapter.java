package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<Card> cardlist;

    public CardAdapter(Context context,ArrayList<Card> cardlist){
        this.cardlist = cardlist;

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cardImage;
        TextView tv_cardTitle,tv_cardTime,tv_cardSection;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_cardTitle = itemView.findViewById(R.id.tv_cardTitle);
            tv_cardTime = itemView.findViewById(R.id.tv_cardTime);
            tv_cardSection = itemView.findViewById(R.id.tv_cardSection);
            iv_cardImage = itemView.findViewById(R.id.iv_cardImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_items,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(cardlist.get(position));
        holder.tv_cardTitle.setText(cardlist.get(position).getTitle());
        holder.tv_cardTime.setText(cardlist.get(position).getTime());
        holder.tv_cardSection.setText(cardlist.get(position).getSection());
    }

    @Override
    public int getItemCount() {
        return cardlist.size();
    }
}
