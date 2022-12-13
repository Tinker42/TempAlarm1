package com.ata.tempalarm1;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;

import java.util.List;
import java.util.Observable;

public class MainViewModel extends ViewModel {
    private MainDB Database;//class that controls all the database access objects
    private LiveData<List<Alarm>> ListOfAlarms;//holds the info that is being held in the LiveData
    private LiveData<List<WorkInfo>> ListOfWorkInfo = WorkManager.getInstance().getWorkInfosByTagLiveData("MainActivity");//holds the info

    public LiveData<List<Alarm>> getListOfAlarms() {
        ListDAO listDAO = Database.listDAO();
        //ListOfAlarms.postValue((List<Alarm>) listDAO.getAllAlarms());//1
        ListOfAlarms= listDAO.getAllAlarms();//2
        /*if(callVar != null) {
            ListOfAlarms.postValue(callVar);
        }*/
        //return ListOfAlarms;//1
        return ListOfAlarms;//2
    }

    public LiveData<List<WorkInfo>> getListOfWorkInfo() {
        return ListOfWorkInfo;
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

    String compare (double currTemp){//,String LastTime){
        List<Alarm> currList = ListOfAlarms.getValue();
        int h=0;

        for(int i=0;i<currList.size();i++){
//        for(Alarm alarm:currList) {
            if(currList.get(i).getHighOrLow() == 0){
//            if(alarm.getHighOrLow() == 0) {//is hotter?
                if(currList.get(i).getTemperature() <= currTemp){
//                if(alarm.getTemperature() <= currTemp) {
                    //call alarm with: currTemp" has exceeded "alarm.getTemperature()
                    //currList.get(i).setHighOrLow(1);
                    //alarm.setHighOrLow(1);
                    //swap those of HOL 0 below to 1
                    h=i;
                    return "The current Temperature of "+currTemp+"℉ has exceeded your monitored Temp of "+currList.get(i).getTemperature()+"℉";// "+LastTime;
//                    return "The current Temperature of "+currTemp+"℉ has exceeded your monitored Temp of "+alarm.getTemperature()+"℉";// "+LastTime;
                }
            }else{
                if(currList.get((currList.size()-(i-h))).getTemperature() >= currTemp){
//                if(alarm.getTemperature() >= currTemp) {//is colder?
                    //call alarm with: currTemp" has fallen below "alarm.getTemperature()
                    //currList.get((currList.size()-i)).setHighOrLow(0);
                    //alarm.setHighOrLow(0);
                    //swap those of HOL 1 above to 0
                    return "The current Temperature of "+currTemp+"℉ has fallen below your monitored Temp of "+currList.get((currList.size()-(i-h))).getTemperature()+"℉";
//                    return "The current Temperature of "+currTemp+"℉ has fallen below your monitored Temp of "+alarm.getTemperature()+"℉";
                }
            }
        }
        return "";
    }
}