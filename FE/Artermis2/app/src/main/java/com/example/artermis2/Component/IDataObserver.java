package com.example.artermis2.Component;

import org.json.JSONObject;

public interface IDataObserver {
    void onDataUpdated(String topic, JSONObject newData);

}
