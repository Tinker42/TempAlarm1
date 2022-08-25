package com.ata.tempalarm1;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;

import java.util.List;

public class AlarmInputViewModel extends ViewModel {
    private MainDB Database;
    private MutableLiveData<List<Alarm>> ListOfAlarms;

    /*public LiveData<List<Alarm>> getListOfAlarms() {
        ListDAO listDAO = Database.listDAO();

        LiveData< List<Alarm> > callVar = listDAO.getAllAlarms();

        return callVar;
    }*/

    Alarm addAlarm (Context context, int inTemp, int inHOL) {
        try {
            //executor, Observable, Coroutine
            Database = Room.databaseBuilder(context, MainDB.class, "userAlarms")
                    .allowMainThreadQueries()
                    .build();
        }catch (Exception FailedToBuild){
            //return false;
        }
        ListDAO listDAO = Database.listDAO();

        Alarm nAlarm = new Alarm (inTemp, inHOL);

        listDAO.Insert(nAlarm);

        return nAlarm;

    }
}
