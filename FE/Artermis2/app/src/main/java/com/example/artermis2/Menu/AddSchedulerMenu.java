package com.example.artermis2.Menu;

import android.content.Context;
import android.content.Intent;
import android.widget.FrameLayout;

import com.example.artermis2.R;
import com.example.artermis2.Screen.DeviceAddItemScreen;
import com.example.artermis2.Screen.ScheduleAddItemScreen;

public class AddSchedulerMenu extends  Menu{
    public AddSchedulerMenu(Context context){
        this.context = context;
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setPadding(8,8,8,8);
        FrameLayout frameLayout1 = new FrameLayout(context);
        frameLayout1.setBackground(context.getDrawable(R.drawable.add_scheduler_icon));
        frameLayout.addView(frameLayout1);
        this.main = frameLayout;
    }
    @Override
    public void onClick() {
        Intent intent = new Intent(context, ScheduleAddItemScreen.class);
        intent.putExtra("add_scheduler_type","new");
        context.startActivity(intent);
    }

    @Override
    public FrameLayout getMain() {
        return this.main;
    }
}
