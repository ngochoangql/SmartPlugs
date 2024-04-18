package com.example.artermis2.Screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.MainActivity;
import com.example.artermis2.R;

public class AnalyticsMenuScreen extends AppCompatActivity implements IMyActionBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics_menu_screen);
        MyActionBar myActionBar = new MyActionBar(this,"Analytics");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.analyticsActionBar, myActionBar)
                .commit();
    }

    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {
        Intent intent = new Intent(AnalyticsMenuScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}