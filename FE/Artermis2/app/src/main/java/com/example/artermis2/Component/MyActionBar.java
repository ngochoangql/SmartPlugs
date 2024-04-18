package com.example.artermis2.Component;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.artermis2.R;


public class MyActionBar extends Fragment {

    private IMyActionBar mListener;
    private FrameLayout back,home;
    private TextView title;
    String mTitle;
    private void Init(View view){
        back = view.findViewById(R.id.backActionBar);
        home = view.findViewById(R.id.homeActionBar);
        title = view.findViewById(R.id.titleActionBar);
    }
    public MyActionBar(){}
    public MyActionBar(IMyActionBar iMyActionBar,String title) {
        // Required empty public constructor
        this.mListener = iMyActionBar;
        this.mTitle = title;
    }


    public static MyActionBar newInstance(IMyActionBar iMyActionBar) {
        MyActionBar fragment = new MyActionBar(iMyActionBar,"");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_action_bar, container, false);
        Init(view);

        title.setText(mTitle);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBackButton();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onHomeButton();
            }
        });
        return  view;
    }

}