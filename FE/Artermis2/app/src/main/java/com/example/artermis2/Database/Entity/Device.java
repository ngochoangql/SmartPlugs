package com.example.artermis2.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "devices")
public class Device {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "uuid")
    public String uuid;
    @ColumnInfo(name = "room_name")
    public String room_name;

    @ColumnInfo(name = "device_name")
    public String device_name;
    @ColumnInfo(name = "state")
    public boolean state;
    @ColumnInfo(name = "value_limit")
    public int value_limit;
    @ColumnInfo(name = "state_limit")
    public boolean state_limit;

    public Device(String uuid,String device_name,String room_name,int value_limit){
        this.device_name = device_name;
        this.room_name =room_name;
        this.uuid = uuid;
        this.value_limit = value_limit;
        this.state = false;
        this.state_limit = false;
    }


}
