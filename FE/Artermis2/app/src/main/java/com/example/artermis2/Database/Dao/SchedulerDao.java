package com.example.artermis2.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.artermis2.Database.Entity.Scheduler;

import java.util.List;

@Dao
public interface SchedulerDao {
    @Insert
    void insert(Scheduler scheduler);

    @Query("SELECT * FROM schedulers")
    List<Scheduler> getAllSchedulers();
    @Query("SELECT * FROM schedulers WHERE deviceId = :deviceId")
    List<Scheduler> getSchedulerByDeviceId(String deviceId);


    @Query("DELETE FROM schedulers WHERE uuid = :uuid")
    void deleteSchedulersByUUId(String uuid);
    // Các phương thức khác để thực hiện các truy vấn và hoạt động khác với cơ sở dữ liệu

    @Query("DELETE FROM schedulers WHERE deviceId = :deviceId")
    void deleteSchedulerByDeviceId(String deviceId);

    @Query("UPDATE schedulers SET timeSelection = :timeSelection , eventType = :eventType ,repeatType = :repeatType ,daysRepeat = :daysRepeat  WHERE uuid = :uuid")
    void updateSchedulerByUUID(String timeSelection,int eventType,String repeatType,String daysRepeat,String uuid);
    @Query("UPDATE schedulers SET status = :status WHERE uuid = :uuid")
    void updateSchedulerStatusByUUID(boolean status,String uuid);
}
