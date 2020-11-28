package com.example.treasurehunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    List<Score> scores;
    ListView listview;
    ArrayList<String> names;
    ScoreArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scores = Score.getScores(this);

        names = new ArrayList<>();
        for (Score score: scores) {
            String text = String.format(getResources().getConfiguration().locale, "Accuracy: %.2f, Hints: %d", score.getAccuracy() * 100, score.hints);
            names.add(text);
        }

        listview = findViewById(R.id.scoreListView);
        adapter = new ScoreArrayAdapter(this,
                android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        final Context context = this;
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                Intent intent = new Intent(context, ScoreActivity.class);
                intent.putExtra("score", scores.get(position));
                startActivity(intent);
            }
        });

    }

    private static class ScoreArrayAdapter extends ArrayAdapter<String> {


        public ScoreArrayAdapter(Context context, int textViewResourceId,
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