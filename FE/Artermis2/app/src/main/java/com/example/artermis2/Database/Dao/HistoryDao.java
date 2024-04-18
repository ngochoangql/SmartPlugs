package com.example.artermis2.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.example.artermis2.Database.Entity.History;


import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insert(History history);

    @Query("SELECT * FROM histories")
    List<History> getAllHistory();

    @Query("SELECT * FROM histories WHERE deviceId = :deviceId AND type = :type ORDER BY date DESC LIMIT 8")
    List<History> getLatestHistoriesByDeviceIdAndType(String deviceId, String type);
    @Query("SELECT * FROM histories WHERE deviceId = :deviceId ORDER BY date DESC")
    List<History> getHistoriesByDeviceIdOrderByDate(String deviceId);

    @Query("DELETE FROM histories WHERE deviceId = :deviceId")
    void deleteHistoryByDeviceId(String deviceId);


}
