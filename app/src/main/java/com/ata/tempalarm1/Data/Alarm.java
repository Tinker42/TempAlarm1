package com.ata.tempalarm1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Alarm implements Serializable {
    @ColumnInfo(name = "temperature")
    private int temperature;
    @ColumnInfo(name = "high_or_low")
    private int HighOrLow;//0=(Red)Check if exceeds, 1=(Blue)Check if below
    @PrimaryKey(autoGenerate = true)
    public int uID;

    public Alarm(int temperature, int HighOrLow){
        this.temperature=temperature;
        this.HighOrLow=HighOrLow;
    }

    public int getTemperature(){ return this.temperature;}
    public int getHighOrLow(){ return this.HighOrLow;}
    //public int getuID(){ return this.uID;}

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
    public void setHighOrLow(int highOrLow) {
        this.HighOrLow = highOrLow;
    }
    //public void setuID(int uID) { this.uID = uID; }

}
