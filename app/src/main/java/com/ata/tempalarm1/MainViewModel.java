package com.ata.tempalarm1;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;

import java.util.Observable;

public class MainViewModel extends ViewModel {
    private MainDB Database;//class that controls all the database access objects

    boolean initialiseDataBase (Context context) {
        try {
            //executor, Observable, Coroutine
            Database = Room.databaseBuilder(context, MainDB.class, "userAlarms")
                    .allowMainThreadQueries()
                    .build();
        }catch (Exception FailedToBuild){
            return false;
        }
        ListDAO listDAO = Database.listDAO();
        //listDAO.Insert(new Alarm(102, 0));
        return true;
    }
}