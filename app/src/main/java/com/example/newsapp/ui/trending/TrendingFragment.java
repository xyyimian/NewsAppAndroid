package com.example.newsapp.ui.trending;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.newsapp.Card;
import com.example.newsapp.CardAdapter;
import com.example.newsapp.R;
import com.android.volley.toolbox.Volley;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class TrendingFragment extends Fragment {

    public interface VollyCallback{
        void onSuccessResponse(LineDataSet result);
    }

    private TrendingViewModel trendingViewModel;
    RequestQueue requestQueue;
    Context context;

    TextView tv_query;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trendingViewModel =
                ViewModelProviders.of(getActivity()).get(TrendingViewModel.class);
        context = getActivity().getApplicationContext();
        requestQueue = Volley.newRequestQueue(context);
        final View root = inflater.inflate(R.layout.fragment_trending, container, false);
        tv_query = root.findViewById(R.id.trend_query);
        String query = tv_query.getText().toString();
        final VollyCallback cbfunc = new VollyCallback() {
            @Override
            public void onSuccessResponse(LineDataSet result) {

                List<ILineDataSet> ilds = new ArrayList<ILineDataSet>();
                ilds.add(result);
                LineData ld = new LineData(ilds);


                LineChart chart = (LineChart) root.findViewById(R.id.line_chart);
//                chart.getAxisRight().setDrawGridLines(false);
//                chart.getXAxis().setDrawGridLines(false);
//                chart.getXAxis().setEnabled(false);
                chart.getXAxis().setDrawGridLines(false);
                chart.getAxisLeft().setDrawGridLines(false);
                chart.getAxisRight().setDrawGridLines(false);
                chart.getAxisLeft().setDrawAxisLine(false);

                Legend legend = chart.getLegend();

                chart.setData(ld);
                chart.invalidate();


            }
        };
        tv_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String query = tv_query.getText().toString();
                    getData(query, cbfunc);
                }
                return true;
            }
        });
        getData(query, cbfunc);







        return root;
    }

    private void getData(final String query, final VollyCallback callback) {
        System.out.println(query);



        String backendurl = "https://xyyimian-cs571-hw8.wl.r.appspot.com/trend?query=" + query;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, backendurl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        List<Entry> dataList = new ArrayList<Entry>();

                        try {
                            JSONArray jsValues = response.optJSONArray("results");
                            for (int i = 0; i < jsValues.length(); ++i) {
                                dataList.add(new Entry(i, Float.parseFloat(jsValues.getString(i))));
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        LineDataSet lds = new LineDataSet(dataList, "Trending chart for "+query);
                        lds.setAxisDependency(YAxis.AxisDependency.LEFT);
                        lds.setColors(Color.BLUE);
                        callback.onSuccessResponse(lds);

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
