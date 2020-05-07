package com.example.newsapp.ui.headlines;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder>{

    private ArrayList<Tab> tablist;
    Context context;
    ItemClicked mCallback;

    public interface ItemClicked{
        void onItemClicked(int index);
    }

    public TabAdapter(Context context,ArrayList<Tab> tablist,ItemClicked callback){
        this.context = context;
        this.tablist = tablist;
        mCallback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button btn_hltag;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_hltag = itemView.findViewById(R.id.btn_hltag);
            btn_hltag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //mCallback.onItemClicked(tablist.indexOf(v.getTag()));
                    mCallback.onItemClicked(tablist.indexOf((Tab)v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public TabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.headline_tabs,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btn_hltag.setTag(tablist.get(position));
        holder.btn_hltag.setText(tablist.get(position).getSection().toUpperCase());
        if(tablist.get(position).isSelected()){
            holder.btn_hltag.setBackground(context.getResources().getDrawable(R.drawable.hl_tag_btn));
        }else{
            holder.btn_hltag.setBackground(context.getResources().getDrawable(R.drawable.hl_tag_btn2));
        }
    }


    @Override
    public int getItemCount() {
        return tablist.size();
    }
}
