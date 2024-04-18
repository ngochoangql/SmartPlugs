package com.example.artermis2.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.MainActivity;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;
import com.example.artermis2.Screen.DeviceMenuScreen;
import com.example.artermis2.Screen.ScheduleAddItemScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class Scheduler extends Fragment implements IDataObserver {
    Context context ;
    List<com.example.artermis2.Database.Entity.Scheduler> schedulerList;
    boolean[] daysOfWeek;
    LinearLayout scheduleListId;
    MqttHandler mqttHandler;
    Switch[]  switches ;
    public Scheduler(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    // TODO: Rename and change types and number of parameters
    public static Scheduler newInstance(Context context) {
        Scheduler fragment = new Scheduler(context);
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
        View view = inflater.inflate(R.layout.fragment_scheduler, container, false);
        mqttHandler = new MqttHandler("scheduler_client",getContext());
        mqttHandler.registerObserver(this);

        scheduleListId = view.findViewById(R.id.schedulerList);
        changeData();
        Button addScheduler = view.findViewById(R.id.addSchedulerDevice);
//
        addScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScheduleAddItemScreen.class);
                intent.putExtra("add_scheduler_type","new");
                startActivity(intent);


            }
        });
        return  view;
    }
    @Override
    public void onResume(){
        super.onResume();
        changeData();
    }
    public void changeData(){
        schedulerList = AppDatabase.getInstance(context).schedulerDao().getSchedulerByDeviceId(DataHolder.getDevice().uuid);

        scheduleListId.removeAllViews();
        switches =new Switch[schedulerList.size()];
        int i=0;
        for (com.example.artermis2.Database.Entity.Scheduler scheduler : schedulerList){

            View itemView = getLayoutInflater().inflate(R.layout.item_schedule,null);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView time = itemView.findViewById(R.id.timeScheduleId);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView repeatType = itemView.findViewById(R.id.repeatTypeId);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Switch aSwitch = itemView.findViewById(R.id.switchSchedule);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ConstraintLayout containerScheduler = itemView.findViewById(R.id.container_scheduler);
            time.setText(scheduler.timeSelection);
            daysOfWeek= convertToBooleanArray(scheduler.daysRepeat);
            if(scheduler.repeatType.equals("daily")){
                repeatType.setText("Một lần");
            }else{
                if(dayOfWeekStringMethod().equals("Th 2,Th 3,Th 4,Th 5,Th 6,Th 7,CN")){
                    repeatType.setText("Hằng ngày");
                }else{
                    repeatType.setText(dayOfWeekStringMethod().replace(","," "));
                }
            }

            aSwitch.setChecked(scheduler.status);
            switches[i]=aSwitch;
            i++;
            scheduleListId.addView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    containerScheduler.setBackground(getResources().getDrawable(R.drawable.border10green));
                    Intent intent = new Intent(context,ScheduleAddItemScreen.class);
                    intent.putExtra("add_scheduler_type","update");
                    DataHolder.setScheduler(scheduler);
                    startActivity(intent);


                }
            });
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mqttHandler.publish("smart-plug.update-scheduler-status","{\"scheduler_id\":\""+scheduler.uuid+"\",\"status\":"+Boolean.toString(isChecked)+"}",0,false);
                }
            });


        }
    }
    private static boolean[] convertToBooleanArray(String daysString) {
        boolean[] daysOfWeek = new boolean[7]; // Mảng 7 phần tử cho 7 ngày trong tuần
        List<String> daysList = Arrays.asList("Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN");

        // Xóa dấu ngoặc vuông và dấu phẩy từ chuỗi
        String cleanedString = daysString.replaceAll("[\\[\\]]", "");

        // Tách các ngày trong chuỗi và đặt true cho ngày tương ứng trong mảng
        for (String day : cleanedString.split(",")) {
            int index = daysList.indexOf(day);
            if (index != -1) {
                daysOfWeek[index] = true;

            }
        }
        return daysOfWeek;
    }
    private String dayOfWeekStringMethod(){

        String[] dayNames = {"Th 2", "Th 3", "Th 4", "Th 5", "Th 6", "Th 7", "CN"};

        StringBuilder stringBuilder = new StringBuilder();

        // Duyệt qua từng phần tử trong mảng boolean
        for (int i = 0; i < daysOfWeek.length; i++) {
            // Nếu giá trị boolean tương ứng là true, thêm tên của ngày vào chuỗi
            if (daysOfWeek[i]) {

                stringBuilder.append(dayNames[i]).append(",");
            }
        }

        // Loại bỏ dấu phẩy cuối cùng (nếu có)
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        // Chuỗi kết quả
        String resultString = stringBuilder.toString();

        return resultString;
    }

    @Override
    public void onDataUpdated(String topic, JSONObject newData) {
        if(topic.equals("smart-plug.update-scheduler-status-reply")){
            Log.d("Mqtt","22222");
            try {
                boolean status = newData.getBoolean("status");
                String schedulerId = newData.getString("scheduler_id");
                Log.d("Mqtt",schedulerId);
                AppDatabase.getInstance(context).schedulerDao().updateSchedulerStatusByUUID(status,schedulerId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
        if(topic.equals("smart-plug.update-scheduler-status-event-reply")){
            Log.d("Mqtt","22333");
            try {
                boolean status = newData.getBoolean("status");
                String schedulerId = newData.getString("scheduler_id");
                Log.d("Mqtt",schedulerId + " " +Boolean.toString(status));
                int size = AppDatabase.getInstance(context).schedulerDao().getSchedulerByDeviceId(DataHolder.getDevice().uuid).size();

                int i=0;
                for (com.example.artermis2.Database.Entity.Scheduler scheduler:schedulerList){
                    int finali = i;
                    if(scheduler.uuid.equals(schedulerId)){
                        Log.d("Mqtt","yse");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                switches[finali].setChecked(status);
                            }
                        });
                        break;
                    }
                    i++;
                }
//                AppDatabase.getInstance(context).schedulerDao().updateSchedulerStatusByUUID(status,schedulerId);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
}