package com.example.artermis2.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.R;
public class Loading extends Fragment {
    Context context;
    public Loading(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container_device, new History(context, AppDatabase.getInstance(context).historyDao().getHistoriesByDeviceIdOrderByDate(DataHolder.getDevice().uuid)));
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        return view;
    }
}