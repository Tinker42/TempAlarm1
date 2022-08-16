package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Database;
import androidx.room.Room;

import android.os.Bundle;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;
import com.ata.tempalarm1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;


    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating a new view model, based on the MainViewModel class, and storing it in the variable mainViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);//passing the class
        mainViewModel.initialiseDataBase(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);



    }
}