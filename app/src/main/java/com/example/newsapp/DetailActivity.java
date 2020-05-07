package com.example.newsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DetailActivity extends AppCompatActivity {
    TextView tv_detail,tv_detailsection,tv_detailtime,tv_detailtitle,tv_detailviewfull;
    ImageView iv_detailimage;
    RequestQueue requestQueue;
    Context context;
    String id,section,url;
    RelativeLayout spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        context = this;
        spinner = findViewById(R.id.detailspinner);
        tv_detailsection = findViewById(R.id.tv_detailsection);
        tv_detailtime = findViewById(R.id.tv_detailtime);
        tv_detailtitle = findViewById(R.id.tv_detailtitle);
        iv_detailimage = findViewById(R.id.iv_detailimage);
        tv_detail = findViewById(R.id.tv_details);
        tv_detailviewfull = findViewById(R.id.tv_detailviewfull);
        requestQueue = Volley.newRequestQueue(this);
        id = getIntent().getStringExtra("id");
        section = getIntent().getStringExtra("section");
//        tv_detail.setText(id);
        url = "https://xyyimian-cs571-hw8.wl.r.appspot.com/api?type=guardian&nid="+id;
        spinner.setVisibility(RelativeLayout.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        //text_home.setText("Response: " + response.toString());
                        JSONObject detailobj = response.optJSONArray("results").optJSONObject(0);
                        setTitle(detailobj.optString("title"));
                        tv_detailtitle.setText(detailobj.optString("title"));
                        tv_detailsection.setText(section+" news");
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime localTimeObj = LocalDateTime.parse(detailobj.optString("date").substring(0,19));
                            DateTimeFormatter dTF = DateTimeFormatter.ofPattern("dd MMM uuuu");
                            tv_detailtime.setText(dTF.format(localTimeObj));
                        }
                        else tv_detailtime.setText(detailobj.optString("date"));
                        tv_detail.setText(detailobj.optString("description"));
                        String viewfull = " <a href="+detailobj.optString("url") +"><u>View Full Article</u></a>";
                        tv_detailviewfull.setMovementMethod(LinkMovementMethod.getInstance());
                        tv_detailviewfull.setText(Html.fromHtml(viewfull, Html.FROM_HTML_MODE_LEGACY));
                        if(detailobj.optString("image").compareTo("")!=0){
                            Picasso.with(context).load(detailobj.optString("image")).resize(4096, 3200).onlyScaleDown().into(iv_detailimage);
                        }
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
}
