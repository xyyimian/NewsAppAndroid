package com.example.newsapp.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.newsapp.Card;
import com.example.newsapp.CardAdapter;
import com.example.newsapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    RequestQueue requestQueue;
    Context context;
    TextView tv_weatherCity,tv_weatherState,tv_weatherTemp,tv_weatherSummary;
    LinearLayout ll_weathercard;
    ArrayList<Card> cardlist;
    String weatherurl;
    //private ProgressBar spinner;
    RelativeLayout spinner;


    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity().getApplicationContext();
        //spinner = (ProgressBar)root.findViewById(R.id.progressBar);
        spinner = root.findViewById(R.id.spinner);
        requestQueue = Volley.newRequestQueue(context);
        tv_weatherCity = root.findViewById(R.id.tv_weatherCity);
        tv_weatherState = root.findViewById(R.id.tv_weatherState);
        tv_weatherTemp = root.findViewById(R.id.tv_weatherTemp);
        tv_weatherSummary = root.findViewById(R.id.tv_weatherSummary);
        ll_weathercard = root.findViewById(R.id.ll_weathercard);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String[] sitystate = s.split(",");
                if(sitystate.length<2) return;
                tv_weatherCity.setText(sitystate[0]);
                tv_weatherState.setText(sitystate[1]);
                weatherurl = "https://api.openweathermap.org/data/2.5/weather?q="+sitystate[0]+"&units=metric&appid=0d4483a90645a0ebcf9a8ea11ce529eb";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, weatherurl, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                //get res
                                int temp = (int) response.optJSONObject("main").optDouble("temp");
                                String weather = response.optJSONArray("weather").optJSONObject(0).optString("main");
                                tv_weatherTemp.setText(Integer.toString(temp)+" °C");
                                tv_weatherSummary.setText(weather);
                                switch(weather){
                                    case "Clouds":
                                        ll_weathercard.setBackgroundResource(R.drawable.cloudy_weather);
                                        break;
                                    case "Clear":
                                        ll_weathercard.setBackgroundResource(R.drawable.clear_weather);
                                        break;
                                    case "Snow":
                                        ll_weathercard.setBackgroundResource(R.drawable.snowy_weather);
                                        break;
                                    case "Rain":
                                    case "Drizzle":
                                        ll_weathercard.setBackgroundResource(R.drawable.rainy_weather);
                                        break;
                                    case "Thunderstorm":
                                        ll_weathercard.setBackgroundResource(R.drawable.thunder_weather);
                                        break;
                                    default:
                                        ll_weathercard.setBackgroundResource(R.drawable.sunny_weather);
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
        });
        String backendurl ="https://xyyimian-cs571-hw8.wl.r.appspot.com/api?type=guardian&cat=home";
        cardlist = new ArrayList<Card>();

        recyclerView = root.findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        //spinner.setVisibility(View.VISIBLE);
        spinner.setVisibility(RelativeLayout.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

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
                        //spinner.setVisibility(View.GONE);
                        spinner.setVisibility(RelativeLayout.GONE);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);


        return root;
    }
}
