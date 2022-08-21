package com.ata.tempalarm1;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ata.tempalarm1.databinding.ActivityAlarmInputBinding;
import com.ata.tempalarm1.databinding.ActivityMainBinding;


public class AlarmInputActivity extends AppCompatActivity {
    private ActivityAlarmInputBinding binding;
    private AlarmInputViewModel alarmInputViewModel;

    @Override //Overriding the AppCompactActivity which is the android class that has a function onCreate that represents the view lifecycle
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        alarmInputViewModel = new ViewModelProvider(this).get(AlarmInputViewModel.class);//passing the class

        binding = ActivityAlarmInputBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
    }
}
