package com.ata.tempalarm1.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListDAO {
    @Insert
    void Insert(Alarm alarm);

    @Delete
    void Delete(Alarm alarm);

    @Query("SELECT * FROM Alarm")//returns list of Alarms
    LiveData<List<Alarm>> getAllAlarms();
}