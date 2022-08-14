package com.ata.tempalarm1.Data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alarm {
    @ColumnInfo(name = "temperature")
    public int temperature;
    @ColumnInfo(name = "high_or_low")
    public int HighOrLow;//0=(Red)Check if exceeds, 1=(Blue)Check if below
    @PrimaryKey(autoGenerate = true)
    public int uID;

    public Alarm(int temperature, int HighOrLow){
        this.temperature=temperature;
        this.HighOrLow=HighOrLow;
    }

}
