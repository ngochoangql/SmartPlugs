package com.example.artermis2.Screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.MainActivity;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DeviceAddItemScreen extends AppCompatActivity implements IMyActionBar, IDataObserver {

    Button okButton;
    EditText deviceName,valueLimit,deviceId;
    RadioGroup radioGroup;
    MqttHandler mqttHandler;
    String roomName = "";
    public void Init(){
        deviceName = findViewById(R.id.editTextDeviceName);
        deviceId = findViewById(R.id.editTextDeviceID);
        valueLimit = findViewById(R.id.editTextValueLimit);
        radioGroup = findViewById(R.id.radio_group_device);
        okButton = findViewById(R.id.buttonOkAddDevice);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_device_add_item_screen);
        MyActionBar myActionBar = new MyActionBar(this,"Add device");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.deviceAddItemActionBar, myActionBar)
                .commit();
        Init();
        mqttHandler = new MqttHandler("add_device",this);
        mqttHandler.registerObserver(this);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);

                    // So sánh ID của RadioButton đã chọn với ID của RadioButton phòng khách
                    if (selectedRadioButton.getId() == R.id.radio_phong_khach) {
                        // RadioButton "Phòng Khách" được chọn
                        roomName = "Phòng khách";
                    } else if (selectedRadioButton.getId() == R.id.radio_phong_ngu) {
                        // RadioButton "Phòng Ngủ" được chọn
                        roomName ="Phòng Ngủ";
                    } else if (selectedRadioButton.getId() == R.id.radio_nha_bep) {
                        // RadioButton "Nhà Bếp" được chọn
                        roomName ="Nhà Bếp";
                    }
                }
                mqttHandler.publish("smart-plug.add-device", "{\"device_id\":\""+deviceId.getText().toString() +"\",\"device_name\":\""+deviceName.getText().toString()+"\",\"room_name\":\""+roomName+"\",\"value_limit\":"+valueLimit.getText().toString()+"}",0,false);

            }


        });
    }

    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {

    }

    @Override
    public void onDataUpdated(String topic, JSONObject newData) {
        if(topic.equals("smart-plug.add-device-reply")){
            Log.d("Mqtt","125555555");
            try {
                String devideId = newData.getString("device_id");
                Device device = new Device(devideId,deviceName.getText().toString(),roomName,Integer.parseInt(valueLimit.getText().toString()) );
                AppDatabase.getInstance(DeviceAddItemScreen.this).deviceDao().insert(device);
                Intent intent = new Intent(DeviceAddItemScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}