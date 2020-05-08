package com.example.newsapp.ui.bookmark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.Card;
import com.example.newsapp.CardAdapter;
import com.example.newsapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    RecyclerView recyclerView;
    TextView msgView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    Context context;
    ArrayList<Card> cardlist;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String savedNews;
    JSONArray savedNewsJson;

    private BookmarkViewModel bookmarkViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookmarkViewModel =
                ViewModelProviders.of(this).get(BookmarkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
        context = getActivity().getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);


        recyclerView = root.findViewById(R.id.bookmark_area);
        msgView = root.findViewById(R.id.nosaved_msg);
        msgView.setVisibility(View.GONE);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        bookmarkViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        pref = context.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        savedNews = pref.getString("SavedNews", null);
        cardlist = new ArrayList<Card>();
        try{
            if(savedNews != null){
                savedNewsJson = new JSONArray(savedNews);
            }
            else{
                savedNewsJson = new JSONArray();
            }
            for(int i = 0; i < savedNewsJson.length(); ++i){
                JSONObject news = new JSONObject(savedNewsJson.getString(i));
                cardlist.add(new Card(
                        news.optString("id"),
                        news.optString("url"),
                        news.optString("title"),
                        news.optString("description"),
                        news.optString("time"),
                        news.optString("section"),
                        news.optString("imgUrl"))
                );
            }
            if(cardlist.size() == 0){
                msgView.setVisibility(View.VISIBLE);
            }
            myAdapter = new BmCardAdapter(context, cardlist);
            recyclerView.setAdapter(myAdapter);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return root;
    }

    public class BmCardAdapter extends RecyclerView.Adapter<BmCardAdapter.ViewHolder> {

        ArrayList<Card> cardlist;
        Context context;

        public BmCardAdapter(Context context, ArrayList<Card> cardlist) {
            this.context = context;
            this.cardlist = cardlist;
        }



        @NonNull
        @Override
        public BmCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BmCardAdapter.ViewHolder holder, int position) {
            holder.itemView.setTag(cardlist.get(position));
            String bmcardTime = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDateTime publishTime = LocalDateTime.parse(cardlist.get(position).getTime());
                DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd MMM");
                bmcardTime = dTF.format(publishTime);
            }
            holder.tv_date.setText(bmcardTime+" | " +cardlist.get(position).getSection());
            holder.tv_title.setText(cardlist.get(position).getTitle());
            holder.card = cardlist.get(position);
            if(!cardlist.get(position).getImgurl().equals("")){
                Picasso.with(context).load(cardlist.get(position).getImgurl()).resize(2048, 1600).onlyScaleDown().into(holder.iv_img);
            }

        }

        @Override
        public int getItemCount() {
            return cardlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_title, tv_date;
            ImageView iv_img, iv_icon;
            Card card;

            public ViewHolder(@NonNull final View itemView) {
                super(itemView);

                tv_title = itemView.findViewById(R.id.bm_title);
                tv_date = itemView.findViewById(R.id.bm_date);
                iv_icon = itemView.findViewById(R.id.bm_saveic);
                iv_img = itemView.findViewById(R.id.bm_img);

                iv_icon.setOnClickListener(new View.OnClickListener(){
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View view) {
                        UnSaveNews();
//                        itemView.setVisibility(View.GONE);
                        myAdapter.notifyDataSetChanged();
                        if(cardlist.size() == 0){
                            msgView.setVisibility(View.VISIBLE);
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context,com.example.newsapp.DetailActivity.class);
                        intent.putExtra("id",((Card)v.getTag()).getId());
                        intent.putExtra("section",((Card)v.getTag()).getSection());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
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
                    for(int i = 0; i < cardlist.size(); ++i){
                        if(cardlist.get(i).getId().equals(id)){
                            cardlist.remove(i);
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
        }



    }
}

