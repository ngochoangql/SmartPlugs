package com.example.artermis2.Database.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "histories")
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "deviceId")
    public String deviceId;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "intro")
    public String intro;

    public History(String deviceId,String type,String intro,String description){
        this.deviceId = deviceId;
        this.description =description;
        this.type = type;
        this.intro =intro;
        this.date = (new Date()).toString();
    }

}
