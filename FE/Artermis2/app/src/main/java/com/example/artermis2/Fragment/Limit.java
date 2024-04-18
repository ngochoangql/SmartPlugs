package com.example.artermis2.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.History;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Limit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Limit extends Fragment implements IDataObserver {

    Context context ;
    MqttHandler mqttHandler;
    public Limit(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    public static Limit newInstance(Context context) {
        Limit fragment = new Limit(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_limit, container, false);
        mqttHandler = new MqttHandler("edit_client",getContext());
        mqttHandler.registerObserver(this);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Switch limitSwitch = view.findViewById(R.id.limitSwitch);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView valueLimit = view.findViewById(R.id.valueLimit);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout limitLayout = view.findViewById(R.id.limitLayout);
        valueLimit.setText(Integer.toString(DataHolder.getDevice().value_limit));
        limitSwitch.setChecked(DataHolder.getDevice().state_limit);
        limitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mqttHandler.publish("smart-plug.limit","{\"device_id\":\""+DataHolder.getDevice().uuid+"\",\"state_limit\":"+Boolean.toString(isChecked)+",\"value_limit\":"+valueLimit.getText()+"}",0,false);

            }
        });
        limitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // Tạo một EditText mới
                final EditText input = new EditText(getActivity());
                input.setHint("Nhập giá trị giới hạn");

                // Đặt EditText vào dialog builder
                builder.setView(input);

                // Thiết lập tiêu đề cho dialog
                builder.setTitle("Nhập giá trị giới hạn");

                // Thiết lập nút cho dialog: "OK" và "Hủy"
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Lấy giá trị từ EditText khi người dùng nhấn OK
                        String limitValue = input.getText().toString();

                        // Gắn giá trị vào TextView hoặc xử lý theo nhu cầu
                        if (!limitValue.isEmpty()) {
                            // Gắn giá trị vào TextView
                            valueLimit.setText(limitValue);
                            mqttHandler.publish("smart-plug.limit","{\"device_id\":\""+DataHolder.getDevice().uuid+"\",\"state_limit\":"+Boolean.toString(limitSwitch.isChecked())+",\"value_limit\":"+limitValue+"}",0,false);




                        } else {
                            // Hiển thị thông báo nếu EditText trống
                            Toast.makeText(getActivity(), "Vui lòng nhập giá trị", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Đóng dialog khi người dùng nhấn Hủy
                        dialog.cancel();
                    }
                });

                // Tạo và hiển thị dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    @Override
    public void onDataUpdated(String topic, JSONObject newData) {
        if(topic.equals("smart-plug.limit-reply")){
            try {
                String deviceId = newData.getString("device_id");
                boolean stateLimit= newData.getBoolean("state_limit");
                int valueLimit = newData.getInt("value_limit");
                if(stateLimit != DataHolder.getDevice().state_limit){
                    AppDatabase.getInstance(context).deviceDao().updateStateLimitDeviceByUUID(stateLimit,DataHolder.getDevice().uuid);
                    History history = new History(DataHolder.getDevice().uuid,"limit",stateLimit ? "Bật" : "Tắt",(stateLimit ? "Bật" : "Tắt")+" limit");
                    AppDatabase.getInstance(context).historyDao().insert(history);
                }
                if(valueLimit!=DataHolder.getDevice().value_limit){
                    History history = new History(DataHolder.getDevice().uuid,"limit","Cập nhật giá trị" ,"Cập nhật giá trị giới hạn là "+ Integer.toString(valueLimit));
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    AppDatabase.getInstance(context).deviceDao().updateValueLimitDeviceByUUID(valueLimit,DataHolder.getDevice().uuid);

                }



            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            }
    }
}