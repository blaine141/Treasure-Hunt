package com.example.treasurehunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HuntingActivity extends AppCompatActivity {
    private ListView listview;
    private ArrayList<String> names;
    private CacheArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunting);

        listview = findViewById(R.id.listview);
        names = new ArrayList<>();
        adapter = new CacheArrayAdapter(this,
                android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startFetchingCaches();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFetchingCaches();
            } else {
                System.out.println("Location permissions were denied.");
            }
        } else {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void startFetchingCaches() {
        final Context thisContext = this;

        Cache.getCaches(this, 10, new Consumer<ArrayList<Cache>>() {
            @Override
            public void accept(final ArrayList<Cache> caches) {
                for (int i = 0; i < caches.size(); ++i)
                    names.add(caches.get(i).name);
                adapter.notifyDataSetChanged();

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        Intent intent = new Intent(thisContext, DirectionsActivity.class);
                        intent.putExtra("cache", caches.get(position));
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private static class CacheArrayAdapter extends ArrayAdapter<String> {


        public CacheArrayAdapter(Context context, int textViewResourceId,
                                 List<String> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}