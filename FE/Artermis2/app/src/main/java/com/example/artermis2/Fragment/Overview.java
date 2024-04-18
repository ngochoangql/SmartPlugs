package com.example.artermis2.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Database.Entity.History;
import com.example.artermis2.Mqtt.MqttHandler;
import com.example.artermis2.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Overview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Overview extends Fragment implements IDataObserver {


    Context context ;
    LinearLayout relayHistory ;
    List<History> listHistory;
    private LineChart lineChart;
    Switch relaySwitch;
    TextView predict,kWh,sumHour,current,voltage,actP,appP;
    MqttHandler mqttHandler;
    public Overview(Context context) {
        // Required empty public constructor
        this.context = context;

    }

    private void Init(View view){
        predict = view.findViewById(R.id.predictDevice);
        kWh = view.findViewById(R.id.kWhDevice);
        sumHour = view.findViewById(R.id.sumHourDevice);
        current = view.findViewById(R.id.currentDvice);
        voltage = view.findViewById(R.id.voltageDevice);
        actP = view.findViewById(R.id.actPDevice);
        appP = view.findViewById(R.id.actPDevice);
    }
    public static Overview newInstance(Context context) {
        Overview fragment = new Overview(context);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        Init(view);
        mqttHandler = new MqttHandler("overview_client",getContext());
        mqttHandler.registerObserver(this);
        lineChart = (LineChart) view.findViewById(R.id.lineChart);
        lineChart.getXAxis().setEnabled(false);
        lineChart.getAxisRight().setEnabled(false);

        List<Entry> list1 = new ArrayList<>();
        List<Entry> list2 = new ArrayList<>();
        List<Entry> list3 = new ArrayList<>();
        List<Entry> list4 = new ArrayList<>();
        list1.add(new Entry(0,1));
        list2.add(new Entry(0,1));
        list3.add(new Entry(0,1));
        list4.add(new Entry(0,1));
        list1.add(new Entry(1,2));
        list2.add(new Entry(1,3));
        list3.add(new Entry(1,4));
        list4.add(new Entry(1,1));
        LineData lineData = createChartData(list1,list2,list3,list4);
        lineChart.setData(lineData);
        lineChart.invalidate();
        relaySwitch = view.findViewById(R.id.relaySwitch);
        relayHistory = view.findViewById(R.id.relayHistory);
        listHistory = AppDatabase.getInstance(context).historyDao().getLatestHistoriesByDeviceIdAndType(DataHolder.getDevice().uuid,"relay");
        changeRelay();
        relaySwitch.setOnCheckedChangeListener(null);


        relaySwitch.setChecked(DataHolder.getDevice().state);
        if(DataHolder.getDevice().state){
            relaySwitch.setText("Bật");
        }else{
            relaySwitch.setText("Tắt");
        }





        relaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    History history = new History(DataHolder.getDevice().uuid,"relay","Bật","Bật relay");
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    relaySwitch.setText("Bật");
                }else{
                    History history = new History(DataHolder.getDevice().uuid,"relay","Tắt","Tắt relay");
                    AppDatabase.getInstance(context).historyDao().insert(history);
                    relaySwitch.setText("Tắt");
                }
                mqttHandler.publish("smart-plug.relay","{\"device_id\":\""+DataHolder.getDevice().uuid+"\",\"status\":\""+(isChecked ? "on" : "off") + "\"}",0,false);


            }

        });
        TextView deviceName = view.findViewById(R.id.deviceName1);
        TextView roomName = view.findViewById(R.id.roomName1);
        deviceName.setText(DataHolder.getDevice().device_name);
        roomName.setText(DataHolder.getDevice().room_name);
        return view;
    }
    private LineData createChartData(List<Entry> current, List<Entry> voltage, List<Entry> active, List<Entry> apparent) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(createDataSet(current, "Current", Color.RED));
        dataSets.add(createDataSet(voltage, "Voltage", Color.BLUE));
        dataSets.add(createDataSet(active, "ActivePower", Color.YELLOW));
        dataSets.add(createDataSet(apparent, "ApparentPower", Color.parseColor("#FFA500"))); // Màu cam

        return new LineData(dataSets);
    }
    private LineDataSet createDataSet(List<Entry> entries, String label, int color) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawCircles(false);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawValues(false); // Tắt hiển thị giá trị trên điểm dữ liệu
        return dataSet;
    }
    private void changeRelay(){
        listHistory = AppDatabase.getInstance(context).historyDao().getLatestHistoriesByDeviceIdAndType(DataHolder.getDevice().uuid,"relay");
        relayHistory.removeAllViews();
        for(History history : listHistory){
            TextView textView = new TextView(context);
            String[] parts = history.date.split("\\s+");
            String day = parts[2];
            String month = parts[1];
            String year = parts[5];
            String time = parts[3];
            String outputDateString = day + "/" + getMonthNumber(month) + "/" + year + " " + time;

            textView.setText(outputDateString+ " " + history.intro);

            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            relayHistory.addView(textView);
        }
    }
    // Phương thức để chuyển đổi tên tháng thành số tháng
    private static String getMonthNumber(String month) {
        switch (month) {
            case "Jan": return "01";
            case "Feb": return "02";
            case "Mar": return "03";
            case "Apr": return "04";
            case "May": return "05";
            case "Jun": return "06";
            case "Jul": return "07";
            case "Aug": return "08";
            case "Sep": return "09";
            case "Oct": return "10";
            case "Nov": return "11";
            case "Dec": return "12";
            default: return "00";
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        relaySwitch.setOnCheckedChangeListener(null);
        mqttHandler.unregisterObserver(this);
        mqttHandler.disconnect();
    }

    @Override
    public void onDataUpdated(String topic, JSONObject newData) {
        if(topic.equals("smart-plug.predict")){
            try {
                String deviceId = newData.getString("device_id");
                String predictString= newData.getString("predict");
                String kWhString= newData.getString("kWh");
                String sumHourString= newData.getString("sum_hour");
                String currentString= newData.getString("current");
                String voltageString= newData.getString("voltage");
                String actPString= newData.getString("active_power");
                String appPString= newData.getString("apparent_power");
                if(deviceId.equals(DataHolder.getDevice().uuid)){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            predict.setText(predictString);
                            kWh.setText(kWhString);
                            sumHour.setText(sumHourString);
                            current.setText(currentString);
                            voltage.setText(voltageString);
                            actP.setText(actPString);
                            appP.setText(appPString);
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    if(topic.equals("smart-plug.relay-reply")){
        try {
            String deviceId = newData.getString("device_id");
            String statusString= newData.getString("status");
            if(deviceId.equals(DataHolder.getDevice().uuid)){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).deviceDao().updateStateDeviceByUUID(statusString.equals("on") ? true : false,DataHolder.getDevice().uuid);
                        Toast.makeText(context,(statusString.equals("on") ? "Bật" : "Tắt" )+ " thành công",Toast.LENGTH_SHORT).show();
                        changeRelay();
                    }
                });
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    if(topic.equals("smart-plug.scheduler-event-reply")){
        try {
            String deviceId = newData.getString("device_id");
            String schedulerId = newData.getString("scheduler_id");
            String statusString= newData.getString("status");
            Log.d("Hoang",deviceId);
            if(deviceId.equals(DataHolder.getDevice().uuid)){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        relaySwitch.setChecked(statusString.equals("on") ? true: false);
                    }
                });
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}


}