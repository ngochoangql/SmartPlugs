package com.example.artermis2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.Menu.AddDeviceMenu;
import com.example.artermis2.Menu.AddSchedulerMenu;
import com.example.artermis2.Menu.AnalyticsMenu;
import com.example.artermis2.Menu.DeviceMenu;
import com.example.artermis2.Menu.Menu;
import com.example.artermis2.Menu.ScheduleMenu;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.Screen.ScheduleAddItemScreen;
import com.example.artermis2.Screen.ScheduleMenuScreen;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IMyActionBar, IDataObserver {
    private MqttHandler mqttHandler;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        mqttHandler = new MqttHandler("mainactivity_client",this);


        MyActionBar myActionBar = new MyActionBar(this,"Home");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.homeActionBar, myActionBar)
                .commit();
        List<Menu> menuList1 = new ArrayList<>();
        mqttHandler.publish("smart-plug.async","async",0,false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) GridLayout mainMenu = findViewById(R.id.mainMenu);
        menuList1.add(new ScheduleMenu(this));
        menuList1.add(new AnalyticsMenu(this));
        menuList1.add(new AddDeviceMenu(this));
        menuList1.add(new AddSchedulerMenu(this));
        for (Menu menu : menuList1){
            menu.getMain().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menu.onClick();
                }
            });
            mainMenu.addView(menu.getMain());
        }
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) GridLayout menuHome = findViewById(R.id.menuHome);
        List<DeviceMenu> menuList = new ArrayList<>();
        List<Device> mListDevice = AppDatabase.getInstance(this).deviceDao().getAllDevices();
        for (Device device : mListDevice){
            menuList.add(new DeviceMenu(this,device));
        }
        int i = 0;
        for (DeviceMenu menu : menuList){
//            TextView textView = new TextView(this);
//            textView.setText("adsa");
            mqttHandler.registerObserver(menu);
            int finalI = i;
            menu.getMain().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataHolder.setDevice(mListDevice.get(finalI));
                    menu.onClick();
                }
            });
            menuHome.addView(menu.getMain());
            i++;
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {

    }

    @Override
    public void onDataUpdated(String id, JSONObject newData) {

    }
    @Override
    protected void onStart() {
        super.onStart();
        mqttHandler.registerObserver(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mqttHandler.unregisterObserver(this);
    }
}