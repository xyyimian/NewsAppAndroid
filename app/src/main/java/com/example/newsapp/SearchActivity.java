package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    //TextView tv_search;
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    ArrayList<Card> cardlist;
    String url;
    String keyword;
    Context context;
    RelativeLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        context = this;
        requestQueue = Volley.newRequestQueue(context);
        //tv_search = findViewById(R.id.tv_search);
        keyword = getIntent().getStringExtra("keyword");
        setTitle("Search Results for "+keyword);
        //tv_search.setText(keyword);
        url = "https://xyyimian-cs571-hw8.wl.r.appspot.com/api?type=guardian&query=" + keyword;
        cardlist = new ArrayList<Card>();
        spinner = findViewById(R.id.searchspinner);
        recyclerView = findViewById(R.id.search_card_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        spinner.setVisibility(RelativeLayout.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //text_home.setText("Response: " + response.toString());
                        JSONArray jsonArray = response.optJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i);
                            cardlist.add(new Card(
                                    jsonObject.optString("id"),
                                    jsonObject.optString("url"),
                                    jsonObject.optString("title"),
                                    jsonObject.optString("description"),
                                    jsonObject.optString("date").substring(0,19),
                                    jsonObject.optString("section"),
                                    jsonObject.optString("image")));
                        }
                        myAdapter = new CardAdapter(context,cardlist);
                        recyclerView.setAdapter(myAdapter);
                        spinner.setVisibility(RelativeLayout.GONE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.);
//    }
}
