package com.example.artermis2.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.History;
import com.example.artermis2.MainActivity;
import com.example.artermis2.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Edit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Edit extends Fragment {

    Context context ;
    public Edit(Context context) {
        // Required empty public constructor
        this.context = context;
    }


    public static Edit newInstance(Context context) {
        Edit fragment = new Edit(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_edit, container, false);
        EditText editText = view.findViewById(R.id.editTextDeviceNameUpdate);
        editText.setText(DataHolder.getDevice().device_name);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout deleteDevice = view.findViewById(R.id.deleteDevice);
        deleteDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getInstance(context).deviceDao().deleteDeviceByUuid(DataHolder.getDevice().uuid);
                AppDatabase.getInstance(context).historyDao().deleteHistoryByDeviceId(DataHolder.getDevice().uuid);
                AppDatabase.getInstance(context).schedulerDao().deleteSchedulerByDeviceId(DataHolder.getDevice().uuid);
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RadioGroup radioGroup = view.findViewById(R.id.radio_group_device_edit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button okUpdateDevice = view.findViewById(R.id.buttonOkEditDevice);
        if (DataHolder.getDevice().room_name.equals("Phòng Khách")) {
            radioGroup.check(R.id.radio_phong_khach_1);
        } else if (DataHolder.getDevice().room_name.equals("Phòng Ngủ")) {
            radioGroup.check(R.id.radio_phong_ngu_1);
        } else if (DataHolder.getDevice().room_name.equals("Nhà Bếp")) {
            radioGroup.check(R.id.radio_nha_bep_1);
        }

        okUpdateDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName = editText.getText().toString();
                int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                String roomName = "";
                if (selectedRadioButtonId != -1) {
                    RadioButton selectedRadioButton = view.findViewById(selectedRadioButtonId);

                    // So sánh ID của RadioButton đã chọn với ID của RadioButton phòng khách
                    if (selectedRadioButton.getId() == R.id.radio_phong_khach_1) {
                        // RadioButton "Phòng Khách" được chọn
                        roomName = "Phòng Khách";
                    } else if (selectedRadioButton.getId() == R.id.radio_phong_ngu_1) {
                        // RadioButton "Phòng Ngủ" được chọn
                        roomName ="Phòng Ngủ";
                    } else if (selectedRadioButton.getId() == R.id.radio_nha_bep_1) {
                        // RadioButton "Nhà Bếp" được chọn
                        roomName ="Nhà Bếp";
                    }
                }
                if(!deviceName.equals(DataHolder.getDevice().device_name) & roomName.equals(DataHolder.getDevice().room_name)){
                    AppDatabase.getInstance(context).deviceDao().updateNameDeviceByUUID(deviceName,DataHolder.getDevice().uuid);
                    History history = new History(DataHolder.getDevice().uuid,"edit","Đổi tên thiết bị","Đổi tên thiết bị là "+deviceName);
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    DataHolder.setDevice(AppDatabase.getInstance(context).deviceDao().getDeviceByUuid(DataHolder.getDevice().uuid));
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().startActivity(intent);
                }
                if(deviceName.equals(DataHolder.getDevice().device_name) & !roomName.equals(DataHolder.getDevice().room_name)){
                    AppDatabase.getInstance(context).deviceDao().updateRoomNameDeviceByUUID(roomName,DataHolder.getDevice().uuid);
                    History history = new History(DataHolder.getDevice().uuid,"edit","Đổi phòng thiết bị","Đổi phòng thiết bị là "+roomName);
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    DataHolder.setDevice(AppDatabase.getInstance(context).deviceDao().getDeviceByUuid(DataHolder.getDevice().uuid));
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().startActivity(intent);
                }
                if(!deviceName.equals(DataHolder.getDevice().device_name) & !roomName.equals(DataHolder.getDevice().room_name)) {
                    AppDatabase.getInstance(context).deviceDao().updateNameDeviceByUUID(deviceName, DataHolder.getDevice().uuid);
                    AppDatabase.getInstance(context).deviceDao().updateRoomNameDeviceByUUID(roomName, DataHolder.getDevice().uuid);
                    History history = new History(DataHolder.getDevice().uuid, "edit", "Đổi tên và phòng thiết bị", "Đổi tên và phòng thiết bị " + deviceName + " và " + roomName);
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    DataHolder.setDevice(AppDatabase.getInstance(context).deviceDao().getDeviceByUuid(DataHolder.getDevice().uuid));
                    Intent intent = getActivity().getIntent();
                    getActivity().finish();
                    getActivity().startActivity(intent);
                }

            }
            });
        return  view;
    }
}



