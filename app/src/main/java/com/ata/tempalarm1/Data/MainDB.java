package com.ata.tempalarm1.Data;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Alarm.class}, version=1, exportSchema = false )//specifying what entities I want as part of this database
public abstract class MainDB extends RoomDatabase {
    public abstract ListDAO listDAO();
}
