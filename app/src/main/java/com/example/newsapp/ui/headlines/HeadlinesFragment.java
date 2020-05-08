package com.example.newsapp.ui.headlines;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.ArrayList;

public class HeadlinesFragment extends Fragment implements TabAdapter.ItemClicked{
    RecyclerView recyclerViewt,recyclerViewc;
    RecyclerView.Adapter myAdaptert,myAdapterc;
    RecyclerView.LayoutManager layoutManagert,layoutManagerc;
    ArrayList<Tab> tablist;
    ArrayList<Card> cardlist;
    RequestQueue requestQueue;
    Context context;
    RelativeLayout spinner;
    SwipeRefreshLayout swiperefresh_headline;

    private HeadlinesViewModel headlinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        headlinesViewModel =
                ViewModelProviders.of(this).get(HeadlinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_headlines, container, false);
        context = getActivity().getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
        spinner = root.findViewById(R.id.headlinespinner);
        swiperefresh_headline = root.findViewById(R.id.swiperefresh_headline);
//        final TextView textView = root.findViewById(R.id.text_headlines);
//        headlinesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        tablist = new ArrayList<Tab>();
        tablist.add(new Tab("world",true));
        tablist.add(new Tab("business",false));
        tablist.add(new Tab("politics",false));
        tablist.add(new Tab("sports",false));
        tablist.add(new Tab("technology",false));
        tablist.add(new Tab("science",false));
        recyclerViewt = root.findViewById(R.id.lv_hltags);
        recyclerViewt.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);
        layoutManagert = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewt.setLayoutManager(layoutManagert);
        myAdaptert = new TabAdapter(context,tablist,this);
        recyclerViewt.setAdapter(myAdaptert);

        recyclerViewc = root.findViewById(R.id.hl_card_list);
        recyclerViewc.setHasFixedSize(true);
        layoutManagerc = new LinearLayoutManager(context);
        recyclerViewc.setLayoutManager(layoutManagerc);
        //cardlist = new ArrayList<Card>();

        refreshCardlist(0,true);
        swiperefresh_headline.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                int id=0;
                for(int i=0;i<tablist.size();i++){
                    if(tablist.get(i).isSelected()) id=i;
                }
                refreshCardlist(id,false);

            }
        });
        return root;
    }

    @Override
    public void onTabItemClicked(int index) {
//        Toast.makeText(context,Integer.toString(index),Toast.LENGTH_LONG).show();
        for(int i=0;i<tablist.size();i++){
            tablist.get(i).setSelected(false);
            myAdaptert.notifyItemChanged(i);
        }
        tablist.get(index).setSelected(true);
        myAdaptert.notifyItemChanged(index);
        if(index>2) recyclerViewt.smoothScrollToPosition(5);
        else recyclerViewt.smoothScrollToPosition(0);
        refreshCardlist(index,true);
    }


    private void refreshCardlist(int index,boolean fetching) {
        String backendurl ="https://xyyimian-cs571-hw8.wl.r.appspot.com/api?type=guardian&cat="+tablist.get(index).getSection();
        cardlist = new ArrayList<Card>();
        if(fetching) spinner.setVisibility(RelativeLayout.VISIBLE);
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
                                    jsonObject.optString("image"))
                            );
                        }
                        myAdapterc = new CardAdapter(context,cardlist);
                        recyclerViewc.setAdapter(myAdapterc);
                        //myAdapterc.notifyItemRangeChanged(0,cardlist.size());
                        spinner.setVisibility(RelativeLayout.GONE);
                        swiperefresh_headline.setRefreshing(false);
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
