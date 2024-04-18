package com.example.artermis2.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.R;
import com.example.artermis2.Screen.DeviceMenuScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Handler;

public class DeviceMenu extends Menu implements IDataObserver {
    private Device device;
    private Handler handler;
    private TextView current,voltage,actP,appP,energy;
    private Switch switchButton;

    public DeviceMenu(Context context, Device device) {
        this.device  = device;
        this.context  =context;
        FrameLayout frameLayout = new FrameLayout(context);
        // Chỉnh sửa các thuộc tính của FrameLayout

        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, // Width match_parent
                ViewGroup.LayoutParams.WRAP_CONTENT// Height wrap_content, bạn có thể thay đổi tùy theo nhu cầu
        );

        frameLayout.setLayoutParams(layoutParams1);
        // Thiết lập background cho FrameLayout (nếu cần)
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_device, frameLayout, false);
        switchButton = itemView.findViewById(R.id.switchButton);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Nếu switch được bật, sử dụng thumb_on.xml cho thumb
                    switchButton.setThumbResource(R.drawable.thumb_on_custom);

                } else {
                    // Nếu switch được tắt, sử dụng thumb_off.xml cho thumb
                    switchButton.setThumbResource(R.drawable.thumb_custom);

                }
            }
        });
        // Thiết lập padding cho FrameLayout (nếu cần)
         frameLayout.setPadding(16,16,16,16);
        frameLayout.addView(itemView);
        this.main = frameLayout;
        current = this.main.findViewById(R.id.currentId);
        voltage = this.main.findViewById(R.id.voltageId);
        actP = this.main.findViewById(R.id.activePId);
        appP = this.main.findViewById(R.id.apparentPId);
        energy = this.main.findViewById(R.id.energyId);

    };
    @Override
    public void onClick() {
        Intent intent = new Intent(context, DeviceMenuScreen.class);
        DataHolder.setDevice(AppDatabase.getInstance(context).deviceDao().getDeviceByUuid(this.device.uuid));
        intent.putExtra("fragment_container","overview" );
        context.startActivity(intent);
    }

    @Override
    public FrameLayout getMain() {
        return this.main;
    }


    @Override
    public void onDataUpdated(String id, JSONObject newData) {
//        if (id.equals(this.id)){
//            Log.d("ACV","ys");
//            if (this.main != null){
//
//               ((Activity) context).runOnUiThread(new Runnable() {
//                   @Override
//                   public void run() {
//                       try {
//                           current.setText(newData.getString("current"));
//                           voltage.setText(newData.getString("voltage"));
//                           actP.setText(newData.getString("active_power"));
//                           appP.setText(newData.getString("apparent_power"));
//                           energy.setText(newData.getString("energy"));
//                           switchButton.setChecked(newData.getBoolean("state"));
//                       } catch (JSONException e) {
//                           throw new RuntimeException(e);
//                       }
//                   }
//               });
//            }
//
//        }
    }

}
