package com.example.artermis2.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.artermis2.Database.Entity.Device;
import com.example.artermis2.Database.Entity.Scheduler;

import java.util.List;
@Dao
public interface DeviceDao {
    @Insert
    void insert(Device device);

    @Query("SELECT * FROM devices")
    List<Device> getAllDevices();
    @Query("SELECT * FROM devices WHERE uuid = :uuid")
    Device getDeviceByUuid(String uuid);
    @Query("UPDATE devices SET state = :state WHERE uuid = :uuid")
    void updateStateDeviceByUUID(boolean state, String uuid);
    @Query("UPDATE devices SET device_name = :device_name WHERE uuid = :uuid")
    void updateNameDeviceByUUID(String device_name, String uuid);
    @Query("UPDATE devices SET room_name = :room_name WHERE uuid = :uuid")
    void updateRoomNameDeviceByUUID(String room_name, String uuid);
    @Query("UPDATE devices SET state_limit = :state_limit WHERE uuid = :uuid")
    void updateStateLimitDeviceByUUID(boolean state_limit, String uuid);
    @Query("UPDATE devices SET value_limit = :value_limit WHERE uuid = :uuid")
    void updateValueLimitDeviceByUUID(int value_limit, String uuid);
    @Query("DELETE FROM devices WHERE uuid = :uuid")
    void deleteDeviceByUuid(String uuid);
}
