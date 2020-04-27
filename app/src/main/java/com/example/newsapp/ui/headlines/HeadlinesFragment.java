package com.example.newsapp.ui.headlines;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.newsapp.R;

public class HeadlinesFragment extends Fragment {

    private HeadlinesViewModel headlinesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        headlinesViewModel =
                ViewModelProviders.of(this).get(HeadlinesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_headlines, container, false);
        final TextView textView = root.findViewById(R.id.text_headlines);
        headlinesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
