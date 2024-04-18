package com.example.artermis2.Screen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.MainActivity;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ScheduleMenuScreen extends AppCompatActivity implements IMyActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_menu_screen);


        MyActionBar myActionBar = new MyActionBar(this,"Schedule");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.scheduleActionBar, myActionBar)
                .commit();
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout scheduleListId = findViewById(R.id.scheduleListId);
        for (int i = 0;i<3;i++){
            View view = getLayoutInflater().inflate(R.layout.item_schedule,null);
            scheduleListId.addView(view);
        }

    }

    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {
        Intent intent = new Intent(ScheduleMenuScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}