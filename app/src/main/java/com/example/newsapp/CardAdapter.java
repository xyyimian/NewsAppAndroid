package com.example.newsapp;

import android.content.Context;
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





        private void SaveNews(){


            String title = tv_cardTitle.getText().toString();
            String time = tv_cardTime.getText().toString();
            String section = tv_cardSection.getText().toString();

            JSONObject news = new JSONObject();
            try {
                news.put("title", title);
                news.put("time", time);
                news.put("section", section);

                savedNewsJson.put(news.toString());

                editor.putString("SavedNews", savedNewsJson.toString());
                editor.apply();
            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private void UnSaveNews(){

            String title = tv_cardTitle.getText().toString();
            String time = tv_cardTime.getText().toString();
            String section = tv_cardSection.getText().toString();
            try {

                for (int i = 0; i < savedNewsJson.length(); ++i) {
                    JSONObject jsontemp = new JSONObject(savedNewsJson.getString(i));
                    if(jsontemp.getString("title").equals(title)){
                        savedNewsJson.remove(i);
                        break;
                    }
                }
                editor.putString("SavedNews", savedNewsJson.toString());
                editor.apply();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }

        public ViewHolder(@NonNull View itemView) {
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

//            itemView.setOnClickListener();
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
        if(cardlist.get(position).getImgurl().compareTo("")!=0){
            //Log.d("Imgurl", "cardlist.get(position).getImgurl(): "+cardlist.get(position).getImgurl());
            Picasso.with(context).load(cardlist.get(position).getImgurl()).resize(2048, 1600).onlyScaleDown().into(holder.iv_cardImage);
        }else{

        }
        //
        String title = cardlist.get(position).getTitle();
        boolean found = false;
        try {
            for (int i = 0; i < savedNewsJson.length(); ++i) {
                JSONObject jsontemp = new JSONObject(savedNewsJson.getString(i));
                if(jsontemp.getString("title").equals(title)){
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
