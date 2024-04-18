package com.example.artermis2.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.artermis2.Database.Dao.DeviceDao;
import com.example.artermis2.Database.Dao.HistoryDao;
import com.example.artermis2.Database.Dao.SchedulerDao;
import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.Database.Entity.History;
import com.example.artermis2.Database.Entity.Scheduler;

@Database(entities = {Scheduler.class, Device.class, History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "artemis2.db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract SchedulerDao schedulerDao();
    public abstract DeviceDao deviceDao();
    public abstract HistoryDao historyDao();
}