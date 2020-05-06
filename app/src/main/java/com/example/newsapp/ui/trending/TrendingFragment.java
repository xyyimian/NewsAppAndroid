package com.example.newsapp.ui.trending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.newsapp.R;

public class TrendingFragment extends Fragment {

    private TrendingViewModel trendingViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trendingViewModel =
                ViewModelProviders.of(getActivity()).get(TrendingViewModel.class);
        //= new ViewModelProvider().get(TrendingViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trending, container, false);
        final TextView textView = root.findViewById(R.id.text_trending);
        trendingViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
