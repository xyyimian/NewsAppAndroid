package com.example.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    TextView tv_detail,tv_detailsection,tv_detailtime,tv_detailtitle;
    ImageView iv_detailimage;
    RequestQueue requestQueue;
    Context context;
    String id,section,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        context = this;
        tv_detailsection = findViewById(R.id.tv_detailsection);
        tv_detailtime = findViewById(R.id.tv_detailtime);
        tv_detailtitle = findViewById(R.id.tv_detailtitle);
        iv_detailimage = findViewById(R.id.iv_detailimage);
        tv_detail = findViewById(R.id.tv_details);
        requestQueue = Volley.newRequestQueue(this);
        id = getIntent().getStringExtra("id");
        section = getIntent().getStringExtra("section");
//        tv_detail.setText(id);
        url = "https://xyyimian-cs571-hw8.wl.r.appspot.com/api?type=guardian&nid="+id;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //text_home.setText("Response: " + response.toString());
                        JSONObject detailobj = response.optJSONArray("results").optJSONObject(0);
                        tv_detailtitle.setText(detailobj.optString("title"));
                        tv_detailsection.setText(section);
                        tv_detailtime.setText(detailobj.optString("date"));
                        tv_detail.setText(detailobj.optString("description"));
                        if(detailobj.optString("image").compareTo("")!=0){
                            Picasso.with(context).load(detailobj.optString("image")).resize(4096, 3200).onlyScaleDown().into(iv_detailimage);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
