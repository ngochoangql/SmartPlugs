package com.example.artermis2.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.R;
import com.example.artermis2.Screen.ScheduleAddItemScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link History#newInstance} factory method to
 * create an instance of this fragment.
 */
public class History extends Fragment {

    Context context;
    LinearLayout historyListId;
    private View loadingView;
    Timer timer = new Timer();
    List<com.example.artermis2.Database.Entity.History> mListHistory;
    public History(Context context) {
        // Required empty public constructor
        this.context =context;
//        this.mListHistory = mListHistory;
    }


    public static History newInstance(Context context) {
        History fragment = new History(context);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        historyListId = view.findViewById(R.id.historyList);
        historyListId.addView(inflater.inflate(R.layout.fragment_loading,container));
        mListHistory = AppDatabase.getInstance(context).historyDao().getHistoriesByDeviceIdOrderByDate(DataHolder.getDevice().uuid);

        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(mListHistory != null){
                            historyListId.removeAllViews();
                        }
                        View itemView1 = getLayoutInflater().inflate(R.layout.item_history, null);
                        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView date1 = itemView1.findViewById(R.id.date);
                        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView type1 = itemView1.findViewById(R.id.type);
                        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView intro1 = itemView1.findViewById(R.id.intro);
                        intro1.setText("Nội dung");
                        intro1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        type1.setText("Chế độ");
                        type1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        date1.setText("Thời gian");
                        date1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        historyListId.addView(itemView1);
                        for (com.example.artermis2.Database.Entity.History history : mListHistory) {
                            View itemView = getLayoutInflater().inflate(R.layout.item_history, null);
                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView date = itemView.findViewById(R.id.date);
                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView type = itemView.findViewById(R.id.type);
                            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView intro = itemView.findViewById(R.id.intro);
                            intro.setText(history.intro);
                            type.setText(history.type);
                            String[] parts = history.date.split("\\s+");
                            String day = parts[2];
                            String month = parts[1];
                            String year = parts[5];
                            String time = parts[3];
                            String outputDateString = day + "/" + getMonthNumber(month) + "/" + year + " " + time;
                            date.setText(outputDateString);

                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showDialog(history.description);


                                }
                            });
                            historyListId.addView(itemView);
                        }
                    }
                });

            }
        },500);

        return view;
    }

    private static String getMonthNumber(String month) {
        switch (month) {
            case "Jan": return "01";
            case "Feb": return "02";
            case "Mar": return "03";
            case "Apr": return "04";
            case "May": return "05";
            case "Jun": return "06";
            case "Jul": return "07";
            case "Aug": return "08";
            case "Sep": return "09";
            case "Oct": return "10";
            case "Nov": return "11";
            case "Dec": return "12";
            default: return "00";
        }
    }
    private void showDialog(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chi tiết");
        builder.setMessage(description);

        // Nút "Đóng" để đóng dialog
        builder.setPositiveButton("Đóng", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Tạo dialog và hiển thị nó
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}