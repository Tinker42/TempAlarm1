package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;
import androidx.room.Room;

import android.os.Bundle;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;
import com.ata.tempalarm1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainDB Database;//class that controls all the database access objects
    private ActivityMainBinding binding;


    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        Database= Room.databaseBuilder(getApplicationContext(), MainDB.class, "userAlarms")
                .allowMainThreadQueries()
                .build();

        ListDAO listDAO= Database.listDAO();
        listDAO.Insert(new Alarm(46, 1));

    }
}