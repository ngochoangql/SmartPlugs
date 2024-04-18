package com.example.artermis2.Screen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.artermis2.Component.Clock.HourAdapter;
import com.example.artermis2.Component.Clock.MinuteAdapter;
import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Component.IMyActionBar;
import com.example.artermis2.Component.MyActionBar;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.Scheduler;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.Arrays;
public class ScheduleAddItemScreen extends AppCompatActivity implements IMyActionBar, IDataObserver {
    Calendar calendar = Calendar.getInstance();
    int hour = calendar.get(Calendar.HOUR_OF_DAY); // Lấy giờ
    int minute = calendar.get(Calendar.MINUTE);
    int selectedHour;
    int selectedMinute;
    MqttHandler mqttHandler;
    Drawable drawable;
    Drawable drawable1;
    String addAlarmType,pageBack;
    TextView deviceName,roomName,daysRepeatText;
    Switch eventTypeSwitch;
    FrameLayout deleteSchedulerButton;
    RadioButton dailyButton,optionButton;
    Button okAddOrUpdateScheduler;
    boolean[] daysOfWeek;
    String dayOfWeekSelected = "";
    public void Init(){
        deviceName = findViewById(R.id.deviceNameScheduler);
        roomName = findViewById(R.id.roomNameScheduler);
        dailyButton = findViewById(R.id.dailyButton);
        optionButton = findViewById(R.id.optionButton);
        eventTypeSwitch = findViewById(R.id.eventTypeSwitch);
        okAddOrUpdateScheduler = findViewById(R.id.okAddOrUpdateScheduler);
        deleteSchedulerButton = findViewById(R.id.deleteScheduler);
        daysRepeatText = findViewById(R.id.daysRepeat);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schedule_add_item_screen);
        Init();
        mqttHandler = new MqttHandler("add_and_update_schduler_client",this);
        mqttHandler.registerObserver(this);
        Intent intent = getIntent();

