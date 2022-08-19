package com.ata.tempalarm1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Database;
import androidx.room.Room;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.ata.tempalarm1.Data.Alarm;
import com.ata.tempalarm1.Data.ListDAO;
import com.ata.tempalarm1.Data.MainDB;
import com.ata.tempalarm1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;


    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //creating a new view model, based on the MainViewModel class, and storing it in the variable mainViewModel
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);//passing the class
        mainViewModel.initializeDataBase(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        DividerItemDecoration drawBox = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        drawBox.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.box));
        binding.recyclerView.addItemDecoration(drawBox);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));//this 4.011230
        //List<Alarm> emptyList = new ArrayList<Alarm>();
        //binding.recyclerView.setAdapter(new MainAdapter( emptyList));

        setContentView(binding.getRoot());

        //mainViewModel.updateListOfAlarms();
        mainViewModel.getListOfAlarms().observe(this,alarms -> {//this or the onResume work
            //update UI
            //onChange(ListOfAlarms)
            binding.recyclerView.setAdapter(new MainAdapter(alarms));
        });

        //setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        binding.zipEditText.setText(Integer.toString(sharedPref.getInt("Zipcode",95928)));
        binding.replaceZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putInt("Zipcode", Integer.parseInt(binding.zipEditText.getText().toString())).apply();
            }
        });
    }

    /*@Override
    protected void onResume(){//an option
        super.onResume();

        mainViewModel.getListOfAlarms().observe(this, alarms ->{
            binding.recyclerView.setAdapter(new MainAdapter(alarms));
        });
    }*/
}