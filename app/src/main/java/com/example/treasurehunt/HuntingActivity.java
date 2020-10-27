package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuntingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunting);

        final Context thisContext = this;

        final ListView listview = (ListView) findViewById(R.id.listview);
        final ArrayList<String> names = new ArrayList<>();
        final CacheArrayAdapter adapter = new CacheArrayAdapter(this,
                android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        Cache.getCaches(this, 39.832, -83.983, 10, new CacheListener() {
            @Override
            public void cachesLoaded(final ArrayList<Cache> caches) {
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