        addAlarmType = intent.getStringExtra("add_scheduler_type");
        MyActionBar myActionBar = new MyActionBar(this,addAlarmType);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.scheduleAddItemActionBar, myActionBar)
                .commit();
        if (addAlarmType.equals("new")){

            eventTypeSwitch.setChecked(true);
            eventTypeSwitch.setText("Bật");
            dailyButton.setChecked(true);
            optionButton.setChecked(false);
            deleteSchedulerButton.setVisibility(View.INVISIBLE);
        }else{
            String time = DataHolder.getScheduler().timeSelection;
            String[] parts = time.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
            String daysRepeatString = DataHolder.getScheduler().daysRepeat;
            daysOfWeek= convertToBooleanArray(daysRepeatString);
            if (DataHolder.getScheduler().eventType == 1){
                eventTypeSwitch.setChecked(true);
                eventTypeSwitch.setText("Bật");
            }else{
                eventTypeSwitch.setChecked(false);
                eventTypeSwitch.setText("Tắt");
            }

            if (DataHolder.getScheduler().repeatType.equals("daily")){
                daysRepeatText.setText("Một lần");
                dailyButton.setChecked(true);
                optionButton.setChecked(false);
            }else{
                dailyButton.setChecked(false);
                optionButton.setChecked(true);
                dayOfWeekSelected = dayOfWeekStringMethod();
                daysRepeatText.setText(dayOfWeekStringMethod().replace(","," "));
            }
        }
        eventTypeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    eventTypeSwitch.setText("Bật");
                }else{
                    eventTypeSwitch.setText("Tắt");
                }
            }
        });

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dailyButton.isChecked()){
                    dailyButton.setChecked(false);
                    optionButton.setChecked(true);
                }else{
                    dailyButton.setChecked(true);
                    daysRepeatText.setText("Một lần");
                    dayOfWeekSelected="";
                    optionButton.setChecked(false);

                }
            }
        });
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!optionButton.isChecked()){
                    dailyButton.setChecked(true);
                    optionButton.setChecked(false);
                }else{

                    dailyButton.setChecked(false);
                    optionButton.setChecked(true);
                    showDialog();

                }
            }
        });
        deleteSchedulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getInstance(ScheduleAddItemScreen.this).schedulerDao().deleteSchedulersByUUId(DataHolder.getScheduler().uuid);
                ScheduleAddItemScreen.super.onBackPressed();
            }
        });
        RecyclerView recyclerViewHour = findViewById(R.id.recyclerViewHours);
        HourAdapter hourAdapter = new HourAdapter(1000);
        recyclerViewHour.setAdapter(hourAdapter);
        recyclerViewHour.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHour.scrollToPosition(960 + hour);
        recyclerViewHour.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewHour.getLayoutManager();
                View viewMiddle = layoutManager.findViewByPosition(960 + hour);
                if (viewMiddle != null) {
                    recyclerViewHour.smoothScrollBy(0, viewMiddle.getTop() - (recyclerViewHour.getHeight() / 2) + (viewMiddle.getHeight() / 2));
                }
            }
        });
        recyclerViewHour.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int recyclerViewHeight = recyclerView.getHeight();
                int centerItemPosition = getCenterItemPosition(recyclerView);
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View itemView = recyclerView.getChildAt(i);
                    int itemPosition = recyclerView.getChildLayoutPosition(itemView);
                    TextView textView = itemView.findViewById(R.id.textViewHour);
                    if (itemPosition == centerItemPosition) {
                        selectedHour = centerItemPosition % 24;
                        if (addAlarmType.equals("update")) {
//                            if (hasChanged(DataHolder.getAlarm(), String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute), state ? 1 : 0, repeatType, daysRepeatSelected)) {
//                                okAddAlarm.setBackground(drawable1);
//                            } else {
//                                okAddAlarm.setBackground(drawable);
//                            }
                        }
                        textView.setTextColor(Color.BLACK);
//                        timeDelayText();
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        textView.setTextColor(Color.GRAY);
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int selectedPosition = getCenterItemPosition(recyclerView);
                    View selectedView = recyclerView.getLayoutManager().findViewByPosition(selectedPosition);
                    int selectedViewHeight = selectedView.getHeight();
                    int recyclerViewHeight = recyclerView.getHeight();
                    int scrollDistance = selectedView.getTop() - (recyclerViewHeight / 2) + (selectedViewHeight / 2);
                    recyclerView.smoothScrollBy(0, scrollDistance);
                }
            }
        });
        RecyclerView recyclerViewMinute = findViewById(R.id.recyclerViewMinutes);
        MinuteAdapter minuteAdapter = new MinuteAdapter(1000);
        recyclerViewMinute.setAdapter(minuteAdapter);
        recyclerViewMinute.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMinute.scrollToPosition(960 + minute);
        recyclerViewMinute.post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewMinute.getLayoutManager();
                View viewMiddle = layoutManager.findViewByPosition(960 + minute);
                if (viewMiddle != null) {
                    recyclerViewMinute.smoothScrollBy(0, viewMiddle.getTop() - (recyclerViewMinute.getHeight() / 2) + (viewMiddle.getHeight() / 2));
                }
            }
        });
        recyclerViewMinute.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int recyclerViewHeight = recyclerView.getHeight();
                int centerItemPosition = getCenterItemPosition(recyclerView);
                for (int i = 0; i < recyclerView.getChildCount(); i++) {
                    View itemView = recyclerView.getChildAt(i);
                    int itemPosition = recyclerView.getChildLayoutPosition(itemView);
                    TextView textView = itemView.findViewById(R.id.textViewMinute);
                    if (itemPosition == centerItemPosition) {
                        selectedMinute = centerItemPosition % 60;
                        if (addAlarmType.equals("update")) {
//                            if (hasChanged(DataHolder.getAlarm(), String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute), state ? 1 : 0, repeatType, daysRepeatSelected)) {
//                                okAddAlarm.setBackground(drawable1);
//                            } else {
//                                okAddAlarm.setBackground(drawable);
//                            }
                        }
//                        timeDelayText();
                        textView.setTextColor(Color.BLACK);
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        textView.setTextColor(Color.GRAY);
                        textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCREEN_STATE_ON) {
                    int selectedPosition = getCenterItemPosition(recyclerView);
                    View selectedView = recyclerView.getLayoutManager().findViewByPosition(selectedPosition);
                    int selectedViewHeight = selectedView.getHeight();
                    int recyclerViewHeight = recyclerView.getHeight();
                    int scrollDistance = selectedView.getTop() - (recyclerViewHeight / 2) + (selectedViewHeight / 2);
                    recyclerView.smoothScrollBy(0, scrollDistance);
                }
            }
        });

        okAddOrUpdateScheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder resultBuilder = new StringBuilder();
                String[] parts = dayOfWeekSelected.split(",");
                // Duyệt qua từng phần tử trong mảng parts
                for (String part : parts) {
                    // Thêm dấu nháy kép vào mỗi phần tử và thêm vào chuỗi kết quả
                    resultBuilder.append("\"").append(part).append("\"").append(",");
                }

                // Loại bỏ dấu phẩy cuối cùng (nếu có)
                if (resultBuilder.length() > 0) {
                    resultBuilder.deleteCharAt(resultBuilder.length() - 1);
                }

                // Chuỗi kết quả
                String resultString = resultBuilder.toString();
                Log.d("Hoang",resultString + "1111");
                if (addAlarmType.equals("new")){

                    mqttHandler.publish("smart-plug.add-scheduler","{"+
                            "\"scheduler_id\":\""+ UUID.randomUUID().toString()+"\","+
                            "\"device_id\":\""+DataHolder.getDevice().uuid +"\","+
                            "\"event_type\":"+( eventTypeSwitch.isChecked() ? "1" : "0" )+","+
                            "\"time_selected\":\""+String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute)+"\","+
                            "\"repeat_type\":\""+(dailyButton.isChecked() ? "daily" : "weekend")+"\","+
                            "\"days_repeat\":"+(dailyButton.isChecked() ? "[]" : ("["+ resultString+"]"))+
                            "}",0,false);
                }else if (addAlarmType.equals("update")){
                    mqttHandler.publish("smart-plug.update-scheduler","{"+
                            "\"scheduler_id\":\""+ DataHolder.getScheduler().uuid+"\","+
                            "\"event_type\":"+( eventTypeSwitch.isChecked() ? "1" : "0" )+","+
                            "\"time_selected\":\""+String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute)+"\","+
                            "\"repeat_type\":\""+(dailyButton.isChecked() ? "daily" : "weekend")+"\","+
                            "\"days_repeat\":"+(dailyButton.isChecked() ? "[]" : ("["+ resultString+"]"))+
                            "}",0,false);
                }


            }
        });
        }
        private int getCenterItemPosition(RecyclerView recyclerView) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
            return (firstVisibleItemPosition + lastVisibleItemPosition) / 2;
        }

        public boolean hasChanged(Scheduler oldEvent,
                                  String newTimeSelected, int newEventType, String newRepeatType,
                                  List<Boolean> list) {
            // Kiểm tra các trường có thay đổi không

            if (!oldEvent.timeSelection.equals(newTimeSelected)) return true;
            if (oldEvent.eventType != newEventType) return true;
            if (!oldEvent.repeatType.equals(newRepeatType)){
                return true;
            }else{
                if (!oldEvent.repeatType.equals("["+list.get(0).toString()+","+list.get(1).toString()+","+list.get(2).toString()+","+list.get(3).toString()+","+list.get(4).toString()+","+list.get(4).toString()+","+list.get(6).toString()+","+list.get(7).toString()+"]")) return true;

            }

            // Nếu không có sự thay đổi, trả về fals
            return false;
        }
    @Override
    public void onBackButton() {
        super.onBackPressed();
    }

    @Override
    public void onHomeButton() {

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mqttHandler.unregisterObserver(this);
        mqttHandler.disconnect();
    }
    @Override
    public void onDataUpdated(String topic, JSONObject newData) {
        if(topic.equals("smart-plug.add-scheduler-reply")){
            try {
                String deviceId = newData.getString("device_id");
                String schedulerId = newData.getString("scheduler_id");
                String messageString= newData.getString("message");
                if(deviceId.equals(DataHolder.getDevice().uuid)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                            Scheduler scheduler = new Scheduler(schedulerId,DataHolder.getDevice().uuid,eventTypeSwitch.isChecked() ? 1 : 0,String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute),dailyButton.isChecked() ? "daily" : "weekend","["+dayOfWeekSelected+"]",true);
                            AppDatabase.getInstance(ScheduleAddItemScreen.this).schedulerDao().insert(scheduler);
                            ScheduleAddItemScreen.super.onBackPressed();
                            Toast.makeText(ScheduleAddItemScreen.this,"Thêm thành công",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        if(topic.equals("smart-plug.update-scheduler-reply")){
            try {
                String schedulerId = newData.getString("scheduler_id");
                Log.d("Hoang",schedulerId);
                Log.d("Hoang",DataHolder.getScheduler().uuid);
                Log.d("Hoang",Boolean.toString(schedulerId.equals(DataHolder.getScheduler().uuid)));
                if(schedulerId.equals(DataHolder.getScheduler().uuid)){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AppDatabase.getInstance(ScheduleAddItemScreen.this).schedulerDao().updateSchedulerByUUID(String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute),eventTypeSwitch.isChecked() ? 1 : 0,dailyButton.isChecked() ? "daily" : "weekend","["+dayOfWeekSelected+"]",DataHolder.getScheduler().uuid);
                            ScheduleAddItemScreen.super.onBackPressed();
                            Toast.makeText(ScheduleAddItemScreen.this,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void showDialog() {
        Dialog dialog = new Dialog(ScheduleAddItemScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_option_scheduler);

        Window window = dialog.getWindow();
        if(window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);


            daysOfWeek= convertToBooleanArray("["+dayOfWeekSelected+"]");

            Log.d("Hoang",daysOfWeek.toString());
            ConstraintLayout mon = dialog.findViewById(R.id.mon);
            ConstraintLayout tue = dialog.findViewById(R.id.tue);
            ConstraintLayout wed = dialog.findViewById(R.id.wed);
            ConstraintLayout thu = dialog.findViewById(R.id.thu);
            ConstraintLayout fri = dialog.findViewById(R.id.fri);
            ConstraintLayout sat = dialog.findViewById(R.id.sat);
            ConstraintLayout sun = dialog.findViewById(R.id.sun);
            ConstraintLayout[] dayOfWeekCS = {mon,tue,wed,thu,fri,sat,sun};
            for(int i=0;i<7;i++){
                onClick(i,dayOfWeekCS[i]);
            }
            Button cancelButton = dialog.findViewById(R.id.cancelButtonOptionScheduler);
            Button okButton = dialog.findViewById(R.id.okButtonOptionScheduler);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss(); // Đóng dialog
                    if(dayOfWeekSelected.equals("")){
                        dailyButton.setChecked(true);
                        optionButton.setChecked(false);
                        daysRepeatText.setText("Một lần");
                    }

                }
            });
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss(); // Đóng dialog
                    Log.d("Hoang",dayOfWeekStringMethod());
                    if(dayOfWeekStringMethod().equals("")){
                        dayOfWeekSelected = "";
                        dailyButton.setChecked(true);
                        optionButton.setChecked(false);
                    }else if(dayOfWeekStringMethod().equals("Th 2,Th 3,Th 4,Th 5,Th 6,Th 7,CN")){
                        dayOfWeekSelected = dayOfWeekStringMethod();
                        daysRepeatText.setText("Hằng ngày");
                    }else{
                        dayOfWeekSelected = dayOfWeekStringMethod();
                        daysRepeatText.setText(dayOfWeekStringMethod().replace(","," "));
                    }

                }
            });
        }
        dialog.show();
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
    private void onClick(int posittion,ConstraintLayout layout){
        if(daysOfWeek[posittion]){
            layout.setBackground(getDrawable(R.drawable.border10green));
        }
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!daysOfWeek[posittion]){
                    layout.setBackground(getDrawable(R.drawable.border10green));
                    daysOfWeek[posittion] = !daysOfWeek[posittion];
                }else{
                    daysOfWeek[posittion] = !daysOfWeek[posittion];
                    layout.setBackground(getDrawable(R.drawable.border10));

                }
            }
        });
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

}