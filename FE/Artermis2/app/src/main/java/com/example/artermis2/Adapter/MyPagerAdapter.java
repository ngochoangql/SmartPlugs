package com.example.artermis2.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.artermis2.Fragment.Edit;
import com.example.artermis2.Fragment.History;
import com.example.artermis2.Fragment.Limit;
import com.example.artermis2.Fragment.Loading;
import com.example.artermis2.Fragment.Overview;
import com.example.artermis2.Fragment.Scheduler;
import com.example.artermis2.Fragment.Statistical;

public class MyPagerAdapter extends FragmentStateAdapter {

    Context context;

    public MyPagerAdapter(FragmentActivity fa) {
        super(fa);
        this.context = fa;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Trả về fragment tương ứng với vị trí
        switch (position) {
            case 0:
                return new Overview(context );
            case 1:
                return new Statistical(context);
            case 2:
                return new Scheduler(context);
            case 3:
                return new Limit(context);
            case 4:
                return new Edit(context);
            case 5:
//                state++;
                return new History(context);
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng fragment
        return 6;
    }

}
