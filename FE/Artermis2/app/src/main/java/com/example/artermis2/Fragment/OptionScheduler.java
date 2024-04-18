package com.example.artermis2.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artermis2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OptionScheduler#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OptionScheduler extends Fragment {


    public OptionScheduler() {
        // Required empty public constructor
    }


    public static OptionScheduler newInstance(String param1, String param2) {
        OptionScheduler fragment = new OptionScheduler();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_option_scheduler, container, false);

        return view;
    }
}