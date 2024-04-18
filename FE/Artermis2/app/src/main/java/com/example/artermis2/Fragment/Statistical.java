package com.example.artermis2.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artermis2.R;


public class Statistical extends Fragment {

    Context context;
    public Statistical(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    public static Statistical newInstance(Context context) {
        Statistical fragment = new Statistical(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistical, container, false);
    }
}