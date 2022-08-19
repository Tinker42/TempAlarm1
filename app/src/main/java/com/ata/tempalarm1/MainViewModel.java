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
import java.util.Observable;

public class MainViewModel extends ViewModel {
    private MainDB Database;//class that controls all the database access objects
    private MutableLiveData<List<Alarm>> ListOfAlarms;//holds the info that is being held in the LiveData

    public LiveData<List<Alarm>> getListOfAlarms() {
        ListDAO listDAO = Database.listDAO();
        //ListOfAlarms.postValue((List<Alarm>) listDAO.getAllAlarms());//1
        LiveData< List<Alarm> > callVar = listDAO.getAllAlarms();//2
        /*if(callVar != null) {
            ListOfAlarms.postValue(callVar);
        }*/
        //return ListOfAlarms;//1
        return callVar;//2
    }

    /*public void updateListOfAlarms(){
        ListDAO listDAO = DataBase.listDAO();
        List<Alarm> callVar = listDAO.getAllAlarms();
        if(callVar != null) {
            ListOfAlarms.postValue(callVar);
        }
    }*/

    boolean initializeDataBase (Context context) {
        try {
            //executor, Observable, Coroutine
            Database = Room.databaseBuilder(context, MainDB.class, "userAlarms")
                    .allowMainThreadQueries()
                    .build();
        }catch (Exception FailedToBuild){
            return false;
        }
        ListDAO listDAO = Database.listDAO();

        //listDAO.Insert(new Alarm(98, 0));
        //listDAO.Insert(new Alarm(27, 1));
        //listDAO.Insert(new Alarm(-4, 1));
        //listDAO.Insert(new Alarm(102, 0));
        return true;
    }
}