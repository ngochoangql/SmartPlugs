package com.example.artermis2.Database.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "schedulers")
public class Scheduler {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "uuid")
    public String uuid;
    @ColumnInfo(name = "deviceId")
    public String deviceId;
    @ColumnInfo(name = "userId")
    public String userId;
    @ColumnInfo(name = "eventType")
    public int eventType;
    @ColumnInfo(name = "timeSelection")
    public String timeSelection;
    @ColumnInfo(name = "repeatType")
    public String repeatType;
    @ColumnInfo(name = "daysRepeat")
    public String daysRepeat;
    @ColumnInfo(name = "description")
    public String description;
    @ColumnInfo(name = "status")
    public boolean status;

    public Scheduler(String uuid,String deviceId,int eventType,String timeSelection,String repeatType,String daysRepeat,boolean status){
        this.uuid = uuid;
        this.deviceId = deviceId;
        this.eventType = eventType;
        this.timeSelection = timeSelection;
        this.repeatType = repeatType;
        this.daysRepeat = daysRepeat;
        this.description ="";
        this.userId = "";
        this.status = status;
    }

}
