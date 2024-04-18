package com.example.artermis2.Mqtt;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.artermis2.Component.IDataObserver;
import com.example.artermis2.Data.DataHolder;
import com.example.artermis2.Database.AppDatabase;
import com.example.artermis2.Fragment.Overview;
import com.example.artermis2.Fragment.Scheduler;
import com.example.artermis2.MainActivity;
import com.example.artermis2.Screen.ScheduleAddItemScreen;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MqttHandler{

    private static final String TAG = "MqttHandler";
    private static final String serverUri = "tcp://192.168.1.9:1883";
    private List<IDataObserver> observers = new ArrayList<>();

    private MqttClient mqttClient;
    private MqttClientPersistence persistence;
    Context context;

    public MqttHandler(String clientId,Context context) {
        this.context = context;
        try{

            mqttClient = new MqttClient(serverUri, clientId,persistence);
        } catch (MqttException e) {
            Log.e(TAG, "Failed to initialize MqttHelper", e);
        }
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(60);
        try {
            connect(mqttConnectOptions, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Connected to MQTT broker");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to connect to MQTT broker", exception);
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "Failed to connect to MQTT broker", e);
        }
        setCallback();
    }

    public void setCallback() {
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                // Xử lý dữ liệu khi nhận được message từ topic đã subscribe
                String payload = new String(message.getPayload());
                // In ra dữ liệu nhận được
                Log.d(TAG, "Received message from topic: " + topic + ", message: " + payload);

                JSONObject jsonObject = new JSONObject(payload);
                notifyObservers(topic,jsonObject);


                if(topic.equals("smart-plug.relay-reply")){
                    try {
                        String deviceId = jsonObject.getString("device_id");
                        String statusString= jsonObject.getString("status");
                        AppDatabase.getInstance(context).deviceDao().updateStateDeviceByUUID(statusString.equals("on") ? true : false,deviceId);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(topic.equals("smart-plug.scheduler-event-reply")){
                    try {
                        String deviceId = jsonObject.getString("device_id");
                        String statusString= jsonObject.getString("status");
                        AppDatabase.getInstance(context).deviceDao().updateStateDeviceByUUID(statusString.equals("on") ? true : false,deviceId);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(topic.equals("smart-plug.update-scheduler-status-reply")){
                    Log.d("Mqtt","22222");
                    try {
                        boolean status = jsonObject.getBoolean("status");
                        String schedulerId = jsonObject.getString("scheduler_id");
                        AppDatabase.getInstance(context).schedulerDao().updateSchedulerStatusByUUID(status,schedulerId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
                if(topic.equals("smart-plug.update-scheduler-status-event-reply")){
                    Log.d("Mqtt","22333");
                    try {
                        boolean status = jsonObject.getBoolean("status");
                        String schedulerId = jsonObject.getString("scheduler_id");
                         AppDatabase.getInstance(context).schedulerDao().updateSchedulerStatusByUUID(status,schedulerId);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }if(topic.equals("smart-plug.async-reply")){
                    // Đọc giá trị từ JSONObject


                    JSONArray schedulersArray = jsonObject.getJSONArray("schedulers");
                    System.out.println("Schedulers:");
                    for (int i = 0; i < schedulersArray.length(); i++) {
                        JSONObject schedulerObject = schedulersArray.getJSONObject(i);
                        String schedulerId = schedulerObject.getString("scheduler_id");
                        String deviceId = schedulerObject.getString("device_id");
                        int eventType = schedulerObject.getInt("event_type");
                        String timeSelected = schedulerObject.getString("time_selected");
                        String repeatType = schedulerObject.getString("repeat_type");
                        boolean status = schedulerObject.getBoolean("status");

                        AppDatabase.getInstance(context).schedulerDao().updateSchedulerStatusByUUID(status,schedulerId);
//                        System.out.println("Scheduler ID: " + schedulerId);
//                        System.out.println("Device ID: " + deviceId);
//                        System.out.println("Event Type: " + eventType);
//                        System.out.println("Time Selected: " + timeSelected);
//                        System.out.println("Repeat Type: " + repeatType);
//                        System.out.println("Status: " + status);
                    }

                    JSONArray devicesArray = jsonObject.getJSONArray("devices");
                    System.out.println("Devices:");
                    for (int i = 0; i < devicesArray.length(); i++) {
                        JSONObject deviceObject = devicesArray.getJSONObject(i);
                        String deviceId = deviceObject.getString("device_id");
                        String deviceName = deviceObject.getString("device_name");
                        String rommName = deviceObject.getString("room_name");
                        String relayStatus = deviceObject.getString("relay_status");
                        int valueLimit = deviceObject.getInt("value_limit");
                        boolean stateLimit = deviceObject.getBoolean("state_limit");

                        AppDatabase.getInstance(context).deviceDao().updateStateDeviceByUUID(relayStatus.equals("on") ? true : false,deviceId);
                        AppDatabase.getInstance(context).deviceDao().updateStateLimitDeviceByUUID(stateLimit,deviceId);
                        AppDatabase.getInstance(context).deviceDao().updateRoomNameDeviceByUUID(rommName,deviceId);
                        AppDatabase.getInstance(context).deviceDao().updateNameDeviceByUUID(deviceName,deviceId);
                        AppDatabase.getInstance(context).deviceDao().updateValueLimitDeviceByUUID(valueLimit,deviceId);
//                        System.out.println("Device ID: " + deviceId);
//                        System.out.println("Name: " + name);
//                        System.out.println("Relay Status: " + relayStatus);
//                        System.out.println("Value Limit: " + valueLimit);
//                        System.out.println("State Limit: " + stateLimit);
                    }
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    public void connect(MqttConnectOptions mqttConnectOptions, IMqttActionListener callback) throws MqttException {
        IMqttToken token = mqttClient.connectWithResult(mqttConnectOptions);
        token.setActionCallback(callback);
//        for(IDataObserver iDataObserver : observers){
//            if(iDataObserver.getClass() == MainActivity.class) {
//                subscribe("smart-plug.relay-reply", 0);
//                subscribe("smart-plug.predict", 0);
//                subscribe("smart-plug.scheduler-event-reply", 0);
//
////                subscribe("smart-plug.relay-event-reply", 0);
////                subscribe("smart-plug.add-device-reply", 0);
////                subscribe("smart-plug.async-reply", 0);
////                subscribe("smart-plug.delete-device-reply", 0);
////                subscribe("smart-plug.limit-reply", 0);
//
//                subscribe("smart-plug.add-scheduler-reply", 0);
//                subscribe("smart-plug.update-scheduler-reply", 0);
//
//
//                subscribe("smart-plug.update-scheduler-status-reply", 0);
//                subscribe("smart-plug.update-scheduler-status-event-reply", 0);
//            }
//            if(iDataObserver.getClass() == Overview.class){
//                subscribe("smart-plug.relay-reply", 0);
//                subscribe("smart-plug.predict", 0);
//                subscribe("smart-plug.scheduler-event-reply", 0);
//            }
//            if(iDataObserver.getClass() == Scheduler.class){
//                subscribe("smart-plug.update-scheduler-status-reply", 0);
//                subscribe("smart-plug.update-scheduler-status-event-reply", 0);
//            }
//            if(iDataObserver.getClass() == ScheduleAddItemScreen.class){
//                subscribe("smart-plug.add-scheduler-reply", 0);
//                subscribe("smart-plug.update-scheduler-reply", 0);
//            }
//        }
        subscribe("smart-plug.relay-reply", 0);
        subscribe("smart-plug.predict", 0);
        subscribe("smart-plug.scheduler-event-reply", 0);
        subscribe("smart-plug.async-reply", 0);
        subscribe("smart-plug.add-device-reply",0);
        subscribe("smart-plug.limit-reply",0);
//                subscribe("smart-plug.relay-event-reply", 0);
//                subscribe("smart-plug.add-device-reply", 0);
//                subscribe("smart-plug.async-reply", 0);
//                subscribe("smart-plug.delete-device-reply", 0);
//                subscribe("smart-plug.limit-reply", 0);

        subscribe("smart-plug.add-scheduler-reply", 0);
        subscribe("smart-plug.update-scheduler-reply", 0);


        subscribe("smart-plug.update-scheduler-status-reply", 0);
        subscribe("smart-plug.update-scheduler-status-event-reply", 0);

    }

    public void disconnect()  {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void publish(String topic, String message, int qos, boolean retained) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(retained);
        try {
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void subscribe(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
    }

    public void unsubscribe(String topic) throws MqttException {
        mqttClient.unsubscribe(topic);
    }
    public void registerObserver(IDataObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(IDataObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String topic, JSONObject newData) {
        for (IDataObserver observer : observers) {
            observer.onDataUpdated(topic,newData);
        }
    }
}
