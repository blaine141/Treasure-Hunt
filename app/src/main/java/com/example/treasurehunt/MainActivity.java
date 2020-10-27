package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cache.getCaches(this, 40, -83, 20, new CacheListener() {
            @Override
            public void cachesLoaded(ArrayList<Cache> caches) {
                System.out.println("Caches have been loaded!");
            }
        });

    }


}