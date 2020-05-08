package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.ui.headlines.TabAdapter;
import com.example.newsapp.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private ArrayList<Card> cardlist;
    Context context;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String savedNews;
    JSONArray savedNewsJson;

    public CardAdapter(Context context,ArrayList<Card> cardlist){
        this.cardlist = cardlist;
        this.context = context;
        this.pref = context.getSharedPreferences("MyPref", 0);
        this.editor = pref.edit();
        this.savedNews = pref.getString("SavedNews", null);
        try {
            if (savedNews != null) {
                savedNewsJson = new JSONArray(savedNews);
            } else {
                savedNewsJson = new JSONArray();
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cardImage;
        ImageView iv_cardBookmark;
        TextView tv_cardTitle,tv_cardTime,tv_cardSection;
        Card card;
        String strNews;

        private void SaveNews(){

            JSONObject news = card2json(card);
            strNews = news.toString();
            savedNewsJson.put(news.toString());

            editor.putString("SavedNews", savedNewsJson.toString());
            editor.apply();
            Toast.makeText(context,"\""+card.getTitle()+"\" was added to bookmarks",Toast.LENGTH_LONG).show();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void UnSaveNews(){

            String id = card.getId();
            try {

                for (int i = 0; i < savedNewsJson.length(); ++i) {
                    JSONObject jsontemp = new JSONObject(savedNewsJson.getString(i));
                    if(jsontemp.optString("id").equals(id)){
                        savedNewsJson.remove(i);
                        break;
                    }
                }
                editor.putString("SavedNews", savedNewsJson.toString());
                editor.apply();
                Toast.makeText(context,"\""+card.getTitle()+"\" was removed from bookmarks",Toast.LENGTH_LONG).show();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        private JSONObject card2json(Card card){
            JSONObject news = new JSONObject();
            try {
                news.put("title", card.getTitle());
                news.put("time", card.getTime());
                news.put("section", card.getSection());
                news.put("imgUrl", card.getImgurl());
                news.put("id", card.getId());
            }catch (JSONException e){
                e.printStackTrace();
            }
            return news;
        }

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tv_cardTitle = itemView.findViewById(R.id.tv_cardTitle);
            tv_cardTime = itemView.findViewById(R.id.tv_cardTime);
            tv_cardSection = itemView.findViewById(R.id.tv_cardSection);
            iv_cardImage = itemView.findViewById(R.id.iv_cardImage);
            iv_cardBookmark = itemView.findViewById(R.id.iv_cardBookmark);



            iv_cardBookmark.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {
                    Integer tag = (Integer) view.getTag();
                    tag = tag == null ? 0 : tag;
                    if(tag == 1){   //saved
                        view.setTag(0);
                        iv_cardBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                        UnSaveNews();
                    }
                    else{
                        view.setTag(1);
                        iv_cardBookmark.setImageResource(R.drawable.ic_bookmark_saved);
                        SaveNews();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,com.example.newsapp.DetailActivity.class);

                    intent.putExtra("news", strNews);
                    boolean isSaved;
                    if(iv_cardBookmark.getTag().toString().equals("0")){
                        isSaved = false;
                    }
                    else{
                        isSaved = true;
                    }
                    intent.putExtra("saved", isSaved);


                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
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
        holder.card = cardlist.get(position);

        if(cardlist.get(position).getImgurl().compareTo("")!=0){
         //   Picasso.with(context).load(cardlist.get(position).getImgurl()).noPlaceholder().into(holder.iv_cardImage);
            Picasso.with(context).load(cardlist.get(position).getImgurl()).resize(2048, 1600).onlyScaleDown().into(holder.iv_cardImage);
        }
        //
        String title = cardlist.get(position).getTitle();
        boolean found = false;
        try {
            for (int i = 0; i < savedNewsJson.length(); ++i) {
                JSONObject jsontemp = new JSONObject(savedNewsJson.getString(i));
                if(jsontemp.optString("title").equals(title)){
                    found = true;
                    break;
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }


        if(found){
            holder.iv_cardBookmark.setImageResource(R.drawable.ic_bookmark_saved);
            holder.iv_cardBookmark.setTag(1);
        }
        else{
            holder.iv_cardBookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
            holder.iv_cardBookmark.setTag(0);
        }


    }

    @Override
    public int getItemCount() {
        return cardlist.size();
    }
}
